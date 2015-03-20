/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Implementation of an Hierarchical Clustering algorithm for decomposing a sequence into independent subsequences  
 * @author thoang
 */
public class Clustering {
    HashMap<Integer, String> labels; // a map from event ids to event names
    ArrayList<Cluster> clusters; 
    Integer Nevents; // the total number of events in the data
    double size; // the current size of the data
    
    /**
     * Initialization: create M clusters, where M is the number distinct events
     * in the alphabet. Each cluster corresponds to an event in the alphabet.
     */
    void initialize(){
        //calculate the compress size of each cluster
        size=0;
        for(int i=0;i<clusters.size();i++){
            clusters.get(i).compress_size=clusters.get(i).seq.size()*LZ78.log2(Nevents/(clusters.get(i).seq.size()+0.0)); // the cost of encoding the cluster id stream
            clusters.get(i).uncompress_size=clusters.get(i).compress_size;
            size+=clusters.get(i).compress_size;
           // System.out.println(clusters.get(i).compress_size);
        }
        
        //calculate the compress size when  a cluster is merged with the another cluster.
        for(int i=0;i<clusters.size();i++){
            clusters.get(i).merge_size=new ArrayList();
            for(int j=0;j<i;j++){
                clusters.get(i).merge_size.add(clusters.get(j).merge_size.get(i));
            }
            clusters.get(i).merge_size.add(Double.MAX_VALUE);
            for(int j=i+1;j<clusters.size();j++){
                //LZW lz=new LZW();
                LZ78 lz=new LZ78();
                lz.seq=clusters.get(i).getSequence();
                lz.merge(clusters.get(j).getSequence());                
                Double sz=lz.seq.size()*LZ78.log2(Nevents/(lz.seq.size()+0.0));// the cost of encoding the cluster id stream
                sz+=lz.encode(); // the cost of encoding the merged cluster
                clusters.get(i).merge_size.add(sz);                
            }
            //System.out.println(clusters.get(i).seq +" "+clusters.get(i).alphabet);
            //System.out.println(clusters.get(i).merge_size);
        }
    }
    
    /**
     * merge two clusters with indices c1 and c2 
     * @param c1 the index of first cluster
     * @param c2 the index of second cluster
     */
    void merge(int c1, int c2){
        int first=c1;
        int second=c2;
        if(first<c2){
            first=c2;
            second=c1;
        }
        
        //LZW lzm=new LZW();
        LZ78 lzm=new LZ78();
        lzm.seq=clusters.get(first).getSequence();
        lzm.merge(clusters.get(second).seq);
        Double szm=lzm.seq.size()*LZ78.log2(Nevents/(lzm.seq.size()+0.0));// the cost of encoding the cluster id stream
        szm+=lzm.encode(); // the cost of encoding the merged cluster (c1,c2)
        for(int i=0;i<clusters.size();i++){
            if(i!=c1&&i!=c2){
                clusters.get(i).merge_size.remove(first);
                clusters.get(i).merge_size.remove(second);
                //LZW lz=new LZW();
                LZ78 lz=new LZ78();
                lz.seq =clusters.get(i).getSequence();
                lz.merge(lzm.seq);
                Double sz=lz.seq.size()*LZ78.log2(Nevents/(lz.seq.size()+0.0));// the cost of encoding the cluster id stream
                sz+=lz.encode(); // the cost of encoding the merged cluster (i,c1,c2)
                clusters.get(i).merge_size.add(sz);
            }
        }
        //copy the alphabet
        ArrayList<Integer> a=new ArrayList();
        for(int i=0;i<clusters.get(first).alphabet.size();i++)
            a.add(new Integer(clusters.get(first).alphabet.get(i)));
        for(int i=0;i<clusters.get(second).alphabet.size();i++)
            a.add(new Integer(clusters.get(second).alphabet.get(i)));
        //remove old clusters
        Cluster c=new Cluster();
        c.uncompress_size=clusters.get(first).uncompress_size+clusters.get(second).uncompress_size;
        size-=clusters.get(first).compress_size;
        size-=clusters.get(second).compress_size;
        size+=szm;
        clusters.remove(first);
        clusters.remove(second);
        //add the merged cluster        
        c.seq=new ArrayList();
        c.seq.addAll(lzm.seq);
        c.compress_size=szm;       
        c.merge_size=new ArrayList();
        for(int i=0;i<clusters.size();i++){
            c.merge_size.add(clusters.get(i).merge_size.get(clusters.get(i).merge_size.size()-1));
        }
        c.merge_size.add(Double.MAX_VALUE);
        c.alphabet=a;
        clusters.add(c);
    }
    
