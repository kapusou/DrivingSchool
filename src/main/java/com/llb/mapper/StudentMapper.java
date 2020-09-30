package com.llb.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 学员信息表 Mapper 接口
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
public interface StudentMapper extends BaseMapper<Student> {

    Student findStudent(String account);

    Student findStudentById(String stuId);
    
    IPage<Map<String,Object>> findTeachertwoById(Page<Map<String, Object>> pageParam, @Param("teaId")String teaId, @Param("stu_name")String stu_name, @Param("start_time")String start_time, @Param("End_time")String End_time);

    Student findStudent(@Param("account") String account, @Param("email") String email);

    Student findStuByAccAndMail(@Param("account") String account, @Param("email") String email);

    /**
     * 分页查询所有学员
     * @param pageParam
     * @return
     */
    IPage<Map<String,Object>>  studentList(IPage<Map<String, Object>> pageParam, @Param("stuAccount") String stuAccount);

    Student findStuByEmail(String stuEmail);

    void updateStuPwd(String stuEmail, String stuPwd);

    void saveStudent(Student student);

    void editStudent(Student student);

    /**
     * 根据id删除学员
     * @param stuId
     */
    void deleteStudent(String stuId);
}
