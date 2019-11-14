package com.ekaaksh.driverapp.Model.Response;

import com.ekaaksh.driverapp.Model.Beans.PreviousOrderBean;

import java.util.List;

public class PreviousOrderResponse {


    /**
     * success : 1
     * order : [{"res_id":"44","order_id":"100","user_id":"43","total_price":"400","status":"4","resta_name":"AHAA BIRYANI"}]
     */

    private String success;
    private List<PreviousOrderBean> order;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<PreviousOrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<PreviousOrderBean> order) {
        this.order = order;
    }
}
