package es.redmic.oauth.security.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.redmic.oauth.security.service.CustomUserDetails;

@Controller
public class UserInfoController {

	@RequestMapping(value = "${controller.mapping.getUserId}", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserId(@AuthenticationPrincipal CustomUserDetails user) {

		return user.getId().toString();
	}

}
