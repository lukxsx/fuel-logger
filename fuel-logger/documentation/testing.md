# Testing

The software is tested with automated JUnit tests and manually testing
the features on different platforms. 

## JUnit tests
Most of the tests are testing the application logic in the
_fuellogger.logic_ package. Tests are simulating typical uses of the
application. Tests of _RefuelManager_ class are testing adding and
managing the cars and refuelings. The tests of _StatisticsManager_ are
testing mainly validity of different calculations. The tests of
_Database_ class are testing if the SQLite database works correctly.
The tests are performed on a temporary test database that is
automatically deleted after the tests. Database tests are testing
if data is saved correctly and cases like duplicates. 

There are not many tests on _Car_ and _Refueling_ classes, because
they are mostly only classes to store data. They consist mainly of
getters. There are tests for the _Refueling_ class that test if
refueling cost is calculated correctly. The _ConfigFile_ class has also
few tests. 

The user interface is not tested. 

## Test coverage
![test coverage](testcoverage.png)

## Manual testing
The software is also tested on Cubbli virtual machine, my own PC running
Arch Linux and a laptop running Cubbli. All requirements are tested and
they are working. Input fields validations are also tested with wrong
inputs and there are no problems. However the virtual Cubbli
installation has some issues occasionally with SQLite. It gives an error
message of a locked or missing database. Other students in this course
have also had the same problem with virtual Cubbli. 
