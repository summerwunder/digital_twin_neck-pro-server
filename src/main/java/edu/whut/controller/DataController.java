package edu.whut.controller;

import edu.whut.config.mqtt.gateway.MqttGateway;
import edu.whut.pojo.SensorData;
import edu.whut.response.Result;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("sys/data")
public class DataController {
    @Autowired
    private MqttGateway gateway;
    @GetMapping("list")
    public Result getDataList(){
        //log.error("fine");
        //System.out.println(SecurityUtil.getLoginUser().getSysUser().getUserName());
        //System.out.println(SecurityUtil.getLoginUser().getSysUser().getUserPwd());
        return Result.success();
    }

    @PostMapping("mqtt")
    public String publish(@RequestBody String data){
        gateway.sendToMqtt(data);
        return "ok";
    }
}
