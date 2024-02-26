package edu.whut.domain.dto;

import lombok.Data;

@Data
public class QuerySensorFieldDTO {
    private String sensorName;
    private Integer pageNum;
    private Integer pageSize;
}
