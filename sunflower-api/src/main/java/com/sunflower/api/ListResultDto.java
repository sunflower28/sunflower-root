package com.sunflower.api;

import com.sunflower.constants.IEnum;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListResultDto<T> extends AbstractResultDto {

	private List<T> data = Collections.emptyList();

	public ListResultDto() {
		this.data = Collections.emptyList();
	}

	public ListResultDto(IEnum ienum) {
		this.code = ienum.code();
		this.message = ienum.message();
	}

	public ListResultDto(IEnum ienum, List<T> data) {
		this.code = ienum.code();
		this.message = ienum.message();
		this.data = data;
	}

	private ListResultDto(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ListResultDto(List<T> data) {
		this.data = data;
	}

	public static <T> ListResultDto<T> error(String code, String message) {
		return new ListResultDto<>(code, message);
	}

	public static <T> ListResultDto<T> error(IEnum ienum) {
		return new ListResultDto<>(ienum);
	}

	public static <T> ListResultDto<T> success(List<T> data) {
		return new ListResultDto<>(data);
	}

	public List<T> getData() {
		return this.data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ListResultDto(data=" + this.getData() + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ListResultDto)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		ListResultDto<?> that = (ListResultDto<?>) o;
		return Objects.equals(getData(), that.getData());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getData());
	}

	@Override
	protected boolean canEqual(Object other) {
		return other instanceof ListResultDto;
	}

}
