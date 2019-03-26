import subprocess
import os

# set parameters
filename = "TestCaseSpecification.txt"
algorithms = ["2pos", "4pos", "1slider"]
difficulties = ["easy", "medium", "hard"] 
point_amounts = [100, 1000, 3000, 6000, 10000, 15000, 20000]
amount_per_points = 10
generate_test_files = True

# set directories
project_dir = "/home/juris/Uni/DBL-Algorithms/" # project root dir
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
#
#if generate_test_files:
#    os.chdir(class_dir)
#    subprocess.check_output(["java", "Main", test_spec_dir + filename])
#
#    os.chdir(project_dir)
#    algorithm_regex = ","
#    algorithm_regex.join(algorithms)
#    subprocess.check_output(["mv", "*{" + algorithm_regex + "}*", test_dir])


