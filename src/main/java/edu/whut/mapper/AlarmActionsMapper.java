package edu.whut.mapper;

import edu.whut.pojo.AlarmActions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author wunder
* @description 针对表【t_alarm_actions(告警措施表)】的数据库操作Mapper
* @createDate 2024-03-06 17:35:56
* @Entity edu.whut.pojo.AlarmActions
*/
public interface AlarmActionsMapper extends BaseMapper<AlarmActions> {


    List<Map<Integer, Integer>> getAllAlarmActionIds(@Param("userId") Integer userId);
}




