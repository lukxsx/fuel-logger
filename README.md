# Fuel logger

A program to keep a log of car's fuel consumption. 

## Documentation
[Requirements specification](fuel-logger/documentation/requirements%20specification.md)
[Working hours](fuel-logger/documentation/working%20hours.md)

## Commands

### Tests
Run tests
```
mvn test
```

Test coverage
```
mvn test jacoco:report
```

Running
```
mvn compile exec:java -Dexec.mainClass=fuellogger.Main
```
