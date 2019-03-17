import matplotlib.pylab as plt
import csv
import sys
import os


project_location = "/home/juris/Uni/DBL-Algorithms/"
csv_location = project_location + "profiling/"
output_name = "combo"
extension = ".pdf"
x_label = "|P|"
y_label = "t (seconds)"

blacklist = [] # which not to plot
full_info = [] # contains info about all plots

for filename in os.listdir(csv_location):
    # skip unnecessary ones
    if not str(filename).endswith(".csv") or str(filename) in blacklist:
        continue
    print(str(filename))
    # read file
    with open(csv_location + str(filename), 'r') as f:
        info = list(csv.reader(f))
    # flatten list and string tuples -> tuples
    info = [eval(j) for i in info for j in i]
    full_info.append(info)

for i in full_info:
    x, y = zip(*i)
    plt.plot(x, y)

plt.xlabel(x_label)
plt.ylabel(y_label)
plt.savefig(f"{csv_location}{output_name}{extension}")
