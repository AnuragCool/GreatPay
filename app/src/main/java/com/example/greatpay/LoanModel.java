package com.example.greatpay;


public class LoanModel {

    private String rUser,amount,image,purpose,name;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoanModel(String rUser, String amount, String image, String purpose, String name) {
        this.rUser = rUser;
        this.amount = amount;
        this.image = image;
        this.purpose = purpose;
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LoanModel() {
    }
}
