package edu.whut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.vo.AlarmRecordVO;
import edu.whut.pojo.SensorAlarmRecords;
import edu.whut.service.SensorAlarmRecordsService;
import edu.whut.mapper.SensorAlarmRecordsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_alarm_records】的数据库操作Service实现
* @createDate 2024-03-05 17:16:40
*/
@Service
public class SensorAlarmRecordsServiceImpl extends ServiceImpl<SensorAlarmRecordsMapper, SensorAlarmRecords>
    implements SensorAlarmRecordsService{

    @Autowired
    private SensorAlarmRecordsMapper sensorAlarmRecordsMapper;
    @Override
    public List<AlarmRecordVO> getAllAlarmRecords(Integer sensorId, Integer size) {
        List<AlarmRecordVO> alarmRecordVOS=
                sensorAlarmRecordsMapper.getAllAlarmRecords(sensorId,size);
        return alarmRecordVOS;
    }
}




