/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;

/**
 * Cluster class holds information about each independent component
 * @author thoang
 */
public class Cluster implements Comparable<Cluster>{
    ArrayList<Event> seq; // the subsequence corresponding to the current cluster
    ArrayList<Integer> alphabet; // the list of events belonging to this cluster
    ArrayList<Double> merge_size; // the size of  merging this cluster with the other clusters
    Double compress_size; // the compress size of this cluster
    Double uncompress_size; // the uncompress size of this cluster
    /**
     * copy the content of seq
     * @return a deep copy of seq
     */
    ArrayList<Event> getSequence(){
        ArrayList<Event> sequence=new ArrayList();
        for(int i=0;i<seq.size();i++ )
            sequence.add(new Event(seq.get(i).id,seq.get(i).timestamp));
        return sequence;
    }
    
    /**
     * print the cluster content
     */
    void print(){
        for(int i=0;i<seq.size();i++)
            System.out.print(seq.get(i).id+" "+seq.get(i).timestamp+" ");
        System.out.println();
    }

    @Override
    public int compareTo(Cluster o) {
        if((o.uncompress_size/o.compress_size)-(this.uncompress_size/this.compress_size)<0)
            return -1;
        else 
            return 1;
    }
}
