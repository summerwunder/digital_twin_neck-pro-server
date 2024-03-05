package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @TableName t_sensor_alarm_records
 */
@TableName(value ="t_sensor_alarm_records")
@Data
public class SensorAlarmRecords implements Serializable {
    @TableId
    private Integer aid;

    private Integer deviceId;

    private Integer fieldId;

    private LocalDateTime alarmTime;

    private String alarmDescription;

    private Integer alarmIntensity;

    private Double alarmValue;

    private Integer isCleared;

    private LocalDateTime clearedTime;

    private String clearedDescription;

    private static final long serialVersionUID = 1L;
}