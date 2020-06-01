package com.example.greatpay;


public class LoanModel {

    private String rUser,amount,image,purpose,name,pUid,commonkey;

    public LoanModel(String rUser, String amount, String image, String purpose, String name, String pUid, String commonkey) {
        this.rUser = rUser;
        this.amount = amount;
        this.image = image;
        this.purpose = purpose;
        this.name = name;
        this.pUid = pUid;
        this.commonkey = commonkey;
    }

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }

    public String getCommonkey() {
        return commonkey;
    }

    public void setCommonkey(String commonkey) {
        this.commonkey = commonkey;
    }

    public LoanModel() {
    }
}
