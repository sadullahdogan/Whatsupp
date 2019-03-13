package com.example.sadul.whatsupp.models;

import java.util.List;

public class TopluMesajModel {
    List<mesajModel> list;

    public TopluMesajModel() {
    }

    public TopluMesajModel(List<mesajModel> list) {
        this.list = list;
    }

    public List<mesajModel> getList() {
        return list;
    }

    public void setList(List<mesajModel> list) {
        this.list = list;
    }

}
