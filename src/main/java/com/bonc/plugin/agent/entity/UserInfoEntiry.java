package com.bonc.plugin.agent.entity;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/12
 */
public class UserInfoEntiry {
    private String username;
    private String realname;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInfoEntiry{" +
                "username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
