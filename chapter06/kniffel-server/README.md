# Spring REST API with Postgres backend

Before you can start the Spring application you need to start a Postgres server.

You can do this with docker:

```bash
docker run -d --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres postgres
```

Now you can start the Spring application with the ususal maven command:

```bash
./mvnd spring-boot:run
```

and access the OpenAPI / swagger doc at : http://localhost:8080/swagger-ui/index.html
