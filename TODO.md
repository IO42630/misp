#### TODO

* In `ClientMock` put `GET (Ride)(Request)(Data)` on it's own thread.
    * Currently it is on the `POST (Ride)` thread.
    * It would be better to "decouple" it.
* Clean the `mispmock` code.
* Copy & adapt the `mispmock` code to `mispclient` and `mispbridge`
    * Test with Tomcat.
 
<br>
<br>
    
#### DO MAYBE

* In `ClientMock` put  `GET (Request)` on it's own thread.
    * Currently it is on the `POST (Ride)` thread.
    * This might be tricky since 
        * `GET (Request)` doesn't neccessarily know `(Ride)`
        * and thus need a trick (?) to be syncronized with it