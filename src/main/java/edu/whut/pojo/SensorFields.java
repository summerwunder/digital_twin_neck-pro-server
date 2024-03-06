package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @TableName t_sensor_fields
 */
@TableName(value ="t_sensor_fields")
@Data
public class SensorFields implements Serializable {
    @TableId
    private Integer nid;

    private String nName;

    private String nUnit;

    private String nDescription;

    //是否告警，1为报警，0为不报警
    private Integer isAlter;

    //告警上限阈值
    private Double alterTop;

    private Double alterDown;

    //告警标注
    private String alterDescription;

    //告警强度 1 2 3
    private Integer alterIntensity;

    @Version
    private Integer version;

    @TableLogic
    private Integer isDeleted;


    private static final long serialVersionUID = 1L;
}