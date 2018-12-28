package com.sunflower.api;

import com.sunflower.constants.IEnum;

/**
 * @author sunflower
 */
public class ResultDto<T> extends AbstractResultDto {

	private T data;

	public ResultDto() {

	}

	public ResultDto(IEnum ienum) {
		this.code = ienum.code();
		this.message = ienum.message();
	}

	public ResultDto(IEnum ienum, T data) {
		this.code = ienum.code();
		this.message = ienum.message();
		this.data = data;
	}

	public ResultDto(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResultDto(T data) {
		this.data = data;
	}

	public static <T> ResultDto<T> error(String code, String message) {
		return new ResultDto<>(code, message);
	}

	public static <T> ResultDto<T> error(IEnum ienum) {
		return new ResultDto<>(ienum);
	}

	public static <T> ResultDto<T> success(T data) {
		return new ResultDto<>(data);

	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String toString() {
		return "ResultDto(data=" + this.getData() + ")";
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		else if (!(o instanceof ResultDto)) {
			return false;
		}
		else {
			ResultDto<?> other = (ResultDto) o;
			if (!other.canEqual(this)) {
				return false;
			}
			else if (!super.equals(o)) {
				return false;
			}
			else {
				Object this$data = this.getData();
				Object other$data = other.getData();
				if (this$data == null) {
					if (other$data != null) {
						return false;
					}
				}
				else if (!this$data.equals(other$data)) {
					return false;
				}

				return true;
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof ResultDto;
	}

	public int hashCode() {
		int result = super.hashCode();
		Object data = this.getData();
		result = result * 59 + (data == null ? 43 : data.hashCode());
		return result;
	}

}
