package com.llb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录、注销、修改密码、注册
 * role 类型:1.用户 2.管理员 3.教练
 * 三种角色登录有可以通过账号、邮箱登录，这两个属性必须唯一。
 * @Author llb
 * Date on 2020/3/4
 */
@Controller
public class LoginController2 {

	@RequestMapping("")
	public String toLogin() {
        return "login";
    }
}
