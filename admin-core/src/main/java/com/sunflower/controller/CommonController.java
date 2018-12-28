package com.sunflower.controller;

import com.sunflower.config.pac4jcas.SunflowerCasProfile;
import com.sunflower.config.pac4jcas.SunflowerUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommonController {

	public CommonController() {
	}

	@ResponseBody
	@RequestMapping({ "a" })
	public SunflowerCasProfile a() {
		return SunflowerUserUtil.get();
	}

}
