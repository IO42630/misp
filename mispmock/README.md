#### About
Mockup for easy debugging.
* There are 4 actors which are mocked:
    * `AppMock` : the App hosted on *localhost*.
    * `ClientMock` : the *mispclient* Servlet.
    * `BridgeMock` : the *mispbridge* Servlet.
    * `PubicMock` : the user agent accessing the *mispbridge* from the internet.
* `MockSet` knows all 4 actors, all the 4 actors know `MockSet`.
    * Thus all 4 actors know each other.