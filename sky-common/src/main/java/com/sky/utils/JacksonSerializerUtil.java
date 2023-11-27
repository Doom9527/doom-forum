package com.sky.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JacksonSerializerUtil {
    public static ObjectMapper createObjectMapper(){
        //Json序列化工具
        ObjectMapper mapper = new ObjectMapper();
        //为jackjson注册序列化提供能力的对象
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //系列化时间格式化
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        //反序列化时间格式化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        //注册进入jackson
        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}
