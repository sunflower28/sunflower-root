package com.sunflower.aop;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author sunflower
 */
public class BackCommonLogEntity implements Serializable {

	private String url;

	private String method;

	private String ip;

	private String classAndMethod;

	private List<Object> params;

	private Serializable result;

	private Date createDate;

	private String createBy;

	private long duration;

	private Date day;

	public BackCommonLogEntity() {
	}

	public String getUrl() {
		return this.url;
	}

	public String getMethod() {
		return this.method;
	}

	public String getIp() {
		return this.ip;
	}

	public String getClassAndMethod() {
		return this.classAndMethod;
	}

	public List<Object> getParams() {
		return this.params;
	}

	public Serializable getResult() {
		return this.result;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public long getDuration() {
		return this.duration;
	}

	public Date getDay() {
		return this.day;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setClassAndMethod(String classAndMethod) {
		this.classAndMethod = classAndMethod;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	public void setResult(Serializable result) {
		this.result = result;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BackCommonLogEntity)) {
			return false;
		}

		BackCommonLogEntity that = (BackCommonLogEntity) o;
		return getDuration() == that.getDuration()
				&& Objects.equals(getUrl(), that.getUrl())
				&& Objects.equals(getMethod(), that.getMethod())
				&& Objects.equals(getIp(), that.getIp())
				&& Objects.equals(getClassAndMethod(), that.getClassAndMethod())
				&& Objects.equals(getParams(), that.getParams())
				&& Objects.equals(getResult(), that.getResult())
				&& Objects.equals(getCreateDate(), that.getCreateDate())
				&& Objects.equals(getCreateBy(), that.getCreateBy())
				&& Objects.equals(getDay(), that.getDay());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUrl(), getMethod(), getIp(), getClassAndMethod(),
				getParams(), getResult(), getCreateDate(), getCreateBy(), getDuration(),
				getDay());
	}

	@Override
	public String toString() {
		return "BackCommonLogEntity{" + "url='" + url + '\'' + ", method='" + method
				+ '\'' + ", ip='" + ip + '\'' + ", classAndMethod='" + classAndMethod
				+ '\'' + ", params=" + params + ", result=" + result + ", createDate="
				+ createDate + ", createBy='" + createBy + '\'' + ", duration=" + duration
				+ ", day=" + day + '}';
	}

}
