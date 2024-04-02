package edu.whut.config.websocket;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.vo.SensorDataVO;
import edu.whut.exception.ServiceException;
import edu.whut.mapper.FieldsMapDevicesMapper;
import edu.whut.mapper.SensorDataMapper;
import edu.whut.pojo.FieldsMapDevices;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@ServerEndpoint("/ws/message/{userId}/{deviceId}")
public class WebSocketSensorDataServer {
    private Session session;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    private Integer userId;

    private Integer deviceId;

    private static ConcurrentHashMap<Integer, WebSocketSensorDataServer> webSocketMap=
            new ConcurrentHashMap();

    //保存上一次的数据
    private List<SensorDataVO> sensorDataList=new ArrayList<>();

    //WebSocket需要如此自动注入
    @Autowired
    private static SensorDataMapper sensorDataMapper;
    @Autowired
    private static FieldsMapDevicesMapper fieldsMapDevicesMapper;
    @Autowired
    public void setSensorDataMapper(SensorDataMapper sensorDataMapper) {
        WebSocketSensorDataServer.sensorDataMapper = sensorDataMapper;
    }
    @Autowired
    public void setFieldsMapDevicesMapper(FieldsMapDevicesMapper fieldsMapDevicesMapper) {
        WebSocketSensorDataServer.fieldsMapDevicesMapper = fieldsMapDevicesMapper;
    }
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")int userId,@PathParam("deviceId")int deviceId){
        this.session = session;
        this.userId= userId;
        this.deviceId=deviceId;
        if(ObjectUtil.isNull(userId)){
            throw new ServiceException(HttpStatus.ERROR,"未登陆，无法读取信息");
        }
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //加入set中
            webSocketMap.put(userId,this);
        }else{
            //加入set中
            webSocketMap.put(userId,this);
        }
        /**
         * 按照一定事件发送消息
         */
        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendSensorData();
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        },
        0, 1, TimeUnit.SECONDS);
        log.info("连接成功，deviceId"+deviceId+"-----userId:"+userId);
        //sendMessage("连接成功");
    }
    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
        }
        log.info("用户退出:"+userId);
    }

    /**
     * 收到客户端消
     * 息后调用的方法
     * @param message
     * 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存数据库、redis
        if(ObjectUtil.isNotEmpty(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //获取指令
                String order=jsonObject.getString("order");
                //传送给对应toUserId用户的websocket
                if(ObjectUtil.isNotEmpty(order)){
                    log.info("收到数据");
                    //这里可以处理命令，在本项目也可以什么都不做
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务
     * 器主动推送
     */
    public void sendMessage(String message) {
        try {
            if (this.session.isOpen()) {
                synchronized(session){
                    this.session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SensorData数据并发送
     */
    public void sendSensorData() throws JsonProcessingException {
        LambdaQueryWrapper<FieldsMapDevices> fieldsMapDevicesLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        fieldsMapDevicesLambdaQueryWrapper.eq(FieldsMapDevices::getDId,this.deviceId);
        //查询所有该设备存在的传感器字段id
        List<Integer> sensorIds =
                fieldsMapDevicesMapper.selectList(fieldsMapDevicesLambdaQueryWrapper)
                        .stream()
                .map(FieldsMapDevices::getFId)
                        .toList();
        List<SensorDataVO> sensorDataList=
                sensorDataMapper.getLatestSensorDataByDeviceId(this.deviceId,sensorIds);
        //log.info("传感器数据--------{}",sensorDataList);
        //如果两次数据一样就不传递
        if(this.sensorDataList.equals(sensorDataList)){
            return;
        }
        //更新上一次的数据
        this.sensorDataList=sensorDataList;
        //需要序列化时间，导入包
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        String str = objectMapper.writeValueAsString(sensorDataList);
        log.warn(str);
        this.sendMessage(str);
    }
}




