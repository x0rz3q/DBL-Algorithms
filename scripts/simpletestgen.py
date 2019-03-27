import subprocess
import os
import argparse

parser = argparse.ArgumentParser(description='')
parser.add_argument('--project', help='Project Location', required=True)
args = parser.parse_args();

# set parameters
filename = "TestCaseSpecification.txt"
algorithms = ["2pos", "4pos", "1slider"]
difficulties = ["easy", "custom"]
point_amounts = [100, 1000, 2500, 5000, 7500, 10000, 12500, 15000, 17500, 20000]
amount_per_points = 200
generate_test_files = True

# set directories
project_dir = os.path.join(args.project, '') # project root dir
test_dir = project_dir + "profiling/tests/" # output for all generated tests
class_dir = project_dir + "out/production/DBL-Algorithms/" # compiled class files
test_spec_dir = project_dir # where to put test specification file 

# generate tests
tests = []
for algo in algorithms:
    for diff in difficulties:
        for amount in point_amounts:
            for _ in range(amount_per_points):
                test = algo + " "
                test += diff + " "
                if diff == "custom":
                    test += "0.65 "
                test += str(amount)
                tests.append(test)

# write to file
f = open(filename, "w+")
f.write("simple\n")
for t in tests:
    f.write(t + "\n")
f.close()

# move test spec file to the specified directory
subprocess.check_output(["mv", filename, test_spec_dir])

# if generate_test_files:
#     os.chdir(project_dir)
#     subprocess.check_output(["make", "ARGS='"+ test_spec_dir + filename +"'", "testgen"])
#     algorithm_regex = ","
#     algorithm_regex.join(algorithms)
#     subprocess.check_output(["mv", "*{" + algorithm_regex + "}*", test_dir], shell=True)


