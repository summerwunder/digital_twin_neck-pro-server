package edu.whut.domain.dto;

import lombok.Data;

/**
 * 用于添加告警反制手段
 */
@Data
public class AlarmActionDTO {
    private String name;
    private String topic;
    private Integer qos;
    private String msg;
}
