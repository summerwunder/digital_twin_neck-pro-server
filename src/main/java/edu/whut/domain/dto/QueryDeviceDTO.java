package edu.whut.domain.dto;

import lombok.Data;
import org.apache.ibatis.plugin.Interceptor;

@Data
public class QueryDeviceDTO {
    private String deviceName;
    private String loginIp;
    private Integer pageNum;
    private Integer pageSize;
}
