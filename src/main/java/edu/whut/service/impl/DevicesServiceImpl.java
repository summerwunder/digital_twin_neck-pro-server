package edu.whut.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.whut.domain.vo.AllDeviceInfoVO;
import edu.whut.response.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.whut.domain.dto.QueryDeviceDTO;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.pojo.Devices;
import edu.whut.service.DevicesService;
import edu.whut.mapper.DevicesMapper;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author wunder
* @description 针对表【t_devices】的数据库操作Service实现
* @createDate 2024-02-26 19:43:40
*/
@Service
@Slf4j
public class DevicesServiceImpl extends ServiceImpl<DevicesMapper, Devices>
    implements DevicesService{

    @Autowired
    private DevicesMapper devicesMapper;
    @Override
    public PageResult getPageDevices(QueryDeviceDTO queryDevice) {

        //分页参数
        Page<Devices> rowPage = new Page(queryDevice.getPageNum().longValue(),
                queryDevice.getPageSize().longValue());

        //queryWrapper组装查询where条件
        LambdaQueryWrapper<Devices> queryWrapper = new LambdaQueryWrapper<>();
        //是否要查询特殊字段
        if(ObjectUtil.isNotEmpty(queryDevice.getDeviceName())){
            queryWrapper.eq(Devices::getDName,queryDevice.getDeviceName());
        }
        if(ObjectUtil.isNotEmpty(queryDevice.getLoginIp())){
            queryWrapper.eq(Devices::getDIp,queryDevice.getLoginIp());
        }
        //按照状态排序，然后在按照更新时间排序
        queryWrapper.orderByDesc(Devices::getDStatus);
        queryWrapper.orderByDesc(Devices::getUpdateTime);
        rowPage = devicesMapper.selectPage(rowPage, queryWrapper);
        //log.info("rowPage====>{}"+rowPage.getRecords());
        List<QueryDeviceVO> listReturn = new ArrayList<>();
        //转化为VO格式
        for(Devices source:rowPage.getRecords()){
            QueryDeviceVO target = new QueryDeviceVO();
            BeanUtils.copyProperties(source, target);
            listReturn.add(target);
        }
        //log.info("listRetrun====>{}"+listReturn);
        PageResult<QueryDeviceVO> pageResult
                =new PageResult<>(listReturn,rowPage.getTotal());

        return pageResult;
    }

    /**
     * 获取设备的完整信息
     * @return
     */
    @Override
    public List<AllDeviceInfoVO> queryAllDeviceInfo() {
        Integer userId= SecurityUtil.getUserId();
        List<AllDeviceInfoVO> devicesList=devicesMapper.queryAllDeviceInfo(userId);
        log.info("查到的数据------->{}",devicesList);
        return devicesList;
    }
}




