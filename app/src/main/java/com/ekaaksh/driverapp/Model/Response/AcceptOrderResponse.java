package com.ekaaksh.driverapp.Model.Response;

public class AcceptOrderResponse {

    /**
     * success : 1
     * order :
     */

    private String success;
    private String order;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
