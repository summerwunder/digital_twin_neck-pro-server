package edu.whut.mapper;

import edu.whut.domain.dto.QueryDeviceDTO;
import edu.whut.domain.vo.AllDeviceInfoVO;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.pojo.Devices;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wunder
* @description 针对表【t_devices】的数据库操作Mapper
* @createDate 2024-02-26 19:43:40
* @Entity edu.whut.pojo.Devices
*/
public interface DevicesMapper extends BaseMapper<Devices> {

    List<Devices> selectListByRules
            (@Param("dName")String dName, @Param("dIp")String dIp);

    List<AllDeviceInfoVO> queryAllDeviceInfo(@Param("userId") Integer userId);
}




