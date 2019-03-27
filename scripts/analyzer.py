import subprocess
import time
import sys
import os
import argparse
import json

# if you want to run the analyzer
# only change this directory and it should work
# for windows people (puke) slashes might need different syntax 

parser = argparse.ArgumentParser(description='')
parser.add_argument('--allresults', help='location of files to parse with results after running', required=True)

args = parser.parse_args();
results = args.allresults

lines_per_run = 6

# assuming first digit is nr Points
def nrPoints(filename):
    return [int(s) for s in filename.split('_') if s.isdigit()][0]

def merge(a):
    return [j.replace('\n', '') for j in a]

def runAlgo():
    analysis = {}

    for r in os.listdir(results):

        current_algorithm = str(r)[:-3] # current algorithm
        print(f"Analyzing {current_algorithm}")

        with open(results + str(r)) as f:
            lines = f.readlines()

        results_per_run = [merge(lines[i + 1 :i+lines_per_run]) for i in range(0, len(lines), lines_per_run)]
        num_tests = len(results_per_run)
        progress = 0;

        for res in results_per_run:
            amount_of_labels = res[0].partition(": ")[2]
            if amount_of_labels not in analysis:
                analysis[amount_of_labels] = {"Time":[],
                                              "Height":[],
                                              "Max label height":[],
                                              "Overlaps":[]}
            for optimize in res[1:]:
                partitioned = optimize.partition(": ")
                category, amount = partitioned[0], partitioned[2]
                analysis[amount_of_labels][category] += [amount]

            time.sleep(0.1)
            sys.stdout.write(f"\r{progress}/{num_tests}") # progress report
            sys.stdout.flush()
            progress += 1

    return current_algorithm, analysis

def analyze():
    # run algo on tests
    filename, r = runAlgo()
    # write to file
    with open(results + filename + "json", 'w') as fp:
        json.dump(r, fp)

analyze()
