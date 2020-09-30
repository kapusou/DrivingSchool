package com.llb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.llb.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教练员表 服务类
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
public interface ITeacherService extends IService<Teacher> {

    /**
     * 上下车记录
     * @param pageParam
     * @param xsmc
     * @param jlmc
     * @return
     */
    IPage<Map<String, Object>>findAppointmentSXCJL(IPage<Map<String, Object>> pageParam, String xsmc,String jlmc,String teaId);

    /**
     * 查找所有的教练
     * @return
     */
    List<Teacher> findAllTeacher();

    /**
     * 根据教练名或邮箱查询教练
     * @param account
     * @return
     */
    Teacher findTeacher(String account);

    /**
     * 根据教练名或邮箱查询教练
     * @param account
     * @return
     */
    Teacher findTeacher(String account, String email);

    /**
     * 通过教练id查询教练
     * @param teaId
     * @return
     */
    Teacher findTeacherById(String teaId);

    /**
     * 修改用户信息
     * @param teacher
     */
    void editTeacher(Teacher teacher);

    /**
     * 根据用户名称和邮箱查询教练
     * @param account
     * @param email
     * @return
     */
    Teacher findTeaByAccAndPwd(String account, String email);

    /**
     * 根据邮箱查询教练
     * @param teaEmail
     * @return
     */
    Teacher findTeacherByEamil(String teaEmail);

    /**
     * 修改教练密码
     * @param teaEmail
     * @param teaPwd
     * @return
     */
    Map<String, Object> editTeaPwd(String teaEmail, String teaPwd);

    /**
     * 保存教练信息
     * @param teacher
     */
    void saveTeacher(Teacher teacher);

    /**
     * 分页查询教练列表
     * @return
     */
    IPage<Map<String, Object>> teacherList(IPage<Map<String, Object>> pageParam, String account);

    /**
     * 删除教练信息
     * @param teaId
     */
    void deleteTeacher(String teaId);
}
