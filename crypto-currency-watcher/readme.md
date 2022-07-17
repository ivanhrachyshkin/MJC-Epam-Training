##CryptoCurrency Watcher
###Simple REST-service performing currency quotation tracking functionality.
- REST-method for currencies list (/currencies)
- REST-method get one currency by id (/currencies/{id})
####
- REST-method get quotes list (/quotes)
- REST-method get quote by id (/quotes/{id})
- REST-method get the latest quote by currency id with (/quotes/currencies/{id}/price)
####
- checks the scope of currency (catalog) every 60 sec
- checks and saves to db every 60 sec quotes of currency
- checks for user (after registration) difference of currency price between registration
  moment and current, logs it if difference more than 1% every 60 second (/notify)
- stop tracking (/notify/stop)
  Technologies: Java 8, Java Persistence API, Spring Boot (MVC, Data, Web, HATEOAS), 
ORM Hibernate, JUnit, Mockito, Log4j, Apache Commons, Git, PostgreSQL, Gradle