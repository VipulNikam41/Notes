# Notes
## How to run?
```shell
mvn clean install
java -jar java -jar target/Notes-1.0-SNAPSHOT.jar
```

## Requirements before running
* Postgres should be running on port 5432 and notes db be should be added
* the code uses flyway migration so when you run the above command, tables should also automatically get created
* `mvn integration-test` to run integration tests
