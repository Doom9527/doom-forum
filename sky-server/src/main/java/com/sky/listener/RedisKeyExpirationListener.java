package com.sky.listener;

import com.sky.service.OrdersService;
import com.sky.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 监听所有db的过期事件__keyevent@*__:expired"
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private OrdersService ordersService;


    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        // 获取到失效的 key，进行取消订单业务处理
        String expiredKey = message.toString();
        String[] split = expiredKey.split("-");
        if (split[0].equals("OD")){
            //还原库存量，更新订单状态
            ordersService.timeOutCancelOrder(expiredKey);
            log.info("订单编号为：" + expiredKey + " 超时自动取消");
        }
    }
}
