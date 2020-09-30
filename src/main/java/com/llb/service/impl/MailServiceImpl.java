package com.llb.service.impl;

import com.llb.service.MailService;
import com.llb.utils.VerifycodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件服务接口实现
 * @Author llb
 * Date on 2020/3/10
 */
@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender mailSender;

    //邮件发件人
    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    TemplateEngine templateEngine;

    /**
     * 发送普通邮件
     * @param to
     * @param subject 邮件主题
     * @return
     */
    @Override
    public Map<String, Object> sendMail(String to, String subject) {
        Map<String, Object> result = new HashMap<>();

        //生成随机验证码
        VerifycodeUtil verifyCode = new VerifycodeUtil();
        int code = verifyCode.randomCode();

        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", code);

        //将模板内容解析成html
        String template = templateEngine.process("emailTemplate", context);

        //设置邮件信息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("欢迎使用驾校预约服务-"+subject);
        message.setText(template);

        try {
            //邮件发送
            mailSender.send(message);
            result.put("code", 200);
            result.put("verifyMailCode", code);
            result.put("msg", "邮件发送成功，请注意查收");
        } catch (Exception e) {
            result.put("code", 201);
            result.put("msg", "邮件发送异常");
            e.printStackTrace();
        }

        return  result;
    }

    /**
     * 发送html邮件
     * @param to
     * @param subject 邮件主题，这里我们将类别氛围，注册、登录、修改密码
     * @return
     */
    public Map<String, Object> sendHtmlMail(String to, String subject) {
        Map<String, Object> result = new HashMap<>();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //生成随机验证码
        VerifycodeUtil verifyCode = new VerifycodeUtil();
        int code = verifyCode.randomCode();

        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", code);
        context.setVariable("subject", subject);
        //将模板内容解析成html
        String template = templateEngine.process("emailTemplate", context);
        try {
            //true表示创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("欢迎使用驾校预约管理服务-"+subject);
            //true表示发送的是html邮件
            helper.setText(template, true);

            mailSender.send(mimeMessage);
            result.put("code", 200);
            result.put("verifyMailCode", code);
            result.put("msg", "邮件发送成功，请注意查收");
        } catch (MessagingException e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "邮件发送异常");
        }
        return result;
    }
}
