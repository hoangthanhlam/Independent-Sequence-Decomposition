/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Implementation  of the sequence independent component decomposition method
 * proposed in the work Decomposition of Event Sequence into Independent Components
 * in SDM 2002 by Heikki Mannila and Dmitriy Rusakov
 * @author thoang
 */
public class Decomposition {
    HashMap<Integer, String> labels; // a map from event ids to event names
    ArrayList<Cluster> data; // the inverted lists of the data
    ArrayList<ArrayList<Integer>> a_matrix; // the adjacency matrix of dependence relations, stored in sparse representation
    int K; // the maximum lag number
    double alpha; // the significant level
    
    /**
     * get the dependence matrix
     */
    void dependence(){
        a_matrix=new ArrayList();
        int T=0;
        for(int i=0;i<data.size();i++){
            T+=data.get(i).seq.size();
            a_matrix.add(new ArrayList());
        }
        for(int i=0;i<data.size();i++){            
            for(int j=i+1;j<data.size();j++){
                 //System.out.println(i+" "+j);
                Dependency d=new Dependency(T,K,alpha);
                d.s1=new ArrayList();
                d.s2=new ArrayList();
                d.s1.addAll(data.get(i).seq);
                d.s2.addAll(data.get(j).seq);
                if(d.test()){ // if two events are dependent, add a connection between them in the adjacency matrix  
                    a_matrix.get(i).add(j);
                    a_matrix.get(j).add(i);
                    System.out.println("Added "+i+" "+j);
                }
            }
        }
    }
    
    /**
     * go through the dependency graph, find connected components
     * each connected component corresponds a cluster, i.e. an independent component
     * @return clusters 
     */
    ArrayList<ArrayList<Integer>> decompose(){
       System.err.println("Building the dependency graph...");
        dependence(); //build the dependency graph
       //System.err.println(a_matrix);
        System.err.println("Decomposing the dependency graph..."); 
        ArrayList<ArrayList<Integer>> clusters=new ArrayList();
        //finding connected components in the dependency graph
        LinkedList<Integer> candidates=new LinkedList();
        for(int i=0;i<data.size();i++)
            candidates.addLast(i);
        while(!candidates.isEmpty()){
            HashMap<Integer,Integer> hm=new HashMap(),nhm=new HashMap();
            int start=candidates.getFirst();
            nhm.put(start, 1);
            while(true){
                HashMap<Integer,Integer> mhm=new HashMap();
                for(Integer key:nhm.keySet()){
                    for(int i=0;i<a_matrix.get(key).size();i++){
                        if(!hm.containsKey(a_matrix.get(key).get(i))&&!nhm.containsKey(a_matrix.get(key).get(i))){
                            mhm.put(a_matrix.get(key).get(i), 1);
                        }
                    }                  
                }
                if(mhm.isEmpty()){
                    hm.putAll(nhm);
                    break;
                }else{
                    nhm.clear();
                    nhm.putAll(mhm);
                    hm.putAll(nhm);
                }
            }
            ArrayList<Integer> cluster=new ArrayList();
            for(Integer key:hm.keySet())
                cluster.addAll(data.get(key).alphabet);
            clusters.add(cluster);
            ListIterator itr = candidates.listIterator();
            while(itr.hasNext())
            {
                Integer a=(Integer)itr.next();
                if(hm.containsKey(a))
                    itr.remove();
            }
 
        }
        return clusters;
    }    
    
}

