Decompose a sequence of events into indepedent subsequences

Given a long sequences or a set of short sequences, the  algorithms  decompose  the given sequences into  independent  components.  For example if the input data is as follows:

0 1 0 1 0 1 0 1

2 3 2 3

0 1 0 1 0 1 0 1 0 1

2 3 2 3

0 1 0 1 0 1 0 1 0 1

0 1 0 1 0 1 0 1 0 1 0 1

2 3 2 3

2 3 2 3

2 3 2 3

where 0-3 are ids of events a-d respectively. 

Since the events 0 and 1 has strong depdendency , 2 and  3 also have strong dependency and (0,1) is indepedent from (2,3) the algorithms will output the independent components:

0 1 0 1 0 1 0 1

0 1 0 1 0 1 0 1 0 1

0 1 0 1 0 1 0 1 0 1

0 1 0 1 0 1 0 1 0 1 0 1

and the 

2 3 2 3

2 3 2 3

2 3 2 3

2 3 2 3

2 3 2 3

# Algorithms

There are two algorithms implemented in this project. The first one called dzip which solves the problem as a data compression problem, i.e. group events to a cluster if compressing them together give compression benefits compared to compressing them independently. This algorithm was published in the following paper:

Decomposing a Sequence into Independent Subsequences Using Compression Algorithms. Hoang Thanh Lam, Julia Kiseleva, Mykola Pechenizhky and Toon Calders, Idea workshop at SIGKDD 2014. poloclub.gatech.edu/idea2014/papers/p67-lam.pdf  

The second implementation called heikki that used statistical test to test dependency between two events and create a dependency graph. The output clusters of events are connected components in the constructed dependency graph. The heikki algorithm requires two parameters to set (K and alpha the significance level used to test for dependency hypothesis). The paper related to the heikki algorithm is:

Heikki Mannila, Dmitry Rusakov: Decomposition of event sequences into independent components. SDM 2001

# Usages:
1. Download the dzip.jar file
2. Copy your data to the same folder
3. The input data should be a long sequence of event id (id start from 0) or multiple lines each with one short sequence of event ids
4. The input file name must be in the form <data_name>.dat, e.g. test.dat
5. Optional label file with the name  <data_name>.lab can be provided in the same directory, e.g. test.lab. The label file is optional which has the same number of lines as
the number if distinct event ids. For example, if my test data contains events with ids from 0-3, and the label of the first event is a, the second event is b, the third event is c and the last event is d then the label file should have 4 lines as follows:

a

b

c

d
 
5. To run the dzip algorithm uses the following command : java -jar dzip data_name  . It is important to notice that data_name is provided without the .dat extension. Forexample, if your data is test.dat and label file is test.lab, you should run with the command java -jar dzip test

6. To run the heikki algorithm uses the following command : java -jar heikki data_name K alpha . If your data is long enough it is recommended to set K as large as 300 and alpha as small as 0.01
