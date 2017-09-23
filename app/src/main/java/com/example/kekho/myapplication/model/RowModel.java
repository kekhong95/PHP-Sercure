package com.example.kekho.myapplication.model;

/**
 * Created by atbic on 2/9/2017.
 */

public class RowModel {

    boolean check;
    private int anhLogo;
    private String ten;

    public RowModel() {
    }

    public RowModel(boolean check, int anhLogo, String ten) {
        this.check = check;
        this.anhLogo = anhLogo;
        this.ten = ten;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getAnhLogo() {
        return anhLogo;
    }

    public void setAnhLogo(int anhLogo) {
        this.anhLogo = anhLogo;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}