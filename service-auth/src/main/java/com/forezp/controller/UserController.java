package com.forezp.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;


/**
 * 本次采用RemoteTokenService这种方式对Token进行验证
 * 如果其他资源需要验证Token需要调这个接口
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(Principal principal) {
		return principal;
	}


}
