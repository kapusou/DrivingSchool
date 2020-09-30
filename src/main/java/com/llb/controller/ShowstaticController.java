package com.llb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 显示X-admin静态页面
 * 参照静态页面修改前端页面
 * @Author llb
 * Date on 2020/3/5
 */
@Controller
@RequestMapping("/static")
public class ShowstaticController {

    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("unicode")
    public String unicode() {
        return "unicode";
    }

    @RequestMapping("role-add")
    public String role_add() {
        return "role-add";
    }

    @RequestMapping("order_list")
    public String order_list() {
        return "order_list";
    }

    @RequestMapping("member_password")
    public String member_password() {
        return "member_password";
    }

    @RequestMapping("member_list")
    public String member_list() {
        return "member_list";
    }

    @RequestMapping("member_list1")
    public String member_list1() {
        return "member_list1";
    }

    @RequestMapping("member_edit")
    public String member_edit() {
        return "member_edit";
    }

    @RequestMapping("member_del")
    public String member_del() {
        return "member_del";
    }

    @RequestMapping("member_add")
    public String member_add() {
        return "member_add";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("error")
    public String error() {
        return "error";
    }

    @RequestMapping("echarts8")
    public String echarts8() {
        return "echarts8";
    }

    @RequestMapping("echarts7")
    public String echarts7() {
        return "echarts7";
    }

    @RequestMapping("echarts6")
    public String echarts6() {
        return "echarts6";
    }

    @RequestMapping("echarts5")
    public String echarts5() {
        return "echarts5";
    }

    @RequestMapping("echarts4")
    public String echarts4() {
        return "echarts4";
    }

    @RequestMapping("echarts3")
    public String echarts3() {
        return "echarts3";
    }

    @RequestMapping("echarts2")
    public String echarts2() {
        return "echarts2";
    }

    @RequestMapping("echarts1")
    public String echarts1() {
        return "echarts1";
    }

    @RequestMapping("city")
    public String city() {
        return "city";
    }

    @RequestMapping("cate")
    public String cate() {
        return "cate";
    }

    @RequestMapping("admin_rule")
    public String admin_rule() {
        return "admin_rule";
    }

    @RequestMapping("admin_role")
    public String admin_role() {
        return "admin_role";
    }

    @RequestMapping("admin_list")
    public String admin_list() {
        return "admin_list";
    }

    @RequestMapping("admin_edit")
    public String admin_edit() {
        return "admin_edit";
    }

    @RequestMapping("admin_cate")
    public String admin_cate() {
        return "admin_cate";
    }

    @RequestMapping("admin_add")
    public String admin_add() {
        return "admin_add";
    }
}
