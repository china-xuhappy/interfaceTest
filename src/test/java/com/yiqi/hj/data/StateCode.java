package com.yiqi.hj.data;

/**
 * 存储状态码
 * @author it_ce
 *
 */
public interface StateCode {
	/**
	 * 服务器请求成功
	 */
	public final static int CODE100 = 100;
	/**
	 * 账号已存在
	 */
	public final static int CODE200 = 200;
	/**
	 * 验证码错误
	 */
	public final static int CODE201 = 201;
	/**
	 * 验证码过期
	 */
	public final static int CODE202 = 202;
	/**
	 * 密码错误
	 */
	public final static int CODE203 = 203;
	/**
	 * 手机已绑定其他账号
	 */
	public final static int CODE204 = 204;
	/**
	 * token错误
	 */
	public final static int CODE205 = 205;
	/**
	 * 用户不存在
	 */
	public final static int CODE207 = 207;
	/**
	 * 该手机审核已经通过
	 */
	public final static int CODE220 = 220;
	/**
	 * 已绑定手机
	 */
	public final static int CODE300 = 300;
	/**
	 * 未绑定手机
	 */
	public final static int CODE301 = 301;
	/**
	 * 已领取
	 */
	public final static int CODE302 = 302;
	/**
	 * 优惠券已经没了
	 */
	public final static int CODE303 = 303;
	/**
	 * 活动已添加
	 */
	public final static int CODE330 = 330;
	/**
	 * 服务器请求超时
	 */
	public final static int CODE404 = 404;
	/**
	 * 密码错误
	 */
	public final static int CODE505 = 505;

}
