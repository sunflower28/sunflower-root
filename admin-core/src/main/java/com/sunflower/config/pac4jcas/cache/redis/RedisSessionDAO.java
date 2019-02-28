package com.sunflower.config.pac4jcas.cache.redis;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.log;
import org.slf4j.logFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSessionDAO extends AbstractSessionDAO {

	private static final log log = logFactory.getlog(RedisSessionDAO.class);

	private SessionRedisTemplate sessionRedisTemplateSession;

	private String sessionKeyPrefix;

	public RedisSessionDAO() {
	}

	public void setRedisTemplate(SessionRedisTemplate sessionRedisTemplateSession) {
		this.sessionRedisTemplateSession = sessionRedisTemplateSession;
	}

	public void setSessionKeyPrefix(String sessionKeyPrefix) {
		this.sessionKeyPrefix = sessionKeyPrefix;
	}

	@Override
	public void update(Session session) {
		if (session instanceof ValidatingSession
				&& !((ValidatingSession) session).isValid()) {
			log.debug("=> Invalid session.");
			this.delete(session);
		}
		else {
			this.saveSession(session);
		}

	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		this.saveSession(session);
		return sessionId;
	}

	private void saveSession(Session session) {
		if (session != null && session.getId() != null) {
			this.sessionRedisTemplateSession.opsForValue()
					.set(this.getKey(session.getId()), session);
		}
		else {
			log.debug("session or session id is null");
		}
	}

	@Override
	public void delete(Session session) {
		if (session != null && session.getId() != null) {
			this.sessionRedisTemplateSession.delete(this.getKey(session.getId()));
		}
		else {
			log.debug("session or session id is null");
		}
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			log.debug("session id is null");
			return null;
		}
		else {
			return this.sessionRedisTemplateSession.opsForValue()
					.get(this.getKey(sessionId));
		}
	}

	@Override
	public Session readSession(Serializable sessionId) {
		Session s = this.doReadSession(sessionId);
		if (s == null) {
			log.debug("session is null");
		}

		return s;
	}

	@Override
	public Collection<Session> getActiveSessions() {
		Set<Session> sessions = new HashSet<>();
		Set<String> keys = this.sessionRedisTemplateSession
				.keys(this.sessionKeyPrefix + "*");
		if (null == keys) {
			return sessions;
		}
		else {
			for (String key : keys) {
				Session session = this.sessionRedisTemplateSession.opsForValue().get(key);
				sessions.add(session);
			}
			return sessions;
		}
	}

	private String getKey(Serializable sessionId) {
		return sessionId == null ? null : this.sessionKeyPrefix + sessionId;
	}

}
