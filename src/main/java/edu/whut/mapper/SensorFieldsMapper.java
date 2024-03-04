package edu.whut.mapper;

import edu.whut.pojo.SensorFields;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_fields】的数据库操作Mapper
* @createDate 2024-02-26 22:58:28
* @Entity edu.whut.pojo.SensorFields
*/
public interface SensorFieldsMapper extends BaseMapper<SensorFields> {

    List<SensorFields> getAllSensors(@Param("userId") Integer userId);
}




