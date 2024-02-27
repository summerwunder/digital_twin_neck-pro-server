package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @TableName t_sensor_data
 */
@TableName(value ="t_sensor_data")
@Data
public class SensorData implements Serializable {
    @TableId
    private Integer sid;

    private Integer deviceId;

    private Integer fieldId;

    private Double valueNum;

    private String valueStr;

    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}