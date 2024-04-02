package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.SensorDataChartsDTO;
import edu.whut.domain.dto.UploadDataDTO;
import edu.whut.domain.vo.QuerySensorDataVO;
import edu.whut.domain.vo.UploadDataVO;
import edu.whut.pojo.SensorData;
import edu.whut.service.SensorDataService;
import edu.whut.mapper.SensorDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_data】的数据库操作Service实现
* @createDate 2024-02-27 21:52:30
*/
@Service
public class SensorDataServiceImpl extends ServiceImpl<SensorDataMapper, SensorData>
    implements SensorDataService{

    @Autowired
    private SensorDataMapper mapper;
    @Override
    public List<QuerySensorDataVO> querySensorData(SensorDataChartsDTO sensorDataChartsDTO) {
        if(ObjectUtil.isNotNull(sensorDataChartsDTO)){
            /*List<QuerySensorDataVO> querySensorDataVOS=
                    mapper.querySensorData(sensorDataChartsDTO.getDeivceId(),sensorDataChartsDTO.getSensorIds());
            return querySensorDataVOS;*/
            List<QuerySensorDataVO> querySensorDataVOS=new ArrayList<>();
            //先判断是否需要查询数据
            if(ObjectUtil.isNotEmpty(sensorDataChartsDTO.getSensorIds())){
                for(Integer sensorId:sensorDataChartsDTO.getSensorIds()){
                    List<QuerySensorDataVO> sensorData =
                            mapper.querySensorData(sensorDataChartsDTO.getDeivceId(),sensorId);
                    // 将传感器数据添加到结果列表中
                    querySensorDataVOS.addAll(sensorData.subList(0, Math.min(sensorData.size(), 20))); // 最多获取20条数据
                }
                return querySensorDataVOS;
            }
        }
        return null;
    }

    /**
     * 此前已经验证过设备id的合理性
     * 根据设备id删除传感器的数据
     * @param deviceId
     */
    @Override
    public void delDataByDeviceId(Integer deviceId) {
        LambdaQueryWrapper<SensorData> sensorDataLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        sensorDataLambdaQueryWrapper.eq(SensorData::getDeviceId,deviceId);
        mapper.delete(sensorDataLambdaQueryWrapper);
    }

    @Override
    public List<UploadDataVO> uploadSensorData(UploadDataDTO uploadDataDTO) {
        if(ObjectUtil.isNotNull(uploadDataDTO)){
            List<UploadDataVO> uploadDataVOList
                    =mapper.uploadSensorData(
                            uploadDataDTO.getDeviceId(),uploadDataDTO.getSensorFieldId(),
                            uploadDataDTO.getDateTimeRange());
            uploadDataVOList.forEach(item -> item.setSensorName(uploadDataDTO.getSensorFieldName()));
            return uploadDataVOList;
        }
        return null;
    }
}




