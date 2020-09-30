package com.llb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.llb.entity.Appointment;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员 Mapper 接口
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
public interface AdminMapper extends BaseMapper<Admin> {

    Admin findAdmin(String account);

    Admin findAdmin(@Param("account") String account, @Param("email") String email);

    Admin findAdminByAccAndMail(@Param("account") String account, @Param("email") String email);

    Admin findAdminByEmail(String adminEmail);

    void updateAdminPwd(@Param("adminEmail") String adminEmail, @Param("adminPwd") String adminPwd);

    void saveAdmin(Admin admin);

    Admin findAdminById(String adminId);

    void updateAdmin(Admin admin);

    IPage<Map<String,Object>> findAppointmentById(Page<List<Appointment>> pageParam, @Param("xsmc") String xsmc, @Param("jlmc") String jlmc);

    /**
     * 查询所有管理员
     * @param pageParam
     * @param account
     * @return
     */
    IPage<Map<String,Object>> findAllAdmin(Page<Map<String, Object>> pageParam, @Param("account") String account);

    /**
     * 根据id删除管理员
     * @param adminId
     */
    void deleteAdmin(String adminId);

    /**
     * 查询所有预约记录(分页)
     * @param pageParam
     * @param start 开始时间
     * @param end 结束时间
     * @param subject 科目
     * @param teaName 教练名称
     * @return
     */
    IPage<Map<String, Object>> findAllAppoint(Page<Map<String, Object>> pageParam, @Param("appointmentStart") String start, @Param("appointmentEnd") String end,
                                              @Param("subject") String subject, @Param("teaName") String teaName,
                                              @Param("stuName") String stuName);
}