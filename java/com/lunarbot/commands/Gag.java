package com.lunarbot.commands;

public class Gag {
    public String name, track;
    public int count, limit;

    public Gag(String name, String track, int count, int limit){
        this.name = name;
        this.count = count;
        this.limit = limit;
        this.track = track;
    }
}
