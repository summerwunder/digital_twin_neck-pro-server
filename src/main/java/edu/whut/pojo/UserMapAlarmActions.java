package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_user_map_alarm_actions
 */
@TableName(value ="t_user_map_alarm_actions")
@Data
public class UserMapAlarmActions implements Serializable {
    @TableId
    private Integer id;

    private Integer userId;

    private Integer alarmActionId;

    private Integer alarmIntensity;

    private static final long serialVersionUID = 1L;
}