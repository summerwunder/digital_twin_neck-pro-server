package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.domain.dto.SensorFieldAddDTO;
import edu.whut.domain.dto.SensorFieldsUpdateDTO;
import edu.whut.domain.vo.QueryDeviceVO;
import edu.whut.mapper.AlarmActionsMapper;
import edu.whut.mapper.SensorDataMapper;
import edu.whut.mapper.UserMapSensorsMapper;
import edu.whut.pojo.*;
import edu.whut.response.PageResult;
import edu.whut.service.SensorFieldsService;
import edu.whut.mapper.SensorFieldsMapper;
import edu.whut.utils.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private SensorDataMapper sensorDataMapper;
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

    /**
     * 删除传感器字段
     * @param sensorId
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务管理，当出现异常时回滚事务
    public void deleteSensorField(Integer sensorId) {
        //首先判断删除消息的合理性
        LambdaQueryWrapper<UserMapSensors> lambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserMapSensors::getUId,SecurityUtil.getUserId());
        lambdaQueryWrapper.eq(UserMapSensors::getSId,sensorId);
        if(ObjectUtil.isNotNull(userMapSensorsMapper.selectOne(lambdaQueryWrapper))){
            try{
                //说明合理，并不是违法操作
                //首先需要剔除传感器字段中的信息
                mapper.deleteById(sensorId);
                //删除设备对应传感器的表数据
                LambdaQueryWrapper<UserMapSensors> deleteWrapper = new LambdaQueryWrapper<>();
                deleteWrapper.eq(UserMapSensors::getSId,sensorId);
                userMapSensorsMapper.delete(deleteWrapper);
                //删除所有传感器的相关数据
                LambdaQueryWrapper<SensorData> dataLambdaQueryWrapper
                        =new LambdaQueryWrapper<>();
                dataLambdaQueryWrapper.eq(SensorData::getFieldId,sensorId);
                sensorDataMapper.delete(dataLambdaQueryWrapper);
            }catch (Exception e) {
                // 发生异常时，进行错误回滚
                throw new RuntimeException("删除传感器字段发生错误: " + e.getMessage(), e);
            }
        }
    }
}




