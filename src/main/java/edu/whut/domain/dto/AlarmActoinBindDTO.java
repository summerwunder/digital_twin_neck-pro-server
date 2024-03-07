package edu.whut.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlarmActoinBindDTO {
    private Integer alarmActionId;
    private List<Integer> alarmIntensity;
}
