package com.darkmatter.greatpay;

public class NotificationModel  {
    private   String type, Email,Money,sUid,commonKey,myname,time,purpose,image,UniqueKey,status;

    public NotificationModel(String type, String email, String money,String sUid,String commonKey,String myname,String time,String purpose,String image,String UniqueKey,String status) {
        this.type = type;
        Email = email;
        Money = money;
        this.sUid=sUid;
        this.commonKey=commonKey;
        this.myname=myname;
        this.time=time;
        this.purpose=purpose;
        this.image=image;
        this.UniqueKey=UniqueKey;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommonKey() {
        return commonKey;
    }

    public void setCommonKey(String commonKey) {
        this.commonKey = commonKey;
    }

    public String getUniqueKey() {
        return UniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        UniqueKey = uniqueKey;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public NotificationModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }
}
