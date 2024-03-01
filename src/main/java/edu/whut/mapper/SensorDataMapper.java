package edu.whut.mapper;

import edu.whut.domain.vo.QuerySensorDataVO;
import edu.whut.domain.vo.SensorDataVO;
import edu.whut.pojo.SensorData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_data】的数据库操作Mapper
* @createDate 2024-02-27 21:52:30
* @Entity edu.whut.pojo.SensorData
*/
public interface SensorDataMapper extends BaseMapper<SensorData> {

    List<SensorDataVO> getLatestSensorDataByDeviceId(@Param("deviceId")Integer deviceId);

    List<QuerySensorDataVO> querySensorData(@Param("deviceId") Integer deviceId, @Param("sensorId") Integer sensorId);
}




