# Scheduling Component for Indoor Athletics (Track&Field) Competitions

    Örebro University - Computer science - second year
    System and Software Engineering group project
---

## Project description

The NNN Track&Field organization wants an intelligent system which will take a list of competitors (csv format) as input and auto-generate a schedule in the same format for their Indoor Athethics track & field competitions, as opposed to doing it manually. An existing
schedule also needs to be editable to accommodate delays for sub competitions.

## Functional requirements

* Take a list of competitors (csv-format) as input and produce a schedule for the entire competition (csv-format) as output.
* Take an existing schedule (csv-format), a certain sub-competition, and an integer representing minutes as input to produce a new schedule to accommodate for delays.
* Qualifiers, quarterfinals, semifinals etc may run during the same days. The finals
aswell as the the prize ceremony needs to be held on a separate day.
* All initial sub-competitions should include names of all the contestants so that users can see when a certain competitor will participate.
* The time needed for sub-competitions will be calculated based on the maximum previous record of the particular group plus a buffer. Jumps/Throws will be based on a standard.
* All of the sub-competitions will have a default delay-parameter set to 0. This can be changed to accommodate for delays to produce a new schedule

## Non-functional requirements

* System needs to be developed in Java.
* The resulting csv-formatted schedule needs to be visually pleasing and easy to understand.
* The resulting schedule should be somewhat optimized so that no station is being left empty for no reason.
* The program should produce a schedule without errors regardless of input size.
* Fast and low resource cost.
* Easy to use.

## Constraints

* Competitors will only compete within it’s own gender and age-span.
* The competition needs to be fair. All the competitors with the best previous records cannot be put in the same initial sub-competition.
* The different disciplines will run in parallel, but one competitor cannot compete in two disciplines at once. A possible solution would be to rotate groups of competitors at different times. Another solution could be to put a timer on each competitor.
* There needs to be an appropriate rest time for all competitors.
or to limit the number of games that each player can participate in.
* The amount of stations/tracks limits the number of sub-competitions needed at each stage. The number if also affected by how many competitors there are within each group.

## Contributors

* Otto von Arnold
* Gorgis Morad
* Henrik Svedsäter
* Sami Al Mousa
