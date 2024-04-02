package edu.whut.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UploadDataVO implements Serializable {
    private String sensorName;
    private LocalDateTime updateTime;
    private Double valueNum;
    private String valueStr;
}
