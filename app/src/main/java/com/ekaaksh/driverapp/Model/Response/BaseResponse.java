package com.ekaaksh.driverapp.Model.Response;

public class BaseResponse {

    /**
     * success : 1
     * message : Location has been updated
     */

    private String success;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
