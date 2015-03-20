/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

/**
 * Event class
 * @author thoang
 */
public class Event {
    int id; // id of the event
    int timestamp; // timestamp
    
    /**
     * constructor
     * @param i id of the event
     * @param t timestamp of the event
     */
    Event(int i, int t){
        id=i;
        timestamp=t;
    }
    
    @Override
    public String toString(){
        return " ["+id +","+timestamp+"] ";
    }
}
