package edu.whut.config.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.mapper.SensorDataMapper;
import edu.whut.pojo.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MqttSubHandler {
    @Autowired
    private SensorDataMapper  dataMapper;
    @Bean
    @ServiceActivator(inputChannel = "mqttInBoundChannel" )
    public MessageHandler mqttInputHandler(){
        MessageHandler handler=new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                //System.out.println(message);
                System.out.println("收到订阅消息：消息主题"+message.getHeaders().get("mqtt_receivedTopic").toString());
                System.out.println("消息内容"+message.getPayload().toString());

                ObjectMapper mapper=new ObjectMapper();
                try {
                    SensorData data = mapper.readValue(message.getPayload().toString(), SensorData.class);
                    //此处自动填充不可用！！！手动添加
                    data.setUpdateTime(LocalDateTime.now());
                    dataMapper.insert(data);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return handler;
    }
}
