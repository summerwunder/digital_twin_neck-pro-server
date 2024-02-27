package edu.whut.config.mqtt.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutBoundChannel")
public interface MqttGateway {
    void sendToMqtt(String payload);

    void sendToMqttWithQos(String payload, @Header(MqttHeaders.QOS) int qos);
}
