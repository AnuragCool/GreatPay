package com.example.greatpay;


public class SummaryModel {

    private String name,amount,status,email,time;


    public SummaryModel() {
    }

    public SummaryModel(String name, String amount,String time) {
        this.name = name;
        this.amount = amount;
        this.time=time;
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
