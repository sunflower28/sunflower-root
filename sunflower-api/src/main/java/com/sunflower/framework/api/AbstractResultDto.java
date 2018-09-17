package com.sunflower.framework.api;

import com.sunflower.framework.constants.error.CommonEnum;

import java.io.Serializable;

/**
 * @author sunflower
 */
public abstract class AbstractResultDto implements Serializable {
    protected String code;
    protected String message;

    public AbstractResultDto() {
        this.code = CommonEnum.SUCCESS.code();
        this.message = CommonEnum.SUCCESS.message();
    }

    public boolean testSuccess() {
        return CommonEnum.SUCCESS.code().equals(this.code);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AbstractResultDto)) {
            return false;
        } else {
            AbstractResultDto other = (AbstractResultDto)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof AbstractResultDto;
    }

    public int hashCode() {
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    public String toString() {
        return "AbstractResultDto(code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }
}

