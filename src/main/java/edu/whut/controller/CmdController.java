package edu.whut.controller;


import edu.whut.config.mqtt.gateway.MqttGateway;
import edu.whut.domain.dto.CmdDTO;
import edu.whut.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cmd")
@Slf4j
public class CmdController {

    @Autowired
    private MqttGateway mqttGateway;

    /**
     * 根据指令发送命令
     * @param commandDTO
     * @return
     */
    @PostMapping
    public Result invokeMqttCmd(@RequestBody CmdDTO commandDTO){
        mqttGateway.sendToMqtt(commandDTO.getCommand(),
                commandDTO.getPubTopic(),
                commandDTO.getQos());
        return Result.success();
    }

}
