package com.codewithgangez;

public class Passenger {
    private String firstName;
    private int seat;
    private int secondsInQueue;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getSeat(){
        return seat;
    }

    public void setSecondsInQueue(int seconds){
        this.secondsInQueue = seconds;
    }

    public int getSecondsInQueue(){
        return secondsInQueue;
    }
}

