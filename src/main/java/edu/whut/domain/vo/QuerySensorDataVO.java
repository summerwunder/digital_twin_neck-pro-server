package edu.whut.domain.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QuerySensorDataVO implements Serializable {
    private Integer sensorId;
    private String sensorName;
    private Double valueNum;
    private String valueStr;
    private LocalDateTime updateTime;
}
