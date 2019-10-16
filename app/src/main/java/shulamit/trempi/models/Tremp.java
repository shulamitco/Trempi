package shulamit.trempi.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.sql.Time;
import java.util.Random;

import shulamit.trempi.R;

public class Tremp {
    private String eventName;
    private String startPosition;
    private String exitTime;
    private int emptyPlaces;
    private String name;
    private String number;
    private int trempId;
    private int distance;
    private boolean enable;

    public Tremp(String eventName, String startPosition, String exitTime, int emptyPlaces, String name, String number, int trempId, boolean isEnable) {
        this.startPosition = startPosition;
        this.exitTime = exitTime;
        this.emptyPlaces = emptyPlaces;
        this.name = name;
        this.number = number;
        this.eventName = eventName;
        this.trempId = trempId;
        this.distance = new Random().nextInt(10) + 1;
        this.enable = isEnable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTrempId() {
        return trempId;
    }

    public void setTrempId(int trempId) {
        this.trempId = trempId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public int getEmptyPlaces() {
        return emptyPlaces;
    }

    public void setEmptyPlaces(int emptyPlaces) {
        this.emptyPlaces = emptyPlaces;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return   name + " from: " + startPosition +
                " go out on: " + getExitTime() +
                " with: " + emptyPlaces + " empty places"+"\n"+getDistance()+" km from you.";


    }
}
