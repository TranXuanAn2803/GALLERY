package com.example.gallery.Model;

import java.io.Serializable;

public class AlbumModel implements Serializable {
    private int id;
    private String name;
    private String password;
    public AlbumModel() {

    }
    public  AlbumModel(String name, String password)
    {
        this.name = name;
        this.password = password;
    }
    public AlbumModel(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
