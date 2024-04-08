package edu.whut.config.websocket;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.constants.ConnectConstants;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.vo.SensorDataVO;
import edu.whut.exception.ServiceException;
import edu.whut.mapper.SensorDataMapper;
import edu.whut.pojo.DotInfo;
import edu.whut.utils.excel.ExcelReaderUtils;
import edu.whut.utils.security.SecurityUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Getter
@ServerEndpoint("/ws/excel/{userId}/{deviceId}")
public class WebSocketExcelServer {
    private Session session;

    private Integer userId;

    private Integer deviceId;
    public  ConcurrentHashMap<Integer,WebSocketExcelServer> webSocketMap
            =new ConcurrentHashMap<>();

    @Autowired
    private static ResourceLoader resourceLoader;

    //保存上一次的数据
    private List<SensorDataVO> sensorDataList=new ArrayList<>();

    //WebSocket需要如此自动注入
    @Autowired
    private static SensorDataMapper sensorDataMapper;
    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        WebSocketExcelServer.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setSensorDataMapper(SensorDataMapper sensorDataMapper) {
        WebSocketExcelServer.sensorDataMapper = sensorDataMapper;
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")
                    int userId,@PathParam("deviceId")int deviceId){
        this.session=session;
        this.userId=userId;
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
            try {
                this.sendWebInfo(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * 按照一定事件发送消息
         */
        scheduler.scheduleAtFixedRate(() -> {

            //TODO 固定为指定力传感器发送数据，后期会修改

            List<SensorDataVO> latestSensorData =
                    sensorDataMapper.getLatestSensorDataByDeviceId(deviceId, Arrays.asList(13));
            log.warn("data----{}",latestSensorData);
            if (sensorDataList.equals(latestSensorData)) {
                //说明数据未更新
                return;
            }else{
                //说明数据更新
                this.sensorDataList=latestSensorData;
                //更新模型
                if(latestSensorData.size()==1){
                    Double sensorValue = latestSensorData.get(0).getValueNum();
                    try {
                        //发送网格数据
                        sendExcel(sensorValue);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        },
        0, 2, TimeUnit.SECONDS);
    }

    private void sendExcel(Double sensorValue) throws IOException {
        if(sensorValue<=0.3){
            sendWebInfo("classpath:0.2n.xlsx");
        }else if(sensorValue<=0.6){
            sendWebInfo("classpath:0.5n.xlsx");
        }else if(sensorValue<=1.0){
            sendWebInfo("classpath:0.8n.xlsx");
        }else if(sensorValue<=1.4){
            sendWebInfo("classpath:1.2n.xlsx");
        }else{
            sendWebInfo("classpath:1.6n.xlsx");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
        }
        log.info("用户退出:"+userId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("用户错误:"+this.userId+",原因:"+throwable.getMessage());
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // 在收到消息时的处理逻辑，可以根据实际需求来实现
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message,Session session) {
        try {
            if (session.isOpen()) {
                synchronized(session){
                    session.getBasicRemote().sendText(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据的主要函数
     * @throws IOException
     */
    public void sendWebInfo(String path) throws IOException {
        Resource resource=null;
        if(ObjectUtil.isNull(path)){
            resource = resourceLoader.getResource(ConnectConstants.excelPath);
        }else{
            resource = resourceLoader.getResource(path);
        }
        File file = resource.getFile();
        //获取点云数据
        List<DotInfo> dots= ExcelReaderUtils.getDotInfoList(file);
        ObjectMapper objectMapper=new ObjectMapper();
        //发送数据
        String str = objectMapper.writeValueAsString(dots);
        this.sendMessage(str,this.session);
    }
}
