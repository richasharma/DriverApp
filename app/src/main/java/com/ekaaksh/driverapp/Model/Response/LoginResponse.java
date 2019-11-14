package com.ekaaksh.driverapp.Model.Response;

import com.ekaaksh.driverapp.Model.Beans.LoginBean;

public class LoginResponse {
    private LoginBean info;
    private String status;
    private String msg;

    public LoginBean getInfo() {
        return info;
    }

    public void setInfo(LoginBean info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
