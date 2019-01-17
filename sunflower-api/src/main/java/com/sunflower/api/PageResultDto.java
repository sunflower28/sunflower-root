package com.sunflower.api;

import com.sunflower.constants.IEnum;

import java.util.Objects;

/**
 * @author sunflower
 * @param <T>
 */
public class PageResultDto<T> extends AbstractResultDto {

	private PageDto<T> data;

	public PageResultDto() {
	}

	public PageResultDto(IEnum ienum) {
		this.code = ienum.code();
		this.message = ienum.message();
	}

	public PageResultDto(IEnum ienum, PageDto<T> data) {
		this.code = ienum.code();
		this.message = ienum.message();
		this.data = data;
	}

	private PageResultDto(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public PageResultDto(PageDto<T> data) {
		this.data = data;
	}

	public static <T> PageResultDto<T> error(String code, String message) {
		return new PageResultDto<>(code, message);
	}

	public static <T> PageResultDto<T> error(IEnum ienum) {
		return new PageResultDto<>(ienum);
	}

	public static <T> PageResultDto<T> success(PageDto<T> data) {
		return new PageResultDto<>(data);
	}

	public PageDto<T> getData() {
		return this.data;
	}

	public void setData(PageDto<T> data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PageResultDto)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		PageResultDto<?> that = (PageResultDto<?>) o;
		return Objects.equals(getData(), that.getData());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getData());
	}

	@Override
	public String toString() {
		return "PageResultDto(data=" + this.getData() + ")";
	}

	@Override
	protected boolean canEqual(Object other) {
		return other instanceof PageResultDto;
	}

}
