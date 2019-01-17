package com.sunflower.api;

import com.sunflower.constants.error.CommonEnum;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author sunflower
 */
public abstract class AbstractResultDto implements Serializable {

	protected String code;

	protected String message;

	public AbstractResultDto() {
		this.code = CommonEnum.SUCCESS.code();
		this.message = CommonEnum.SUCCESS.message();
	}

	public boolean testSuccess() {
		return CommonEnum.SUCCESS.code().equals(this.code);
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AbstractResultDto that = (AbstractResultDto) o;
		return Objects.equals(code, that.code) && Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, message);
	}

	protected boolean canEqual(Object other) {
		return other instanceof AbstractResultDto;
	}

	@Override
	public String toString() {
		return "AbstractResultDto(code=" + this.getCode() + ", message="
				+ this.getMessage() + ")";
	}

}
