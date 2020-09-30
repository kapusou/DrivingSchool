package com.llb.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 教练员表
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teacher")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教练唯一标识
     */
    @TableId("tea_id")
    private String teaId;

    /**
     * 教练用户名
     */
    @TableField("tea_account")
    private String teaAccount;

    /**
     * 教练姓名
     */
    @TableField("tea_name")
    private String teaName;

    /**
     * 教练邮箱
     */
    @TableField("tea_email")
    private String teaEmail;

    /**
     * 教练密码
     */
    @TableField("tea_pwd")
    private String teaPwd;

    /**
     * 性别 1:男 0:女
     */
    @TableField("tea_sex")
    private Integer teaSex;

    /**
     * 教练年龄
     */
    @TableField("tea_birthday")
    private String teaBirthday;

    /**
     * 教练地址
     */
    @TableField("tea_address")
    private String teaAddress;

    /**
     * 教练联系方式
     */
    @TableField("tea_phone")
    private String teaPhone;

    /**
     * 创建时间
     */
    @TableField("tea_createdate")
    private String teaCreatedate;


}
