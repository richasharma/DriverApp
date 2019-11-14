package com.ekaaksh.driverapp.Model.Response;

public class GetProfileResponse {

    /**
     * success : 1
     * name : delivery boy1
     * phone : 1234567890
     * email : deliveryboy@gmail.com
     * vehicle_no : rj-14-3423
     * vehicle_type : bike
     * limit_price : 2000
     * image : uploads/deliveryboy/my.jpeg
     * address :
     * revenue : 800
     * total_order : 3
     * complete_order : 2
     * cancel_order : 0
     */

    private String success;
    private String name;
    private String phone;
    private String email;
    private String vehicle_no;
    private String vehicle_type;
    private String limit_price;
    private String image;
    private String address;
    private int revenue;
    private int total_order;
    private int complete_order;
    private int cancel_order;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getLimit_price() {
        return limit_price;
    }

    public void setLimit_price(String limit_price) {
        this.limit_price = limit_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getTotal_order() {
        return total_order;
    }

    public void setTotal_order(int total_order) {
        this.total_order = total_order;
    }

    public int getComplete_order() {
        return complete_order;
    }

    public void setComplete_order(int complete_order) {
        this.complete_order = complete_order;
    }

    public int getCancel_order() {
        return cancel_order;
    }

    public void setCancel_order(int cancel_order) {
        this.cancel_order = cancel_order;
    }
}
