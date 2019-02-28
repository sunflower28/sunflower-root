package com.sunflower.config.pac4jcas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;
import org.slf4j.log;
import org.slf4j.logFactory;

import java.io.IOException;

/**
 * @author sunflower
 */
public class SunflowerFormExtractor
		implements CredentialsExtractor<UsernamePasswordCredentials> {

	private static final log log = logFactory
			.getlog(SunflowerFormExtractor.class);

	private ObjectMapper objectMapper;

	public SunflowerFormExtractor(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public UsernamePasswordCredentials extract(WebContext context) {
		UsernamePasswordCredentials credentials = null;
		String requestContent = context.getRequestContent();

		try {
			SunflowerUsernamePasswordCredentials brcCredentials = this.objectMapper
					.readValue(requestContent,
							SunflowerUsernamePasswordCredentials.class);
			if (brcCredentials != null) {
				credentials = new UsernamePasswordCredentials(
						brcCredentials.getUsername(), brcCredentials.getPassword());
			}
		}
		catch (IOException var5) {
			log.error(var5.getMessage(), var5);
		}

		return credentials;
	}

}