package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.pojo.Devices;
import edu.whut.pojo.SensorFields;
import edu.whut.response.PageResult;
import edu.whut.service.SensorFieldsService;
import edu.whut.mapper.SensorFieldsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author wunder
* @description 针对表【t_sensor_fields】的数据库操作Service实现
* @createDate 2024-02-26 22:58:28
*/
@Service
public class SensorFieldsServiceImpl extends ServiceImpl<SensorFieldsMapper, SensorFields>
    implements SensorFieldsService{

    @Autowired
    private SensorFieldsMapper mapper;
    @Override
    public PageResult getPageSensors(String sensorName, Integer pageNum, Integer pageSize) {
        //分页参数
        Page<SensorFields> rowPage = new Page(pageNum,pageSize);
        //queryWrapper组装查询where条件
        LambdaQueryWrapper<SensorFields> queryWrapper = new LambdaQueryWrapper<>();
        //是否要查询特殊字段
        if(ObjectUtil.isNotEmpty(sensorName)&&(!sensorName.equals("null"))){
            queryWrapper.eq(SensorFields::getNName,sensorName);
        }
        //按照是否报警排序，然后在按照id排序
        queryWrapper.orderByDesc(SensorFields::getIsAlter);
        queryWrapper.orderByAsc(SensorFields::getNid);
        rowPage = mapper.selectPage(rowPage, queryWrapper);

        PageResult<SensorFields> pageResult
                =new PageResult<>(rowPage.getRecords(),rowPage.getTotal());

        return pageResult;
    }
}




