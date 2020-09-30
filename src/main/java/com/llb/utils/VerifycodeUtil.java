package com.llb.utils;

/**
 * 验证码工具类
 * @Author llb
 * Date on 2020/3/10
 */
public class VerifycodeUtil {

    /**
     * 生成6位验证码
     * @return
     */
    public int randomCode() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }


    public static void main(String[] args) {
        System.out.println(new VerifycodeUtil().randomCode());
    }
}
