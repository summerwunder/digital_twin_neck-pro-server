package edu.whut.mapper;

import edu.whut.domain.vo.AlarmRecordVO;
import edu.whut.pojo.SensorAlarmRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_alarm_records】的数据库操作Mapper
* @createDate 2024-03-05 17:16:40
* @Entity edu.whut.pojo.SensorAlarmRecords
*/
public interface SensorAlarmRecordsMapper extends BaseMapper<SensorAlarmRecords> {

    List<AlarmRecordVO> getAllAlarmRecords(
            @Param("sensorId") Integer sensorId, @Param("size") Integer size);
}




