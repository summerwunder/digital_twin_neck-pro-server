package edu.whut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.pojo.SensorData;
import edu.whut.service.SensorDataService;
import edu.whut.mapper.SensorDataMapper;
import org.springframework.stereotype.Service;

/**
* @author wunder
* @description 针对表【t_sensor_data】的数据库操作Service实现
* @createDate 2024-02-27 21:52:30
*/
@Service
public class SensorDataServiceImpl extends ServiceImpl<SensorDataMapper, SensorData>
    implements SensorDataService{

}




