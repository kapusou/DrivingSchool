package com.llb.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Appointment;
import com.llb.entity.Student;
import com.llb.entity.Teacher;
import com.llb.service.IAppointmentService;
import com.llb.service.IStudentService;
import com.llb.service.ITeacherService;
import com.llb.service.MailService;
import com.llb.utils.DateUtil;
import com.llb.web.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.*;

/**
 * <p>
 * 教练员表 前端控制器
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;
    
    @Autowired
    private IStudentService studentService;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private IAppointmentService appointmentService;

    //密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 展示首页
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView showIndex() {
        ModelAndView modelAndView = new ModelAndView("teacher/index");
        return modelAndView;
    }

    /**
     * 教练查看学员列表
     * @param teaId
     * @return
     */
    @RequestMapping(value = "/student_administer")
    public ModelAndView student_administer(String teaId) {
        ModelAndView modelAndView = new ModelAndView("teacher/student_administer");
        modelAndView.addObject("teaId", teaId);
//        modelAndView.addObject("student_list", studentService.findTeaTwoById(teaId,stu_name));
        return modelAndView;
    }
    
    /**
     * 教练查看预约记录
     * @param teaId
     * @param stu_name
     * @return
     */
    @RequestMapping(value = "/student_appointment")
    public ModelAndView student_appointment(String teaId,String stu_name) {
    	ModelAndView modelAndView = new ModelAndView("teacher/student_appointment");
    	modelAndView.addObject("teaId", teaId);
//        modelAndView.addObject("student_list", studentService.findTeaTwoById(teaId,stu_name));
    	return modelAndView;
    }
    
    //根据教练id查询学员表
    @RequestMapping(value = "/student_two_id")
    @ResponseBody
    public Map<String, Object> student_two_id(String teaId,String stu_name,String bm_date,
    		@RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
            @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
    	System.out.println("sdgsghsh");
    	String start_time=null;
    	String End_time = null;
    	if (bm_date != "") {
    		System.out.println("sdgsghsh");
    		System.out.println(bm_date.substring(0,10));
    		start_time=bm_date.substring(0,10);
    		System.out.println(bm_date.substring(13,23));
    		End_time = bm_date.substring(13,23);
		}
    	Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);
    	IPage<Map<String, Object>> student_map = studentService.findTeaTwoById(pageParam,teaId,stu_name,start_time,End_time);
    	System.out.println(student_map.getRecords());
