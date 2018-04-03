#!/usr/bin/python
"""
This is the from simple example to showcase Containernet.
Run it bt sudo python /path/to/file/cluster.py
"""
from mininet.net import Containernet

from mininet.node import Controller
from mininet.cli import CLI
from mininet.link import TCLink
from mininet.log import info, setLogLevel
setLogLevel('info')

net = Containernet(controller=Controller)
info('*** Adding controller\n')
net.addController('c0')
info('*** Adding docker containers\n')

# TODO
d1 = net.addDocker('d1', ip='10.0.0.1', dimage="cnp2")
d2 = net.addDocker('d2', ip='10.0.0.2', dimage="cnp2")
d3 = net.addDocker('d3', ip='10.0.0.3', dimage="cnp2")
d4 = net.addDocker('d4', ip='10.0.0.4', dimage="cnp2")
s1 = net.addSwitch('s1')
s2 = net.addSwitch('s2')
s3 = net.addSwitch('s3') 
# create topology here.
# Hint :
# s1 = net.addSwitch('s1')
# d1 = net.addDocker('d1', ip='10.0.0.1', dimage="cnp2")




info('*** Creating links\n')

# TODO
# create other links
# Hint :
net.addLink(d1, s1, cls=TCLink, delay='10ms', bw=5)
net.addLink(d2, s1, cls=TCLink, delay='10ms', bw=5)
net.addLink(d3, s2, cls=TCLink, delay='10ms', bw=5)
net.addLink(d4, s2, cls=TCLink, delay='10ms', bw=5)
net.addLink(s1, s3, cls=TCLink, delay='5ms', bw=1)
net.addLink(s2, s3, cls=TCLink, delay='5ms', bw=1)

info('*** Starting network\n')
net.start()
info('*** Testing connectivity\n')
# you can test connectivity between nodes with:
net.ping([d1, d2])
net.ping([d1,d3])
net.ping([d1,d4])
net.ping([d2,d3])
net.ping([d2,d4])
net.ping([d3,d4])

info('*** Setup nodes\n')

# start ssh on hosts
for host in net.hosts:
    host.cmd('/usr/sbin/sshd -D &')

# TODO
# By using d1.cmd('command') you can run `command` on container d1
# Run 'export HADOOP_HOSTS="list of comma separated host for example : `10.0.0.1
# master, 10.0.0.2 slave1`"' on each host
# to declare nodes of cluster for it and on master node you also need to run '
#export MY_ROLE="master"' to define it's role.
# run '/$HADOOP_HOME/etc/hadoop/start.sh > result' on each host to start hadoop
# on it.


info('*** Running CLI\n')
CLI(net)
info('*** Stopping network')
net.stop()

