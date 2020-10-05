# pokemon-alea
Spring Boot multi-module project integrated with gradle, docker, docker-compose, h2database and swaggerUI.

## Requirements
Docker is required if you want to make use of the `buildDockerImage` gradle task and `docker-compose.yml` file. 

## Build
To build the jars for each module:
```bash
./gradlew build 
```

To build the docker image:
```bash
./gradlew buildDockerImage
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
The project is composed by 2 modules. The persistence module contains the model and persistence classes and the service module contains the controller, application and swagger configuration classes.
```bash
pokemon-alea
├── persistence
│   └── src
│       └── main
│           └── java
│               └── com.example.pokemonalea.persistence
│                   ├── PokemonDTO.java
│                   ├── PokemonModel.java
│                   └── PokemonRepository.java
└── service
    └── src
        └── main
            └── java
                └── com.example.pokemonalea.service
                    ├── Application.java
                    ├── PokeApiKeysResponse.java
                    ├── PokemonCache.java
                    ├── PokemonController.java
                    ├── PokemonSpecies.java
                    └── SwaggerConfig.java

```

## SwaggerUI
Default swaggerUI: http://localhost:8080/pokemon-alea/swagger-ui.html

PokemonController example: http://localhost:8080/pokemon-alea/swagger-ui.html#/pokemon-controller
