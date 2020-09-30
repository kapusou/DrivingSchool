package com.llb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.llb.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 教练员表 Mapper 接口
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

    List<Teacher> findAllTeacher();

    Teacher findTeacher(String account);

    Teacher findTeacher(@Param("account") String account, @Param("email") String email);

    Teacher findTeacherById(String teaId);

    void editTeacher(Teacher teacher);

    Teacher findTeaByAccAndPwd(@Param("account") String account, @Param("email") String email);

    Teacher findTeacherByEmail(String teaEmail);

    void updateTeaPwd(String teaEmail, String teacherPwd);

    void saveTeacher(Teacher teacher);

    IPage<Map<String, Object>> teacherList(IPage<Map<String, Object>> pageParam,@Param("teaAccount") String account);

    IPage<Map<String, Object>>findAppointmentSXCJL(IPage<Map<String, Object>> pageParam, @Param("xsmc") String xsmc, @Param("jlmc") String jlmc,@Param("teaId") String teaId);

    void deleteTeacher(String teaId);
}
