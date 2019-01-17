package com.sunflower.api;

import com.sunflower.constants.IEnum;

import java.util.Objects;

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

	@Override
	public String toString() {
		return "ResultDto(data=" + this.getData() + ")";
	}

	@Override
	protected boolean canEqual(Object other) {
		return other instanceof ResultDto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ResultDto)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		ResultDto<?> resultDto = (ResultDto<?>) o;
		return Objects.equals(getData(), resultDto.getData());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getData());
	}

}
