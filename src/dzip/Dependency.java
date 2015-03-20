/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dzip;

import java.util.ArrayList;

/**
 * Implementation  of the statistical dependency test of two events
 * The test is proposed in the work Decomposition of Event Sequence into Independent Components
 * in SDM 2002 by Heikki Mannila and Dmitriy Rusakov
 * @author thoang
 */
public class Dependency {
     ArrayList<Event> s1; // the subsequence corresponding to the first event 
     ArrayList<Event> s2; // the subsequence corresponding to the second event
     int T; // the maximum number of slots
     int K; // the maximum lag number
     double alpha; // the significant level 
     
     Dependency(int t, int k, double a){
         T=t;
         K=k;
         alpha=a;
     } 
     
                  
     /**
      * dependency test
      * @return true if S1 and S2 are dependent, otherwise, returns false
      */
     boolean test(){
         int extreme=correlation();
         if(alpha<p_value(extreme))
             return false; 
         else 
             return true;      
     }
     
     /**
      * calculate the p_value, i.e. the probability of the event that if S1 and S2 are independent
      * we will see the extreme value is at least as large as the value returned by the correlation function
      * @param extreme the extreme value observed from the data
      * @return the p_value
      */
     double p_value(int extreme){
         double p,lambda;
         double r;
         double b=0;
         for(int i=1;i<=extreme;i++)
             b+=Math.log(i);
         lambda=s2.size()/(T+0.0)*s1.size();
          //System.out.println(lambda+" "+extreme);
         r=-lambda+extreme*Math.log(lambda)-b;
         p=Math.exp(r);
         for(int i=extreme+1;i<=T;i++){
             r+=Math.log(lambda)-Math.log(i);
             p+=Math.exp(r);            
         }
         //System.out.println(p);
         p=1-Math.pow(1-p, 2*K+1);             
         //System.out.println(p);
         return p;
     }
     
     /**
      * calculate the maximum correlation between the first and the second sequences
      * this will be the extreme value in the dependency test
      * @return the maximum correlation number 
      */
     int correlation(){
        int c=0;
        for(int k=-K;k<=K;k++){
          int cr= intersect(k); 
          if(c<cr)
              c=cr;
        }
        return c;
     }
     
     /**
      * calculate the size of the intersection of the first sequence with the second sequence when lag is k
      * @param k the lag 
      * @return size of the intersection
      */
     int intersect(int k){
         int r=0;
         int i=0,j=0;
         while(i<s1.size()&&j<s2.size()){
             if(s1.get(i).timestamp+k==s2.get(j).timestamp){
                 i++;
                 j++;
                 r++;
             } else if(s1.get(i).timestamp+k<s2.get(j).timestamp){
                 i++;
             } else {
                 j++;
             }
         }
         return r;
     }
     
     /**
      * return the density of Poisson distribution
      * @param k 
      * @param lambda parameter of the Poisson distribution
      * @return Poisson(k,lambda)
      */
     double poisson(int k, double lambda){
         double r=0;
         double b=0;
         for(int i=1;i<=k;i++)
             b+=Math.log(i);
         r+=-lambda+k*Math.log(lambda)-b;
         r=Math.exp(r);
         return r;
     }
}
