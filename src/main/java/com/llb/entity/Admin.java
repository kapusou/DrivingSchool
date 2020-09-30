package com.llb.entity;

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
 * 管理员
 * </p>
 *
 * @author llb
 * @since 2020-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员唯一标识
     */
    @TableId("admin_id")
    private String adminId;

    /**
     * 管理员账户名称
     */
    @TableField("admin_account")
    private String adminAccount;

    /**
     * 管理员密码
     */
    @TableField("admin_pwd")
    private String adminPwd;

    /**
     * 管理员邮箱
     */
    @TableField("admin_mail")
    private String adminMail;

    /**
     * 管理员创建时间
     */
    @TableField("admin_createdate")
    private String adminCreatedate;
}
