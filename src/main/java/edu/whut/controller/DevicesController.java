package edu.whut.controller;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.DeviceAddDTO;
import edu.whut.domain.dto.QueryDeviceDTO;
import edu.whut.domain.vo.AllDeviceInfoVO;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.mapper.DevicesMapper;
import edu.whut.response.PageResult;
import edu.whut.response.Result;
import edu.whut.service.DevicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("sys/device")
public class DevicesController {
    @Autowired
    private DevicesService devicesService;
    @PostMapping("list")
    public Result getPageDevices(@RequestBody QueryDeviceDTO queryDevice){
        PageResult pageResult=devicesService.getPageDevices(queryDevice);
        return Result.success("fine",pageResult);
    }

    @GetMapping("list")
    public Result getAllDeviceInfo(){
        List<AllDeviceInfoVO> allDeviceInfoList=devicesService.queryAllDeviceInfo();
        return Result.success("fine",allDeviceInfoList);
    }

    @PostMapping("list/add")
    public Result addDeviceInfo(@RequestBody DeviceAddDTO deviceAddDTO){
        int row=devicesService.addDeviceInfo(deviceAddDTO);
        if(row==0){
            return Result.error(HttpStatus.ERROR,"插入失败");
        }
        return Result.success("添加成功");
    }
}
