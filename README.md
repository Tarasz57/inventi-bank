# inventi-bank

Spring Boot 3 project. To run it you need to have Java 17 and Maven. Command is `mvn spring-boot:run`.

Project uses in-memory H2 database to store the data therefore everything is lost after each run.

Export endpoint generates zip file with found statements for each of the accounts inside. Zipped files follow 'accNo_statement.csv' naming pattern.
