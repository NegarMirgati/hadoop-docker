# Hadoop Docker 

# Build the image :

If you'd like to pull this image directly from the Docker hub you can build the image as:

1. Using caches :

```
docker build  -t cnp2 .
```

2. Don't use caches :
If you want to refresh config files in your image use below command to build your image. This ignores any cache for line
 after `ARG RECONFIG=1` and execute  every line. It is useful if you want to change config files.

```
docker build -t cnp2 --build-arg RECONFIG=$(date +%s) . 
```

# Run the image :
There are two ways to run hadoop single-node and multi-node (default config of image is single-node). You can change image 
 behavior with some environment variable and change it to multi-node.
 
# single-node
just run this: 
```
docker run -it cnp2 
```

# python cluster
If error "RTNETLINK answers: File exists" occured run :
```
sudo mn -c 
```

To see results on each host run :
```
xterm host_name
$HADOOP_HOME/etc/hadoop/start.sh > result
```

# multi-node
```
docker run --net hadoop-cluster --ip 172.20.0.11 -it -e HADOOP_HOSTS="172.20.0.10 master, 172.20.0.11 slave1, 172.20.0.12 slave2, 172.20.0.13 slave3" cnp2

docker run --net hadoop-cluster --ip 172.20.0.12 -it -e HADOOP_HOSTS="172.20.0.10 master, 172.20.0.11 slave1, 172.20.0.12 slave2, 172.20.0.13 slave3" cnp2

docker run --net hadoop-cluster --ip 172.20.0.13 -it -e HADOOP_HOSTS="172.20.0.10 master, 172.20.0.11 slave1, 172.20.0.12 slave2, 172.20.0.13 slave3" cnp2

docker run --net hadoop-cluster --ip 172.20.0.10 -it -e HADOOP_HOSTS="172.20.0.10 master, 172.20.0.11 slave1, 172.20.0.12 slave2, 172.20.0.13 slave3" -e MY_ROLE="master" cnp2
```


# run map-red

#To see output run this command :
```
hdfs dfs -ls /user/sina/output
```

#To see the contents of output of WordCount.java run this command :
```
hdfs dfs -cat /user/sina/output/part-r-000*
```


# run BigData.java 

```
hadoop com.sun.tools.javac.Main BigData.java
jar cf bd.jar BigData*.class
hadoop jar bd.jar BigData
```

# copy raw.data to hadoop 

```
Compile: hadoop com.sun.tools.javac.Main HdfsWriter.java
Create Jar file: jar cf HdfsWriter.jar HdfsWriter*.class
Run: hadoop jar HdfsWriter.jar HdfsWriter raw.data /user/training/raw_copyyy.data
```



