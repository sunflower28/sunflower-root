package com.sunflower.framework.amqp.daemontask;

import com.sunflower.framework.amqp.config.RabbitTemplateConfig;
import com.sunflower.framework.amqp.sender.RabbitSender;
import com.sunflower.framework.amqp.util.AlertSender;
import com.sunflower.framework.amqp.util.DbCoordinator;
import com.sunflower.framework.amqp.util.MqConstants;
import com.sunflower.framework.amqp.util.RabbitMetaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sunflower
 */
@Component
public class ResendProcess {

	private Logger logger = LoggerFactory.getLogger(RabbitTemplateConfig.class);

	@Autowired
	DbCoordinator<String> dbCoordinator;

	@Autowired
	DbCoordinator<RabbitMetaMessage> dbCoordinatorRabbitMetaMessage;

	@Autowired
	RabbitSender rabbitSender;

	@Autowired
	AlertSender alertSender;

	/**
	 * prepare状态的消息超时告警
	 * @throws Exception
	 */
	public void alertPrepareMsg() throws Exception {
		List<String> messageIds = this.dbCoordinator.getMsgPrepare();
		for (String messageId : messageIds) {
			alertSender.doSend(messageId);
		}
	}

	public void resendReadyMsg() throws Exception {
		List<RabbitMetaMessage> messages = this.dbCoordinatorRabbitMetaMessage
				.getMsgReady();
		for (RabbitMetaMessage message : messages) {
			long msgCount = dbCoordinator.getResendValue(MqConstants.MQ_RESEND_COUNTER,
					message.getMessageId());
			if (msgCount > MqConstants.MAX_RETRY_COUNT) {
				alertSender.doSend(message.getMessageId());
			}
			rabbitSender.send(message);
			dbCoordinator.incrResendKey(MqConstants.MQ_RESEND_COUNTER,
					message.getMessageId());
		}
	}

}
