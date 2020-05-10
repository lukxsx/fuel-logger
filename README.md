# Fuel logger

A program to keep track of the fuel consumption of a car. It can show interesting charts and data of your car's fuel consumption and cost of ownership. 

## Releases
* [Final release](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/final)
* [Viikko 6](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/viikko6)
* [Viikko 5](https://github.com/Lukxsx/ot-harjoitustyo/releases/tag/viikko5)

## Documentation
* [Requirements](fuel-logger/documentation/requirements.md)
* [User manual](fuel-logger/documentation/user-manual.md)
* [Architecture](fuel-logger/documentation/architecture.md)
* [Working hours](fuel-logger/documentation/working%20hours.md)
* [Testing report](fuel-logger/documentation/testing.md)

## Commands
### Running from Maven
```
mvn compile exec:java -Dexec.mainClass=fuellogger.Main
```

### Package jar
```
mvn package
```
Jar file will be generated in _target/fuel-logger-1.0-SNAPSHOT.jar_

### Running tests
```
mvn test
```

### Test coverage
```
mvn test jacoco:report
```

### Checkstyle
```
mvn jxr:jxr checkstyle:checkstyle
```
Checkstyle is generated in _target/site/checkstyle.html_

### Javadoc
```
mvn javadoc:javadoc
```
Javadoc is generated in _target/site/apidocs/index.html_
