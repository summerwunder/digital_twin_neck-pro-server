package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_fields_map_devices
 */
@TableName(value ="t_fields_map_devices")
@Data
public class FieldsMapDevices implements Serializable {
    @TableId
    private Integer mId;

    private Integer dId;

    private Integer fId;

    private static final long serialVersionUID = 1L;
}