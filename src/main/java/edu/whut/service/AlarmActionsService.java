package edu.whut.service;

import edu.whut.domain.dto.AlarmActionDTO;
import edu.whut.pojo.AlarmActions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_alarm_actions(告警措施表)】的数据库操作Service
* @createDate 2024-03-06 17:35:56
*/
public interface AlarmActionsService extends IService<AlarmActions> {

    boolean addAlarmAction(AlarmActionDTO alarmActionDTO);

    List<AlarmActions> getAllAlarmActions();
}
