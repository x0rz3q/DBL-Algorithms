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
parser.add_argument('--resultloc', help='location of the file to analyze', required=True)

args = parser.parse_args();
results_loc = args.resultloc

lines_per_run = 7

# assuming first digit is nr Points
def nrPoints(filename):
    return [int(s) for s in filename.split('_') if s.isdigit()][0]

def merge(a):
    return [j.replace('\n', '') for j in a]

def get_new_table():
    return {"Time" : [],
            "Height" : [],
            "Max label height" : [],
            "Overlaps" : []
            }

def runAlgo():
    easy = {}
    hard = {}

    r = results_loc.partition('/')[-1]
    current_algorithm = r.partition('.')[0] # current algorithm
    print(f"Analyzing {current_algorithm}")

    with open(results_loc) as f:
        lines = f.readlines()

    results_per_run = [merge(lines[i + 1 :i+lines_per_run]) for i in range(0, len(lines), lines_per_run)]
    num_tests = len(results_per_run)
    progress = 0;

    for res in results_per_run:
        amount_of_labels = res[0].partition(": ")[2]
        difficulty = res[4].partition(": ")[2]

        if ( difficulty == "hard" and amount_of_labels not in hard ) or ( difficulty == "easy" and amount_of_labels not in easy ):
                if difficulty == "hard":
                    hard[amount_of_labels] = get_new_table()
                else:
                    easy[amount_of_labels] = get_new_table()

        for optimize in res[1:]:
            partitioned = optimize.partition(": ")
            category, amount = partitioned[0], partitioned[2]

            if category == "Difficulty":
                continue

            if difficulty == "hard":
                hard[amount_of_labels][category] += [amount]
            else:
                easy[amount_of_labels][category] += [amount]

        time.sleep(0.1)
        sys.stdout.write(f"\r{progress}/{num_tests}") # progress report
        sys.stdout.flush()
        progress += 1

    return easy, hard

def analyze():
    # run algo on tests
    ez, hrd = runAlgo()

    # write to file
    with open(f"{results_loc}_easy.json", 'w') as fp:
        json.dump(ez, fp)

    with open(f"{results_loc}_hard.json", 'w') as fp:
        json.dump(hrd, fp)

analyze()
