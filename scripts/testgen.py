import random
import math

filename = "TestCaseSpecification.txt"
algorithms = ["2pos"] # algorithms to generate tests for
# model, npoints, ratio, result xgen, ygen
point_amounts = [10, 100, 1000, 5000, 10000, 15000]
result_range = [6, 24] # range of picking random result
amount = 100 # amount of cases per point amount

# currently assume uniform generator
generator = "Uniform"
lowerbound_range = [10, 150]
upperbound_range = [500, 10000]

def generatorGen():
    return random.randint(lowerbound_range[0], lowerbound_range[1]), random.randint(upperbound_range[0], upperbound_range[1]) 

def divisorGenerator(n):
    divisors = []
    for i in range(1, int(math.sqrt(n) + 1)):
        if n % i == 0:
            if i*i != n:
                divisors.append(i)

    return divisors

def getRatio(result):
    start = 2
    while (result % start == 0):
        start *= 2
    start /= 2

    return result / start if result % 2 == 0 else 1



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
for t in tests:
    f.write(t + "\n")
f.close()





