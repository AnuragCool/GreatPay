package com.example.greatpay;


public class LoanModel {

    private String rUser,amount,image;


    public String getrUser() {
        return rUser;
    }

    public void setrUser(String rUser) {
        this.rUser = rUser;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LoanModel(String rUser, String amount, String image) {
        this.rUser = rUser;
        this.amount = amount;
        this.image = image;
    }

    public LoanModel() {
    }
}
