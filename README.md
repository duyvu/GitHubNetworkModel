# GitHubNetworkModel

This Java project implements a relational event model for the GitHub collaboration network.

The model and results were presented at XXI Organization Science Winter Conference, February 5 - 8, 2015. The presentation slides are stored in the directory **slides**.

The original SQL data dump was downloaded from [the MSR 2014 Mining Challenge Dataset](http://ghtorrent.org/msr14.html). It was then queried and stored in the directory **data/events** under CSV format.

Other implementation notes:

+ Gradle is used to run test suite (TestNG) and build jar files.

+ Java code is responsible to process event streams and sample nested case control data, 

+ SAS scripts with PHREG commands are generated to obtain coefficient estimates. Therefore, this implementation requires SAS to be installed and the shell command **sas** is available. Another option is to use **clogit** in R by modifying the class **java.engines.NetworkEventInferenceEngine.java**.




