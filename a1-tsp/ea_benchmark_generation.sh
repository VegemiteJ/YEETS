#!/bin/bash

# Generate a folder with the following:
# folder/
# |
# +-Main.jar
# |
# +-subfolder1/
# | |
# | +-config.properties
# | |
# | +- symlink_to_tests/
# |
# +-subfolder2/
# | |
# | +-config.properties
# | |
# | +- symlink_to_tests/
# |
# +-tests/
#   |
#   +- *.tsp
#   |
#   +- *.opt.tour
# but have a subfolder for every single combination of algorithm

# Everything but selectparents
# for mutate in {In{s,v}ert,Swap} ;do
# 	for recombine in {{Cycle,Order}Crossover} ;do
# 		for selectparents in {UniformRandom} ;do
# 			for selectsurvivors in {Elitism,FitnessProportional,TournamentSelect} ;do
# 				printf "got %s %s %s %s %s\n" "$mutate" "$recombine" "$selectparents" "$selectsurvivors"
# 			done
# 		done
# 	done
# done

target=ea_benchmarks
mvn jar:jar

package_path=uni.evocomp.a1

alg_num=1
for recombine in {{Order,Cycle}Crossover,EdgeRecombination,PMXCrossover} ;do

	for sselection in {Elitism,TournamentSelect,FitnessProportional} ;do

		# echo "$alg_num"

		# file for each population size
		for pop_size in {2,5,{1,2}0}0 ;do

		target_dir="$target/$recombine-Invert-$sselection-$pop_size"

		# Make directory
		# echo "$recombine-Invert-$sselection"
		mkdir -p "$target_dir"

			# create config.properties inside
			echo -e "Algorithm=EA$alg_num\nSelectParents=$package_path.selectparents.UniformRandom\nRecombine=$package_path.recombine.$recombine\nMutate=$package_path.mutate.Invert\nSelectSurvivors=$package_path.selectsurvivors.$sselection\nPopulationSize=$pop_size\n" > "$target_dir/config.properties"

			# symlink to the tests
			for item in tests/* ;do
				if [ -f "$item" ] ;then
					if [ ! -d "$target_dir/tests" ] ;then
						mkdir -p "$target_dir/tests"
					fi
					if [ ! -L "$target_dir/$item" ] ;then
						ln -s "$item" "$target_dir/$item"
					fi
				fi
			done

		done
		alg_num=$((alg_num+1))
	done
done


# symlink the .jar
if [ ! -L "$target/a1-tsp.jar" ] ;then
	ln -s "target/a1-tsp.jar" "$target/a1-tsp.jar"
fi
