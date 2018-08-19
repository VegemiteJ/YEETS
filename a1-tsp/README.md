# README
## Dependencies
Maven is required to build and run.
Java Version: 1.8.0_121

## Running code
`mvn clean`
`mvn compile`

To build the EvolutionaryAlgorithm Jar:
`mvn jar:jar@ea`

To build the LocalSearch Jar:
`mvn jar:jar@localsearch`

To Run either:
`java -jar target/EABenchmark.jar`
`java -jar target/LocalSearch.jar`

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
ea_benchmark_generation.sh creates a series of folders for phoenix runs.
It creates different combinations of operators + population sizes.
