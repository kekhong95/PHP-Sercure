package com.example.kekho.myapplication.object;

/**
 * Created by kekho on 9/18/2017.
 */

public class User {
    String id;
    String user;
    String session;
    String phone;
    String name;

    public User(String id, String user, String session, String phone, String name) {
        this.id = id;
        this.user = user;
        this.session = session;
        this.phone = phone;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
