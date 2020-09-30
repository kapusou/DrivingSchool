package com.llb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.llb.entity.Teacher;
import com.llb.mapper.TeacherMapper;
import com.llb.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教练员表 服务实现类
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 查询上下车记录
     * @param pageParam
     * @param xsmc
     * @param jlmc
     * @return
     */
    @Override
    public IPage<Map<String, Object>> findAppointmentSXCJL(IPage<Map<String, Object>> pageParam, String xsmc, String jlmc,String teaId) {
        return teacherMapper.findAppointmentSXCJL(pageParam,xsmc,jlmc,teaId);
    }

    /**
     * 查找所有的教练
     * @return
     */
    @Override
    public List<Teacher> findAllTeacher() {
        return teacherMapper.findAllTeacher();
    }

    /**
     * 根据教练名或邮箱查询教练
     * @param account
     * @return
     */
    @Override
    public Teacher findTeacher(String account) {
        return teacherMapper.findTeacher(account);
    }

    /**
     * 根据教练名或邮箱查询教练
     * @param account
     * @param email
     * @return
     */
    @Override
    public Teacher findTeacher(String account, String email) {
        return teacherMapper.findTeacher(account, email);
    }

    /**
     * 通过教练id查询教练
     * @param teaId
     * @return
     */
    @Override
    public Teacher findTeacherById(String teaId) {
        return teacherMapper.findTeacherById(teaId);
    }

    /**
     * 修改教练信息
     * @param teacher
     */
    @Override
    public void editTeacher(Teacher teacher) {
        teacherMapper.editTeacher(teacher);
    }

    /**
     * 根据用户名称和邮箱查询教练
     * @param account
     * @param email
     * @return
     */
    @Override
    public Teacher findTeaByAccAndPwd(String account, String email) {
        return teacherMapper.findTeaByAccAndPwd(account, email);
    }

    /**
     * 根据邮箱查询教练
     * @param teaEmail
     * @return
     */
    @Override
    public Teacher findTeacherByEamil(String teaEmail) {
        return teacherMapper.findTeacherByEmail(teaEmail);
    }

    /**
     * 修改教练密码
     * @param teaEmail
     * @param teaPwd
     * @return
     */
    @Override
    public Map<String, Object> editTeaPwd(String teaEmail, String teaPwd) {
        Map<String, Object> result = new HashMap<>();
        //先通过邮箱查询是否已经注册
        Teacher teacher = findTeacherByEamil(teaEmail);
        //未注册
        if(teacher == null) {
            result.put("code", 201);
            result.put("msg", "该邮箱没有被注册，请先注册！");
            return result;
        }
        teacherMapper.updateTeaPwd(teaEmail, teaPwd);
        result.put("code", 200);
        result.put("msg", "密码修改成功！");
        return result;
    }

    /**
     * 保存教练信息
     * @param teacher
     */
    @Override
    public void saveTeacher(Teacher teacher) {
        teacherMapper.saveTeacher(teacher);
    }

    /**
     * 分页查询教练列表
     * @return
     */
    @Override
    public IPage<Map<String, Object>> teacherList(IPage<Map<String, Object>> pageParam, String account) {
        return teacherMapper.teacherList(pageParam, account);
    }

    /**
     * 删除教练信息
     * @param teaId
     */
    @Override
    public void deleteTeacher(String teaId) {
        teacherMapper.deleteTeacher(teaId);
    }
}
