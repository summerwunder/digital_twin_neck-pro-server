package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.SensorFieldAddDTO;
import edu.whut.domain.dto.SensorFieldsUpdateDTO;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.mapper.AlarmActionsMapper;
import edu.whut.mapper.UserMapSensorsMapper;
import edu.whut.pojo.AlarmActions;
import edu.whut.pojo.Devices;
import edu.whut.pojo.SensorFields;
import edu.whut.pojo.UserMapSensors;
import edu.whut.response.PageResult;
import edu.whut.service.SensorFieldsService;
import edu.whut.mapper.SensorFieldsMapper;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* @author wunder
* @description 针对表【t_sensor_fields】的数据库操作Service实现
* @createDate 2024-02-26 22:58:28
*/
@Service
@Slf4j
public class SensorFieldsServiceImpl extends ServiceImpl<SensorFieldsMapper, SensorFields>
    implements SensorFieldsService{

    @Autowired
    private SensorFieldsMapper mapper;

    @Autowired
    private UserMapSensorsMapper userMapSensorsMapper;
    @Override
    public PageResult getPageSensors(String sensorName, Integer pageNum, Integer pageSize) {
        //分页参数
        Page<SensorFields> rowPage = new Page(pageNum,pageSize);
        Integer userId = SecurityUtil.getUserId();
        //此处需要先判断用户
        LambdaQueryWrapper<UserMapSensors> sensorsLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        sensorsLambdaQueryWrapper.eq(UserMapSensors::getUId,userId);
        List<Integer> sensorIds = userMapSensorsMapper.selectList(sensorsLambdaQueryWrapper).stream()
                .map(UserMapSensors::getSId).toList();
        // 如果sensorIds为空，则直接返回空结果集
        if (sensorIds.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), 0);
        }
        //queryWrapper组装查询where条件
        LambdaQueryWrapper<SensorFields> queryWrapper = new LambdaQueryWrapper<>();
        //判断是哪个用户创建的
        queryWrapper.in(SensorFields::getNid,sensorIds);
        //是否要查询特殊字段
        if(ObjectUtil.isNotEmpty(sensorName)&&(!sensorName.equals("null"))){
            queryWrapper.eq(SensorFields::getNName,sensorName);
        }
        //按照是否报警排序，然后在按照id排序
        queryWrapper.orderByDesc(SensorFields::getIsAlter);
        queryWrapper.orderByAsc(SensorFields::getNid);
        rowPage = mapper.selectPage(rowPage, queryWrapper);
        return new PageResult<>(rowPage.getRecords(),rowPage.getTotal());
    }

    /**
     *
     * 获取所有传感器的信息
     * @return
     */
    @Override
    public List<SensorFields> getAllSensors() {
        Integer userId= SecurityUtil.getUserId();
        List<SensorFields> sensorFieldsList = mapper.getAllSensors(userId);
        return sensorFieldsList;
    }

    /**
     * 更新传感器的信息
     * @param sensorFieldsUpdateDTO
     * @return
     */
    @Override
    public boolean updateSensorField(SensorFieldsUpdateDTO sensorFieldsUpdateDTO) {
        //log.info("{}",sensorFieldsUpdateDTO);
        LambdaQueryWrapper<SensorFields> queryWrapper
                =new LambdaQueryWrapper<>();
        queryWrapper.eq(SensorFields::getNid,sensorFieldsUpdateDTO.getSensorId());
        SensorFields sensorFields=new SensorFields();
        //设置更新信息
        sensorFields.setAlterDown(sensorFieldsUpdateDTO.getAlertDown());
        sensorFields.setAlterTop(sensorFieldsUpdateDTO.getAlertTop());
        sensorFields.setAlterIntensity(sensorFieldsUpdateDTO.getAlertIntensity());
        return mapper.update(sensorFields, queryWrapper)>0;
    }

    /**
     * 添加传感器的字段信息
     * @param sensorFieldAddDTO
     * @return
     */
    @Override
    public boolean addSensorField(SensorFieldAddDTO sensorFieldAddDTO) {
        // 首先判断字段名是否相同
        LambdaQueryWrapper<SensorFields> fieldsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        fieldsLambdaQueryWrapper.eq(SensorFields::getNName, sensorFieldAddDTO.getFieldName());
        SensorFields originSensorFields = mapper.selectOne(fieldsLambdaQueryWrapper);
        // 如果找到了同名的传感器字段
        if (ObjectUtil.isNotNull(originSensorFields)) {
            // 判断是否是当前用户的字段名
            LambdaQueryWrapper<UserMapSensors> userMapSensorsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userMapSensorsLambdaQueryWrapper.eq(UserMapSensors::getSId, originSensorFields.getNid());
            UserMapSensors userMapSensors = userMapSensorsMapper.selectOne(userMapSensorsLambdaQueryWrapper);
            // 如果是当前用户的字段名，则返回 false
            if (ObjectUtil.isNotNull(userMapSensors) && userMapSensors.getUId().equals(SecurityUtil.getUserId())) {
                return false;
            }
        }
        // 创建新的传感器字段对象
        SensorFields sensorFields = new SensorFields();
        sensorFields.setAlterIntensity(sensorFieldAddDTO.getAlertIntensity());
        sensorFields.setAlterDown(sensorFieldAddDTO.getAlertDown());
        sensorFields.setAlterDescription(sensorFieldAddDTO.getAlertMark());
        sensorFields.setAlterTop(sensorFieldAddDTO.getAlertTop());
        sensorFields.setIsAlter(sensorFieldAddDTO.getIsAlert());
        sensorFields.setNName(sensorFieldAddDTO.getFieldName());
        sensorFields.setNDescription(sensorFieldAddDTO.getFieldDescription());
        sensorFields.setNUnit(sensorFieldAddDTO.getFieldUnit());

        // 插入新的传感器字段
        mapper.insert(sensorFields);

        // 添加到用户传感器对应表
        UserMapSensors userMapSensors = new UserMapSensors();
        userMapSensors.setUId(SecurityUtil.getUserId());
        userMapSensors.setSId(sensorFields.getNid()); // 直接使用新插入的传感器字段的nid属性
        userMapSensorsMapper.insert(userMapSensors);
        return true; // 返回 true 表示添加成功
    }
}




