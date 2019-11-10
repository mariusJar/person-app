# person-app
Simple CRUD app to retrieve Person data from DB

<h1>Start</h1>
Step one: start postges docker container, docker-compose file is added in /postgres-docker directory. 
<br/>
Step Two: To start an app simply execute ./gradlew bootRun for a terminal.

Note: Swagger documentation is available at endpoint: "/swagger-ui.html"

<h1>Deployment</h1>
To build deployable Jar simple execute "./gradlew fatJar" from a terminal.