package edu.whut.config.mqtt;

import edu.whut.config.properties.MqttProperties;
import edu.whut.utils.security.SecurityUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.event.*;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.UUID;


@Configuration
@DependsOn()
@Slf4j
public class MqttConfig {
    @Autowired
    private MqttProperties properties;
    private MqttPahoClientFactory mqttPahoClientFactory;

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        MqttConnectOptions options=new MqttConnectOptions();
        //配置超时时间
        options.setConnectionTimeout(properties.getTimeout());
        //配置代理账户信息，为登陆用户信息
        options.setPassword(UUID.randomUUID().toString().toCharArray());
        options.setUserName(UUID.randomUUID().toString());
        //配置会话信息
        options.setKeepAliveInterval(properties.getKeepAlive());
        options.setAutomaticReconnect(true);
        options.setServerURIs(new String[]{properties.getBroker()});

        DefaultMqttPahoClientFactory factory=new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        factory.setPersistence(new MemoryPersistence());
        return factory;
    }

    /*配置输出的ServiceActivator*/
    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MqttPahoMessageHandler mqttPahoMessageHandler(){
        MqttPahoMessageHandler messageHandler=
                new MqttPahoMessageHandler(UUID.randomUUID().toString(),mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setAsyncEvents(false);
        messageHandler.setDefaultQos(properties.getQos());
        messageHandler.setDefaultRetained(false);
        messageHandler.setDefaultTopic(properties.getSubTopic());
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutBoundChannel(){
        return new DirectChannel();
    }

    //订阅通道
    @Bean
    public MessageChannel mqttInBoundChannel() {
        return new DirectChannel();
    }

    //配置订阅
    @Bean
    public MessageProducer messageProducer(){
        MqttPahoMessageDrivenChannelAdapter adapter
                =new MqttPahoMessageDrivenChannelAdapter(UUID.randomUUID().toString()
                ,mqttPahoClientFactory(),properties.getPubTopic());
        adapter.setCompletionTimeout(properties.getTimeout());
        adapter.setQos(properties.getQos());
        //设置默认转换器
        adapter.setConverter(new DefaultPahoMessageConverter());
        //将从 MQTT 主题接收到的消息发送到指定的消息通道
        adapter.setOutputChannel(mqttInBoundChannel());
        return adapter;
    }


    /**
     * 监听事件
     *//*
    @EventListener
    public void handleEvent(MqttIntegrationEvent event) {
        if (event instanceof MqttConnectionFailedEvent) {
            // 连接失败
            MqttConnectionFailedEvent connectionFailedEvent = (MqttConnectionFailedEvent) event;
            log.error("[MQTT]-连接失败 - [{}] - [{}]", event.getClass().getSimpleName(), connectionFailedEvent.getCause().getMessage());
            return;
        }
        if (event instanceof MqttMessageSentEvent) {
            MqttMessageSentEvent sentEvent = (MqttMessageSentEvent) event;
            log.info("[MQTT]-消息发出 - [{}] - [{}] - [{}]", event.getClass().getSimpleName(), sentEvent.getMessageId(), sentEvent.getMessage());
        }
        if (event instanceof MqttMessageDeliveredEvent) {
            MqttMessageDeliveredEvent deliveredEvent = (MqttMessageDeliveredEvent) event;
            log.info("[MQTT]-消息送达 - [{}] - [{}]", event.getClass().getSimpleName(), deliveredEvent.getMessageId());
            return;
        }
        if (event instanceof MqttSubscribedEvent) {
            MqttSubscribedEvent subscribedEvent = (MqttSubscribedEvent) event;
            log.info("[MQTT]-消息订阅 [{}] - [{}]", event.getClass().getSimpleName(), subscribedEvent.getMessage());
            return;
        }
        log.info("[MQTT]-其他事件 - [{}] - [{}] - [{}]", event.getClass().getSimpleName(), event.getSource().toString(), event.getCause().getMessage());
    }
*/
}
