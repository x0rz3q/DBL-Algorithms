import random
import os
import math
import subprocess
import argparse

parser = argparse.ArgumentParser(description='')
parser.add_argument('--project', help='Project Location', required=True)
args = parser.parse_args();

filename = "TestCaseSpecification.txt"
algorithms = ["2pos"] # algorithms to generate tests for
# model, npoints, ratio, result xgen, ygen
point_amounts = [10, 100, 1000, 10000, 15000, 20000, 25000]
result_range = [2, 12] # range of picking random result
amount = 100 # amount of cases per point amount

project_location = os.path.join(args.project, '')

test_location = project_location +"profiling/tests"
class_files = project_location + "out/make"

# currently assume uniform generator
generator = "Uniform" # when more generators are used, adjust regex at the bottom of the script
lowerbound_range = [10,500]
upperbound_range = [5000,10000]

def generatorGen():
    lower = random.randint(lowerbound_range[0], lowerbound_range[1]) 
    return lower, random.randint(lower + upperbound_range[0], upperbound_range[1]) 

def divisorGenerator(n):
    divisors = []
    for i in range(1, int(math.sqrt(n) + 1)):
        if n % i == 0:
            if i*i != n:
                divisors.append(i)

    return divisors

def getRatio(result):
    return random.randint(1, max(int(result/2), 1)) 

tests = [] # contains the tests 
for algo in algorithms:
    for nPoints in point_amounts:
        for _ in range(amount): # generate tests
            test = ""
            test += algo + " "
            test += str(nPoints) + " " 
            result = random.randint(result_range[0], result_range[1])
            ratio = getRatio(result)
            test += str(ratio) + " "
            test += str(result) + " "
            lower, upper = generatorGen()
            for g in range(2):
                test += generator + " "
                test += str(lower) + " " + str(upper) + " "
            test.strip()
            tests.append(test)

# write the tests to a file
f = open(filename, "w+")
f.write("complex\n")
for t in tests:
    f.write(t + "\n")
f.close()

os.chdir(project_location)
main_command = ["make", "testgen"]
subprocess.check_output(main_command)
subprocess.check_output(["mkdir", "-p", test_location])

# move all files
# if you use wildcards you need to set shell=True
subprocess.check_output(["mv", f"{project_location}*{generator}*", test_location], shell=True)
