package edu.whut.domain.dto;

import lombok.Data;


@Data
public class SensorFieldsUpdateDTO {
    private Integer sensorId;
    private Double alertTop;
    private Double alertDown;
    private Integer alertIntensity;
}
