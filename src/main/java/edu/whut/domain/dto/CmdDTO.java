package edu.whut.domain.dto;

import lombok.Data;

@Data
public class CmdDTO {
    private String command;
    private String pubTopic;
    private Integer qos;
}
