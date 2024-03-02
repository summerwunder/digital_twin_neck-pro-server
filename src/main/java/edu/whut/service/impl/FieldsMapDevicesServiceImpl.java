package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.AddLinkDTO;
import edu.whut.pojo.FieldsMapDevices;
import edu.whut.service.FieldsMapDevicesService;
import edu.whut.mapper.FieldsMapDevicesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author wunder
* @description 针对表【t_fields_map_devices】的数据库操作Service实现
* @createDate 2024-03-02 10:35:12
*/
@Service
public class FieldsMapDevicesServiceImpl extends ServiceImpl<FieldsMapDevicesMapper, FieldsMapDevices>
    implements FieldsMapDevicesService{

    @Autowired
    private FieldsMapDevicesMapper mapper;
    @Override
    public boolean addLinker(AddLinkDTO addLinkDTO) {
        LambdaQueryWrapper<FieldsMapDevices> lambdaQueryWrapper=
                new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(FieldsMapDevices::getDId,addLinkDTO.getDeviceId());
        lambdaQueryWrapper.eq(FieldsMapDevices::getFId,addLinkDTO.getSensorId());
        FieldsMapDevices fieldsMapDevices = mapper.selectOne(lambdaQueryWrapper);
        if(ObjectUtil.isNull(fieldsMapDevices)){
            //说明还没有此类的绑定
            FieldsMapDevices devices=new FieldsMapDevices();
            devices.setDId(addLinkDTO.getDeviceId());
            devices.setFId(addLinkDTO.getSensorId());
            mapper.insert(devices);
            return true;
        }
        return false;
    }
}




