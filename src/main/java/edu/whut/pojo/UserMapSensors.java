package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_user_map_sensors
 */
@TableName(value ="t_user_map_sensors")
@Data
public class UserMapSensors implements Serializable {
    @TableId
    private Integer mId;

    private Integer uId;

    private Integer sId;

    private static final long serialVersionUID = 1L;
}