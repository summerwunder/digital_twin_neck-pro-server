package edu.whut.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class SensorDataVO {
    private String sensorFieldName;
    private Double valueNum;//数值
    private String valueStr;//字符数值
    private LocalDateTime updateTime;
}
