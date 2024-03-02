package edu.whut.controller;

import edu.whut.domain.dto.AddLinkDTO;
import edu.whut.response.PageResult;
import edu.whut.response.Result;
import edu.whut.service.FieldsMapDevicesService;
import edu.whut.service.SensorFieldsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("sys/sensor")
public class SensorFieldController {
    @Autowired
    private SensorFieldsService sensorFieldsService;
    @Autowired
    private FieldsMapDevicesService fieldsMapDevicesService;
    @GetMapping("/list")
    public Result getSensorList(@RequestParam String sensorName,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        log.info("sensorName--->{}",sensorName);
        PageResult pageResult=sensorFieldsService.getPageSensors(sensorName,pageNum,pageSize);
        return Result.success("fine",pageResult);
    }

    /**
     * 添加传感器字段向设备字段的绑定
     * @return
     */
    @PostMapping("link")
    public Result addLinker(@RequestBody AddLinkDTO addLinkDTO){
        boolean isfine=fieldsMapDevicesService.addLinker(addLinkDTO);
        String msg=isfine?"绑定成功":"已存在相应的绑定，绑定失败";
        return Result.success(msg);
    }
}
