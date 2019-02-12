# structure
## Parser
The way the parser should work with the structure
### Input reader

* read input file
* make a list of labels
* make a tree of labels, with an object reference to the same object as in the list
* return a DataRecord

### Output reader
* write basic information from datarecord towards output stream
* loop over list of labels and call .toString()

## Algorithm
For each algorithm you get a datarecord this contains all the information we have at 
the point of calling this algorithm. The algorithm will therefore get a list of labels and
a tree of labels.

The algorithm is expected to modify either one of these such that the information in
the objects is a correct placement. 

## Square Interface
The square will have two implementations, Point and Rectangle. This is so that we have a
better structure in the code. The two types will have exactly the same methods, the implementation
is just a bit simplified for a point.

## Label Interface
The label interface will have two implementations, one with an alpha slider and one with a placement direction.
**The algorithm that handles the placement will need to cast to the type that it expect**!

## Abstract Collection
The abstract collection interface will depend on the SquareInterface, so that the queries
can be done in a general way.

**The algorithm that gets result from this will need to cast towards the right datatype if 
they need label specific information**