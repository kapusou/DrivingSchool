package com.llb.service;

import com.llb.entity.Student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学员信息表 服务类
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
public interface IStudentService extends IService<Student> {

    /**
     * 根据学员名或邮箱查询学员
     * @param account
     * @return
     */
    Student findStudent(String account);

    /**
     * 根据学员id查询信息
     * @param stuId
     * @return
     */
    Student findStuById(String stuId);
    
    /**
     * 根据教练id查询信息
     * @param stuId
     * @return
     */
    IPage<Map<String, Object>> findTeaTwoById(Page<Map<String, Object>> pageParam,String teaId,String stu_name,String start_time,String End_time);

    /**
     * 根据学员名和邮箱查询学员
     * @param account
     * @return
     */
    Student findStudent(String account, String email);

    /**
     * 根据学员账号和邮箱查找学员
     * @param account
     * @param email
     * @return
     */
    Student findStuByAccAndMail(String account, String email);
    /**
     * 修改学院账号信息
     * @param student
     * @return
     */
//    Map<String, Object> updateStudent(Student student);

    /**
     * 根据邮箱查询学员
     * @param email
     * @return
     */
    Student findStuByEmail(String email);

    /**
     * 修改学员密码
     * @param stuEmail
     * @param stuPwd
     * @return
     */
    Map<String, Object> editStuPwd(String stuEmail, String stuPwd);

    /**
     * 保存学员信息
     * @param student
     */
    void saveStudent(Student student);

    /**
     * 修改学员信息
     * @param student
     */
    void editStudent(Student student);

    /**
     * 验证密码是否正确
     * @param pwd
     * @return
     */
    Map<String, Object> verifyPwd(String email, String pwd);

    /**
     * 查询所有学员（分页）
     * @param pageParam
     * @return
     */
    IPage<Map<String, Object>> studentList(IPage<Map<String, Object>> pageParam,String stuAccount);

    /**
     * 根据id删除学员
     * @param stuId
     */
    void deleteStudent(String stuId);
}
