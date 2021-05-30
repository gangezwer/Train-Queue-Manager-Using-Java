package com.codewithgangez;

public class PassengerQueue {
    public static Passenger[] queueArray = new Passenger[42];
    static int front;
    static int rear;
    static int qsize;
    static int maxStayInQueue;
    static int maxLength;

    public static void addPassenger(int index, Passenger next) {//adding the passenger to queue according to their seat number
        queueArray[index]=next;
        rear = (rear+1)%42;
        qsize++;
    }

    public Boolean isEmpty() {
        return true;
    }


    public static int getLength(){
        int count = 0;
        for (Passenger passenger: queueArray) {
            if (passenger != null){
                count++;
            }
        }
        return count;
    }

}