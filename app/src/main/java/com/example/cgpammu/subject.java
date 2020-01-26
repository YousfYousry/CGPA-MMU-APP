package com.example.cgpammu;

public class subject {
    private String Code;
    private String Name;
    private String Hours;
    private int Color;


    public subject(String code, String name, String hours, int color) {
        this.Code = code;
        this.Name = name;
        this.Hours = hours;
        this.Color = color;
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


    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}
