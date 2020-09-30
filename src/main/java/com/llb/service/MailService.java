package com.llb.service;

import java.util.Map;

/**
 * 邮件服务类接口
 * @Author llb
 * Date on 2020/3/9
 */
public interface MailService {

    /**
     * 发送邮件验证码
     * @param to
     * @param subject
     */
    public Map<String, Object> sendMail(String to, String subject);

    /**
     * 发送HTML邮件验证码
     * @param to
     * @param subject
     */
    public Map<String, Object> sendHtmlMail(String to, String subject);

}
