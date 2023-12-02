package com.sky.handler;

import com.sky.constant.AutoFillConstant;
import com.sky.utils.BaseContext;
import com.sky.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {
    /**
     * 插入操作自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue(AutoFillConstant.CREATE_TIME, LocalDateTime.now());
        metaObject.setValue(AutoFillConstant.UPDATE_TIME, LocalDateTime.now());
        Long id = Long.valueOf(JwtUtils.getUserId(BaseContext.getToken()));
        metaObject.setValue(AutoFillConstant.CREATE_USER, id);
        metaObject.setValue(AutoFillConstant.UPDATE_USER, id);
    }

    /**
     * 更新操作自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue(AutoFillConstant.UPDATE_TIME, LocalDateTime.now());
        Long id = Long.valueOf(JwtUtils.getUserId(BaseContext.getToken()));
        metaObject.setValue(AutoFillConstant.UPDATE_USER, id);
    }
}
