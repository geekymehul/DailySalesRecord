package com.example.gargc.dailysalesrecord.Model;

import java.io.Serializable;

/**
 * Created by gargc on 16-07-2018.
 */

public class CustomerContent implements Serializable {

    private String CustomerName;
    private  String EmailId;
    private  String PhoneNumber;
    private String AmountPaid;
    private String Notes;
    private String Image;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        AmountPaid = amountPaid;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }



}
