# Requirements specification

## Purpose
The purpose of this software is to keep logs of refueling costs of a car.
The software can then view graphs and other interesting stats of your car
and it's costs. You can add multiple cars to the software. 

## Users
There will be no other roles than normal users because there is no really
need for them.

## Basic functionality
- [x] Graphical user interface
- [x] Adding a car (name, fuel tank capacity etc.)
- [x] Selecting a car to use
- [x] Logging a refueling (kilometrage, volume in litres)
- [x] Viewing average fuel consumption
- [x] Viewing graphs of fuel consumption
- [ ] Viewing graphs of cost
- [ ] Viewing graphs of driven kilometres
- [ ] Backup and restore of the data to a file (to use the software easily on
  another computer)

## Future features
- [ ] Text-based UI for quick fuel logging without need for opening the
   graphical UI
- [ ] Logging of other costs like repairs, taxes and insurances
- [ ] Support for other than SI and euro units (miles, dollars etc.)
- [ ] Exporting data as CSV or some other common file type

## Limitations
* Only works when tank is filled full, no partial refuelings
* Min. 2 refuelings needed to count fuel consumptions
* Must work on all common platforms supported by Java and JavaFX
(Linux, macOS and Windows)
* Only supports SI units and euros
* Data is saved locally

## UI draft
![UI draft](https://raw.githubusercontent.com/Lukxsx/ot-harjoitustyo/master/fuel-logger/documentation/ui%20draft.jpg)