# Log Files
It contains several different types of lines:

    Lines starting with "Job", that indicate that refer to the job, listing information about the job (priority, submit time, configuration, number of map tasks, number of reduce tasks, etc...


    Lines starting with "Task" referring to the creation or completion of Map or Reduce tasks, indicating which host they start on, and which split they work on. On completion, all the counters associated with the task are listed.



    Lines starting with "MapAttempt", reporting mostly status update, except if they contain the keywords SUCCESS and/or FINISH_TIME, indicating that the task has completed. The final time when the task finished is included in this line.

    Lines starting with "ReduceAttempt", similar to the MapAttempt tasks, report on the intermediary status of the tasks, and when the keyword SUCCESS is included, the finish time of the sort and shuffle phases will also be included.


# Report

NameNode:

It maintains and manages the slave nodes and assign the task to them.
It only stores the metadata of HDFS. Namenode executes file system namespace operations like opening, closing and renaming files and directions.
All replication factor details should maintain in Name node.This metadata is available in memory in the master for faster retrieval data.

DataNode:

This is the daemon that runs on the slave, these are actual workers nodes that store the data. Data nodes are the slaves which are deployed on each machine and provide actual storage. Data nodes are responsible for serving read and write requests from the file system clients, also perform block creation, deletion, and replication upon instruction from the name node.



# Understanding Mapreduce cycle Job

This section briefly sketches the life cycle of a MapReduce job and the roles of the primary actors in the life cycle. The full life cycle is much more complex. For details, refer to the documentation for your Hadoop distribution or the Apache Hadoop MapReduce documentation.

Though other configurations are possible, a common Hadoop cluster configuration is a single master node where the Job Tracker runs, and multiple worker nodes, each running a Task Tracker. The Job Tracker node can also be a worker node.

When the user submits a MapReduce job to Hadoop:

The local Job Client prepares the job for submission and hands it off to the Job Tracker.
The Job Tracker schedules the job and distributes the map work among the Task Trackers for parallel processing.
Each Task Tracker spawns a Map Task. The Job Tracker receives progress information from the Task Trackers.
As map results become available, the Job Tracker distributes the reduce work among the Task Trackers for parallel processing.
Each Task Tracker spawns a Reduce Task to perform the work. The Job Tracker receives progress information from the Task Trackers.
All map tasks do not have to complete before reduce tasks begin running. Reduce tasks can begin as soon as map tasks begin completing. Thus, the map and reduce steps often overlap.

Job Client
The Job Client prepares a job for execution.When you submit a MapReduce job to Hadoop, the local JobClient:

5-Validates the job configuration.
Generates the input splits. See How Hadoop Partitions Map Input Data.
Copies the job resources (configuration, job JAR file, input splits) to a shared location, such as an HDFS directory, where it is accessible to the Job Tracker and Task Trackers.
Submits the job to the Job Tracker.
Job Tracker
The Job Tracker is responsible for scheduling jobs, dividing a job into map and reduce tasks, distributing map and reduce tasks among worker nodes, task failure recovery, and tracking the job status. Job scheduling and failure recovery are not discussed here; see the documentation for your Hadoop distribution or the Apache Hadoop MapReduce documentation.

When preparing to run a job, the Job Tracker:

Fetches input splits from the shared location where the Job Client placed the information.
Creates a map task for each split.
Assigns each map task to a Task Tracker (worker node).
The Job Tracker monitors the health of the Task Trackers and the progress of the job. As map tasks complete and results become available, the Job Tracker:

Creates reduce tasks up to the maximum enableed by the job configuration.
Assigns each map result partition to a reduce task.
Assigns each reduce task to a Task Tracker.
A job is complete when all map and reduce tasks successfully complete, or, if there is no reduce step, when all map tasks successfully complete.

Task Tracker
A Task Tracker manages the tasks of one worker node and reports status to the Job Tracker. Often, the Task Tracker runs on the associated worker node, but it is not required to be on the same host.

When the Job Tracker assigns a map or reduce task to a Task Tracker, the Task Tracker:

Fetches job resources locally.
Spawns a child JVM on the worker node to execute the map or reduce task.
Reports status to the Job Tracker.
The task spawned by the Task Tracker runs the job's map or reduce functions.

Map Task
The Hadoop MapReduce framework creates a map task to process each input split. The map task:

Uses the InputFormat to fetch the input data locally and create input key-value pairs.
Applies the job-supplied map function to each key-value pair.
Performs local sorting and aggregation of the results.
If the job includes a Combiner, runs the Combiner for further aggregation.
Stores the results locally, in memory and on the local file system.
Communicates progress and status to the Task Tracker.
Map task results undergo a local sort by key to prepare the data for consumption by reduce tasks. If a Combiner is configured for the job, it also runs in the map task. A Combiner consolidates the data in an application-specific way, reducing the amount of data that must be transferred to reduce tasks. For example, a Combiner might compute a local maximum value for a key and discard the rest of the values. The details of how map tasks manage, sort, and shuffle results are not covered here. See the documentation for your Hadoop distribution or the Apache Hadoop MapReduce documentation.

When a map task notifies the Task Tracker of completion, the Task Tracker notifies the Job Tracker. The Job Tracker then makes the results available to reduce tasks.

Reduce Task
The reduce phase aggregates the results from the map phase into final results. Usually, the final result set is smaller than the input set, but this is application dependent. The reduction is carried out by parallel reduce tasks. The reduce input keys and values need not have the same type as the output keys and values.

The reduce phase is optional. You may configure a job to stop after the map phase completes. For details, see Configuring a Map-Only Job.

Reduce is carried out in three phases, copy, sort, and merge. A reduce task:

Fetches job resources locally.
Enters the copy phase to fetch local copies of all the assigned map results from the map worker nodes.
When the copy phase completes, executes the sort phase to merge the copied results into a single sorted set of (key, value-list) pairs.
When the sort phase completes, executes the reduce phase, invoking the job-supplied reduce function on each (key, value-list) pair.
Saves the final results to the output destination, such as HDFS.
The input to a reduce function is key-value pairs where the value is a list of values sharing the same key. For example, if one map task produces a key-value pair ('eat', 2) and another map task produces the pair ('eat', 1), then these pairs are consolidated into ('eat', (2, 1)) for input to the reduce function. If the purpose of the reduce phase is to compute a sum of all the values for each key, then the final output key-value pair for this input is ('eat', 3). For a more complete example, see Example: Calculating Word Occurrences.

Output from the reduce phase is saved to the destination configured for the job, such as HDFS or MarkLogic Server. Reduce tasks use an OutputFormat subclass to record results. The Hadoop API provides OutputFormat subclasses for using HDFS as the output destination. The MarkLogic Connector for Hadoop provides OutputFormat subclasses for using a MarkLogic Server database as the destination. For a list of available subclasses, see OutputFormat Subclasses. The connector also provides classes for defining key and value types; see MarkLogic-Specific Key and Value Types.

How Hadoop Partitions Map Input Data
When you submit a job, the MapReduce framework divides the input data set into chunks called splits using the org.apache.hadoop.mapreduce.InputFormat subclass supplied in the job configuration. Splits are created by the local Job Client and included in the job information made available to the Job Tracker.

The JobTracker creates a map task for each split. Each map task uses a RecordReader provided by the InputFormat subclass to transform the split into input key-value pairs. The diagram below shows how the input data is broken down for analysis during the map phase:


![alt text](https://docs.marklogic.com/media/apidoc/9.0/guide/mapreduce/hadoop/hadoop-5.gif)
