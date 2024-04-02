package edu.whut.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class UploadDataDTO {
    private List<String> dateTimeRange;
    private Integer deviceId;
    private Integer sensorFieldId;
    private String sensorFieldName;
}
