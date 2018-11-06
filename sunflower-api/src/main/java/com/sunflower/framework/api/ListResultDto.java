package com.sunflower.framework.api;

import com.sunflower.framework.constants.IEnum;

import java.util.Collections;
import java.util.List;

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
		return new ListResultDto(code, message);
	}

	public static <T> ListResultDto<T> error(IEnum ienum) {
		return new ListResultDto(ienum);
	}

	public static <T> ListResultDto<T> success(List<T> data) {
		return new ListResultDto(data);
	}

	public List<T> getData() {
		return this.data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String toString() {
		return "ListResultDto(data=" + this.getData() + ")";
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		else if (!(o instanceof ListResultDto)) {
			return false;
		}
		else {
			ListResultDto<?> other = (ListResultDto) o;
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
		return other instanceof ListResultDto;
	}

	public int hashCode() {
		int result = super.hashCode();
		Object data = this.getData();
		result = result * 59 + (data == null ? 43 : data.hashCode());
		return result;
	}

}
