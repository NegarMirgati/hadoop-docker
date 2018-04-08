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



