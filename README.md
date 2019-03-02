# Documentation Test Generator
This document serves as a guide for using the testGenerator branch to generate custom testcases
## General Interface
All interaction with the generator is achieved via the *"TestCaseSpecification.txt"* file.
Every line of this file serves as the specification for a certain test case to be generated.
After executing the *"Main"* class, the test cases will be generated as new *".txt"* files in the project directory.
Their name will reflect their specification.

## Test Case Specification
### General format
The general format for a line in the *"TextCaseSpecification.txt"* file is as follows:
```
(model) (nPoints) (result) (ratio) (xGenerator) (yGenerator)
``` 
#### model
Which model the testcase is meant for. Possibilities: "2pos", "4pos", "1slider"

#### nPoints
The number of points

#### result
The desired result. It must follow the following format:

**2pos:** 
```
result == integer || result * ratio * 2 == integer
```
**4pos:**
```
result * 2 == integer || result * ratio * 2 == integer
```
**1slider:**
```
result == real
```

#### ratio
The ratio between height and width as specified in the Problem Discription.   

#### xGenerator
The distribution generator to be used for generating the *x*-coordinates of the points.
Each distribution takes its own argument(s). 
Keep in mind that the boundaries of these distributions concern the points, not the rectangles.
To ensure that the rectangles fall within the QuadTree's range, make sure to limit the points accordingly.


**Uniform:**
```
Uniform (lower) (upper)
```
Generates numbers according to a uniform distribution, with *(lower)* being the lower bound of the range and *(upper)* the upper bound.


**Binomial:**
```
Binomial (p) (n)
```
Generates numbers according to a binomial distribution, with *(p)* the success probability of a trial, and *(n)* the number of trials.

**Poisson:**
```
Poisson (p)
```
Generates numbers according to a Poisson distribution, with *(p)* the Poisson mean. Note that this distribution is the equivalent of a normal distribution for discrete variables.

**Geometric:**
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
