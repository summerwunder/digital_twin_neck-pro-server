package edu.whut.config.mqtt;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.config.mqtt.gateway.MqttGateway;
import edu.whut.config.websocket.WebSocketExcelServer;
import edu.whut.constants.ConnectConstants;
import edu.whut.exception.ServiceException;
import edu.whut.mapper.*;
import edu.whut.pojo.*;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class MqttSubHandler {
    @Autowired
    private WebSocketExcelServer webSocketExcelServer;
    @Autowired
    private SensorDataMapper  dataMapper;
    @Autowired
    private DevicesMapper devicesMapper;
    @Autowired
    private SensorFieldsMapper sensorFieldsMapper;
    @Autowired
    private SensorAlarmRecordsMapper sensorAlarmRecordsMapper;
    @Autowired
    private UserMapAlarmActionsMapper userMapAlarmActionsMapper;
    @Autowired
    private UserMapDevicesMapper userMapDevicesMapper;
    @Autowired
    private AlarmActionsMapper alarmActionsMapper;
    @Autowired
    private MqttGateway mqttGateway;
    @Bean
    @ServiceActivator(inputChannel = "mqttInBoundChannel" )
    public MessageHandler mqttInputHandler(){
        MessageHandler handler=new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                //System.out.println(message);
                //System.out.println("收到订阅消息：消息主题"+message.getHeaders().get("mqtt_receivedTopic").toString());
                //System.out.println("消息内容"+message.getPayload().toString());
                ObjectMapper mapper=new ObjectMapper();
                try {
                    //MQTT消息为数组，
                    //参考格式
                    /*
                            [{
                              "deviceId":10000,
                              "fieldId":3,
                              "valueNum":32.1,
                              "valueStr":null
                            },
                            {
                              "deviceId":10000,
                              "fieldId":1,
                              "valueNum":22.1,
                              "valueStr":null
                            }]
                     */
                    List<SensorData> sensorDataList =new ArrayList<>();
                    //首先判断是不是数组
                    try {
                        //JSONArray jsonArray = new JSONArray(message.getPayload().toString());
                        sensorDataList= mapper.readValue(message.getPayload().toString(), new TypeReference<List<SensorData>>(){});
                    } catch (JSONException e) {
                        throw new RuntimeException();
                    }
                    //此处自动填充不可用！！！手动添加
                    LocalDateTime currentTime = LocalDateTime.now();
                    for (SensorData data : sensorDataList) {
                        //此处需要设置是否数据需要报警
                        //如果需要，应该将其放在报警列表中sensorAlarmRecordsMapper
                        //首先需要判断是否超过上限，低于下限
                        if (checkAlarm(data)) {
                            /**
                             * 此处需要根据告警反制信息发送MQTT消息
                             * 1、查询告警反制记录
                             */
                            Integer fieldId=data.getFieldId();
                            Integer deviceId=data.getDeviceId();
                            //获取用户的ID ---- 可以先从deviceId下手--- 获取userId  （通过UserMapDevice）
                            LambdaQueryWrapper<UserMapDevices> userMapDevicesLambdaQueryWrapper
                                    =new LambdaQueryWrapper<>();
                            userMapDevicesLambdaQueryWrapper.eq(UserMapDevices::getDId,deviceId);
                            Integer userId =
                                    userMapDevicesMapper.selectOne(userMapDevicesLambdaQueryWrapper).getUId();
                            if(ObjectUtil.isNull(userId)){
                                throw new ServiceException("填写的数据错误");
                            }
                            //获取字段的告警强度
                            SensorFields sensorFields = sensorFieldsMapper.selectById(fieldId);
                            if(ObjectUtil.isNull(sensorFields)){
                                throw new ServiceException("填写的数据错误");
                            }
                            Integer alterIntensity = sensorFields.getAlterIntensity();
                            LambdaQueryWrapper<UserMapAlarmActions> lambdaQueryWrapper
                                    =new LambdaQueryWrapper<>();
                            lambdaQueryWrapper.eq(UserMapAlarmActions::getUserId,userId);
                            lambdaQueryWrapper.eq(UserMapAlarmActions::getAlarmIntensity,
                                    alterIntensity);
                            //获取所有的报警事件
                            List<UserMapAlarmActions> userMapAlarmActions =
                                    userMapAlarmActionsMapper.selectList(lambdaQueryWrapper);
                            //从中获取所有的alarmId
                            List<Integer> alarmIdList =
                                    userMapAlarmActions.stream().map(UserMapAlarmActions::getAlarmActionId).toList();
                            if(ObjectUtil.isNotEmpty(alarmIdList)){
                                //确保不会为空
                                LambdaQueryWrapper<AlarmActions> alarmActionsWrapper =new LambdaQueryWrapper<>();
                                alarmActionsWrapper.in(AlarmActions::getId,alarmIdList);
                                List<AlarmActions> alarmActionsList =
                                        alarmActionsMapper.selectList(alarmActionsWrapper);
                                for(AlarmActions alarmActions:alarmActionsList){
                                    // 此处发出告警信号
                                    sendMqttMsg(alarmActions);
                                }
                            }
                        }
                        data.setUpdateTime(currentTime);
                        // 将数据插入数据库
                        dataMapper.insert(data);
                        //此处需要修改物联网设备的更新时间
                        Devices devices=new Devices();
                        devices.setUpdateTime(LocalDateTime.now());
                        devices.setDid(data.getDeviceId());
                        //同时需要将设备设置为在线状态
                        devices.setDStatus(1);
                        devicesMapper.updateById(devices);
                        //TODO 此处将数据信息发送至SOCKET
                        //此处默认只传送力传感器的数据，后续需要重新设计表数据
/*                        if(data.getFieldId().equals(3)){
                            invokePython(data.getValueNum());
                        }*/
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return handler;
    }

    /**
     * 是否超过阈值范围
     * @param sensorData
     * @return
     */
    private boolean checkAlarm(SensorData sensorData){
        //获取基本信息
        Integer fieldId=sensorData.getFieldId();
        Double value= sensorData.getValueNum();
        //获取字段的上下阈值
        SensorFields sensorFields = sensorFieldsMapper.selectById(fieldId);
        if(sensorFields.getAlterIntensity().equals(0)){
            //此时标识没有设置报警功能
            return false;
        }
        Double valueTop=sensorFields.getAlterTop();
        Double valueDown=sensorFields.getAlterDown();
        if(value<valueDown||value>valueTop){
            //此时说明已经超过了阈值
            SensorAlarmRecords records=new SensorAlarmRecords();
            records.setIsCleared(0);
            records.setClearedTime(null);
            records.setClearedDescription(null);
            records.setAlarmIntensity(sensorFields.getAlterIntensity());
            records.setDeviceId(sensorData.getDeviceId());
            records.setAlarmValue(value);
            records.setFieldId(fieldId);
            records.setAlarmTime(LocalDateTime.now());
            records.setAlarmDescription(sensorFields.getAlterDescription());
            //记录告警记录
            return sensorAlarmRecordsMapper.insert(records) > 0;
        }
        return false;
    }

    /**
     * 根据alarmAction的要求发送MQTT消息
     * @param alarmActions
     */
    private void sendMqttMsg(AlarmActions alarmActions){
        String actionMsg = alarmActions.getActionMsg();
        String actionTopic=alarmActions.getActionTopic();
        int actionQos=alarmActions.getActionQos();
        mqttGateway.sendToMqtt(actionMsg,actionTopic,actionQos);
    }

    /**
     * 执行python脚本，向远程服务器发送数据
     * @param val
     */
    /* public void invokePython(Double val){
        log.info("force  == {}",val);
        //此处将力传感器数据送至python执行socket代码
        if(PythonHelper.sendDgramData(val)){
            log.info("运行python脚本成功");
        }
    }*/
}
