import random
import os
import math
import subprocess
import argparse

parser = argparse.ArgumentParser(description='')
parser.add_argument('-o', '--outputname', help='output file name', required=True)
args = parser.parse_args();
filename = args.outputname

algorithms = ["2pos", "4pos"] # algorithms to generate tests for
difficulties = ["hard", "easy"]
# model, npoints, ratio, result xgen, ygen
point_amounts = [1000, 2500, 5000, 7500, 10000, 12500, 15000]
amount = 100 # amount of cases per point amount in a difficulty. 
# currently assume uniform generator
generator = "Geometric" # when more generators are used, adjust regex at the bottom of the script
def getEasy():
    return {
        1000 : [random.uniform(0.01, 0.06), random.uniform(0.01, 0.06)],
        2500 : [random.uniform(0.01, 0.04), random.uniform(0.01, 0.04)],
        5000 : [random.uniform(0.01, 0.03), random.uniform(0.01, 0.03)],
        7500 : [random.uniform(0.01, 0.02), random.uniform(0.01, 0.02)],
        10000 : [random.uniform(0.001, 0.01), random.uniform(0.001, 0.01)],
        12500 : [random.uniform(0.001, 0.009), random.uniform(0.001, 0.009)],
        15000 : [random.uniform(0.001, 0.009), random.uniform(0.001, 0.009)]
    }
def getHard():
    return {
        1000 : [random.uniform(0.09, 0.11),random.uniform(0.09, 0.11)],
        2500 : [random.uniform(0.055, 0.065),random.uniform(0.055, 0.065)],
        5000 : [random.uniform(0.03, 0.04),random.uniform(0.03, 0.04)],
        7500 : [random.uniform(0.02, 0.03),random.uniform(0.02, 0.03)],
        10000 : [random.uniform(0.02, 0.025),random.uniform(0.02, 0.025)],
        12500 : [random.uniform(0.015, 0.025),random.uniform(0.015, 0.025)],
        15000 : [random.uniform(0.015, 0.020),random.uniform(0.015, 0.020)]
    }

def getProbability(nPoints, diff):
    if diff == "hard":
        return getHard()[nPoints]
    elif diff == "easy":
        return getEasy()[nPoints]

def getResult(algo, ratio):
    return random.choice([3])

def getRatio(n):
    return 0.5

tests = {}
for algo in algorithms:
    easy_tests = [] # contains the tests 
    hard_tests = [] # contains the tests 
    for diff in difficulties:
        for nPoints in point_amounts:
            for _ in range(amount): # generate tests
                test = ""
                test += algo + " "
                test += str(nPoints) + " "
                ratio = getRatio(nPoints)
                result = getResult(algo, ratio)
                test += str(ratio) + " "
                test += str(result) + " "

                probs = getProbability(nPoints, diff)
                test += generator + " " + str(probs[0]) + " "
                test += generator + " " + str(probs[1])
                test.strip()

                if diff == "hard":
                    hard_tests.append(test)
                else:
                    easy_tests.append(test)
        res = []
        if diff == "hard":
            res = hard_tests
        else:
            res = easy_tests
        tests[algo + diff] = res

for algo in algorithms:
    for diff in difficulties:
        f = open(f"{algo}_{diff}_{filename}", "w+")
        f.write("complex\n")
        for t in tests[algo+diff]:
            f.write(t + "\n")
        f.close()
