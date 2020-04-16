### About
The goal of this project is to bypass the limitations caused by ISPs blocking incoming connections.
It is estimated to require two servlets - one on the webhost (`mispbridge`), and one on the localhost (`mispclient`).

### Overview
![](overview.png)


### How to Run
* Install Tomcat
    * `apt-get install tomcat9`
* Start Tomcat:
    * `/usr/bin/tomcat9-instance-create -p 9090 <foo>/tomcat/`
    * `<foo>/tomcat/bin/startup.sh`
* Link this project to where Tomcat expects files:
    * `ln -s <bar>/mispbridge/mispbridge/ <foo>/tomcat/webapps/`
    * `ln -s <bar>/mispclient/mispclient/ <foo>/tomcat/webapps/`
* Set up your IDE to compile to:
    * `<bar>/mispbridge/mispbridge/WEB-INF/classes/`
    * `<bar>/mispclient/mispclient/WEB-INF/classes/`
* Compile the project & restart Tomcat.
