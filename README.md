# YEETS - Yearning to Explore Evolutionary TSP Solvers
YEETS is a framework for implementing evolutionary algorithms for the Travelling Salesman Problem (TSP).

## Group Members:
Name | id | email
--- | --- | ---
Jack Baxter | a1666857 | a1666857@student.adelaide.edu.au
Joshua Loh | a1670961 | a1670961@student.adelaide.edu.au
Denton Phosvanh | a1680965 | a1680965@student.adelaide.edu.au
Robert McAuley | a1668724 | a1668724@student.adelaide.edu.au
James Kortman | a1648090 | a1648090@student.adelaide.edu.au
Nehal Jain | a1689549 | a1689549@student.adelaide.edu.au

## Dependencies
- Maven is required to build and run.
- Java Version: 1.8.0_121

## Running code
- `mvn clean`
- `mvn compile`

### Building and running individual .jars:
Algorithm | Build command | Run command
--- | --- | ---
EvolutionaryAlgorithm | `mvn jar:jar@ea` | `java -jar target/EABenchmark.jar`
LocalSearch | `mvn jar:jar@localsearch` | `java -jar target/LocalSearch.jar`
InverOver | `mvn jar:jar@inverover` | `java -jar target/InverOver.jar`

## Configuring Evolutionary Algorithm
Modify config.properties to use the desired mutate, crossover, selection and terminating conditions.

An example layout:
```
// Default config file for TSP solution
// put the String name of the implementation to use for each step
// copy this file and edit the copy to run a different configuration
// probably don't need to version control copies
SelectParents=uni.evocomp.a1.selectparents.UniformRandom
Recombine=uni.evocomp.a1.recombine.CycleCrossover
Mutate=uni.evocomp.a1.mutate.Invert
SelectSurvivors=uni.evocomp.a1.selectsurvivors.TournamentSelection
// Put params like mutate prob
MutateProbability=0.1
PopulationSize=100
// General params - Timeout limit (seconds), Number of benchmarks to run (averaging)
TotalGenerations=20000
TimeoutLimit=900
NumberOfRuns=2
// Tests to run
TestDirPrefix=tests/
TestFiles=st70,eil51,eil76,eil101,kroA100,kroC100,kroD100,lin105,usa13509
```

## Phoenix run script generation
- ea_benchmark_generation.sh creates a series of folders for phoenix runs.
- It creates different combinations of operators + population sizes.
