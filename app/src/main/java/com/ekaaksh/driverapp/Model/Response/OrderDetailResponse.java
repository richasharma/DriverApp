package com.ekaaksh.driverapp.Model.Response;

import com.ekaaksh.driverapp.Model.Beans.DeliveryBoyBean;
import com.ekaaksh.driverapp.Model.Beans.OrderBean;
import com.ekaaksh.driverapp.Model.Beans.OrderDetailsBean;
import com.ekaaksh.driverapp.Model.Beans.UserBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailResponse {
    /**
     * success : 1
     * order_details : {"0":"100","order_id":"100","1":"43","res_id":"43","2":"KINGS PALACE FUNCTIONAL","restaurant_address":"KINGS PALACE FUNCTIONAL","3":"400","order_amount":"400","4":"4","order_status":"4","5":"43","user_id":"43","6":"jaipur","user_address":"jaipur","7":"26.9124","user_lat":"26.9124","8":"75.7873","user_long":"75.7873","9":"9314497070","user_phone":"9314497070","10":"1572431559","created_at":"1572431559","11":null,"accept_date_time":null,"12":null,"accept_status":null,"13":null,"delivery_date_time":null,"14":"19","deliveryboy_id":"19","15":null,"delivery_status":null,"16":"06-11-2019 01:41","delivered_date_time":"06-11-2019 01:41","17":null,"reject_date_time":null,"18":null,"reject_status":null,"19":"43","id":"43","20":"KINGS FOOD","restaurant_name":"KINGS FOOD","21":"26.9124","restaurant_lat":"26.9124","22":"resto_1548074999.jpg","photo":"resto_1548074999.jpg","23":"75.7872","restaurant_lng":"75.7872","24":"9848687852","phone":"9848687852","25":"30","delivery_time":"30"}
     * order : [{"order_id":"100","ItemId":"1","ItemQty":"5","ItemAmt":"400","menu_name":"Dal Fry"},{"order_id":"100","ItemId":"1","ItemQty":"2","ItemAmt":"500","menu_name":"Chicken"}]
     * deliveryboy : {"0":"19","id":"19","1":"43","res_id":"43","2":"delivery boy1","name":"delivery boy1","3":"1234567890","phone":"1234567890","4":"deliveryboy@gmail.com","email":"deliveryboy@gmail.com","5":"123456","password":"123456","6":"rj-14-3423","vehicle_no":"rj-14-3423","7":"bike","vehicle_type":"bike","8":null,"attendance":null,"9":"1571981962","created_at":"1571981962","10":"0","device_type":"0","11":"0","token":"0","12":"1","isactive":"1","13":"26.9161","lat":"26.9161","14":"75.8163","lng":"75.8163","15":"1","admin_id":"1","16":"0","isassign":"0","17":null,"limit_price":null,"18":"uploads/deliveryboy/my.jpeg","image":"uploads/deliveryboy/my.jpeg"}
     * user : {"0":"43","id":"43","1":"mukesh","fullname":"mukesh","2":"9314497070","phone_no":"9314497070","3":"test@test.com","email":"test@test.com","4":"R1hWdWtmcEIzTk01YkM4VjRTZXN0dz09","password":"R1hWdWtmcEIzTk01YkM4VjRTZXN0dz09","5":"302015","referal_code":"302015","6":"profile_1567755813.png","image":"profile_1567755813.png","7":"1567755813","created_at":"1567755813","8":"0","notify":"0","9":"appuser","login_with":"appuser"}
     */

    private String success;
    private OrderDetailsBean order_details;
    private DeliveryBoyBean deliveryboy;
    private UserBean user;
    private List<OrderBean> order;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public OrderDetailsBean getOrder_details() {
        return order_details;
    }

    public void setOrder_details(OrderDetailsBean order_details) {
        this.order_details = order_details;
    }

    public DeliveryBoyBean getDeliveryboy() {
        return deliveryboy;
    }

    public void setDeliveryboy(DeliveryBoyBean deliveryboy) {
        this.deliveryboy = deliveryboy;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<OrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderBean> order) {
        this.order = order;
    }




    /*    *//**
     * success : 1
     * order_details : {"0":"99","order_id":"99","1":"44","res_id":"44","2":"NEAR CABWALA OFFICE","restaurant_address":"NEAR CABWALA OFFICE","3":"123","order_amount":"123","4":"0","order_status":"0","5":"43","user_id":"43","6":"Jaipur","user_address":"Jaipur","7":"26.00000","user_lat":"26.00000","8":"75.0000000","user_long":"75.0000000","9":"1572424452","created_at":"1572424452","10":null,"accept_date_time":null,"11":null,"accept_status":null,"12":null,"delivery_date_time":null,"13":null,"delivery_status":null,"14":null,"delivered_date_time":null,"15":null,"reject_date_time":null,"16":null,"reject_status":null,"17":"44","id":"44","18":"AHAA BIRYANI","restaurant_name":"AHAA BIRYANI","19":"26.9124","restaurant_lat":"26.9124","20":"resto_1548076648.jpg","photo":"resto_1548076648.jpg","21":"75.7873","restaurant_lng":"75.7873","22":"8540899999","phone":"8540899999","23":"29","delivery_time":"29"}
     * order : [{"order_id":"99","ItemId":"250","ItemQty":"6","ItemAmt":"150","menu_name":"VEG BIRYANI"},{"order_id":"99","ItemId":"249","ItemQty":"6","ItemAmt":"150","menu_name":"EGG BIRYANI"},{"order_id":"99","ItemId":"4","ItemQty":"9","ItemAmt":"210","menu_name":"MUTTON BIRYANI"}]
     * restaurant_name : AHAA BIRYANI
     *//*

    private String success;
    private OrderDetailsBean order_details;
    private String restaurant_name;
    private List<OrderBean> order;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public OrderDetailsBean getOrder_details() {
        return order_details;
    }

    public void setOrder_details(OrderDetailsBean order_details) {
        this.order_details = order_details;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public List<OrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderBean> order) {
        this.order = order;
    }*/



}
