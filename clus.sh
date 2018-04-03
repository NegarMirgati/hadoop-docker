docker run --net hadoop-cluster --ip 172.20.0.11 -it -e HADOOP_HOSTS="172.20.0.10 master,172.20.0.11 slave1" cnp2

docker run --net hadoop-cluster --ip 172.20.0.12 -it -e HADOOP_HOSTS="172.20.0.10 master,172.20.0.12 slave2" cnp2

docker run --net hadoop-cluster --ip 172.20.0.13 -it -e HADOOP_HOSTS="172.20.0.10 master,172.20.0.13 salve3" cnp2

docker run --net hadoop-cluster --ip 172.20.0.10 -it -e HADOOP_HOSTS="172.20.0.10 master,172.20.0.11 slave1,172.20.0.12 slave2,172.20.0.13 slave3" -e MY_ROLE="master" cnp2




