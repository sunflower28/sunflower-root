package com.sunflower.config.pac4jcas;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;

import java.io.IOException;

/**
 * @author sunflower
 */
@Slf4j
public class SunflowerFormExtractor
		implements CredentialsExtractor<UsernamePasswordCredentials> {

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
		catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return credentials;
	}

}