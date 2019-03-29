import matplotlib.pyplot as plt
import statistics
import argparse
import json

parser = argparse.ArgumentParser(description='')
parser.add_argument('-i', '--inputloc',help='location of the json to visualize', required=True)
parser.add_argument('-o', '--outputdir',help='directory of graph outputs', required=False)

args = parser.parse_args()
file_loc = args.inputloc
output_dir = args.outputdir if args.outputdir else ""

def get_dictionary(json_location):
    json_f = open(json_location)
    return json.loads(json_f.read())

def apply(func, r, x, objective):
   return [func(list(map(int, r[str(labels)][objective]))) for labels in x]

def visualize_time(r, filename, x):
    plt.plot(x, apply(statistics.mean, r, x, 'Time'), label='mean')
    plt.plot(x, apply(statistics.median, r, x, 'Time'), label='median')
    plt.legend(loc='upper left')

    saveplot(filename, "|P|", "t(ms)", "Running time")

# saves currently opened plot and closes it
def saveplot(filename, xlabel, ylabel, title):
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)

    plt.savefig(f"{output_dir}{filename}.png")
    plt.close()

# currently assumes that optimal height is always 10.0
def visualize_optimality(r, filename, x):
    optim_dict = {}
    y = []
    for label in x:
        optim_dict[label] = [float(h) / 10.0 for h in r[str(label)]['Height']]

    plt.plot(x, [statistics.mean(optim_dict[labels]) for labels in x], label='mean')
    plt.plot(x, [statistics.median(optim_dict[labels]) for labels in x], label='median')
    plt.legend(loc='upper left')

    saveplot(filename, "|P|", "h/h_opt", "Optimality")

def visualize_overlaps(r, filename, x):
    plt.plot(x, apply(sum, r, x, 'Overlaps'), label='total')
    plt.plot(x, apply(statistics.mean, r, x, 'Overlaps'), label='mean')
    plt.plot(x, apply(statistics.median, r, x, 'Overlaps'), label='median')
    plt.legend(loc='upper left')

    saveplot(filename, "|P|", "#overlaps", "Overlaps")

def visualize(algo, difficulty):
    results = get_dictionary(file_loc)
    x = sorted([int(labels) for labels in results.keys()])

    visualize_time(results, f"{algo}_time_{difficulty}", x)
    visualize_optimality(results, f"{algo}_optimality_{difficulty}", x)
    visualize_overlaps(results, f"{algo}_overlaps_{difficulty}", x)

visualize("2pos", "hard")
