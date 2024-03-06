package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.AlarmActionDTO;
import edu.whut.pojo.AlarmActions;
import edu.whut.service.AlarmActionsService;
import edu.whut.mapper.AlarmActionsMapper;
import edu.whut.utils.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @author wunder
* @description 针对表【t_alarm_actions(告警措施表)】的数据库操作Service实现
* @createDate 2024-03-06 17:35:56
*/
@Service
public class AlarmActionsServiceImpl extends ServiceImpl<AlarmActionsMapper, AlarmActions>
    implements AlarmActionsService{

    @Autowired
    private AlarmActionsMapper alarmActionsMapper;

    /**
     * 添加报警对象
     * @param alarmActionDTO
     * @return
     */
    @Override
    public boolean addAlarmAction(AlarmActionDTO alarmActionDTO) {
        AlarmActions alarmActions=new AlarmActions();
        //设置基本信息
        alarmActions.setActionMsg(alarmActionDTO.getMsg());
        alarmActions.setActionQos(alarmActionDTO.getQos());
        alarmActions.setActionName(alarmActionDTO.getName());
        alarmActions.setActionTopic(alarmActionDTO.getTopic());
        //此处日期为自动注入
        return  alarmActionsMapper.insert(alarmActions)>0;
    }

    @Override
    public List<AlarmActions> getAllAlarmActions() {
        Integer userId= SecurityUtil.getUserId();
        List<Map<Integer, Integer>> alarmActionIds =
                alarmActionsMapper.getAllAlarmActionIds(userId);
        //存储所有的数据信息
        List<AlarmActions> alarmActionsList=new ArrayList<>();
        for(Map<Integer,Integer> alarmMapIntensity:alarmActionIds){
            Integer alarmActionId = alarmMapIntensity.get("alarm_action_id");
            Integer alarmIntensity = alarmMapIntensity.get("alarm_intensity");
            AlarmActions alarmActions = alarmActionsMapper.selectById(alarmActionId);
            if(ObjectUtil.isNotNull(alarmActions)){
                alarmActions.setAlarmIntensity(alarmIntensity);
                alarmActionsList.add(alarmActions);
            }
        }
        return alarmActionsList;
    }
}




