package edu.whut.controller;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.config.mqtt.gateway.MqttGateway;
import edu.whut.domain.dto.SensorDataChartsDTO;
import edu.whut.domain.dto.UploadDataDTO;
import edu.whut.domain.vo.QuerySensorDataVO;
import edu.whut.domain.vo.UploadDataVO;
import edu.whut.response.Result;
import edu.whut.service.SensorDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("sys/data")
public class DataController {
    @Autowired
    private MqttGateway gateway;
    @Autowired
    private SensorDataService sensorDataService;

    /**
     * 获取传感器数据渲染表格
     * @param sensorDataChartsDTO
     * @return
     */
    @PostMapping("list")
    public Result getDataList(@RequestBody SensorDataChartsDTO sensorDataChartsDTO){
        List<QuerySensorDataVO> sensorDataVOS=
                sensorDataService.querySensorData(sensorDataChartsDTO);
        Map<String,Object> map=new HashMap<>();
        map.put("data",sensorDataVOS);
        if(ObjectUtil.isEmpty(sensorDataVOS)){
            map.put("msg","无对应的传感器数据");
        }else{
            map.put("msg","成功获取数据");
        }
        return Result.success(map);
    }

    /**
     * 用于获取既定时间的传感器数据渲染下载表格
     * @param uploadDataDTO
     * @return
     */
    @PostMapping("upload")
    public Result getDataList(@RequestBody UploadDataDTO uploadDataDTO){
        List<UploadDataVO> uploadDataVOList
                =sensorDataService.uploadSensorData(uploadDataDTO);
        //返回数据
        Map<String,Object> map=new HashMap<>();
        map.put("data",uploadDataVOList);
        if(ObjectUtil.isEmpty(uploadDataVOList)){
            map.put("msg","无对应的传感器数据");
        }else{
            map.put("msg","成功获取数据");
            map.put("size",uploadDataVOList.size());
        }
        return Result.success(map);
    }
}
