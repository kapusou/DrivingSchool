package com.llb.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学员-教练 预约练车记录表
 * </p>
 *
 * @author llb
 * @since 2020-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学员预约练车表主键
     */
    @TableId("id")
    private String id;

    /**
     * 学员id主键
     */
    @TableField("stu_id")
    private String stuId;

    /**
     * 教练id主键
     */
    @TableField("tea_id")
    private String teaId;

    /**
     * 预约科目
     */
    @TableField("subject")
    private String subject;

    /**
     * 学员预约开始时间
     */
    @TableField("appointment_start")
    private String appointmentStart;

    /**
     * 学员预约结束时间
     */
    @TableField("appointment_end")
    private String appointmentEnd;

    /**
     * 学员上车时间
     */
    @TableField("boarding_time")
    private String boardingTime;

    /**
     * 学员下车时间
     */
    @TableField("alighting_time")
    private String alightingTime;

    /**
     * 学员评星。1-5星
     */
    @TableField("stu_star")
    private Integer stuStar;

    /**
     * 学员评论
     */
    @TableField("stu_content")
    private String stuContent;

    /**
     * 教练评论
     */
    @TableField("tea_content")
    private String teaContent;

    /**
     * 预约状态。1：学员预约，教练未同意。2：学员预约，教练同意
     */
    @TableField("appointment_flag")
    private Integer appointmentFlag;

    /**
     * 时间戳
     */
    @TableField("create_date")
    private String createDate;

}
