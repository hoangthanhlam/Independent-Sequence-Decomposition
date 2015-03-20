/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author thoang
 */
public class LZW {
    int D_MAX; //2^D_MAX_BITS
    int D_MAX_BITS;// the maximum number of bits to store a dictionary word, must be greater than  log2(alphabet.size()) 
    ArrayList<Event> seq; // the sequence
    ArrayList<Word> dictionary; // the dictionary in a prefix-tree form 
    HashMap<Integer,Integer> alphabet; // the alphabet, map from id to its index in the dictionary

    /**
     * create the alphabet and initialize the dictionary with characters in the alphabet
     * @return the number of distinct events in the sequence, i.e. the alphabet size 
     */
    int alphabet(){
        alphabet=new HashMap();
        dictionary = new ArrayList();
        for(int i=0;i<seq.size();i++){
            if(!alphabet.containsKey(seq.get(i).id)){
                dictionary.add(new Word(-1,seq.get(i).id,-1,-1,1));  
                alphabet.put(seq.get(i).id, dictionary.size()-1);                 
            }
        }
        if(upround(log2(alphabet.size()))>16)
            D_MAX_BITS=upround(log2(alphabet.size()));  
        else
            D_MAX_BITS=16;
        D_MAX=upround(Math.pow(2, D_MAX_BITS));
        //System.out.println(D_MAX_BITS+" aaa "+D_MAX+" "+alphabet.size());
        return alphabet.size();
    }
    
    /**
     * when the dictionary is filled up, re-initialize the dictionary with singleton
     */
    void reinit(){
        dictionary.clear();
        for(Integer key : alphabet.keySet()){
             dictionary.add(new Word(-1,key,-1,-1,1)); 
             alphabet.put(key, dictionary.size()-1);
        }
    }
    
    /**
     * encode the sequence with the LZW algorithm 
     * @return the compressed size (# bits)
     */
    double encode(){
        double sz=0;
        int from=0;
        alphabet();
        int Nw=dictionary.size()-1;
        while (true){
            int m=longestMatch(from);
            from+=dictionary.get(m).length;
            if(from>=seq.size()){
                Nw++;
                //sz+=bits(m); //cost of encoding the pointer with Elias Delta code
                sz+=upround(log2(dictionary.size()));
                break;
            }
            dictionary.add(new Word(m,seq.get(from).id,-1,-1,dictionary.get(m).length+1));
            if(dictionary.get(m).first==-1)
                dictionary.get(m).first=dictionary.size()-1;
            else{
                int index=m,next=dictionary.get(m).first;
                while(next!=-1){
                    index=next;
                    next=dictionary.get(next).next;
                }
                dictionary.get(index).next=dictionary.size()-1;
            }
            Nw++;
            //sz+=bits(m); //cost of encoding the pointer with Elias Delta code
            sz+=upround(log2(dictionary.size()));
            if(from>=seq.size())
                break;
            if(dictionary.size()>D_MAX){
                reinit();
            }
        }
        //System.out.println(gaps);
        //for(Integer key:gaps.keySet())
            //sz+=gaps.get(key)*log2((Nw-alphabet.size())/(gaps.get(key)+0.0));
        return sz;
    }
    
    /**
     * find the longest dictionary word matching the subsequence starting at from
     * @param from the position starting the match
     * @return the index of the longest word in the dictionary matching the subsequence starting at from 
     */
    int longestMatch(int from){
        int nf=from;
        int index=alphabet.get(seq.get(nf).id);
        nf++;
        while(true){
            if(nf>=seq.size())
                return index;
            int next=dictionary.get(index).first;
            while(true){
                if(next==-1)
                    return index;
                if(dictionary.get(next).symbol==seq.get(nf).id){
                  nf++;
                  index=next;
                  break;
                }                
                next=dictionary.get(next).next;                  
            }
        }       
    }
    
    /**
     * logarithm with base 2
     * @param x the input
     * @return logarithm with base 2
     */
    public static double log2(double x){
        return Math.log(x)/Math.log(2);
    }
    
    /**
     * calculate the number of bits to represent a word in the dictionary
     * @param m
     * @return 
     */
    int bits(int m){
        if(m<alphabet.size())
            return upround(log2(alphabet.size()));
        else
            return upround(log2(m+1));
    }   
    
    /**
    * 
    * @param x
    * @return the lower round value of x 
    */
    int upround(double x){
       int y=(int) Math.round(x);
       if(x-y>0.0)
           y=y+1;
       return y;
    }

    /**
     * merging the sequence with seq, the order of the events are preserved
     * @param sequence the sequence to be merged
     */
    void merge(ArrayList<Event> sequence){
        ArrayList<Event> m=new ArrayList();
        int i=0,j=0;
        while(true){
            if(i>=seq.size()&&j>=sequence.size())
                break;
            else if(i>=seq.size()){
                Event e=new Event(sequence.get(j).id,sequence.get(j).timestamp);
                m.add(e);
                j++;
            } else if (j>=sequence.size()){
                Event e=new Event(seq.get(i).id,seq.get(i).timestamp);
                m.add(e);
                i++;
            } else{
                if(seq.get(i).timestamp<sequence.get(j).timestamp){
                    Event e=new Event(seq.get(i).id,seq.get(i).timestamp);
                    m.add(e);
                    i++;
                } else if (seq.get(i).timestamp>sequence.get(j).timestamp){
                    Event e=new Event(sequence.get(j).id,sequence.get(j).timestamp);
                    m.add(e);
                    j++;
                }
            }                
        }
        seq.clear();
        seq=m;
    }
    
    /**
     * get the word content 
     * @param index the id of the word
     * @return the word content
     */
    ArrayList<Integer> getWord(int index){
        ArrayList<Integer> w=new ArrayList();
        int next=index;
        while(true){
            if(next>dictionary.size()||next==-1)
                return w;
            w.add(0, dictionary.get(next).symbol);
            next=dictionary.get(next).prefix;
        }        
    }
    
    /**
     * print the dictionary content
     */
    void printDictionary(){
        for(int i=0;i<dictionary.size();i++){
            System.out.print(getWord(i));
            System.out.println(" "+dictionary.get(i).prefix+" "+dictionary.get(i).symbol+" "+dictionary.get(i).first+" "+dictionary.get(i).next);
        }
        System.out.println("-----------------");
    }
}
