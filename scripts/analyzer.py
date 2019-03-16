import subprocess
import time
import os

project_location = "/home/juris/Uni/DBL-Algorithms/"
test_location = project_location + "profiling/tests/" # location of generated tests
class_location = project_location + "out/production/DBL-Algorithms/" # location of compiled class files
output_location = project_location + "profiling/" # output graphs location 

os.chdir(class_location) # go to class files

# assuming first digit is nr Points
def nrPoints(filename):
    return [int(s) for s in filename.split('_') if s.isdigit()][0]

def analyze(algo_name):
    results = {} # npoints -> [totaltime, timesrun]

    for test in os.listdir(test_location): # for each test
        if not str(test).startswith(algo_name):
           continue 

        main_command = ["java", "main.MainWrapper"] # this will run MainWrapper
        main_command.append(test_location + test)

        cur_time = time.time()
        subprocess.check_output(main_command)
        running_time = time.time() - cur_time
        # update results
        n = nrPoints(str(test))
        if n not in results:
            results[n] = [0, 0]
        results[n][0] += running_time
        results[n][1] += 1
        print(str(n) + " " + str(results[n]))

    return results

analyze("2pos")

      

