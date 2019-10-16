package shulamit.trempi.models;

import java.sql.Time;
import java.util.Date;

public class Event {
    private String name;
    private String date;
    private String time;
    private String address;
    private boolean isRecycler;
    public Event(String name, String date, String time, String address, boolean isRecycler) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.address = address;
        this.isRecycler = isRecycler;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int isRecycler() {
        if(isRecycler)
            return 1;
        return 0;
    }

    public void setRecycler(boolean recycler) {
        isRecycler = recycler;
    }

    @Override
    public String toString() {
        return name  + " " + getDate()  +"\n"+ getTime()+ " " + address;
    }
}
