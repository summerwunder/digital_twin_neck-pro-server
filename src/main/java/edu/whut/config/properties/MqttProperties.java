package edu.whut.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MqttProperties {

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.timeout}")
    private Integer timeout;

    @Value("${mqtt.keepAlive}")
    private Integer keepAlive;

    @Value("${mqtt.qos}")
    private Integer qos;

    @Value("${mqtt.pubTopic}")
    private String[] pubTopic;

    @Value("${mqtt.subTopic}")
    private String subTopic;

}
