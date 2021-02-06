# Scala JWT using Akka
<hr>

#### Scala Jwt application which generates token when user logs in and checks that token for rest of the API.
### <u>Table of contents:</u>
1) Scala version
2) Sbt version
3) Steps to run
4) Dependencies used
5) Features

1. **Scala version : 2.12.2** <br><br>
2. **Sbt version : 1.3.8**<br><br>

3. `Steps to run project:` <br>
`1) sbt run `

    `Steps to run test files:` <br>
    `1) sbt test `

     `To generate coverage report:` <br>
      `1) sbt coverageReport`
4. #### Dependencies used:
<ul>
<li> Akka Actors 2.6.8 </li>
<li> Akka http 10.2.1 </li>
<li> Akka Streams 2.6.8 </li>
<li> BSON Codec 1.0.1 </li>
<li> Mongo-Scala driver 2.7.0 </li>
<li> Akka-http-spray-json 10.2.1 </li>
<li> Akka-http-xml 10.2.1 </li>
<li> Xstream 1.4.11.1 </li>
<li> Scala-logging 3.9.2 </li>
<li> Classic logging 1.2.3 </li>
<li> nimbus-jose-jwt 9.3 </li>
<li> courier 3.0.0-M2 </li>
<li> scalatest 3.2.2 </li>
</ul>

5. #### Features:
    a) User can register and login <br>
    b) User can get his own token and access any API <br>
    c) User gets verification link on mentioned email id