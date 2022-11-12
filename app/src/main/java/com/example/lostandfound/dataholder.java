package com.example.lostandfound;

import java.util.Locale;

public class dataholder {
    String Mail, Name, Password, Pfp, Phone, RollNo, Whatsapp;

    public dataholder(String mail, String name, String password, String pfp, String phone, String rollNo, String whatsapp) {
        Mail = mail;
        Name = name;
        Password = password;
        Pfp = pfp;
        Phone = phone;
        RollNo = rollNo.toUpperCase(Locale.ROOT);
        Whatsapp = whatsapp;
    }
    public dataholder(){

    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPfp() {
        return Pfp;
    }

    public void setPfp(String pfp) {
        Pfp = pfp;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public String getWhatsapp() {
        return Whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        Whatsapp = whatsapp;
    }
}
