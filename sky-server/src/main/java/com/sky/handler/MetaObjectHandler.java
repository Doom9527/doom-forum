package com.sky.handler;

import com.sky.constant.AutoFillConstant;
import com.sky.utils.BaseContext;
import com.sky.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {
    /**
     * 插入操作自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        metaObject.setValue(AutoFillConstant.CREATE_TIME, now);
        metaObject.setValue(AutoFillConstant.UPDATE_TIME, now);
//        Long id = Long.valueOf(JwtUtils.getUserId(BaseContext.getToken()));
//        metaObject.setValue(AutoFillConstant.CREATE_USER, id);
//        metaObject.setValue(AutoFillConstant.UPDATE_USER, id);
    }

    /**
     * 更新操作自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        metaObject.setValue(AutoFillConstant.UPDATE_TIME, now);
//        Long id = Long.valueOf(JwtUtils.getUserId(BaseContext.getToken()));
//        metaObject.setValue(AutoFillConstant.UPDATE_USER, id);
    }
}
