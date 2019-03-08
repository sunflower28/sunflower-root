package com.sunflower.controller;

import com.sunflower.api.ResultDto;
import com.sunflower.config.pac4jcas.CasProperties;
import com.sunflower.config.pac4jcas.SunflowerCasProfile;
import com.sunflower.config.pac4jcas.SunflowerUserUtil;
import com.sunflower.util.Servlets;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.jwt.profile.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Optional;

@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CasRestFormClient casRestFormClient;

	@Autowired
	private CasProperties casProperties;

	@Autowired
	private JwtGenerator<CommonProfile> jwtGenerator;

	@Value("${spring.application.name}")
	private String springApplicationName;

	public CommonController() {
	}

	@GetMapping({ "a/index" })
	public String loginGet() {
		return "redirect:" + this.casProperties.getCasServerUrlPrefix() + "/index.html";
	}

	@ResponseBody
	@RequestMapping({ "a" })
	public SunflowerCasProfile a() {
		return SunflowerUserUtil.get();
	}

	@ResponseBody
	@RequestMapping({ "a/user/login" })
	public ResultDto<String> login() {
		J2EContext context = new J2EContext(Servlets.getRequest(),
				Servlets.getResponse());
		ProfileManager<CasRestProfile> manager = new ProfileManager<>(context);
		Optional<CasRestProfile> profile = manager.get(true);
		String serviceUrl = this.casProperties.getCasServerUrlPrefix() + "/"
				+ this.springApplicationName.split("-")[0];
		logger.debug(profile.get().getClass().getClassLoader().getClass().getName());
		logger.debug(CasRestProfile.class.getClassLoader().getClass().getName());
		CasRestProfile casRestProfile = profile.get();
		TokenCredentials tokenCredentials = this.casRestFormClient
				.requestServiceTicket(serviceUrl, casRestProfile, context);
		CasProfile casProfile = this.casRestFormClient.validateServiceTicket(serviceUrl,
				tokenCredentials, context);
		this.jwtGenerator.setExpirationTime(new Date(System.currentTimeMillis()
				+ this.casProperties.getGlobalSessionTimeout()));
		return ResultDto.success(this.jwtGenerator.generate(casProfile));
	}

}
