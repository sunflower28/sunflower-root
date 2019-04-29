package com.sunflower.framework.amqp.util;

/**
 * <p>
 * <b>Description:</b> 常量类
 * <p>
 * <b>Company:</b>
 *
 * @author created by hongda at 22:49 on 2017-10-23
 * @version V0.1
 */
public class RabbitMetaMessage {

	private String messageId;

	private String exchange;

	private String routingKey;

	private Object payload;

	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

}
