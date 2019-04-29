package com.sunflower.framework.amqp.util;
/**
 * <p><b>Description:</b> 常量类 <p>
 * <b>Company:</b> 
 *
 * @author created by fw at 22:49 on 2017-10-23
 * @version V0.1
 */
public interface MqConstants {

	/** 消息重发计数*/
	String MQ_RESEND_COUNTER = "mq.resend.counter";

	/** 消息最大重发次数*/
	long MAX_RETRY_COUNT = 3;

	/** 分隔符*/
	String DB_SPLIT = ",";

	/** 缓存超时时间,超时进行重发 */
	long TIME_GAP = 2000;

	String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**处于ready状态消息*/
	String MQ_MSG_READY = "mq.msg.ready";

	/**处于prepare状态消息*/
	String MQ_MSG_PREPARE = "mq.msg.prepare";
	
	/**你的业务交换机名称*/
	String BUSINESS_EXCHANGE = "business.exchange";

	/**你的业务队列名称*/
	String BUSINESS_QUEUE = "business.queue";

	/**你的业务key*/
	String BUSINESS_KEY = "business.key";
	
	String MQ_PRODUCER_RETRY_KEY = "mq.producer.retry.key";

	String MQ_CONSUMER_RETRY_COUNT_KEY = "mq.consumer.retry.count.key";
	/**死信队列配置*/
	String DLX_EXCHANGE = "dlx.exchange";

	String DLX_QUEUE = "dlx.queue";

	String DLX_ROUTING_KEY = "dlx.routing.key";

	/**发送端重试次数(ms)*/
	int MUTIPLIER_TIME = 500;

	/** 发送端最大重试时时间（s）*/
	int MAX_RETRY_TIME = 10;

	/** 消费端最大重试次数 */
	int MAX_CONSUMER_COUNT = 5;

	/** 递增时的基本常量 */
	int BASE_NUM = 2;

	/** 空的字符串 */
	String BLANK_STR = "";



}
