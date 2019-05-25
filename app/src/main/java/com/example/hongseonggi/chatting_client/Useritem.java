package com.example.hongseonggi.chatting_client;

public class Useritem {
    private String name;
    private String contents;

    public Useritem(String name , String contents){
        this.name = name;
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
