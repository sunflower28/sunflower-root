package com.sunflower.framework.amqp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sunflower
 */
@Component
public class DbRedisCoordinator implements DbCoordinator {


    private final RedisTemplate<String,String> redisTemplate;

    private final RedisTemplate<String,Long> redisTemplateLong;

    private final RedisTemplate<String,RabbitMetaMessage> redisTemplateRabbitMetaMessage;

    @Autowired
    public DbRedisCoordinator(RedisTemplate<String, String> redisTemplate,
                              RedisTemplate<String,RabbitMetaMessage> redisTemplateRabbitMetaMessage,
                              RedisTemplate<String,Long> redisTemplateLong
    ) {
        this.redisTemplate = redisTemplate;
        this.redisTemplateRabbitMetaMessage = redisTemplateRabbitMetaMessage;
        this.redisTemplateLong = redisTemplateLong;
    }

    @Override
    public void setMsgPrepare(String msgId) {
        this.redisTemplate.opsForSet().add(MqConstants.MQ_MSG_PREPARE, msgId);
    }

    @Override
    public void setMsgReady(String msgId, RabbitMetaMessage rabbitMetaMessage) {
        this.redisTemplate.opsForHash().put(MqConstants.MQ_MSG_READY, msgId, rabbitMetaMessage);
        this.redisTemplate.opsForSet().remove(MqConstants.MQ_MSG_PREPARE,msgId);
    }

    @Override
    public void setMsgSuccess(String msgId) {
        this.redisTemplate.opsForHash().delete(MqConstants.MQ_MSG_READY, msgId);
    }

    @Override
    public RabbitMetaMessage getMetaMsg(String msgId) {
        return (RabbitMetaMessage) this.redisTemplate.opsForHash().get(MqConstants.MQ_MSG_READY, msgId);
    }

    @Override
    public List<String> getMsgPrepare() throws Exception  {
        SetOperations<String,String> setOperations = this.redisTemplate.opsForSet();
        Set<String> messageIds = setOperations.members(MqConstants.MQ_MSG_PREPARE);
        List<String> messageAlert = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageIds)) {
            for(String messageId: messageIds){
                // 如果消息超时，加入超时队列
                if(messageTimeOut(messageId)){
                    messageAlert.add(messageId);
                }
            }
        }

        // 在redis中删除已超时的消息
        setOperations.remove(MqConstants.MQ_MSG_READY,messageAlert);
        return messageAlert;
    }

    @Override
    public List<RabbitMetaMessage> getMsgReady() throws Exception {
        HashOperations<String,String,RabbitMetaMessage> hashOperations = this.redisTemplateRabbitMetaMessage.opsForHash();
        List<RabbitMetaMessage> messages = hashOperations.values(MqConstants.MQ_MSG_READY);
        List<RabbitMetaMessage> messageAlert = new ArrayList<>();
        List<String> messageIds = new ArrayList<>();
        for(RabbitMetaMessage message : messages){
            // 如果消息超时，加入超时队列
            if(messageTimeOut(message.getMessageId())){
                messageAlert.add(message);
                messageIds.add(message.getMessageId());
            }
        }
        // 在redis中删除已超时的消息
        hashOperations.delete(MqConstants.MQ_MSG_READY, messageIds);
        return messageAlert;
    }

    @Override
    public Long incrResendKey(String key, String hashKey) {
        return  this.redisTemplate.opsForHash().increment(key, hashKey, 1);
    }

    @Override
    public Long getResendValue(String key, String hashKey) {
        return (Long) this.redisTemplateLong.opsForHash().get(key, hashKey);
    }

    /**
     * 判断消息是否超时
     * @param messageId
     * @return
     * @throws Exception
     */
    private boolean messageTimeOut(String messageId) throws Exception{
        String messageTime = (messageId.split(MqConstants.DB_SPLIT))[1];
        long timeGap = System.currentTimeMillis() -
                new SimpleDateFormat(MqConstants.TIME_PATTERN).parse(messageTime).getTime();
        return timeGap > MqConstants.TIME_GAP;
    }
}
