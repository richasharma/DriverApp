package com.ekaaksh.driverapp.Model.Beans;

import java.io.Serializable;
import java.util.List;

public class NotificationData implements Serializable {


    private String success;
    private List<NotificationBean> order_details;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<NotificationBean> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<NotificationBean> order_details) {
        this.order_details = order_details;
    }
}
