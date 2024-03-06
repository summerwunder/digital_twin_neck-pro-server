package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @TableName t_alarm_actions
 */
@TableName(value ="t_alarm_actions")
@Data
public class AlarmActions implements Serializable {
    @TableId
    private Integer id;

    private String actionName;

    private Integer actionQos;

    private String actionTopic;

    private String actionMsg;

    @TableField(fill=FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer isDeleted;

    @Version
    private Integer version;

    @TableField(exist = false)
    private Integer alarmIntensity;

    private static final long serialVersionUID = 1L;
}