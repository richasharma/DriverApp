package com.ekaaksh.driverapp.Model.Beans;

public class OrderBean {
    /**
     * order_id : 97
     * ItemId : 250
     * ItemQty : 6
     * ItemAmt : 150
     * menu_name : VEG BIRYANI
     */

    private String order_id;
    private String ItemId;
    private String ItemQty;
    private String ItemAmt;
    private String menu_name;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String ItemId) {
        this.ItemId = ItemId;
    }

    public String getItemQty() {
        return ItemQty;
    }

    public void setItemQty(String ItemQty) {
        this.ItemQty = ItemQty;
    }

    public String getItemAmt() {
        return ItemAmt;
    }

    public void setItemAmt(String ItemAmt) {
        this.ItemAmt = ItemAmt;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
}
