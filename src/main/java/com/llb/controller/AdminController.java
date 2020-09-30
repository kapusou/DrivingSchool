package com.llb.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.*;
import com.llb.service.IAdminService;
import com.llb.service.ICarsService;
import com.llb.service.IStudentService;
import com.llb.service.ITeacherService;
import com.llb.utils.DateUtil;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 管理员 前端控制器
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private ICarsService carsService;
    // 密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 展示管理员模块首页
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/index");
        return modelAndView;
    }

    /**
     * 评论管理页面
     * @return
     */
    @RequestMapping(value = "/appointmentList")
    public ModelAndView Appointment() {
        ModelAndView modelAndView = new ModelAndView("admin/appointmentList");
        return modelAndView;
    }

    /**
     * 评论管理页面
     * @return
     */
    @RequestMapping(value = "/appointmentById")
    @ResponseBody
    public Map<String,Object> AppointmentById(String xsmc,String jlmc,
                                              @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                              @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit ) {
        Map<String,Object> result = new HashMap<>();
        if ("".equals(xsmc)){
            xsmc = null;
        }
        if ("".equals(jlmc)){
            jlmc = null;
        }
        Page<List<Appointment>> pageParam = new Page<List<Appointment>>(page,limit);
        IPage<Map<String,Object>> map =  adminService.findAppointmentById(pageParam,xsmc,jlmc);
        result.put("data", map.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", map.getTotal());
        return result;
    }

    /**
     * 显示管理员修改密码(此处不应该与教练和学员相同，管理员无权通过邮箱修改密码！如果后期逻辑问题，在进行修改)
     * @return
     */
    @RequestMapping(value = "/showEditPwd")
    public ModelAndView showEditPwd() {
        ModelAndView modelAndView = new ModelAndView("admin/editPassword");
        return modelAndView;
    }

    /**
     * 修改管理员密码
     * @param map
     * @param request
     * @return
     */
    @RequestMapping(value = "/editAdminPwd", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editAdminPwd(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        Admin admin = adminService.findAdminById(map.get("adminId"));

        //没有该用户，返回提示
        if(admin == null) {
            result.put("code", 201);
            result.put("msg", "没有该管理员，请正规操作！");
            return result;
        }

        //判断密码是否正确，正确则修改密码
        if(!encoder.matches(map.get("oldPwd"), admin.getAdminPwd())) {
            result.put("code", 201);
            result.put("msg", "密码错误！");
            return result;
        }

        adminService.editAdminPwd(admin.getAdminMail(), encoder.encode(map.get("newPwd")));
        result.put("code", 200);
        result.put("msg", "修改成功！");
        return result;
    }

    /**
     * 修改管理员信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/editAdminInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editAdminInfo(@RequestBody Map<String, String> map,
                                             HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        //补全管理员信息
        Admin admin = adminService.findAdminById(map.get("adminId"));

        //没有该用户，返回提示
        if(admin == null) {
            result.put("code", 201);
            result.put("msg", "没有该管理员，请正规操作！");
            return result;
        }

        //判断密码是否正确，正确则修改密码
        if(!encoder.matches(map.get("adminPwd"), admin.getAdminPwd())) {
            result.put("code", 201);
            result.put("msg", "密码错误！");
            return result;
        }

        admin.setAdminAccount(map.get("adminAccount"));
        admin.setAdminMail(map.get("adminMail"));

        adminService.updateAdmin(admin);

        //修改完成密码后需要重新进行session缓存
        request.getSession().setAttribute("admin", admin);

        result.put("code", 200);
        result.put("msg", "修改信息成功！");
        return result;
    }

    /**
     * 显示管理员界面
     * @return
     */
    @RequestMapping("adminList")
    public ModelAndView adminList() {
        ModelAndView mv = new ModelAndView("admin/adminList");
        return mv;
    }

    /**
     * 查询所有管理员
     * @param account
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/findAllAdmin")
    @ResponseBody
    public Map<String, Object> findAllAdmin(String account,
                                            @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                            @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
        Map<String, Object> result = new HashMap<>();

        //分页操作
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);

        //查询所有管理员
        IPage<Map<String, Object>> adminList = adminService.findAllAdmin(pageParam, account);
        if(adminList.getTotal() == 0) {
            result.put("code", 200);
            result.put("msg", "没有管理员！");
            return result;
        }

        //分页查询数据传递
        result.put("data", adminList.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", adminList.getTotal());
        return result;
    }

    /**
     * 根据id修改管理员信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/editAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editAdmin(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();

        //将用户传来的表单数据转换成实体类
        Admin admin = JSONObject.parseObject(JSONObject.toJSONString(map), Admin.class);

        //密码加密
        admin.setAdminPwd(encoder.encode(map.get("adminPwd")));
        String adminId = admin.getAdminId();
        //根据id查找管理员信息
        Admin adminById = adminService.findAdminById(adminId);

        //判断是否有该管理员信息
        if(adminById == null) {
            result.put("code", 201);
            result.put("msg", "没有该管理员！");
            return result;
        }

        //修改信息
        adminService.updateAdmin(admin);

        result.put("code", 200);
        result.put("msg", "修改成功！");
        return result;
    }

    /**
     * 添加管理员信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addAdmin(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //将用户传来的表单数据转换成实体类
        Admin admin = JSONObject.parseObject(JSONObject.toJSONString(map), Admin.class);

        //密码加密
        admin.setAdminPwd(encoder.encode(map.get("adminPwd")));
        String adminId = admin.getAdminAccount();
        String adminMail = admin.getAdminMail();
        //根据id查找管理员信息
        Admin adminById = adminService.findAdminById(adminId);
        Admin adminByMail = adminService.findAdminByEmail(adminMail);

        //判断是否有该管理员信息
        if(adminById != null) {
            result.put("code", 201);
            result.put("msg", "用户名已存在！");
            return result;
        }

        if(adminByMail != null) {
            result.put("code", 201);
            result.put("msg", "邮箱已注册！");
            return result;
        }
        Date date = new Date();
        //添加
        admin.setAdminId(adminId);
        admin.setAdminCreatedate(new DateUtil().formatDate(date, "yyyy-MM-dd HH:mm:ss"));
        adminService.saveAdmin(admin);

        result.put("code", 200);
        result.put("msg", "修改成功！");
        return result;
    }

    /**
     * 根据id删除管理员信息
     * @param adminId
     * @return
     */
    @RequestMapping(value = "/deleteAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteAdmin(@RequestBody String adminId) {
        Map<String, Object> result = new HashMap<>();

        try {
            adminService.deleteAdmin(adminId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "删除失败！"+ e);
            return result;
        }

        result.put("code", 200);
        result.put("msg", "删除成功！");
        return result;
    }

    /**
     * 批量删除管理员
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteBatchAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteBatchAdmin(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //取出所有的id
        String adminIds = map.get("adminIds");
        String[] ids = adminIds.split(",");

        //遍历删除
        try {
            for (int i = 0; i < ids.length; i++) {
                String adminId = ids[i];
                adminService.deleteAdmin(adminId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "批量删除失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "批量删除成功！");
        return result;
    }

    /**
     * 展示学员管理页面
     * @return
     */
    @RequestMapping("/studentList")
    public ModelAndView studentList() {
        ModelAndView mv = new ModelAndView("admin/studentList");
        return mv;
    }

    /**
     * 展示车辆管理页面
     * @return
     */
    @RequestMapping("/carList")
    public ModelAndView carList() {
        ModelAndView mv = new ModelAndView("admin/carList");
        return mv;
    }


    /**
     * 展示教练管理页面
     * @return
     */
    @RequestMapping("/teacherList")
    public ModelAndView teacherList() {
        ModelAndView mv = new ModelAndView("admin/teacherList");
        return mv;
    }

    /**
     * 查找所有学员
     * @param
     * @return
     */
    @RequestMapping(value = "/findAllStudent")
    @ResponseBody
    public Map<String, Object> findAllStudent(String account,
                                              @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                              @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
        Map<String, Object> result = new HashMap<>();

        //分页操作
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);

        //查询所有管理员
        IPage<Map<String, Object>> stuList = studentService.studentList(pageParam, account);
        if(stuList.getTotal() == 0) {
            result.put("code", 200);
            result.put("msg", "没有管理员！");
            return result;
        }

        //分页查询数据传递
        result.put("data", stuList.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", stuList.getTotal());
        return result;
    }

    /**
     * 查找所有车辆
     * @param
     * @return
     */
    @RequestMapping(value = "/findAllCar")
    @ResponseBody
    public Map<String, Object> findAllCar(String account,
                                              @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                              @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
        Map<String, Object> result = new HashMap<>();

        //分页操作
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);

        //查询所有管理员
        IPage<Map<String, Object>> carList = carsService.carList(pageParam, account);
        if(carList.getTotal() == 0) {
            result.put("code", 200);
            result.put("msg", "没有车辆信息！");
            return result;
        }

        //分页查询数据传递
        result.put("data", carList.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", carList.getTotal());
        return result;
    }

    /**
     * 查找所有教练
     * @param
     * @return
     */
    @RequestMapping(value = "/findAllTeacher")
    @ResponseBody
    public Map<String, Object> findAllTeacher(String account,
                                              @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                              @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
        Map<String, Object> result = new HashMap<>();

        //分页操作
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);

        //查询所有管理员
        IPage<Map<String, Object>> teaList = teacherService.teacherList(pageParam, account);
        if(teaList.getTotal() == 0) {
            result.put("code", 200);
            result.put("msg", "没有教练！");
            return result;
        }

        //分页查询数据传递
        result.put("data", teaList.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", teaList.getTotal());
        return result;
    }
    /**
     * 批量删除学员
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteBatchStudent", method = RequestMethod.POST)
    public Map<String, Object> deleteBatchStudent(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //取出所有的id
        String stuIds = map.get("stuIds");
        String[] ids = stuIds.split(",");

        //遍历删除
        try {
            for (int i = 0; i < ids.length; i++) {
                String stuId = ids[i];
                studentService.deleteStudent(stuId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "批量删除失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "批量删除成功！");
        return result;
    }

    /**
     * 批量删除车辆信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteBatchCars", method = RequestMethod.POST)
    public Map<String, Object> deleteBatchCars(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //取出所有的id
        String stuIds = map.get("carIds");
        String[] ids = stuIds.split(",");

        //遍历删除
        try {
            for (int i = 0; i < ids.length; i++) {
                String carId = ids[i];
                carsService.deleteCar(carId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "批量删除失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "批量删除成功！");
        return result;
    }

    /**
     * 批量删除教练
     * @param map
     * @return
     */
    @RequestMapping(value = "/deleteBatchTeacher", method = RequestMethod.POST)
    public Map<String, Object> deleteBatchTeacher(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //取出所有的id
        String stuIds = map.get("teaIds");
        String[] ids = stuIds.split(",");

        //遍历删除
        try {
            for (int i = 0; i < ids.length; i++) {
                String teaId = ids[i];
                teacherService.deleteTeacher(teaId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "批量删除失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "批量删除成功！");
        return result;
    }

    /**
     * 根据学员id删除学员
     * @param stuId
     * @return
     */
    @RequestMapping(value = "/deleteStudent", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteStudent(@RequestBody String stuId) {
        Map<String, Object> result = new HashMap<>();
        try {
            studentService.deleteStudent(stuId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "删除失败！"+ e);
            return result;
        }

        result.put("code", 200);
        result.put("msg", "删除成功！");
        return result;
    }

    /**
     * 根据车辆id删除车辆
     * @param carId
     * @return
     */
    @RequestMapping(value = "/deleteCar", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteCar(@RequestBody String carId) {
        Map<String, Object> result = new HashMap<>();
        try {
            carsService.deleteCar(carId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "删除失败！"+ e);
            return result;
        }

        result.put("code", 200);
        result.put("msg", "删除成功！");
        return result;
    }

    /**
     * 根据学员id删除教练
     * @param teaId
     * @return
     */
    @RequestMapping(value = "/deleteTeacher", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteTeacher(@RequestBody String teaId) {
        Map<String, Object> result = new HashMap<>();
        try {
            teacherService.deleteTeacher(teaId);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "删除失败！"+ e);
            return result;
        }

        result.put("code", 200);
        result.put("msg", "删除成功！");
        return result;
    }

    /**
     * 管理员修改教练信息。管理员拥有最高权限，不需要验证任何信息！
     * @param map
     * @return
     */
    @RequestMapping(value = "/editTeacher", method = RequestMethod.POST)
    public Map<String, Object> editTeacher(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //转换成实体类
        Teacher teacher = JSONObject.parseObject(JSONObject.toJSONString(map), Teacher.class);

        //密码加密
        teacher.setTeaPwd(encoder.encode(map.get("teaPwd")));
        try {
            teacherService.editTeacher(teacher);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "修改信息失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "修改信息成功！");
        return result;
    }

    /**
     * 管理员修改用户信息。管理员拥有最高权限，不需要验证任何信息！
     * @param map
     * @return
     */
    @RequestMapping(value = "/editStudent", method = RequestMethod.POST)
    public Map<String, Object> editStudent(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //转换成实体类
        Student student = JSONObject.parseObject(JSONObject.toJSONString(map), Student.class);

        //密码加密
        student.setStuPwd(encoder.encode(map.get("stuPwd")));
        try {
            studentService.editStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "修改信息失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "修改信息成功！");
        return result;
    }

    /**
     * 管理员修改用户信息。管理员拥有最高权限，不需要验证任何信息！
     * @param map
     * @return
     */
    @RequestMapping(value = "/editCar", method = RequestMethod.POST)
    public Map<String, Object> editCar(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        //转换成实体类
        Cars car = JSONObject.parseObject(JSONObject.toJSONString(map), Cars.class);
        try {
            carsService.editCar(car);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "修改信息失败！");
            return result;
        }
        result.put("code", 200);
        result.put("msg", "修改信息成功！");
        return result;
    }

    /**
     * 展示预约记录
     * @return
     */
    @RequestMapping("/appointmentRecord")
    public ModelAndView showAppointRecord() {
        ModelAndView mv = new ModelAndView("admin/appointmentRecord");
        return mv;
    }

    /**
     * 管理员查询所有预约记录
     * @return
     */
    @RequestMapping(value = "/appointList")
    @ResponseBody
    public Map<String, Object> appointList(String start,
                                           String end,
                                           String subject,
                                           String teaName,
                                           String stuName,
                                           @RequestParam(defaultValue = "1", required = false, value = "page") Integer page,
                                           @RequestParam(defaultValue = "5", required = false, value = "limit") Integer limit) {
        Map<String, Object> result = new HashMap<>();

        //分页操作
        Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(page, limit);
        //分页查询
        IPage<Map<String, Object>> allAppoint = null;
        try {
            allAppoint = adminService.findAllAppoint(pageParam, start, end, subject, teaName, stuName);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 201);
            result.put("msg", "查询失败");
            return result;
        }
        result.put("data", allAppoint.getRecords());
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("count", allAppoint.getTotal());
        return result;
    }
    @RequestMapping(value = "/admin-list")
    public ModelAndView admin_list() {
    	ModelAndView modelAndView = new ModelAndView("admin-list");
    	return modelAndView;
    }
    
    @RequestMapping(value = "/admin-edit")
    public ModelAndView admin_edit() {
    	ModelAndView modelAndView = new ModelAndView("admin-edit");
    	return modelAndView;
    }
    
    /**
     * 展示管理员信息
     * @return
     */
    @RequestMapping(value = "/admin-info")
    public ModelAndView showInfo(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/admin-info");
        return modelAndView;
    }

    /**
     * 添加教练信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/addtea", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addTeacher(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        System.out.println(map);
        //将用户传来的表单数据转换成实体类
        Teacher teacher = JSONObject.parseObject(JSONObject.toJSONString(map), Teacher.class);

        //密码加密
        teacher.setTeaPwd(encoder.encode(map.get("teaPwd")));
        String studentMail = teacher.getTeaEmail();
        //根据id查找管理员信息
        Teacher adminByMail = teacherService.findTeacherByEamil(studentMail);

        //判断是否有该管理员信息
        if(adminByMail != null) {
            result.put("code", 201);
            result.put("msg", "邮箱已注册！");
            return result;
        }
        Date date = new Date();
        //添加
        teacher.setTeaId(UUID.randomUUID().toString().replace("-",""));
        teacher.setTeaCreatedate(new DateUtil().formatDate(date, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(teacher.toString());

        teacherService.saveTeacher(teacher);

        result.put("code", 200);
        result.put("msg", "添加成功！");
        return result;
    }


    /**
     * 添加学员信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/addStu", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addStudent(@RequestBody Map<String, String> map) {
        Map<String, Object> result = new HashMap<>();
        System.out.println(map);
        //将用户传来的表单数据转换成实体类
        Student student = JSONObject.parseObject(JSONObject.toJSONString(map), Student.class);

        //密码加密
        student.setStuPwd(encoder.encode(map.get("stuPwd")));
        String studentMail = student.getStuEmail();
        //根据id查找管理员信息
        Student adminByMail = studentService.findStuByEmail(studentMail);

        //判断是否有该管理员信息
        if(adminByMail != null) {
            result.put("code", 201);
            result.put("msg", "邮箱已注册！");
            return result;
        }
        Date date = new Date();
        //添加
        student.setStuId(UUID.randomUUID().toString().replace("-",""));
        student.setStuCreatedate(new DateUtil().formatDate(date, "yyyy-MM-dd HH:mm:ss"));

        studentService.saveStudent(student);

        result.put("code", 200);
        result.put("msg", "添加成功！");
        return result;
    }


    /**
     * 查询所有教练信息
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    @ResponseBody
    public List<Teacher> editPwdByMail() {
        List<Teacher> teachers = teacherService.findAllTeacher();
        return teachers;
    }


    /**
     * 添加车辆信息
     * @return
     */
    @RequestMapping("/addCar")
    @ResponseBody
    public Map<String, Object> addCar(@RequestBody Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        Cars car = JSONObject.parseObject(JSONObject.toJSONString(map), Cars.class);
        car.setCarId(UUID.randomUUID().toString().replace("-", ""));
        try {
            carsService.saveCar(car);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("msg", e);
            return result;
        }
        result.put("code", 200);
        result.put("msg", "成功添加车辆信息");
        return result;
    }

}

