package edu.whut.controller;

import edu.whut.response.PageResult;
import edu.whut.response.Result;
import edu.whut.service.SensorFieldsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("sys/sensor")
public class SensorFieldController {
    @Autowired
    private SensorFieldsService service;
    @GetMapping("/list")
    public Result getSensorList(@RequestParam String sensorName,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        log.info("sensorName--->{}",sensorName);
        PageResult pageResult=service.getPageSensors(sensorName,pageNum,pageSize);
        return Result.success("fine",pageResult);
    }
}
