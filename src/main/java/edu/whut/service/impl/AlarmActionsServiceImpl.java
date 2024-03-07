package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.AlarmActionDTO;
import edu.whut.domain.dto.AlarmActoinBindDTO;
import edu.whut.mapper.UserMapAlarmActionsMapper;
import edu.whut.pojo.AlarmActions;
import edu.whut.pojo.UserMapAlarmActions;
import edu.whut.response.Result;
import edu.whut.service.AlarmActionsService;
import edu.whut.mapper.AlarmActionsMapper;
import edu.whut.utils.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserMapAlarmActionsMapper userMapAlarmActionsMapper;
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
        alarmActionsMapper.insert(alarmActions);
        //同时需要在用户对应策略表上添加
        UserMapAlarmActions userMapAlarmActions
                =new UserMapAlarmActions();
        userMapAlarmActions.setUserId(SecurityUtil.getUserId());
        userMapAlarmActions.setAlarmIntensity(0);
        //此处actionId需要判断
        LambdaQueryWrapper<AlarmActions> actionsLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        actionsLambdaQueryWrapper.eq(AlarmActions::getActionMsg,alarmActionDTO.getMsg());
        actionsLambdaQueryWrapper.eq(AlarmActions::getActionName,alarmActionDTO.getName());
        actionsLambdaQueryWrapper.eq(AlarmActions::getActionQos,alarmActionDTO.getQos());
        AlarmActions selectedOne = alarmActionsMapper.selectOne(actionsLambdaQueryWrapper);
        if(ObjectUtil.isNotNull(selectedOne)) {
            userMapAlarmActions.setAlarmActionId(selectedOne.getId());
            userMapAlarmActionsMapper.insert(userMapAlarmActions);
            return true;
        }
        return false;
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

    /**
     * 删除告警措施
     * @return
     */
    @Override
    public boolean delAlarmAction(Integer actionId) {
        if(ObjectUtil.isNotNull(alarmActionsMapper.selectById(actionId))){
           //说明存在该要素
            if(alarmActionsMapper.deleteById(actionId)>0){
                //此处继续删除对应表的数据
                LambdaQueryWrapper<UserMapAlarmActions> lambdaQueryWrapper
                        =new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(UserMapAlarmActions::getAlarmActionId,actionId);
                lambdaQueryWrapper.eq(UserMapAlarmActions::getUserId,SecurityUtil.getUserId());
                userMapAlarmActionsMapper.delete(lambdaQueryWrapper);
                return true;
            }

        }
        return false;
    }

    /**
     * 绑定强度
     * @param alarmActoinBindDTO
     */
    @Override
    public void bindAlarmAction(AlarmActoinBindDTO alarmActoinBindDTO) {
        Integer userId=SecurityUtil.getUserId();
        //先找到所有的alarmId和强度的对应关系
        LambdaQueryWrapper<UserMapAlarmActions> lambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserMapAlarmActions::getAlarmActionId,
                alarmActoinBindDTO.getAlarmActionId());
        lambdaQueryWrapper.eq(UserMapAlarmActions::getUserId,userId);
        List<UserMapAlarmActions> userMapAlarmActions =
                userMapAlarmActionsMapper.selectList(lambdaQueryWrapper);
        //获取所有的强度
        List<Integer> intensityList =
                userMapAlarmActions.stream().map(UserMapAlarmActions::getAlarmIntensity).toList();
        //转换为Set进行快速查找
        Set<Integer> originSet=new HashSet<>(intensityList);
        Set<Integer> newSet=new HashSet<>(alarmActoinBindDTO.getAlarmIntensity());
        //需要删除的元素
        List<Integer> removeList=new ArrayList<>();
        // 检查要删除的元素
        for (Integer element : originSet) {
            if (!newSet.contains(element)) {
                removeList.add(element);
            }
        }
        //先删除元素
        if (!removeList.isEmpty()) {
            LambdaQueryWrapper<UserMapAlarmActions> wrapper
                    = new LambdaQueryWrapper<>();
            wrapper.eq(UserMapAlarmActions::getAlarmActionId,
                    alarmActoinBindDTO.getAlarmActionId());
            wrapper.eq(UserMapAlarmActions::getUserId, userId);
            wrapper.in(UserMapAlarmActions::getAlarmIntensity, removeList);
            userMapAlarmActionsMapper.delete(wrapper);
        }
        // 添加元素
        List<UserMapAlarmActions> toInsert = newSet.stream()
                .filter(element -> !originSet.contains(element))
                .map(intensity -> {
                    UserMapAlarmActions action = new UserMapAlarmActions();
                    action.setUserId(userId);
                    action.setAlarmActionId(alarmActoinBindDTO.getAlarmActionId());
                    action.setAlarmIntensity(intensity);
                    return action;
                })
                .collect(Collectors.toList());
        if(ObjectUtil.isNotEmpty(toInsert)) {
            for (UserMapAlarmActions action : toInsert) {
                userMapAlarmActionsMapper.insert(action);
            }
        }
        //如果没有任何值，说明未确定
        if(ObjectUtil.isEmpty(alarmActoinBindDTO.getAlarmIntensity())){
            //此处表明要将强度设置为0
            UserMapAlarmActions userActions=new UserMapAlarmActions();
            userActions.setAlarmActionId(alarmActoinBindDTO.getAlarmActionId());
            userActions.setUserId(userId);
            userActions.setAlarmIntensity(0);
            userMapAlarmActionsMapper.insert(userActions);
        }
    }
}




