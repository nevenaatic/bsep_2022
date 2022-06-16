package com.example.demo.dto;

public class UserTokenState {
    private String accessToken;
    private Long expiresIn;
    private String role;
    private Boolean enabled;
    private Boolean must_change_password;
    private Boolean twoFA;
    private long id;

    public UserTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
    }

    public UserTokenState(String accessToken, long expiresIn, String role, boolean enabled,boolean must_change_password, boolean twoFA, long id) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
        this.enabled = enabled;
        this.must_change_password = must_change_password;
        this.twoFA = twoFA;
        this.id = id;
    }

    public boolean isMust_change_password() {
        return must_change_password;
    }

    public void setMust_change_password(boolean must_change_password) {
        this.must_change_password = must_change_password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public Boolean getTwoFA() {
		return twoFA;
	}

	public void setTwoFA(Boolean twoFA) {
		this.twoFA = twoFA;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
    
    
}

