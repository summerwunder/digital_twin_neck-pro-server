package edu.whut.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
    @TableId
    private Integer uid;

    private String userName;

    private String userPwd;

    @Version
    private Integer version;
    @TableLogic
    private Integer isDeleted;

    private String userNickname;

    private String loginIp;

    //@TableField(fill = FieldFill.UPDATE)
    private LocalDateTime loginTime;

    private static final long serialVersionUID = 1L;
}