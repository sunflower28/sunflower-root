
package com.sunflower.framework.amqp.listener;

import com.rabbitmq.client.Channel;
import com.sunflower.framework.amqp.util.MqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p><b>Description:</b> RabbitMQ抽象消息监听，所有消息消费者必须继承此类
 * <p><b>Company:</b> 
 *
 * @author created by hongda at 13:26 on 2017-10-24
 * @version V0.1
 */
@Slf4j
public abstract class AbstractMessageListener implements ChannelAwareMessageListener {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 接收消息，子类必须实现该方法
     *
     * @param message          消息对象
     */
    public abstract void receiveMessage(Message message);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        Long consumerCount = redisTemplate.opsForHash().increment(MqConstants.MQ_CONSUMER_RETRY_COUNT_KEY,
                messageProperties.getMessageId(), 1);

        log.info("收到消息,当前消息ID:{} 消费次数：{}", messageProperties.getMessageId(), consumerCount);

        try {
            receiveMessage(message);
            // 成功的回执
            channel.basicAck(deliveryTag, false);
            // 如果消费成功，将Redis中统计消息消费次数的缓存删除
            redisTemplate.opsForHash().delete(MqConstants.MQ_CONSUMER_RETRY_COUNT_KEY,
                    messageProperties.getMessageId());
        } catch (Exception e) {
            log.error("RabbitMQ 消息消费失败，" + e.getMessage(), e);
            if (consumerCount >= MqConstants.MAX_CONSUMER_COUNT) {
                // 入死信队列
                channel.basicReject(deliveryTag, false);
            } else {
                // 重回到队列，重新消费, 按照2的指数级递增
                Thread.sleep((long) (Math.pow(MqConstants.BASE_NUM, consumerCount)*1000));
                redisTemplate.opsForHash().increment(MqConstants.MQ_CONSUMER_RETRY_COUNT_KEY,
                        messageProperties.getMessageId(), 1);
                channel.basicNack(deliveryTag, false, true);
            }
        }
    }

}
