package com.sunflower.framework.api;

import com.sunflower.framework.constants.IEnum;

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
        return new PageResultDto(code, message);
    }

    public static <T> PageResultDto<T> error(IEnum ienum) {
        return new PageResultDto(ienum);
    }

    public static <T> PageResultDto<T> success(PageDto<T> data) {
        return new PageResultDto(data);
    }

    public PageDto<T> getData() {
        return this.data;
    }

    public void setData(PageDto<T> data) {
        this.data = data;
    }

    public String toString() {
        return "PageResultDto(data=" + this.getData() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResultDto)) {
            return false;
        } else {
            PageResultDto<?> other = (PageResultDto)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageResultDto;
    }

    public int hashCode() {
        int result = super.hashCode();
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }
}
