package com.sunflower.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import java.util.Objects;

/**
 * @author sunflower
 */
@ApiModel(description = "基础请求参数")
public class InputPageDto extends InputDto {

	@ApiModelProperty(allowEmptyValue = false, required = true, value = "当前页码")
	@Min(1L)
	private int pageNum = 1;

	@ApiModelProperty(allowEmptyValue = false, required = true, value = "页大小")
	@Min(1L)
	private int pageSize = 10;

	public InputPageDto() {
	}

	public int getPageNum() {
		return this.pageNum;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "InputPageDto(pageNum=" + this.getPageNum() + ", pageSize="
				+ this.getPageSize() + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InputPageDto)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		InputPageDto that = (InputPageDto) o;
		return getPageNum() == that.getPageNum() && getPageSize() == that.getPageSize();
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getPageNum(), getPageSize());
	}

	@Override
	protected boolean canEqual(Object other) {
		return other instanceof InputPageDto;
	}

}
