package com.llb.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Admin;
import com.llb.entity.Appointment;
import com.llb.entity.Student;
import com.llb.entity.Teacher;
import com.llb.service.IAppointmentService;
import com.llb.service.IStudentService;
import com.llb.service.ITeacherService;
import com.llb.service.MailService;
import com.llb.utils.DateUtil;
import com.llb.utils.RedisUtils;
import com.llb.web.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.*;
import java.util.function.Consumer;

/**
 * <p>
 * 学员信息表 前端控制器
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private MailService mailService;
    @Autowired
    private IAppointmentService appointmentService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 展示学员首页
     * @return
     */
    @RequestMapping(value="/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("student/index");
        modelAndView.addObject("student",redisUtils.get("student"));
        return modelAndView;
    }

    /**
     * 显示学员修改信息页
     * @return
     */
    @RequestMapping(value ="/student-info")
    public ModelAndView studentInfo() {
        ModelAndView modelAndView = new ModelAndView("student/student-info");
        modelAndView.addObject("student",redisUtils.get("student"));
        return modelAndView;
    }

    /**
     * 显示学员修改密码页
     * @return
     */
    @RequestMapping("/showStuEditPwd")
    public ModelAndView showStuEditPwd() {
        ModelAndView modelAndView = new ModelAndView("student/editPassword");
        modelAndView.addObject("student",redisUtils.get("student"));
        return modelAndView;
    }

    /**
     * 编辑学员信息
     * @param stuInfo 前端表单中填写的的数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/editStudentInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editStudentInfo(@RequestBody Map<String, String> stuInfo,
                                               HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        //将用户传来的表单数据转换成实体类
        Student student = JSONObject.parseObject(JSONObject.toJSONString(stuInfo), Student.class);
        String stuEmail = stuInfo.get("stuEmail");
        String stuId = stuInfo.get("stuId");
        //验证密码是否正确
        result = studentService.verifyPwd(stuId, student.getStuPwd());
        //密码正确
        if("200".equals(result.get("code").toString())) {
            student.setStuPwd(encoder.encode(stuInfo.get("stuPwd")));
            //修改学员信息
            studentService.editStudent(student);
            //将student重新缓存到session
            student = studentService.findStuByEmail(stuEmail);
            redisUtils.set("student",student, (long) 10);
            result.put("code", 200);
            result.put("msg", "修改成功");
            return result;
        }
        return result;
    }

    /**
     * 学员修改密码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/editSutPwd", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editSutPwd(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        //将用户传来的表单数据转换成实体类
        Student newUser = JSONObject.parseObject(JSONObject.toJSONString(map), Student.class);

        String stuNewPwd = newUser.getStuPwd();
        Student student = studentService.findStuById(newUser.getStuId());
        //既然能够给到stuId，那么用户就是存在的
        if(student == null) {
            result.put("code", 202);
            result.put("msg", "用户不存在！");
            return result;
        }

        //判断学员密码是否正确
        if(!encoder.matches(stuNewPwd, student.getStuPwd())) {
            result.put("code", 201);
            result.put("msg", "密码错误！");
            return result;
        }
        //密码加密
        newUser.setStuPwd(encoder.encode(map.get("stuNewPwd")));
        studentService.editStudent(newUser);

        result.put("code", 200);
        result.put("msg", "修改密码成功！");
        return result;
    }

    /**
     * 显示邮箱找回密码界面
     * @return
     */
    @RequestMapping("/found_password")
    public ModelAndView foundPassword(){
        ModelAndView modelAndView = new ModelAndView("student/found_password");
        return modelAndView;
    }

    /**
     * 发送学员验证码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping("/getCode")
    public Map<String, Object> sendForgetPwd(@RequestBody Map<String, String> map, HttpServletRequest request) {
        final HttpSession httpSession = request.getSession();
        Map<String, Object> result = new HashMap<>();
        //获取学员id
        String stuId = map.get("stuId");
        //获取邮箱号
        String stuEmail = map.get("stuEmail");
        //验证邮箱号是不是该学员绑定的邮箱号
        Student student = studentService.findStuById(stuId);
        if(!stuEmail.equals(student.getStuEmail())) {
            result.put("code", 200);
            result.put("msg", "邮箱未绑定该账号");
            return result;
        }

        //给邮箱发送验证码
       result = mailService.sendHtmlMail(stuEmail, "忘记密码");

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
     * 通过邮箱验证码来修改密码-学员忘记密码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/editPwdByMail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editPwdByMail(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        //将用户传来的表单数据转换成实体类
        Student student = JSONObject.parseObject(JSONObject.toJSONString(map), Student.class);
        //获取用户验证码
        String verifyCode = map.get("verifyCode");

        //获取验证码
        Object verifyMailCode = request.getSession().getAttribute("verifyMailCode");
        if("".equals(verifyMailCode) || verifyMailCode == null) {
            result.put("code", 201);
            result.put("msg", "验证码已过期！");
            return result;
        } else if(verifyCode == verifyMailCode) {
            result.put("code", 201);
            result.put("msg", "验证码不正确，请重新输入！");
            return result;
        }

//        studentService.editStudent(student);
        result.put("code", 200);
        result.put("msg", "修改密码成功！");
        return result;
    }

    /**
     * 学员预约教练
     * @return
     */
    @RequestMapping("/appointmentCoach")
    public ModelAndView appointmentCoach() {
        ModelAndView modelAndView = new ModelAndView("student/appointmentCoach");
        //学员预约时，需要知道有哪些教练可以预约
        List<Teacher> teacherList = teacherService.findAllTeacher();
        modelAndView.addObject("teacherList", teacherList);
        modelAndView.addObject("student", redisUtils.get("student"));
        return modelAndView;
    }

    /**
     * 学员预约教练
     * @return
     */
    @RequestMapping("/appointmentRecord")
    public ModelAndView appointmentRecord() {
        ModelAndView modelAndView = new ModelAndView("student/appointmentRecord");
        modelAndView.addObject("student", redisUtils.get("student"));
        return modelAndView;
    }

    /**
     * 展示学员预约时间线
     * @return
     */
    @RequestMapping("/appointmentTimeline")
    public ModelAndView appointmentTimeline() {
       ModelAndView modelAndView = new ModelAndView("student/appointment_timeline");
        Student student = (Student) redisUtils.get("student");

        //查询学员预约记录
        List<Map<String, Object>> list = appointmentService.findAppointListByStuId(student.getStuId());

        System.out.println(list);
        modelAndView.addObject("appointList", list);

        return modelAndView;
    }

    /**
     * 学员评价(评星，评论)
     * @param map
     * @return
     */
    @RequestMapping(value = "/stuContent", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> stuContent(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //获取id，根据id查询评论信息
        Appointment apponit = appointmentService.findApponitById(map.get("id"));
        String xypl = map.get("xypl");
        if (xypl== null) {
            xypl = "";
        }
        if (xypl.equals("1")) {
            result.put("code", 201);
            result.put("msg", "学员未评论，无法回复！");
            return result;
        }else if(xypl.equals("2")){

            //判断该评论是否不存在
            if(apponit == null) {
                result.put("code", 201);
                result.put("msg", "该条评论不存在！");
                return result;
            }

            //进行逻辑判断，当预约状态为3是学员才能进行评星、评价
            else if(5 == apponit.getAppointmentFlag()) {
                //符合评价操作
                apponit.setTeaContent(map.get("teaContent"));
                appointmentService.editAppoint(apponit);

                result.put("code", 200);
                result.put("msg", "评论成功！");
            }

            //当有评星或评价时，不能进行重复评论操作
            else if(apponit.getStuContent() != null || apponit.getStuStar() != null) {
                result.put("code", 201);
                result.put("msg", "您已对该次练车进行过评价，不重复评价！");
                return result;
            }else {
                result.put("code", 201);
                result.put("msg", "练车未结束，不能进行评价！");
                return result;
            }
        }else {

            //判断该评论是否不存在
            if(apponit == null) {
                result.put("code", 201);
                result.put("msg", "该条评论不存在！");
                return result;
            }

            //进行逻辑判断，当预约状态为3是学员才能进行评星、评价
            else if(5 == apponit.getAppointmentFlag()) {
                //符合评价操作
                apponit.setStuStar(Integer.parseInt(map.get("star")));
                apponit.setStuContent(map.get("stuContent"));
                appointmentService.editAppoint(apponit);

                result.put("code", 200);
                result.put("msg", "评论成功！");
            }

            //当有评星或评价时，不能进行重复评论操作
            else if(apponit.getStuContent() != null || apponit.getStuStar() != null) {
                result.put("code", 201);
                result.put("msg", "您已对该次练车进行过评价，不重复评价！");
                return result;
            }else {
                result.put("code", 201);
                result.put("msg", "练车未结束，不能进行评价！");
                return result;
            }
        }
        return result;
    }


    /**
     * 根据stuid查询预约记录
     * @param stuId
     * @return
     */
    @RequestMapping("/recordListById")
    @ResponseBody
    public Map<String, Object> recordListById(@RequestParam(required = true) String stuId,
                                              String start,
                                              String end,
                                              String subject,
                                              String teaName,
                                              @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                              @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
        Map<String, Object> result = new HashMap<>();

        //分页操作
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);

        //查询学员预约记录
        IPage<Map<String, Object>> appoints = appointmentService.findAppointByStuId(pageParam, stuId, start, end, subject, teaName);
        if(appoints.getTotal() == 0) {
            result.put("code", 201);
            result.put("msg", "没有预约记录！");
            return result;
        }

        result.put("data", appoints.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", appoints.getTotal());
        return result;
    }

    /**
     * 学员取消预约
     * @return
     */
    @RequestMapping("/cancle")
    @ResponseBody
    public Map<String, Object> cancleAppoint(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();

        if("3".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "已取消，不能再次取消！");
            return result;
        } else if ("4".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "已拒绝，不能再次取消！");
            return result;
        } else if ("5".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "已完成，不能再次取消！");
            return result;
        }

        appointmentService.editAppointFlag(map.get("id"), 3);
        result.put("code", 200);
        result.put("msg", "取消预约成功！");
        return result;
    }

    /**
     * 学员预约教练
     * @param map
     * @return
     */
    @RequestMapping(value = "/appointTeacher", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> appointTeacher(@RequestBody Map<String, String> map,
                                              HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        String dateRange = map.get("dateRange");
        String subject = map.get("subject");
        String teaId = map.get("teaId");
        //不为空
        if("".equals(dateRange)) {
            result.put("code", 201);
            result.put("msg", "请选择预约时间！");
            return result;
        }
        if("".equals(subject)) {
            result.put("code", 201);
            result.put("msg", "请选择预约科目！");
            return result;
        }
        if("".equals(teaId)) {
            result.put("code", 201);
            result.put("msg", "请选择预约教练！");
            return result;
        }

        //将用户传来的表单数据转换成实体类
        Appointment appointment = JSONObject.parseObject(JSONObject.toJSONString(map), Appointment.class);
        //截取预约开始、结束时间
        //截取开始和结束时间
        String start = dateRange.substring(0, dateRange.length()/2-1);
        String end = dateRange.substring(dateRange.length()/2+2, dateRange.length());
        //补全字段
        appointment.setId(UUID.randomUUID().toString().replaceAll("-", "").trim());
        appointment.setAppointmentStart(start);
        appointment.setAppointmentEnd(end);
        //        appointment.setCreateDate(new DateUtil().formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
        //预约提交后，状态应为待同意  1.待同意 2.已拒绝 3.已批准
        appointment.setAppointmentFlag(1);
        //保存预约信息
        try {
            appointmentService.saveAppointMent(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 200);
            result.put("msg", "预约信息已经发送！");
            return result;
        }

        result.put("code", 200);
        result.put("msg", "预约信息已经发送！");
        return result;
    }


}