    /**
     * cluster events in the alphabet by a bottom-up hierarchical clustering algorithm
     */
    void hierarchical_clustering(){
        System.err.println("Initialization... ");
        initialize();
        double un_compsize=size;
        System.err.println("Initialized with "+clusters.size()+" clusters");
        double max;
        int c1=0,c2=1;
        while(true){
            System.out.println("Current size... "+ size + " bits");           
            max=0;
            for(int i=0;i<clusters.size();i++){
                for(int j=i+1;j<clusters.size();j++){
                    double ben=clusters.get(i).compress_size;
                    ben+=clusters.get(j).compress_size;
                    ben-=clusters.get(i).merge_size.get(j);
                    //System.out.println(i+" "+j+" "+ben+" "+clusters.get(i).merge_size.get(j));
                    if(max<ben){
                        c1=i;
                        c2=j;
                        max=ben;
                    }
                }
            }
            if(max==0)
                break;
            else
                merge(c1,c2);
        }
        System.out.println("Compression ratio: "+un_compsize/size);
    }
    
    /**
     * print the clusters
     */
    void print(){
        Collections.sort(clusters);
        for(int i=0;i<clusters.size();i++){
                System.out.print("[ ");
                for(int j=0;j<clusters.get(i).alphabet.size();j++){
                     if(!labels.isEmpty()){
                         System.out.print(labels.get(clusters.get(i).alphabet.get(j))+" ");
                     } else{
                         System.out.print(clusters.get(i).alphabet.get(j));
                     }
                     if(j!=clusters.get(i).alphabet.size()-1)
                        System.out.print(", ");                                                 
                }
                System.out.println(" ]");
                //System.out.println(" "+clusters.get(i).uncompress_size/clusters.get(i).compress_size+" "+clusters.get(i).uncompress_size);
        }
        
        /*for(int i=0;i<clusters.size();i++){
                for(int j=0;j<clusters.get(i).alphabet.size();j++){
                      System.out.print(clusters.get(i).alphabet.get(j));
                     if(j!=clusters.get(i).alphabet.size()-1)
                        System.out.print(" ");                                                 
                }
                System.out.println();                
        }*/
    }
    
    /**
     * calculate the compression size of the current clusters
     */
    double compress_size(){
        HashMap<Integer,Integer> hm=new HashMap();
        for(int i=0;i<clusters.size();i++){
            for(int j=0;j<clusters.get(i).seq.size();j++){
                if(hm.containsKey(clusters.get(i).seq.get(j).id))
                    hm.put(clusters.get(i).seq.get(j).id, hm.get(clusters.get(i).seq.get(j).id)+1);
                else
                    hm.put(clusters.get(i).seq.get(j).id, 1);             
            }
        }
        double ucompsize=0;
        for(Integer key:hm.keySet()){
            ucompsize+=hm.get(key)*LZ78.log2(Nevents/(hm.get(key)+0.0));
        }
        size=0;
        for(int i=0;i<clusters.size();i++){
            clusters.get(i).compress_size=clusters.get(i).seq.size()*LZ78.log2(Nevents/(clusters.get(i).seq.size()+0.0)); // the cost of encoding the cluster id stream
            size+=clusters.get(i).compress_size; 
            //System.out.println(clusters.get(i).alphabet.toString()+" "+clusters.get(i).compress_size+" "+clusters.get(i).seq.size());
        }
        
        for(int i=0;i<clusters.size();i++){
            if(clusters.get(i).alphabet.size()==1)
                continue;
            LZ78 lz=new LZ78();
            lz.seq=clusters.get(i).getSequence();
            size+=lz.encode();
        }
        System.out.println(ucompsize/size);
        return ucompsize/size;
    }
}
