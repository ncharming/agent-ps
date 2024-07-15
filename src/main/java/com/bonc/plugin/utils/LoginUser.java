package com.bonc.plugin.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 在线用户信息
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginUser {

	private String id;


	private String username;


	private String realname;


	private String password;

     /**
      * 当前登录部门code
      */
    private String orgCode;
	/**
	 * 当前登录部门名称
	 */
	private String orgCodeTxt;
	/**
	 * 头像
	 */

	private String avatar;

	/**
	 * 生日
	 */

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	/**
	 * 性别（1：男 2：女）
	 */
	private Integer sex;

	/**
	 * 电子邮件
	 */

	private String email;

	/**
	 * 电话
	 */

	private String phone;

	/**
	 * 状态(1：正常 2：冻结 ）
	 */
	private Integer status;

	private Integer delFlag;
	/**
     * 同步工作流引擎1同步0不同步
     */
    private Integer activitiSync;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 *  身份（1 普通员工 2 上级）
	 */
	private Integer userIdentity;

	/**
	 * 管理部门ids
	 */
	private String departIds;


	private String post;

	private String telephone;

	/**
	 * 多租户ids临时用，不持久化数据库(数据库字段不存在)
	 */
	private String relTenantIds;

	/**
	 * 登录用户当前租户名称
	 */
	private String tenantName;
	/**
	 * 登录用户当前租户id
	 */
	private Integer tenantId;

	/**
	 * 设备id uniapp推送用
	 */
	private String clientId;

	/**
	 * 其他信息
	 */
	private Map<String, Object> otherInfo;

}
