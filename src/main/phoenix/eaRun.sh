#!/bin/bash
#SBATCH -p batch                                                # partition (this is the queue your job will be added to)
#SBATCH -n 4                                                    # number of cores (sequential job uses 1 core)
#SBATCH --time=20:00:00                                         # time allocation, which has the format (D-HH:MM:SS), here set to 1 hour
#SBATCH --mem=64GB                                               # specify memory required per node (here set to 4 GB)

# Notification configuration
#SBATCH --mail-type=ALL
#SBATCH --mail-type=TIME_LIMIT_90
#SBATCH --mail-user=a1666857@student.adelaide.edu.au            # Email to which notification will be sent

# Name
#SBATCH -J EARun.sh
bash $run_script
