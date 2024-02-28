package edu.whut.config.websocket;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
@ServerEndpoint("/ws/message/{userId}")
public class WebSocketServer {
    private Session session;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    private Integer userId;

    private Integer deviceId;

    private static ConcurrentHashMap<Integer,WebSocketServer> webSocketMap=new ConcurrentHashMap();
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")int userId,@PathParam("userId")int deviceId){
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
        log.info("连接成功，deviceId"+deviceId+"-----userId:"+userId);
        sendMessage("连接成功");
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
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




