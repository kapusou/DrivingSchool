package com.llb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.llb.entity.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学员-教练 预约练车记录表 Mapper 接口
 * </p>
 *
 * @author llb
 * @since 2020-03-17
 */
public interface AppointmentMapper {

    void saveAppointMent(Appointment appointment);

    IPage<Map<String, Object>> appointment_teaId(Page<Map<String, Object>> pageParam, @Param("teaId") String teaId,
    		@Param("appointmentStart") String appointmentStart,
            @Param("appointmentEnd") String appointmentEnd);
    
    List<Map<String, Object>> appointment_teaId(@Param("teaId") String teaId,
    		@Param("appointmentStart") String appointmentStart,
            @Param("appointmentEnd") String appointmentEnd);
    
    IPage<Map<String,Object>> findAppointByStuId(Page<Map<String, Object>> pageParam, @Param("stuId") String stuId,
                                                 @Param("appointmentStart") String appointmentStart,
                                                 @Param("appointmentEnd") String appointmentEnd,
                                                 @Param("subject") String subject,
                                                 @Param("teaName") String teaName
                                                 );

    List<Map<String, Object>> findAppointByStuId(@Param("stuId") String stuId,
                                                 @Param("appointmentStart") String appointmentStart,
                                                 @Param("appointmentEnd") String appointmentEnd,
                                                 @Param("subject") String subject,
                                                 @Param("teaName") String teaName);

    void editAppointFlag(@Param("id") String id, @Param("appointmentFlag") Integer appointmentFlag);

    Appointment findApponitById(String id);

    void editAppoint(Appointment appointment);
}
