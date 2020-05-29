package com.example.greatpay;


public class CollectionModel {

    private String rname,remail,amount,image;

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRemail() {
        return remail;
    }

    public void setRemail(String remail) {
        this.remail = remail;
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

    public CollectionModel(String rname, String amount, String image) {
        this.rname = rname;
        this.amount = amount;
        this.image = image;
    }

    public CollectionModel(String rname, String image) {
        this.rname = rname;
        this.image = image;
    }

    public CollectionModel() {
    }
}
