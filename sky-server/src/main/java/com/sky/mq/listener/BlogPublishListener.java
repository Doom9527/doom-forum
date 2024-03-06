package com.sky.mq.listener;

import cn.hutool.json.JSONUtil;
import com.sky.MQInfo.BlogPublishInfo;
import com.sky.constant.RabbitMQConstant;
import com.sky.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogPublishListener {
    @Autowired
    private BlogService blogService;

    /**
     * 发布成功存入oss，更新数据库url
     * @param payload
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMQConstant.BLOG_PUBLISH_QUEUE, durable = RabbitMQConstant.DURABLE_TRUE),
            exchange = @Exchange(name = RabbitMQConstant.BLOG_PUBLISH_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConstant.BLOG_PUBLISH_KEY
    ))
    @Transactional(rollbackFor = Exception.class)
    public void listenBlogPublish(@Payload String payload){
        // 更新数据库
        BlogPublishInfo blogPublishInfo = getBlogPublishInfo(payload);
        blogService.updateMySQL(blogPublishInfo.getTitle(), blogPublishInfo.getContent(),blogPublishInfo.getCategoryId(),blogPublishInfo.getAuthorId());
        log.info("MQ更新数据库成功");
    }

    private static BlogPublishInfo getBlogPublishInfo(String payload) {
        BlogPublishInfo blogPublishInfo = JSONUtil.toBean(payload, BlogPublishInfo.class);
        return blogPublishInfo;
    }
}
