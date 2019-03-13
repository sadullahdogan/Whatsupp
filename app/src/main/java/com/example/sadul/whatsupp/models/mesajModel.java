package com.example.sadul.whatsupp.models;

public class mesajModel {
    private String from,text,seen,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public mesajModel() {
    }

    public mesajModel(String from, String text,String seen,String type) {
        this.from = from;
        this.text = text;
        this.seen=seen;
        this.type=type;

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "mesajModel{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                ", seen='" + seen + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
