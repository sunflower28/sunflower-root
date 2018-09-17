package com.sunflower.framework.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

/**
 *
 * @author sunflower
 */
@ApiModel(
        description = "基础请求参数"
)
public class InputPageDto extends InputDto {
    @ApiModelProperty(
            allowEmptyValue = false,
            required = true,
            value = "当前页码"
    )
    @Min(1L)
    private int pageNum = 1;
    @ApiModelProperty(
            allowEmptyValue = false,
            required = true,
            value = "页大小"
    )
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

    public String toString() {
        return "InputPageDto(pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof InputPageDto)) {
            return false;
        } else {
            InputPageDto other = (InputPageDto) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else if (this.getPageNum() != other.getPageNum()) {
                return false;
            } else {
                return this.getPageSize() == other.getPageSize();
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof InputPageDto;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = result * 59 + this.getPageNum();
        result = result * 59 + this.getPageSize();
        return result;
    }
}
