/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

/**
 * A word in a dictionary 
 * @author thoang
 */
public class Word {
    int prefix; // the id of the prefix
    int symbol; // the id of the last symbol
    int next; // the next word in the dictionary that shares the same prefix as the current word
    int first; // the first word in the dictionary that has this word as a prefix
    int length; // word length
    
    Word(int p, int s, int n, int f, int l){
        prefix=p;
        symbol=s;
        next=n;
        first=f;
        length=l;
    }
}
