# Data Engineer - Technical Challenge Solution

## Requirements
* Java 11
* Scala 2.12
* sbt 1.6.1

Once the repo has been cloned, run this command:
```shell
sbt compile
```

## 1. Coding Challenge
The solution of this exercise has two App objects:
### SumFinder
Location: `src.main.scala.com.exercise1.SumFinder` 

This App object has the solution for the first part of the exercise and the definition of the
required function that takes two arguments: value to search and the list of integers.

Testing run:
```shell
sbt "run 24 1,2,33,23,25,-1,22"
```
Then *type 1* to get the output of SumFinder.

### SumFinderAll
Location: `src.main.scala.com.exercise1.SumFinderAll` 

Like SumFinder, this app object returns the output of all possible combinations of elements in the
array of values that sum the given value as parameter.

Testing run:
```shell
sbt "run 24 1,2,33,23,25,-1,22"
```
Then *type 2* to get the output of SumFinderAll.

### Process does not fit in memory
The answer to the requirement is described in file: `/src/main/scala/com/exercise1/not_fits_in_memory_solution.md`

## 2. Spark Challenge
The solution of this exercise has two components:
* `src.main.scala.com.exercise2.AvgTenMinutesETL`: contains the processing logic described in the exercise.
* `src.main.scala.com.exercise2.AvgTenMinutesJob`: contains the Spark Context initialization and its configurations.

### How to test

```shell
sbt "run <input_path> <output_path>"
```
For example:
```shell
sbt "run /resources /resources"
```
Then *type 3* to execute `AvgTenMinutesJob`.

The majority of the documentation is done with scaladocs inside the ETL class.

Lastly, the testing and data quality check proposal was done in the file `/src/main/scala/com/exercise2/testing_and_quality_proposal.md`.

## 3. SQL Challenge
The content of the solution is found in the folder `/sql`. Inside, there is a file with the query 
for each exercise (`EXERCISE_N.sql`) as well as two other files `CREATE_SCHEMA.sql` and `INSERT_MOCK_DATA.sql` to create a testing schema.

## 4. Data Architecture Challenge
The solution for this section is divided into four .md files, one for each question. It is inside the folder `/data_architecture_challenge`.