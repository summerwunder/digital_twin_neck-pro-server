package edu.whut.domain.dto;

import lombok.Data;

@Data
public class SensorFieldAddDTO {
    private String fieldName;
    private String fieldUnit;
    private String fieldDescription;
    private Integer isAlert;
    private Integer alertIntensity;
    private Double alertTop;
    private Double alertDown;
    private String alertMark;
}
