package edu.whut.controller;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.AlarmActionDTO;
import edu.whut.pojo.AlarmActions;
import edu.whut.response.Result;
import edu.whut.service.AlarmActionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sys/alarm/action")
@Slf4j
public class AlarmActionController {
    @Autowired
    private AlarmActionsService alarmActionsService;
    @PostMapping
    public Result addAlarmAction(@RequestBody AlarmActionDTO alarmActionDTO){
        if(ObjectUtil.isNotNull(alarmActionDTO)){
            boolean isOk=alarmActionsService.addAlarmAction(alarmActionDTO);
            if(isOk){
                return Result.success("添加成功");
            }
            return Result.success("添加失败");
        }
        return Result.error(HttpStatus.BAD_REQUEST,"");
    }
    @GetMapping
    public Result getAllAlarmActions(){
        List<AlarmActions> alarmActionsList= alarmActionsService.getAllAlarmActions();
        return Result.success(alarmActionsList);
    }

    /**
     * 删除告警措施
     * @param actionId
     * @return
     */
    @DeleteMapping
    public Result delAlarmAction(@RequestParam Integer actionId){
        boolean isOk=alarmActionsService.delAlarmAction(actionId);
        if(isOk){
            return Result.success("删除成功");
        }
        return Result.success("删除失败");
    }
}
