package edu.whut.controller;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.config.mqtt.gateway.MqttGateway;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.SensorDataChartsDTO;
import edu.whut.domain.vo.QuerySensorDataVO;
import edu.whut.domain.vo.SensorDataVO;
import edu.whut.pojo.SensorData;
import edu.whut.response.Result;
import edu.whut.service.SensorDataService;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("sys/data")
public class DataController {
    @Autowired
    private MqttGateway gateway;
    @Autowired
    private SensorDataService sensorDataService;
    @PostMapping("list")
    public Result getDataList(@RequestBody SensorDataChartsDTO sensorDataChartsDTO){
        //log.error("fine");
        //System.out.println(SecurityUtil.getLoginUser().getSysUser().getUserName());
        //System.out.println(SecurityUtil.getLoginUser().getSysUser().getUserPwd());
        List<QuerySensorDataVO> sensorDataVOS=sensorDataService.querySensorData(sensorDataChartsDTO);
        if(ObjectUtil.isEmpty(sensorDataVOS)){
            return Result.error(HttpStatus.BAD_REQUEST,"请求格式错误");
        }
        return Result.success(sensorDataVOS);
    }

    @PostMapping("mqtt")
    public String publish(@RequestBody String data){
        gateway.sendToMqtt(data);
        return "ok";
    }
}
