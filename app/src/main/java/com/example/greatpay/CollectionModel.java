package com.example.greatpay;


public class CollectionModel {

    private String rname,remail,amount,image,key,bUid,status,myname,purpose;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public CollectionModel(String rname, String amount, String image,String key,String bUid,String status,String myname,String purpose) {
        this.rname = rname;
        this.amount = amount;
        this.image = image;
        this.key=key;
        this.bUid=bUid;
        this.status=status;
        this.myname=myname;
        this.purpose=purpose;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getbUid() {
        return bUid;
    }

    public void setbUid(String bUid) {
        this.bUid = bUid;
    }

    public CollectionModel(String rname, String image) {
        this.rname = rname;
        this.image = image;
    }

    public CollectionModel() {
    }
}
