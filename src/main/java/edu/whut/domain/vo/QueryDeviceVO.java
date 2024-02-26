package edu.whut.domain.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QueryDeviceVO implements Serializable {
    private Integer did;
    private String dName;
    private String dMark;
    private Integer dStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String dIp;
}
