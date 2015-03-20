/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A data reader class
 * @author thoang
 */
public class Data {
   
    /**
     * it looks for and read the label file with name dataname+".lab"
     * @param dataname data name
     * @return a map from event id to event name
     */
     HashMap<Integer, String>  readLabels(String dataname){
        HashMap<Integer, String> labels; // a map from event ids to event names
        labels=new HashMap();
        File file = new File(dataname+".lab");
        if(!file.exists()){ //the label file with such name does not exist
            System.err.println("Warning: the label file "+dataname+".lab"+ " was not found !!!");
            return labels;
        }
        try{
            DataInputStream in;
            FileInputStream fstream = new FileInputStream(dataname+".lab");
                in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                int k=0;
                while((strLine = br.readLine()) != null){
                    labels.put(k, strLine);
                    k++;
                }
                in.close();
        }catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
        return labels;
    }
    
     /**
      * read the sequence data
      * @param dataname data name
      * @return a Clustering object
      */
    Clustering readData(String dataname){
        HashMap<Integer,ArrayList<Event>> hm=new HashMap();
        Clustering c=new Clustering();
        try{
            DataInputStream in;
            FileInputStream fstream = new FileInputStream(dataname+".dat");
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int size=0;
            while((strLine = br.readLine()) != null){
                String[] temp;
                String delimiter = " ";
                temp = strLine.split(delimiter);
                for(int i=0;i<temp.length;i++){
                    Event e=new Event(Integer.parseInt(temp[i]),size);
                    size++;
                    if(!hm.containsKey(e.id)){
                            ArrayList<Event> a=new ArrayList();
                            a.add(e);
                            hm.put(e.id, a);                        
                    }else{
                         hm.get(e.id).add(e);                      
                    }                    
                }
            }
            System.err.println("data size:"+ size);
            c.Nevents=size;
            c.clusters=new ArrayList();
            for(Integer key:hm.keySet()){
                Cluster cc=new Cluster();
                cc.alphabet=new ArrayList();
                cc.alphabet.add(key);
                cc.seq=new ArrayList();
                cc.seq.addAll(hm.get(key));            
                c.clusters.add(cc);
            }
            in.close();
        }catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
        return c;
    }
    
    /**
      * read the sequence data and the clusters
      * @param dataname data name
      * @return a Clustering object
      */
    Clustering readDataClusters(String dataname, int N){
        Clustering c=new Clustering();
        c.clusters=new ArrayList();
        try{
             //read the cluster file
            String strLine;
            DataInputStream in1;
            FileInputStream fstream1 = new FileInputStream(dataname+".cluster");
            in1 = new DataInputStream(fstream1);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
            HashMap<Integer,Integer> chm=new HashMap();
            int cid=0;
            while((strLine = br1.readLine()) != null){
                 Cluster cc=new Cluster();
                 cc.alphabet=new ArrayList();
                 cc.seq=new ArrayList();
                String[] temp;
                String delimiter = " ";
                temp = strLine.split(delimiter);
                for(int i=0;i<temp.length;i++){
                    int id=Integer.parseInt(temp[i]);
                    chm.put(id, cid);
                    cc.alphabet.add(id);
                }
                 cid++;                
                 c.clusters.add(cc);
            }
           
            //read the data
            DataInputStream in;
            FileInputStream fstream = new FileInputStream(dataname+".dat");
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            int size=0;
            while((strLine = br.readLine()) != null&&size<N){
                String[] temp;
                String delimiter = " ";
                temp = strLine.split(delimiter);
                for(int i=0;i<temp.length;i++){
                    Event e=new Event(Integer.parseInt(temp[i]),size);
                    size++;
                    c.clusters.get(chm.get(e.id)).seq.add(e);
                }
            }
            System.err.println("data size:"+ size);
            c.Nevents=size;
            in.close();
            in1.close();
        }catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
        return c;
    }
    
    /**
      * read the sequence data
      * @param dataname data name
      * @return a Clustering object
      */
    Decomposition readDataDecomposition(String dataname){
        HashMap<Integer,ArrayList<Event>> hm=new HashMap();
        Decomposition c=new Decomposition();
        try{
            DataInputStream in;
            FileInputStream fstream = new FileInputStream(dataname+".dat");
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int size=0;
            while((strLine = br.readLine()) != null){
                String[] temp;
                String delimiter = " ";
                temp = strLine.split(delimiter);
                for(int i=0;i<temp.length;i++){
                    Event e=new Event(Integer.parseInt(temp[i]),size);
                    size++;
                    if(!hm.containsKey(e.id)){
                            ArrayList<Event> a=new ArrayList();
                            a.add(e);
                            hm.put(e.id, a);                        
                    }else{
                         hm.get(e.id).add(e);                      
                    }                    
                }
            }
            System.err.println("data size:"+ size);
            c.data=new ArrayList();
            for(Integer key:hm.keySet()){
                Cluster cc=new Cluster();
                cc.alphabet=new ArrayList();
                cc.alphabet.add(key);
                cc.seq=new ArrayList();
                cc.seq.addAll(hm.get(key));
                System.out.println(key+" "+hm.get(key).size());
                c.data.add(cc);
            }
            in.close();
        }catch (IOException e){
                System.err.println("Error: " + e.getMessage());
        }
        return c;
    }
}