# Documentation Test Generator
This document serves as a guide for using the testGenerator branch to generate custom testcases
## General Interface
All interaction with the generator is achieved via the *"TestCaseSpecification.txt"* file.
The first line specifies the style in which the test cases are specified.
Every other line of this file serves as the specification for a certain test case to be generated.
After executing the *"Main"* class, the test cases will be generated as new *".txt"* files in the project directory.
Their name will reflect their specification. Note: if the program runs out of computational time, a specified testcase
will be aborted and no labels will be printed. Afterwards it will commence with the next testcase.

## Specification style
The first line of the *"TestCaseSpecification.txt* file specifies the style in which the testcases are described.
There are two styles: **simple** and **complex**.  
The simple style allows for quick generation of testcases, which can be specified by their difficulty.
The complex style allows for a more fine-tuned approach, with a far higher degree of adaptability.

The first line should only contain the name of the style (all lower case).

## Test Case Specification Simple
### General format
The general format for a line in the *"TextCaseSpecification.txt"* file is as follows:
```
(model) (difficulty) (nPoints)
``` 
#### model
Which model the testcase is meant for. Possibilities: "2pos", "4pos", "1slider"

#### difficulty
The difficulty of the testcase. Possibilities: "easy", "medium", "hard", "custom" 

easy = 20% coverage

medium = 30% coverage

hard = 40% coverage

custom: specify coverage by supplying an extra parameter:
```
custom (coverage)
```
where *coverage* is the coverage as a ratio (0.1 = 10%)
#### nPoints
The number of points


## Test Case Specification Complex
### General format
The general format for a line in the *"TextCaseSpecification.txt"* file is as follows:
```
(model) (nPoints) (ratio) (result) (xGenerator) (yGenerator)
``` 
#### model
Which model the testcase is meant for. Possibilities: "2pos", "4pos", "1slider"

#### nPoints
The number of points

#### ratio
The ratio between height and width as specified in the Problem Description.

#### result
The desired result. It must follow the following format:

2pos: 
```
result == integer || result * ratio * 2 == integer
```
4pos:
```
result * 2 == integer || result * ratio * 2 == integer
```
1slider:
```
(result * ratio * 2 == integer && result * ratio >= 1.5) || (result == integer && result * ratio >= 1.0)
```

#### xGenerator
The distribution generator to be used for generating the *x*-coordinates of the points.
Each distribution takes its own argument(s). 
All points are limited to (0, 10000).

Uniform:
```
Uniform (lower) (upper)
```
Generates numbers according to a uniform distribution, with *(lower)* being the lower bound of the range and *(upper)* the upper bound.


Binomial:
```
Binomial (p) (n)
```
Generates numbers according to a binomial distribution, with *(p)* the success probability of a trial, and *(n)* the number of trials.

Poisson:
```
Poisson (p)
```
Generates numbers according to a Poisson distribution, with *(p)* the Poisson mean. Note that this distribution is the equivalent of a normal distribution for discrete variables.

Geometric:
```
Geometric (p)
```
Generates numbers according to a geometric distribution, with *(p)* the success probability.

#### yGenerator
Identical to *xGenerator* for the *y*-coordinates of the points.

## Provided solution format
The optimal solution to the test case will provided in the output file. After all the points in the output file,
two more lines are added:

```
label height: (result)
minimum expected result: (expectedMinimum) 
```
where *(result)* is replaced by the optimal solution and *(expectedMinimum)* is replaced by the minimum result needed for passing the testcase.

### 2pos
```
expectedMinimum = result
```
### 4pos
```
expectedMinimum = result / 2
```
### 1slider
```
expectedMinimum = result / 2
```    
