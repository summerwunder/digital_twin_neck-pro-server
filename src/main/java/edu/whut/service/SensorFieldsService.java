package edu.whut.service;

import edu.whut.pojo.SensorFields;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.whut.response.PageResult;

/**
* @author wunder
* @description 针对表【t_sensor_fields】的数据库操作Service
* @createDate 2024-02-26 22:58:28
*/
public interface SensorFieldsService extends IService<SensorFields> {

    PageResult getPageSensors(String sensorName, Integer pageNum, Integer pageSize);
}