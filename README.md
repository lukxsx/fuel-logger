# Fuel logger

A program to keep a log of car's fuel consumption. It can show interesting charts and data of your car's fuel consumption and cost of ownership. 

[Release (viikko 5)](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/viikko5)

## Documentation
[Requirements](fuel-logger/documentation/requirements.md)

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

