package edu.whut.service;

import edu.whut.domain.dto.AlarmRecordUpdateDTO;
import edu.whut.domain.vo.AlarmRecordVO;
import edu.whut.pojo.SensorAlarmRecords;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_alarm_records】的数据库操作Service
* @createDate 2024-03-05 17:16:40
*/
public interface SensorAlarmRecordsService extends IService<SensorAlarmRecords> {

    List<AlarmRecordVO> getAllAlarmRecords(Integer sensorId, Integer size);

    boolean updateAlarmInfo(AlarmRecordUpdateDTO recordUpdateDTO);
}
