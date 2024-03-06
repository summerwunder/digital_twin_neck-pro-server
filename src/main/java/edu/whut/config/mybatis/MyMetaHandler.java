package edu.whut.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class MyMetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        LocalDateTime now=LocalDateTime.now();
       // this.setFieldValByName("updateTime",now,metaObject);
        this.strictInsertFill(metaObject, "createTime", () -> now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", () -> now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now=LocalDateTime.now();
        this.strictUpdateFill(metaObject,"updateTime",() -> now,LocalDateTime.class);
        //this.strictUpdateFill(metaObject,"loginTime",() -> now,LocalDateTime.class);
        //this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
