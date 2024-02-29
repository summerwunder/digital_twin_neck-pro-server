package edu.whut.service;

import edu.whut.domain.dto.QueryDeviceDTO;
import edu.whut.domain.vo.AllDeviceInfoVO;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.pojo.Devices;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.whut.response.PageResult;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_devices】的数据库操作Service
* @createDate 2024-02-26 19:43:40
*/
public interface DevicesService extends IService<Devices> {

    PageResult getPageDevices(QueryDeviceDTO queryDevice);

    List<AllDeviceInfoVO> queryAllDeviceInfo();
}
