package com.ftn.isa.DTO;

public class UserTokenDTO {
    private String accessToken;

    private String role;
    private Long expiresIn;

    public UserTokenDTO() {
        this.accessToken = null;
        this.expiresIn = null;
        this.role = null;
    }

    public UserTokenDTO(String accessToken, long expiresIn, String role) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
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
}
