package edu.whut.config.websocket;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.constants.HttpStatus;
import edu.whut.exception.ServiceException;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/ws/excel/{userId}/{deviceId}")
public class WebSocketExcelServer {
    private Session session;

    private Integer userId;

    private Integer deviceId;
    private static ConcurrentHashMap<Integer,WebSocketExcelServer> webSocketMap
            =new ConcurrentHashMap<>();

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
        }
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

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("用户错误:"+this.userId+",原因:"+throwable.getMessage());
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // 在收到消息时的处理逻辑，可以根据实际需求来实现
    }

}
