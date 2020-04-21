# Fuel logger

A program to keep a log of car's fuel consumption. 

[Release](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/viikko5)

## Documentation
[Requirements specification](fuel-logger/documentation/requirements.md)

[Architecture](fuel-logger/documentation/architecture.md)

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

Package jar
```
mvn package
```

Checkstyle
```
mvn jxr:jxr checkstyle:checkstyle
```

