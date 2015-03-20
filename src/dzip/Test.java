/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;

/**
 * Test class used for testing the implementation
 * @author thoang
 */
public class Test {
    
    /**
     * test Dzip
     */
    static void Dzip(){
        Data d=new Data();
        System.err.println("Reading the data ...");
        Clustering c=d.readData("test");
        c.labels=d.readLabels("test");
        c.hierarchical_clustering();
        c.print();    
    }
    
    /**
     * test the LZ78 encode function and the merge function
     */
    static void LZ78(){
        LZ78 lz=new LZ78();
        lz.seq=new ArrayList();
        lz.seq.add(new Event(1,1));
        lz.seq.add(new Event(2,3));
        lz.seq.add(new Event(1,5));
        lz.seq.add(new Event(2,7));
        lz.seq.add(new Event(1,9));
        lz.seq.add(new Event(2,10));
        lz.seq.add(new Event(1,15));
        lz.seq.add(new Event(2,17));
        lz.seq.add(new Event(1,18));
        lz.seq.add(new Event(2,20));
        lz.seq.add(new Event(1,22));
        lz.seq.add(new Event(2,29));       
        double sz=lz.encode();
        lz.printDictionary();
        System.out.println(sz);
        
        LZ78 lzz=new LZ78();
        lzz.seq=new ArrayList();
        lzz.seq.add(new Event(2,2));
        lzz.seq.add(new Event(1,8));
        lzz.seq.add(new Event(2,11));
        lzz.seq.add(new Event(1,12));
        lzz.seq.add(new Event(2,19));
        lzz.seq.add(new Event(2,23));
        lzz.seq.add(new Event(1,24));
        lzz.seq.add(new Event(2,119));
        lzz.seq.add(new Event(1,123));
        lzz.seq.add(new Event(2,124));
        lzz.seq.add(new Event(1,1119));
        lzz.seq.add(new Event(2,1123));
        lzz.seq.add(new Event(1,1124));
        sz=lzz.encode();
        lzz.printDictionary();
        System.out.println(sz);
        
        LZ78 lzzz=new LZ78();
        lzzz.seq=new ArrayList();
        lzzz.seq.addAll(lz.seq);
        lzzz.merge(lzz.seq);
        sz=lzzz.encode();
        lzzz.printDictionary();
        System.out.println(sz);       
    }
    
    /**
     * test the Dependency class
     */
    static void Dependency(){
        // test the intersect and the correlation function
        Dependency d=new Dependency(10,5,0.1);
        d.s1=new ArrayList();
        d.s2=new ArrayList();
        d.s1.add(new Event(1,1));
        d.s1.add(new Event(1,3));
        d.s1.add(new Event(1,5));
        d.s1.add(new Event(1,7));
        d.s1.add(new Event(1,9));
        d.s2.add(new Event(1,2));
        d.s2.add(new Event(1,4));
        d.s2.add(new Event(1,6));
        d.s2.add(new Event(1,8));
        d.s2.add(new Event(1,10));
        System.out.println("-5 "+d.intersect(-5));
        System.out.println("-4 "+d.intersect(-4));
        System.out.println("-3 "+d.intersect(-3));
        System.out.println("-2 "+d.intersect(-2));
        System.out.println("-1 "+d.intersect(-1));
        System.out.println("0 "+d.intersect(0));
        System.out.println("1 "+d.intersect(1));
        System.out.println("2 "+d.intersect(2));
        System.out.println("3 "+d.intersect(3));
        System.out.println("4 "+d.intersect(4));
        System.out.println("5 "+d.intersect(5));
        System.out.println("max "+d.correlation());
        
        //test p_value function
        System.out.println(d.p_value(d.correlation()));
    }
   
    static void Decomposition(){
        //test decompose function
        Decomposition d=new Decomposition();
        d.data=new ArrayList();
        d.data.add(new Cluster());
        d.data.get(0).alphabet=new ArrayList();
        d.data.add(new Cluster());
        d.data.get(1).alphabet=new ArrayList();
        d.data.add(new Cluster());
        d.data.get(2).alphabet=new ArrayList();
        d.data.add(new Cluster());
        d.data.get(3).alphabet=new ArrayList();
        d.data.add(new Cluster());
        d.data.get(4).alphabet=new ArrayList();
        
        d.data.get(0).alphabet.add(1);
        d.data.get(1).alphabet.add(2);
        d.data.get(2).alphabet.add(3);
        d.data.get(3).alphabet.add(4);
        d.data.get(4).alphabet.add(5);
        d.a_matrix=new ArrayList();
        d.a_matrix.add(new ArrayList());
        d.a_matrix.get(0).add(2);d.a_matrix.get(0).add(4);
        d.a_matrix.add(new ArrayList());
        d.a_matrix.get(1).add(3);
        d.a_matrix.add(new ArrayList());
        d.a_matrix.get(2).add(0);d.a_matrix.get(2).add(4);
        d.a_matrix.add(new ArrayList());
        d.a_matrix.get(3).add(1);
        d.a_matrix.add(new ArrayList());
        d.a_matrix.get(4).add(0);d.a_matrix.get(4).add(2);
        System.out.println(d.decompose());
        System.out.println(d.a_matrix);
    }  
}

