package com.sunflower.framework.constants.error;

import com.sunflower.framework.constants.IEnum;

/**
 * @author sunflower
 */
public enum CommonEnum implements IEnum {

    /**
     * 请求成功
     */
    SUCCESS("S000000", "请求成功"),
    /**
     * 系统异常,请重试
     */
    ERROR("S000010", "系统异常,请重试"),
    /**
     * 获取验证码次数已超过上限，账号异常，请24小时后重试
     */
    ERROR_ALIYUN_SMS("S000011", "获取验证码次数已超过上限，账号异常，请24小时后重试"),
    /**
     * 系统异常
     */
    PARAM_VALID_FAIL("V000001", "系统异常"),
    /**
     * 无效请求
     */
    HTTP_FAIL_400("E400", "无效请求"),
    /**
     * 权限异常
     */
    HTTP_FAIL_403("E403", "权限异常"),
    /**
     * 无效请求
     */
    HTTP_FAIL_405("E405", "无效请求"),
    /**
     * 无效请求
     */
    HTTP_FAIL_415("E415", "无效请求"),
    /**
     * 系统异常,请重试
     */
    HTTP_FAIL_500("E500", "系统异常,请重试"),
    /**
     * 非法请求
     */
    HTTP_FAIL_666("E666", "非法请求"),
    /**
     *
     * 您的帐号已在其他微信帐号登录
     */
    LOGIN_FORCEOUT("S0003", "您的帐号已在其他微信帐号登录"),
    /**
     * 登录超时，请稍后再试
     */
    LOGIN_TIMEOUT("S0001", "登录超时，请稍后再试"),
    /**
     * 请绑定手机号后进行该操作
     */
    OPTNEEDPHONE("S0002", "请绑定手机号后进行该操作");

    private String code;

    private String message;

    CommonEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return null;
    }

    @Override
    public String message() {
        return null;
    }

}
