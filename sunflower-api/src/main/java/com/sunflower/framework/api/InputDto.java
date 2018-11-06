package com.sunflower.framework.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

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
		if (o == this) {
			return true;
		}
		else if (!(o instanceof Object)) {
			return false;
		}
		else {
			InputDto other = (InputDto) o;
			if (!other.canEqual(this)) {
				return false;
			}
			else {
				Object this$ip = this.getIp();
				Object other$ip = other.getIp();
				if (this$ip == null) {
					if (other$ip != null) {
						return false;
					}
				}
				else if (!this$ip.equals(other$ip)) {
					return false;
				}

				Object this$operator = this.getOperator();
				Object other$operator = other.getOperator();
				if (this$operator == null) {
					if (other$operator != null) {
						return false;
					}
				}
				else if (!this$operator.equals(other$operator)) {
					return false;
				}

				return true;
			}
		}
	}

	@Override
	public int hashCode() {
		int result = 1;
		Object ip = this.getIp();
		result = result * 59 + (ip == null ? 43 : ip.hashCode());
		Object operator = this.getOperator();
		result = result * 59 + (operator == null ? 43 : operator.hashCode());
		return result;
	}

	protected boolean canEqual(Object other) {
		return other instanceof InputDto;
	}

	@Override
	public String toString() {
		return "InputDto{" + "ip='" + ip + '\'' + ", operator='" + operator + '\'' + '}';
	}

}