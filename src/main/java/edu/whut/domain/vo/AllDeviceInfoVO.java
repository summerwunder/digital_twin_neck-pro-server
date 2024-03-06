package edu.whut.domain.vo;

import edu.whut.pojo.AlarmActions;
import edu.whut.pojo.SensorData;
import edu.whut.pojo.SensorFields;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AllDeviceInfoVO implements Serializable {
    private Integer did;
    private String dName;
    private String dMark;
    private Integer dStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String dIp;
    private List<SensorFields> sensorFieldsList=new ArrayList<>();
}
