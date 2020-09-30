package com.llb.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车辆信息表
 * </p>
 *
 * @author llb
 * @since 2020-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Cars")
public class Cars implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车的唯一标识
     */
    @TableId("car_id")
    private String carId;

    /**
     * 教练外键
     */
    @TableField("tea_id")
    private String teaId;

    /**
     * 车辆车牌号
     */
    @TableField("car_number")
    private String carNumber;

    /**
     * 车辆出厂时间
     */
    @TableField("car_productDate")
    private String carProductDate;

    /**
     * 车子的类型
     */
    @TableField("car_type")
    private String carType;

    /**
     * 开始练车时间
     */
    @TableField("start_time")
    private String startTime;


}
