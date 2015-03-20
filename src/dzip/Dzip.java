/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;

/**
 * implementation of the sequence independent component decomposition problem
 * @author thoang
 */
public class Dzip {

    /**
     * decompose a sequence into independent components using the dzip algorithm 
     * @param dataname  input file name
     */
    static void dzip(String dataname){
        Data d=new Data();
        System.err.println("Reading the data ...");
        Clustering c=d.readData(dataname);
        c.hierarchical_clustering();
        c.labels=d.readLabels(dataname);
        c.print();        
    }
    
    /**
     * decompose a sequence into independent components using the statistical dependency test, method by Heikki et al. 
     * @param dataname the input file
     * @param K should set to 300
     * @param alpha the significant level used for rejecting the null hypothesis, should set to 0.01
     */
    static void dcomp(String dataname,int K, double alpha){
        Data d=new Data();
        System.err.println("Reading the data ...");
        Decomposition c=d.readDataDecomposition(dataname);
        c.K=K;
        c.alpha=alpha;
        c.labels=d.readLabels(dataname);
        ArrayList<ArrayList<Integer>> r=c.decompose();
        for(int i=0;i<r.size();i++){
                System.out.print("[ ");
                for(int j=0;j<r.get(i).size();j++){
                     if(!c.labels.isEmpty()){
                         System.out.print(c.labels.get(r.get(i).get(j)));
                     } else{
                         System.out.print(r.get(i).get(j));
                     }
                     if(j!=r.get(i).size()-1)
                        System.out.print(" ");                                                 
                }
                System.out.println(" ]");                
        }
        
        for(int i=0;i<r.size();i++){
                for(int j=0;j<r.get(i).size();j++){
                      System.out.print(r.get(i).get(j));
                     if(j!=r.get(i).size()-1)
                        System.out.print(" ");                                                 
                }
                System.out.println();                
        }
    }
    
    
    static void compsize(String dataname, int N){
        Data d=new Data();
        System.err.println("Reading the data ...");
        Clustering c=d.readDataClusters(dataname,N);
        c.compress_size();
        System.out.println(c.size);        
    }
   
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        switch (args[0]) {
            case "dzip":
                System.err.println("Usage: java -jar Dzip.jar dzip data_name");
                dzip(args[1]);
                break;
            case "heikki":
                System.err.println("Usage: java -jar Dzip.jar heikki data_file K alpha ");
                System.err.println("Heikki et al. recommends to set K as 300,  alpha as 0.01");
                dcomp(args[1],Integer.parseInt(args[2]),Double.parseDouble(args[3]));
                break;
            default:
                System.err.println("The first argument should be either dzip or heikki");
                break;
        }
       long endTime   = System.currentTimeMillis();
       long totalTime = endTime - startTime;
       System.out.println("Running time: "+totalTime/1000+" seconds");       
    }
}