//    	Message me= new Message();
        Map<String, Object> result = new HashMap<>();
        result.put("data", student_map.getRecords()) ;
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count",student_map.getTotal());
    	return result;
    }
    
    /**
     * 展示教练的学员预约时间线
     * @return
     */
    @RequestMapping("/appointment_Student")
    public ModelAndView appointmentTimeline(String teaId) {
       ModelAndView modelAndView = new ModelAndView("teacher/appointment_Student");

        //查询学员预约记录
        List<Map<String, Object>> list = appointmentService.appointment_teaId(teaId);

        System.out.println(list);
        modelAndView.addObject("appointList", list);

        return modelAndView;
    }
    
    
    /**
     * 展示教练信息
     * @return
     */
    @RequestMapping("teacherInfo")
    public ModelAndView teacherInfo() {
        ModelAndView modelAndView = new ModelAndView("teacher/teacher_info");
        return modelAndView;
    }

    /**
     * 修改教练信息
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "editTeacherInfo", method = RequestMethod.POST)
    public Map<String, Object> editTeacherInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        //将用户传来的表单数据转换成实体类
        Teacher newTeacher = JSONObject.parseObject(JSONObject.toJSONString(map), Teacher.class);

        //密码加密
        newTeacher.setTeaPwd(encoder.encode(map.get("teaPwd")));

        //TODO:通过账号和邮箱查询，判断是否存在，如果存在，则该账号已被注册
        Teacher teacher1 = teacherService.findTeacher(newTeacher.getTeaAccount());
        Teacher teacher2 = teacherService.findTeacher(newTeacher.getTeaEmail());
        if(teacher1 != null) {
            //判断是否是一个账号，如果不是，则该账号已被注册
            if(!teacher1.getTeaId().equals(newTeacher.getTeaId())) {
                result.put("code", 201);
                result.put("msg", "该账号已被注册！");
                return result;
            }
        }
        if(teacher2 != null) {
            //判断是否是一个账号，如果不是，则该邮箱已被绑定
            if(!teacher2.getTeaId().equals(newTeacher.getTeaId())) {
                result.put("code", 201);
                result.put("msg", "该邮箱已被绑定！");
                return result;
            }
        }


        Teacher teacher = teacherService.findTeacherById(newTeacher.getTeaId());
        //既然能够给到stuId，那么用户就是存在的
        if(teacher == null) {
            result.put("code", 202);
            result.put("msg", "用户不存在！");
            return result;
        }

        //判断学员密码是否正确
        if(!encoder.matches(map.get("teaPwd"), teacher.getTeaPwd())) {
            result.put("code", 201);
            result.put("msg", "密码错误！");
            return result;
        }

        //修改密码
        teacherService.editTeacher(newTeacher);
        //将教练重新放入缓存中
        teacher = teacherService.findTeacherById(newTeacher.getTeaId());
        request.getSession().setAttribute("teacher", teacher);

        result.put("code", 200);
        result.put("msg", "修改成功");
        return result;
    }

    /**
     * 修改密码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/editTeaPwd", method = RequestMethod.POST)
    public Map<String, Object> editTeaPwd(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        //将用户传来的表单数据转换成实体类
        Teacher newTeacher = JSONObject.parseObject(JSONObject.toJSONString(map), Teacher.class);
        //密码加密
        newTeacher.setTeaPwd(encoder.encode(map.get("teaNewPwd")));

        String teaNewPwd = newTeacher.getTeaPwd();

        Teacher teacher = teacherService.findTeacherById(map.get("teaId"));
        //既然能够给到teaId，那么用户就是存在的
        if(teacher == null) {
            result.put("code", 202);
            result.put("msg", "用户不存在！");
            return result;
        }
        //判断学员密码是否正确
        if(!encoder.matches(map.get("teaPwd"), teacher.getTeaPwd())) {
            result.put("code", 201);
            result.put("msg", "密码错误！");
            return result;
        }
        //修改密码
        teacherService.editTeacher(teacher.setTeaPwd(teaNewPwd));
        result.put("code", 200);
        result.put("msg", "修改密码成功！");
        return result;
    }

    /**
     * 通过邮箱修改密码
     * @return
     */
    @RequestMapping("/foundPassword")
    public ModelAndView foundPassword() {
        ModelAndView modelAndView = new ModelAndView("teacher/found_password");
        return modelAndView;
    }

    /**
     * 上下车记录
     * @return
     */
    @RequestMapping("/SXCJL")
    public ModelAndView appointmentAXC(String teaId) {
        ModelAndView modelAndView = new ModelAndView("teacher/appointmentAXCList");
        modelAndView.addObject("teaId", teaId);
        return modelAndView;
    }

    /**
     * 模糊查询上下车记录
     * @return
     */
    @RequestMapping("/SXCJLList")
    public Map<String,Object> appointmentAXCList(String xsmc,String jlmc,String teaId,
                                                 @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                                 @RequestParam(defaultValue = "5", required = false, value = "size") Integer size) {
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, size);
        if ("".equals(xsmc)){
            xsmc = null;
        }
        System.out.println(jlmc);
        if ("".equals(jlmc)){
            jlmc = null;
        }
        System.out.println(xsmc+"*"+jlmc+teaId);
        IPage<Map<String, Object>> mapIPage = teacherService.findAppointmentSXCJL(pageParam,xsmc,jlmc,teaId);
        Message me= new Message();
        me.put("data", mapIPage.getRecords()) ;
        me.put("code", 200);
        me.put("msg", "查询成功");
        me.put("count",mapIPage.getTotal());
        return me;
    }

    /**
     * 展示修改密码页面
     * @return
     */
    @RequestMapping("/editPassword")
    public ModelAndView editPassword() {
        ModelAndView modelAndView = new ModelAndView("teacher/editPassword");
        return modelAndView;
    }

    /**
     * 教练发送验证码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getCode(@RequestBody Map<String, String> map, HttpServletRequest request) {
        final HttpSession httpSession = request.getSession();
        Map<String, Object> result = new HashMap<>();
        //获取教练id
        String teaId = map.get("teaId");
        //获取邮箱号
        String email = map.get("teaEmail");

        //验证邮箱是否是绑定该账号的邮箱
        Teacher teacher = teacherService.findTeacherById(teaId);
        if(!email.equals(teacher.getTeaEmail())) {
            result.put("code", 200);
            result.put("msg", "邮箱未绑定该账号");
            return result;
        }

        //给邮箱发送验证码
        result = mailService.sendHtmlMail(email, "忘记密码");
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
     * 通过邮箱验证码来修改密码-教练忘记密码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/editPwdByMail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editPwdByMail(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        //将用户传来的表单数据转换成实体类
        Teacher teacher = JSONObject.parseObject(JSONObject.toJSONString(map), Teacher.class);
        //密码加密
        teacher.setTeaPwd(encoder.encode(map.get("teaPwd")));

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

        //修改密码
        teacherService.editTeacher(teacher);
        result.put("code", 200);
        result.put("msg", "修改密码成功！");
        return result;
    }

    /**
     * 根据教练id查询预约记录
     */

    @RequestMapping(value = "/appointment_teaId")
    @ResponseBody
    public Message student_order(String teaId, String bm_date,
                                 @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                 @RequestParam(defaultValue = "5", required = false, value = "size") Integer size) {
        String start_time=null;
        String End_time = null;
        if (bm_date == null || bm_date == "") {
            bm_date = null;
        }else {
            start_time=bm_date.substring(0,10);
            End_time = bm_date.substring(13,23);
        }
        //    	if (bm_date != "") {
        //    		System.out.println("sdgsghsh");
        //    		System.out.println(bm_date.substring(0,10));
        //    		start_time=bm_date.substring(0,10);
        //    		System.out.println(bm_date.substring(13,23));
        //    		End_time = bm_date.substring(13,23);
        //    	}
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, size);
        IPage<Map<String, Object>> student_map = appointmentService.appointment_teaId(pageParam, teaId, start_time, End_time);
        Message me= new Message();
        me.put("data", student_map.getRecords()) ;
        me.put("code", 200);
        me.put("msg", "查询成功");
        me.put("count",student_map.getTotal());
        return me;
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
     * 教练拒绝预约
     * @return
     */
    @RequestMapping("/teaCancle")
    @ResponseBody
    public Map<String, Object> cancleTeaCancleAppoint(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        if("3".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "已取消，不能再次取消！");
            return result;
        }else if ("4".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "已拒绝，不能再次拒绝！");
            return result;
        }else if ("2".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "已同意预约！");
            return result;
        }else if ("5".equals(map.get("appointmentFlag"))) {
            result.put("code", 201);
            result.put("msg", "练车已完成，不可修改状态！");
            return result;
        }else if ("同   意 ".equals(map.get("appointmentFlag"))) {
            appointmentService.editAppointFlag(map.get("id"), 2);
            Appointment appointment = appointmentService.findApponitById(map.get("id"));
            Student student = new Student();
            student.setStuId(appointment.getStuId());
            student.setStuTwo(appointment.getTeaId());
            studentService.editStudent(student);
            result.put("code", 200);
            result.put("msg", "同意预约！");
            return result;
        }

        appointmentService.editAppointFlag(map.get("id"), 4);
        result.put("code", 200);
        result.put("msg", "拒绝预约成功！");
        return result;
    }


}

