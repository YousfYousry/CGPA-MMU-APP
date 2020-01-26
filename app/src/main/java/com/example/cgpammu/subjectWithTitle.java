package com.example.cgpammu;

public class subjectWithTitle {
    private String Title;
    private String Code;
    private String Name;
    private String Hours;


    public subjectWithTitle(String title,String code, String name, String hours) {
        this.Title = title;
        this.Code = code;
        this.Name = name;
        this.Hours = hours;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getHours() {
        return Hours;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public void setHours(String hours) {
        this.Hours = hours;
    }

}
