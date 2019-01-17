package com.sunflower.exceptions;

import com.sunflower.constants.IEnum;

/**
 * @author sunflower
 */
public class BusinessException extends RuntimeException {

	public static final long serialVersionUID = 1L;

	private String code;

	private String type;

	private Object data;

	public BusinessException() {
	}

	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public BusinessException(IEnum ienum) {
		super(ienum.message());
		this.code = ienum.code();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
