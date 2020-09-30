package com.llb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Student;
import com.llb.mapper.StudentMapper;
import com.llb.service.IStudentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学员信息表 服务实现类
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private StringRedisTemplate template;
    /**
     * 根据学员名或邮箱查询学员
     * @param account
     * @return
     */
    @Override
    public Student findStudent(String account) {
        return studentMapper.findStudent(account);
    }

    /**
     * 根据学员id查询信息
     * @param stuId
     * @return
     */
    @Override
    public Student findStuById(String stuId) {
        return studentMapper.findStudentById(stuId);
    }
    
    /**
     * 根据教练id查询信息
     * @param stuId
     * @return
     */
    @Override
    public IPage<Map<String,Object>> findTeaTwoById(Page<Map<String, Object>> pageParam, String teaId, String stu_name, String start_time, String End_time) {
    	return studentMapper.findTeachertwoById(pageParam,teaId,stu_name,start_time,End_time);
    }

    /**
     * 根据学员名或邮箱查询学员
     * @param account
     * @return
     */
    @Override
    public Student findStudent(String account, String email) {
        return studentMapper.findStudent(account, email);
    }

    /**
     * 根据学员账号和邮箱查找学员
     * @param account
     * @param email
     * @return
     */
    @Override
    public Student findStuByAccAndMail(String account, String email) {
        return studentMapper.findStuByAccAndMail(account, email);
    }

    /**
     * 根据邮箱查询学员
     * @param email
     * @return
     */
    @Override
    public Student findStuByEmail(String email) {
        return studentMapper.findStuByEmail(email);
    }

    

    /**
     * 修改学员密码
     * @param stuEmail
     * @param stuPwd
     * @return
     */
    @Override
    public Map<String, Object> editStuPwd(String stuEmail, String stuPwd) {
        Map<String, Object> result = new HashMap<>();
        //先通过邮箱查询是否已经注册
        Student student = findStuByEmail(stuEmail);
        //未注册
        if(student == null) {
            result.put("code", 201);
            result.put("msg", "该邮箱没有被注册，请先注册！");
            return result;
        }
        studentMapper.updateStuPwd(stuEmail, stuPwd);
        result.put("code", 200);
        result.put("msg", "密码修改成功！");
        return result;
    }

    /**
     * 保存学员信息
     * @param student
     */
    @Override
    public void saveStudent(Student student) {
        studentMapper.saveStudent(student);
    }

    /**
     * 修改学员信息
     * @param student
     */
    @Override
    public void editStudent(Student student) {
        studentMapper.editStudent(student);
    }

    /**
     * 验证密码是否正确
     * @param id
     * @param pwd
     * @return
     */
    @Override
    public Map<String, Object> verifyPwd(String id, String pwd) {
        Map<String, Object> result = new HashMap<>();
        //邮箱号唯一，可以根据邮箱号查找学员的密码
        Student student = studentMapper.findStudentById(id);
        //对比密码是否正确
        if(!encoder.matches(pwd, student.getStuPwd())) {
            result.put("code", 201);
            result.put("msg", "密码错误，请重新输入！");
            return result;
        }
        result.put("code", 200);
        result.put("student", student);
        result.put("msg", "密码正确！");
        return result;
    }

    /**
     * 查询所有学员（分页）
     * @param pageParam
     * @return
     */
    @Override
    public IPage<Map<String, Object>> studentList(IPage<Map<String, Object>> pageParam, String stuAccount) {
        return studentMapper.studentList(pageParam, stuAccount);
    }

    /**
     * 根据id删除学员
     * @param stuId
     */
    @Override
    public void deleteStudent(String stuId) {
        studentMapper.deleteStudent(stuId);
    }
}
