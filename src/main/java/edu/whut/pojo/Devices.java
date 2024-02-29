package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName t_devices
 */
@Data
public class Devices implements Serializable {
    @TableId
    private Integer did;

    private String dNumber;

    private String dName;

    private String dMark;

    private Integer dStatus;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String dIp;

    @Version
    private Integer version;
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private List<SensorFields> sensorFieldsList;

    private static final long serialVersionUID = 1L;
}