package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_user_map_devices
 */
@TableName(value ="t_user_map_devices")
@Data
public class UserMapDevices implements Serializable {
    @TableId
    private Integer mId;

    private Integer dId;

    private Integer uId;

    private static final long serialVersionUID = 1L;
}