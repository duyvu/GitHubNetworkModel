# GitHubNetworkModel

This Java project implements a relational event model for the GitHub collaboration network.

The model and results were presented at XXI Organization Science Winter Conference, February 5 - 8, 2015. The presentation slides are stored in the directory **slides**.

The original SQL data dump was downloaded from [the MSR 2014 Mining Challenge Dataset](http://ghtorrent.org/msr14.html). It was then queried and stored in the directory **data/events** under CSV format.

The code requires SAS to be installed and the shell command **sas** is available:

+ Java code is responsible to process event streams and sample nested case control data. 

+ SAS PHREG command is used to obtain coefficient estimates.    



