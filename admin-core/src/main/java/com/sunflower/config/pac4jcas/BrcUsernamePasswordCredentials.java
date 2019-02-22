package com.sunflower.config.pac4jcas;

import java.io.Serializable;
import java.util.Objects;

class BrcUsernamePasswordCredentials implements Serializable {

	private String username;

	private String password;

	public BrcUsernamePasswordCredentials() {
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BrcUsernamePasswordCredentials)) {
			return false;
		}
		BrcUsernamePasswordCredentials that = (BrcUsernamePasswordCredentials) o;
		return Objects.equals(getUsername(), that.getUsername())
				&& Objects.equals(getPassword(), that.getPassword());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUsername(), getPassword());
	}

	@Override
	public String toString() {
		return "BrcUsernamePasswordCredentials{" + "username='" + username + '\''
				+ ", password='" + password + '\'' + '}';
	}

}
