package edu.whut.controller;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.domain.dto.AddLinkDTO;
import edu.whut.domain.vo.AlarmRecordVO;
import edu.whut.pojo.SensorAlarmRecords;
import edu.whut.pojo.SensorFields;
import edu.whut.response.PageResult;
import edu.whut.response.Result;
import edu.whut.service.FieldsMapDevicesService;
import edu.whut.service.SensorAlarmRecordsService;
import edu.whut.service.SensorFieldsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("sys/sensor")
public class SensorFieldController {
    @Autowired
    private SensorFieldsService sensorFieldsService;
    @Autowired
    private FieldsMapDevicesService fieldsMapDevicesService;
    @Autowired
    private SensorAlarmRecordsService sensorAlarmRecordsService;
    @GetMapping("/list")
    public Result getSensorList(@RequestParam String sensorName,
                                @RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        //log.info("sensorName--->{}",sensorName);
        PageResult pageResult=sensorFieldsService.getPageSensors(sensorName,pageNum,pageSize);
        return Result.success("fine",pageResult);
    }

    /**
     * 获取所有传感器的信息
     * @return
     */
    @GetMapping("/all")
    public Result getAllSensors(){
        List<SensorFields> sensorFieldsList=
                sensorFieldsService.getAllSensors();
        return Result.success(sensorFieldsList);
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

    /**
     * 获取指定传感器的报警记录
     */
    @GetMapping("alarm")
    public Result getAllAlarmRecords(@RequestParam("sensorId") Integer sensorId
            ,@RequestParam("size")Integer size){
        if(ObjectUtil.isNull(sensorId)){
            return Result.error("缺少参数");
        }
        List<AlarmRecordVO> sensorAlarmRecords
                =sensorAlarmRecordsService.getAllAlarmRecords(sensorId,size);
        return Result.success(sensorAlarmRecords);
    }
}
