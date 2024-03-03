package edu.whut.service;

import edu.whut.domain.dto.AddLinkDTO;
import edu.whut.pojo.FieldsMapDevices;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wunder
* @description 针对表【t_fields_map_devices】的数据库操作Service
* @createDate 2024-03-02 10:35:12
*/
public interface FieldsMapDevicesService extends IService<FieldsMapDevices> {

    boolean addLinker(AddLinkDTO addLinkDTO);

    void delMapByDeviceId(Integer deviceId);
}
