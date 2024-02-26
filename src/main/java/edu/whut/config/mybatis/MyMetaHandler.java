package edu.whut.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;


public class MyMetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now=LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", () -> now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now=LocalDateTime.now();
        this.strictUpdateFill(metaObject,"updateTime",() -> now,LocalDateTime.class);
        //this.strictUpdateFill(metaObject,"loginTime",() -> now,LocalDateTime.class);
    }
}
