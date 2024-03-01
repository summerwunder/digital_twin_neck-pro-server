package edu.whut.config.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

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
                    List<SensorData> sensorDataList = mapper.readValue(message.getPayload().toString(), new TypeReference<List<SensorData>>(){});
                    //此处自动填充不可用！！！手动添加
                    LocalDateTime currentTime = LocalDateTime.now();
                    for (SensorData data : sensorDataList) {
                        data.setUpdateTime(currentTime);
                        // 将数据插入数据库或进行其他操作
                        dataMapper.insert(data);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return handler;
    }
}
