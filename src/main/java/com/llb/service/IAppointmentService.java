package com.llb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.llb.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学员-教练 预约练车记录表 服务类
 * </p>
 *
 * @author llb
 * @since 2020-03-17
 */
public interface IAppointmentService{

    /**
     * 保存预约记录
     * @param appointment
     */
    void saveAppointMent(Appointment appointment);
    
    /**
     * 根据教练id查找预约记录
     * @param pageParam
     * @param stuId
     * @param appointmentStart
     * @param appointmentEnd
     * @param subject
     * @param teaName
     * @return
     */
    IPage<Map<String,Object>> appointment_teaId(Page<Map<String, Object>> pageParam, String teaId,
            String appointmentStart,
            String appointmentEnd
            );
    
    /**
     * 根据教练id查找预约记录
     * @param pageParam
     * @param stuId
     * @param appointmentStart
     * @param appointmentEnd
     * @param subject
     * @param teaName
     * @return
     */
    List<Map<String,Object>> appointment_teaId(String teaId);
    
    /**
     * 根据学员id查找预约记录
     * @param stuId
     * @return
     */
    IPage<Map<String,Object>> findAppointByStuId(Page<Map<String, Object>> pageParam, String stuId,
                                                 String appointmentStart,
                                                 String appointmentEnd,
                                                 String subject,
                                                 String teaName
                                                 );

    /**
     * 根据学员id查找预约列表
     * @param stuId
     * @return
     */
    List<Map<String, Object>> findAppointListByStuId(String stuId);

    /**
     * 根据预约记录id修改状态
     * @param stuId
     * @param appointmentFlag
     */
    void editAppointFlag(String id, Integer appointmentFlag);

    /**
     * 根据id来查询预约记录
     * @param id
     * @return
     */
    Appointment findApponitById(String id);

    /**
     * 修改预约记录
     * @param appointment
     */
    void editAppoint(Appointment appointment);

}
