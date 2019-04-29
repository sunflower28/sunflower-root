package com.sunflower.framework.amqp.util;

import java.util.List;

/**
 * @author sunflower
 * @param <T>
 */
public interface DbCoordinator<T> {

    /**
     * 设置消息为prepare状态
     * @param msgId 消息ID
     */
    void setMsgPrepare(String msgId);

    /**
     * 设置消息为ready状态，删除prepare状态
     * @param msgId 消息ID
     * @param rabbitMetaMessage
     */
    void setMsgReady(String msgId, RabbitMetaMessage rabbitMetaMessage);

    /**
     * 消息发送成功，删除ready状态消息
     * @param msgId 消息ID
     */
    void setMsgSuccess(String msgId);

    /**
     * 从db中获取消息实体
     * @param msgId 消息ID
     * @return 元消息
     */
    RabbitMetaMessage getMetaMsg(String msgId);

    /**
     * 获取ready状态消息
     * @return
     * @throws Exception
     */
    List<T> getMsgReady() throws Exception;

    /**
     * 获取prepare状态消息
     * @return
     * @throws Exception
     */
    List<T> getMsgPrepare() throws Exception;

    /**
     * 消息重发次数+1
     * @param key
     * @param hashKey
     * @return
     */
    Long incrResendKey(String key, String hashKey);

    Long getResendValue(String key, String hashKey);



}
