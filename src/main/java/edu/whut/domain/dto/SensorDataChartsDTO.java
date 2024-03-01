package edu.whut.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class SensorDataChartsDTO {
    private Integer deivceId;
    private List<Integer> sensorIds;
}
