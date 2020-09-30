package com.llb.controller;

import com.alibaba.fastjson.JSONObject;
import com.llb.entity.*;
import com.llb.service.IAdminService;
import com.llb.service.IStudentService;
import com.llb.service.ITeacherService;
import com.llb.service.MailService;
import com.llb.utils.DateUtil;
import com.llb.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登录、注销、修改密码、注册
 * role 类型:1.用户 2.管理员 3.教练
 * 三种角色登录有可以通过账号、邮箱登录，这两个属性必须唯一。
 * @Author llb
 * Date on 2020/3/4
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IAdminService adminService;
    @Autowired
    private MailService mailService;
    //密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    private RedisConn redisConn;
//    @Autowired
//    private Myprops myprops;
    /**
     * 显示登录页
     * @return
     */
    @RequestMapping("/index")
    public String toLogin() {
        return "login";
    }

    /**
     * 登录 用户/教练/管理员 登录
     * @param role 类型:1.用户 2.管理员 3.教练
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        //账号密码是否正确标志
        boolean flag = false;
        //封装返回结果
        Map<String, Object> result = new HashMap<>();

        String email = map.get("email");
        String account = map.get("account");
        String password = map.get("password");
        String role = map.get("role");

        Student student = new Student();
        Admin admin = new Admin();
        Teacher teacher = new Teacher();

        if("1".equals(role)) {
            student = studentService.findStudent(account);
            if(student != null && encoder.matches(password, student.getStuPwd())) {
                flag = true;
                //TODO:redis的key不能设置为固定值
                redisUtil.set("student",student, (long) 10);
//                request.getSession().setAttribute("student", student);
                result.put("student",student);
            } else if(student == null){
                result.put("msg", "用户名或账户不存在！");
            } else {
                result.put("msg", "用户名或密码错误！");
            }
        } else if("2".equals(role)) {
            admin = adminService.findAdmin(account);
            if(admin != null && encoder.matches(password, admin.getAdminPwd())) {
                flag = true;
                request.getSession().setAttribute("admin", admin);
            } else if(admin == null){
                result.put("msg", "用户名或账户不存在！");
            } else {
                result.put("msg", "用户名或密码错误！");
            }
        } else {
            teacher = teacherService.findTeacher(account);
            if(teacher != null && encoder.matches(password, teacher.getTeaPwd())) {
                flag = true;
                request.getSession().setAttribute("teacher", teacher);
            } else if(teacher == null){
                result.put("msg", "用户名或账户不存在！");
            } else {
                result.put("msg", "用户名或密码错误！");
            }
        }

        if(flag) {
            result.put("code", 200);
            result.put("role", role);
            result.put("msg", "登录成功！");
        } else{
            result.put("code", 201);
        }
        return result;
    }

    /**
     * 管理员、学员、教练账号注册
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> regist(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        //封装返回结果
        String account = map.get("account");
        String password = encoder.encode(map.get("password"));
        String role = map.get("role");
        String email = map.get("email");

        Student student = new Student();
        Admin admin = new Admin();
        Teacher teacher = new Teacher();

        //角色判断
        if("1".equals(role)) {
            String s = UUID.randomUUID().toString();
            System.out.println(s);
            //补全参数
            student.setStuId(UUID.randomUUID().toString().replace("-", ""));
            student.setStuAccount(account);
            student.setStuEmail(email);
            student.setStuPwd(password);
            student.setStuCreatedate(new DateUtil().formatDate(new Date(), "yyyy-MM-dd"));

            //保存信息
            studentService.saveStudent(student);

            request.getSession().setAttribute("student", student);
        } else if("2".equals(role)) {
            //补全参数
            admin.setAdminId(UUID.randomUUID().toString().replace("-", ""));
            admin.setAdminAccount(account);
            admin.setAdminMail(email);
            admin.setAdminPwd(password);
            admin.setAdminCreatedate(new DateUtil().formatDate(new Date(), "yyyy-MM-dd"));
            //保存管理员
            adminService.saveAdmin(admin);

            request.getSession().setAttribute("admin", admin);

        } else {

            //补全参数
            teacher.setTeaId(UUID.randomUUID().toString().replace("-", ""));
            teacher.setTeaAccount(account);
            teacher.setTeaEmail(email);
            teacher.setTeaPwd(password);
            teacher.setTeaCreatedate(new DateUtil().formatDate(new Date(), "yyyy-MM-dd"));
            //保存教练
            teacherService.saveTeacher(teacher);

            request.getSession().setAttribute("teacher", teacher);
        }

        result.put("code", 200);
        result.put("role", role);
        result.put("msg", "注册成功！");
        return result;
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/loginOut")
    public String loginOut(String role, HttpServletRequest request) {
        //判断是哪个角色，注销登录
        if("1".equals(role)) {
            request.getSession().removeAttribute("student");
            redisUtil.remove("student");
        } else if("2".equals(role)) {
            request.getSession().removeAttribute("admin");
        } else {
            request.getSession().removeAttribute("teacher");
        }
        return "redirect:/login/index";
    }


    /**
     * 重置密码
     * @param map
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> map,
                                             HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        //判断角色类型
        String role = map.get("role");

        //获取页面传来的验证码
        String emailCode = map.get("emailCode");
        //获取验证码
        Object verifyMailCode = request.getSession().getAttribute("verifyMailCode");
        if("".equals(verifyMailCode) || verifyMailCode == null) {
            result.put("code", 201);
            result.put("msg", "验证码已过期！");
            return result;
        } else if(emailCode == verifyMailCode) {
            result.put("code", 201);
            result.put("msg", "验证码不正确，请重新输入！");
            return result;
        }

//        String account
        String email = map.get("email");
        String password = encoder.encode(map.get("password"));
        //判断是哪个角色，重置密码
        if("1".equals(role)) {

            //修改密码
            result = studentService.editStuPwd(email, password);
        } else if("2".equals(role)) {
            //修改密码
            result = adminService.editAdminPwd(email, password);
        } else {
            //修改密码
            result = studentService.editStuPwd(email, password);
        }

        return result;
    }

    /**
     * 发送重置密码邮箱验证码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendCode(@RequestBody Map<String, String> map,
                                        HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String email = map.get("email");
        String account = map.get("account");

//        adminService.findAdminByEmail(email);

        final HttpSession httpSession = request.getSession();

        //判断用户名和邮箱是否对应，即是否是一个账户
        Admin admin = adminService.findAdminByAccAndMail(account, email);
        Student student = studentService.findStuByAccAndMail(account, email);
        Teacher teacher = teacherService.findTeaByAccAndPwd(account, email);
        //管理员、教练、学员都为空
        if(admin == null && student == null && teacher == null) {
            result.put("code", 202);
            result.put("msg", "用户名和邮箱不是同一个账户！");
            return result;
        }

        //邮箱和账户名对应则发送验证码邮件
        result = mailService.sendHtmlMail(email, "修改密码");

        //将验证码放到浏览器缓存5分钟，5分钟失效
        httpSession.setAttribute("verifyMailCode", result.get("verifyMailCode"));

        //设置过期时间
        try {
            //设置失效时间
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    httpSession.removeAttribute("verifyMailCode");
                    System.out.println("邮箱验证码缓存信息删除成功");
                    timer.cancel();
                }
            }, 5*60*1000);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "发送验证码失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "验证码已发送至邮箱，注意查收！");

        return result;
    }

    /**
     * 发送重置密码邮箱验证码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getRegistCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendRegistCode(@RequestBody Map<String, String> map,
                                        HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String email = map.get("email");
        String account = map.get("account");
        String role = map.get("role");

//        adminService.findAdminByEmail(email);

        final HttpSession httpSession = request.getSession();

        //判断用户名和邮箱是否已被注册
        Admin admin = adminService.findAdmin(account, email);
        Student student = studentService.findStudent(account, email);
        Teacher teacher = teacherService.findTeacher(account, email);
        //管理员、教练、学员是否被注册
        if(admin != null || student != null || teacher != null) {
            result.put("code", 202);
            result.put("msg", "用户名或邮箱已被注册！");
            return result;
        }

        //邮箱和账户名对应则发送验证码邮件
        result = mailService.sendHtmlMail(email, "注册");

        //将验证码放到浏览器缓存5分钟，5分钟失效
        httpSession.setAttribute("verifyMailCode", result.get("verifyMailCode"));

        try {
            //设置失效时间
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    httpSession.removeAttribute("verifyMailCode");
                    System.out.println("邮箱验证码缓存信息删除成功");
                    timer.cancel();
                }
            }, 5*60*1000);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "发送验证码失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "验证码已发送至邮箱，注意查收！");

        return result;
    }
    @RequestMapping("/test")
    @ResponseBody
    public String getSessionId() {
        redisUtil.set("123", "测试", (long) 10);
        System.out.println("进入了方法");
//        System.out.println(redisConn.toString());
//        System.out.println(myprops.toString());
        String string = redisUtil.get("123").toString();
        return string;
    }
}
