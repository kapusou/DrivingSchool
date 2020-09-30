package com.llb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.llb.entity.Appointment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员 服务类
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 查询评论
     * @return
     */
    IPage<Map<String,Object>>  findAppointmentById(Page<List<Appointment>> pageParam, String xsmc,String jlmc);

    /**
     * 根据管理员名称或邮箱查询管理员
     * @param account
     * @return
     */
    Admin findAdmin(String account);

    /**
     * 根据管理员名称或邮箱查询管理员
     * @param account
     * @param admin
     * @return
     */
    Admin findAdmin(String account, String email);

    /**
     * 根据管理员名称和密码查询管理员
     * @param account
     * @param admin
     * @return
     */
    Admin findAdminByAccAndMail(String account, String admin);

    /**
     * 根据管理员邮箱查询管理员
     * @param email
     * @return
     */
    Admin findAdminByEmail(String email);

    /**
     * 修改管理员密码
     * @param email
     * @param adminPwd
     * @return
     */
    Map<String, Object> editAdminPwd(String email, String adminPwd);

    /**
     * 保存管理员
     * @param admin
     */
    void saveAdmin(Admin admin);

    /**
     * 根据管理员id查找密码
     * @param adminId
     * @return
     */
    Admin findAdminById(String adminId);

    /**
     * 修改管理员信息
     * @param admin
     */
    void updateAdmin(Admin admin);

    /**
     * 查询所有管理员(分页)
     * @param pageParam
     * @param account
     * @return
     */
    IPage<Map<String,Object>> findAllAdmin(Page<Map<String, Object>> pageParam, String account);

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
    IPage<Map<String, Object>> findAllAppoint(Page<Map<String, Object>> pageParam, String start,
                                              String end, String subject, String teaName, String stuName);
}
