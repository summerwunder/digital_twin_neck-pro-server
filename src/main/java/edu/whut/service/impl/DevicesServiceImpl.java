package edu.whut.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.whut.domain.dto.DeviceAddDTO;
import edu.whut.domain.vo.AllDeviceInfoVO;
import edu.whut.mapper.AlarmActionsMapper;
import edu.whut.mapper.UserMapDevicesMapper;
import edu.whut.pojo.AlarmActions;
import edu.whut.pojo.UserMapDevices;
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

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private UserMapDevicesMapper userMapDevicesMapper;
    @Autowired
    private AlarmActionsMapper alarmActionsMapper;
    /**
     * 删除某个设备
     * @param  deviceId
     * @return 是否成功删除
     */
    @Override
    public boolean delDevice(Integer deviceId) {
        LambdaQueryWrapper<Devices> lambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Devices::getDid,deviceId);
        Devices devices = devicesMapper.selectOne(lambdaQueryWrapper);
        //这里表明没有收到相关的数据，不能删除
        if(ObjectUtil.isNull(devices)){
            return false;
        }
        /*此处将进行删除用户设备对应数据，设备传感器对应表和传感器的数据表
        *   其余操作在Controller层另外设定*/
        LambdaQueryWrapper<UserMapDevices> userMapDevicesLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        userMapDevicesLambdaQueryWrapper.eq(UserMapDevices::getDId,deviceId);
        //删除用户设备对应表
        userMapDevicesMapper.delete(userMapDevicesLambdaQueryWrapper);

        return devicesMapper.deleteById(deviceId) > 0;
    }

    @Override
    public PageResult getPageDevices(QueryDeviceDTO queryDevice) {
        //TODO 此处需要判断是否为当前用户（其实放在前面判断其实更好）
        LambdaQueryWrapper<UserMapDevices> queryUserWrapper = new LambdaQueryWrapper<>();
        queryUserWrapper.eq(UserMapDevices::getUId,SecurityUtil.getUserId());
        List<UserMapDevices> mapDevicesList = userMapDevicesMapper.selectList(queryUserWrapper);
        //存储了当前用户的设备编号信息
        List<Integer> didList = mapDevicesList.stream().map(UserMapDevices::getDId).toList();
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
        //判断是否是用户设备
        queryWrapper.in(Devices::getDid,didList);
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
        return devicesList;
    }

    @Override
    public int addDeviceInfo(DeviceAddDTO deviceAddDTO) {
        if(ObjectUtil.isNull(deviceAddDTO)){
            return 0;
        }
        LambdaQueryWrapper<Devices> lambdaQueryWrapper=
               new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Devices::getDName,deviceAddDTO.getDeviceName());
        Devices devices = devicesMapper.selectOne(lambdaQueryWrapper);
        //代表重复
        if(ObjectUtil.isNotNull(devices)){
            return 0;
        }
        Devices device=new Devices();
        //设置插入的设备信息
        device.setDName(deviceAddDTO.getDeviceName());
        device.setIsDeleted(0);
        device.setVersion(1);
        device.setDStatus(0);
        device.setDNumber(UUID.randomUUID().toString());
        device.setDIp(deviceAddDTO.getDeviceIp());
        device.setDMark(deviceAddDTO.getDeviceMark());
        //此处不采用自动注入
        LocalDateTime now=LocalDateTime.now();
        device.setCreateTime(now);
        device.setUpdateTime(now);
        devicesMapper.insert(device);
        //获取设备Id号
        Integer deviceId = devicesMapper.selectOne(lambdaQueryWrapper).getDid();
        //同时在设备用户映射表中添加
        UserMapDevices userMapDevices=new UserMapDevices();
        userMapDevices.setUId(SecurityUtil.getUserId());
        userMapDevices.setDId(deviceId);
        return userMapDevicesMapper.insert(userMapDevices);
    }
}




