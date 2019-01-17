package com.sunflower.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author sunflower
 */
@ApiModel(description = "基础请求参数")
public class InputDto implements Serializable {

	@JsonIgnore
	@ApiModelProperty(allowEmptyValue = true, required = false, value = "请求IP")
	private String ip;

	@JsonIgnore
	@ApiModelProperty(allowEmptyValue = true, required = false, value = "操作人")
	private String operator;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InputDto)) {
			return false;
		}
		InputDto inputDto = (InputDto) o;
		return Objects.equals(getIp(), inputDto.getIp())
				&& Objects.equals(getOperator(), inputDto.getOperator());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getIp(), getOperator());
	}

	protected boolean canEqual(Object other) {
		return other instanceof InputDto;
	}

	@Override
	public String toString() {
		return "InputDto{" + "ip='" + ip + '\'' + ", operator='" + operator + '\'' + '}';
	}

}