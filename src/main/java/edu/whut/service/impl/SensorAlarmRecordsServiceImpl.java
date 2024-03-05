package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.AlarmRecordUpdateDTO;
import edu.whut.domain.vo.AlarmRecordVO;
import edu.whut.pojo.SensorAlarmRecords;
import edu.whut.service.SensorAlarmRecordsService;
import edu.whut.mapper.SensorAlarmRecordsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public boolean updateAlarmInfo(AlarmRecordUpdateDTO recordUpdateDTO) {
        //先查看是否存在相关数据
        if(ObjectUtil.isNotNull(sensorAlarmRecordsMapper.selectById(recordUpdateDTO.getAlarmId()))){
            LambdaQueryWrapper<SensorAlarmRecords> queryWrapper
                    =new LambdaQueryWrapper<>();
            queryWrapper.eq(SensorAlarmRecords::getAid,recordUpdateDTO.getAlarmId());
            SensorAlarmRecords sensorAlarmRecords
                    =new SensorAlarmRecords();
            //修改相关信息
            sensorAlarmRecords.setClearedTime(LocalDateTime.now());
            sensorAlarmRecords.setIsCleared(1);
            sensorAlarmRecords.setClearedDescription(recordUpdateDTO.getAlarmDescription());
            sensorAlarmRecordsMapper.update(sensorAlarmRecords,queryWrapper);
            return true;
        }
        return false;
    }
}




