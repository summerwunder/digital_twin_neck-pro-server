package edu.whut.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AlarmRecordVO {
    //设备基本信息
    private Integer dId;

    private String dName;

    private String dMark;
    //传感器告警信息
    private LocalDateTime alarmTime;

    private String alarmDescription;

    private Integer alarmIntensity;
    //告警的数值
    private Double alarmValue;
    //是否被清除
    private Integer isCleared;

    private LocalDateTime clearedTime;

    private String clearedDescription;
}
