package com.llb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Appointment;
import com.llb.mapper.AppointmentMapper;
import com.llb.service.IAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学员-教练 预约练车记录表 服务实现类
 * </p>
 *
 * @author llb
 * @since 2020-03-17
 */
@Service
public class AppointmentServiceImpl  implements IAppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    /**
     * 保存预约记录
     * @param appointment
     */
    @Override
    public void saveAppointMent(Appointment appointment) {
        appointmentMapper.saveAppointMent(appointment);
    }

    /**
     * 根据教练id查询预约记录
     */
    @Override
	public IPage<Map<String, Object>> appointment_teaId(Page<Map<String, Object>> pageParam, String teaId,
			String appointmentStart, String appointmentEnd) {
		
		return appointmentMapper.appointment_teaId(pageParam, teaId, appointmentStart, appointmentEnd);
	}
    
    /**
     * 根据教练id查询预约记录
     */
    @Override
    public List<Map<String, Object>> appointment_teaId(String teaId) {
    	
    	return appointmentMapper.appointment_teaId(teaId,null,null);
    }
    
    /**
     * 根据学员id查找预约记录
     * @param stuId
     * @return
     */
    @Override
    public IPage<Map<String, Object>> findAppointByStuId(Page<Map<String, Object>> pageParam, String stuId,
                                                         String appointmentStart,
                                                         String appointmentEnd,
                                                         String subject,
                                                         String teaName) {
        return appointmentMapper.findAppointByStuId(pageParam, stuId, appointmentStart, appointmentEnd, subject, teaName);
    }

    /**
     * 根据学员id查找预约列表
     * @param stuId
     * @return
     */
    @Override
    public List<Map<String, Object>> findAppointListByStuId(String stuId) {
        return appointmentMapper.findAppointByStuId(stuId, null, null, null, null);
    }

    /**
     * 根据预约记录id修改状态
     * @param id
     * @param appointmentFlag
     */
    @Override
    public void editAppointFlag(String id, Integer appointmentFlag) {
        appointmentMapper.editAppointFlag(id, appointmentFlag);
    }

    /**
     * 根据id来查询预约记录
     * @param id
     * @return
     */
    @Override
    public Appointment findApponitById(String id) {
        return appointmentMapper.findApponitById(id);
    }

    /**
     * 修改预约记录
     * @param appointment
     */
    @Override
    public void editAppoint(Appointment appointment) {
        appointmentMapper.editAppoint(appointment);
    }

}
