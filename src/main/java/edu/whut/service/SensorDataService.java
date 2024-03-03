package edu.whut.service;

import edu.whut.domain.dto.SensorDataChartsDTO;
import edu.whut.domain.vo.QuerySensorDataVO;
import edu.whut.domain.vo.SensorDataVO;
import edu.whut.pojo.SensorData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_data】的数据库操作Service
* @createDate 2024-02-27 21:52:30
*/
public interface SensorDataService extends IService<SensorData> {

    List<QuerySensorDataVO> querySensorData(SensorDataChartsDTO sensorDataChartsDTO);

    void delDataByDeviceId(Integer deviceId);
}
