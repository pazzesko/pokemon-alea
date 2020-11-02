# pokemon-alea
Spring Boot multi-module project integrated with gradle, docker, docker-compose, h2database, swaggerUI and lombok.

## Requirements
Docker is required if you want to make use of the `buildDockerImage` gradle task and `docker-compose.yml` file. 

## Build
To run the tests:
```bash
./gradlew test
```

To build the entire project:
```bash
./gradlew build 
```

To build the docker image:
```bash
./gradlew buildDockerImage
```

Combined:
```bash
./gradlew build && ./gradlew buildDockerImage
```

Check with `docker images` that the image was created and tagged as `pokemon-alea`:
```bash
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
pokemon-alea      latest              9442eae8fbcd        26 seconds ago      390MB
```

## Run
```bash
docker-compose up
```
It creates the `pokemon-alea` container.
```bash
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                            NAMES
91a222fee55a        pokemon-alea       "java -Xdebug -Xrunj…"   6 minutes ago       Up 6 minutes        0.0.0.0:7080->7080/tcp, 0.0.0.0:8080->8080/tcp   pokemon-alea
```

## Modules
The project is using DDD (Domain Driven Design). 
```bash
pokemon-alea
├── persistence
├── domain
└── application 
```

The persistence layer contains the classes that interact with the database.

The domain layer contains the classes that define the business models and should be implementation agnostic.

The application layer contains the classes that define the services which will interact with domain and persistance layers.


## H2 Database
The h2 console: `http://localhost:8080/pokemon-alea/h2-console`

JDBC URL: `jdbc:h2:mem:pokedexdb`

user: `ash`

password: `ketchum`


## SwaggerUI
Default swaggerUI: `http://localhost:8080/pokemon-alea/swagger-ui.html`

PokemonController example: `http://localhost:8080/pokemon-alea/swagger-ui.html#/pokemon-controller`


## Examples
### getTopBaseExperience
```bash
curl -X GET "http://localhost:8080/pokemon-alea/pokemon/topBaseExperience" -H "accept: */*"
```
### getTopHeight
```bash
curl -X GET "http://localhost:8080/pokemon-alea/pokemon/topHeight" -H "accept: */*"
```
### getTopWeight
```bash
curl -X GET "http://localhost:8080/pokemon-alea/pokemon/topWeight" -H "accept: */*"
```
### random
```bash
curl -X GET "http://localhost:8080/pokemon-alea/pokemon/random" -H "accept: */*"
```
### all
```bash
curl -X GET "http://localhost:8080/pokemon-alea/pokemon/all" -H "accept: */*"
```
### getPokemonById
```bash
curl -X GET "http://localhost:8080/pokemon-alea/pokemon/151" -H "accept: */*"
```
