### About
The goal of this project is to bypass the limitations caused by ISPs blocking incoming connections.
It is estimated to require two servlets - one on the webhost (`mispbridge`), and one on the localhost (`mispclient`).

<br>

### Overview
![](overview.png)

<br>

### Run / Deploy

<br>

#### How to Run / Debug
* `com.olexyn.misp.embedded.RunAll.main()`

<br>

#### How to Deploy
* Set the URIs in code.
* Build (e.g. with `build-install-all.sh`)
* Put the generated `forward-0.1.war` in a servlet container (e.g. Jetty).
* Launch the `reverse-0.1.jar` on your host. 

<br>

### TODO
* See [TODO.md](TODO.md).