package edu.whut.config.websocket;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.constants.ConnectConstants;
import edu.whut.constants.HttpStatus;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Getter
@ServerEndpoint("/ws/excel/{userId}/{deviceId}")
public class WebSocketExcelServer {
    private Session session;

    private Integer userId;

    private Integer deviceId;
    public static ConcurrentHashMap<Integer,WebSocketExcelServer> webSocketMap
            =new ConcurrentHashMap<>();

    @Autowired
    private static ResourceLoader resourceLoader;
    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        WebSocketExcelServer.resourceLoader = resourceLoader;
    }
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
                this.sendWebInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public void sendWebInfo() throws IOException {
        Resource resource = resourceLoader.getResource(ConnectConstants.excelPath);
        File file = resource.getFile();
        //获取点云数据
        List<DotInfo> dots= ExcelReaderUtils.getDotInfoList(file);
        ObjectMapper objectMapper=new ObjectMapper();
        //发送数据
        String str = objectMapper.writeValueAsString(dots);
        this.sendMessage(str,this.session);
    }
}
