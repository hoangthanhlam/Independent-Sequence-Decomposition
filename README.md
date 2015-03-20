# Decompose a sequence of events into indepedent subsequences

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

There are two algorithms implemented in this project. The first one called dzip which solves the problem as a data compression problem, i.e. grouping events to a cluster if compressing them together gives additional compression benefits compared to compress them independently. This algorithm was published in the following paper:

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

# Output:
	The output will be in the following form:
		
		Usage: java -jar Dzip.jar dzip data_name

		Reading the data ...
	
		data size:60

		Initialization... 

		Initialized with 4 clusters

		Current size... 115.09775004326937 bits

		Current size... 115.09775004326936 bits

		Compression ratio: 1.0000000000000002
		
		[ b , a  ]

		[ c  ]
	
		[ d  ]

		Running time: 0 seconds

	The mentioned output contains three clusters [b, a], [c], [d] which means [b, a] has strong dependent and independent from events in the other clusters. It is important to notice our implementation does not cover overlapped clusters. Clusters are disjoint partition of the alphabet.

# License: FreeBSD

Copyright (c) <2013>, <Hoang Thanh Lam>
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by the <organization>.
4. Neither the name of the <organization> nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
