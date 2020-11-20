package com.darkmatter.greatpay;


public class SummaryModel {

    private String name,amount,status,email,time,image;


    public SummaryModel() {
    }

    public SummaryModel(String name, String amount,String time,String image) {
        this.name = name;
        this.amount = amount;
        this.time=time;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
