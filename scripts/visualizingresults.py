import matplotlib.pyplot as plt
import time
import statistics
import argparse
import json

parser = argparse.ArgumentParser(description='')
parser.add_argument('-d', '--directory', help='directory containing the jsons', required=True)
parser.add_argument('--easy', help='filename of the json with easy difficulty', required=True)
parser.add_argument('--hard', help='filename of the json with hard difficulty', required=True)
parser.add_argument('-o', '--outputdir', help='directory of graph outputs', required=False)

args = parser.parse_args()

file_loc = args.directory
hard_name = args.hard
easy_name = args.easy
output_dir = args.outputdir if args.outputdir else ""

def get_dictionary(json_location, hard, easy):
    json_hard = open(str(json_location) + str(hard))
    json_easy = open(str(json_location) + str(easy))
    return json.loads(json_hard.read()), json.loads(json_easy.read())

def apply(func, r, x, objective):
   return [func(list(map(int, r[str(labels)][objective]))) for labels in x]

def visualize_time(r, x, diff):
    plt.plot(x, apply(statistics.mean, r, x, 'Time'), label=diff + "_" + 'mean')
    plt.plot(x, apply(statistics.median, r, x, 'Time'), label=diff + "_" + 'median')
    plt.legend(loc='upper left')


# saves currently opened plot and closes it
def saveplot(filename, xlabel, ylabel, title):
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)

    plt.savefig(f"{output_dir}{filename}.png")
    plt.close()

# currently assumes that optimal height is always 10.0
def visualize_optimality(r, x, diff):
    optim_dict = {}
    y = []
    for label in x:
        optim_dict[label] = [float(h) / 10.0 for h in r[str(label)]['Height']]

    plt.plot(x, [statistics.mean(optim_dict[labels]) for labels in x], label=diff + "_" + 'mean')
    plt.plot(x, [statistics.median(optim_dict[labels]) for labels in x], label=diff + "_" + 'median')
    plt.legend(loc='upper left')


def visualize_overlaps(r, x, diff):
    plt.plot(x, apply(sum, r, x, 'Overlaps'), label='total')
    plt.plot(x, apply(statistics.mean, r, x, 'Overlaps'), label=diff + '_' + 'mean')
    plt.plot(x, apply(statistics.median, r, x, 'Overlaps'), label=diff + '_' + 'median')
    plt.legend(loc='upper left')


def visualize(stamp):
    hard, easy = get_dictionary(file_loc, hard_name, easy_name)
    x = sorted([int(labels) for labels in hard.keys()])

    visualize_time(hard, x, "hard")
    visualize_time(easy, x, "easy")
    saveplot(f"time_{stamp}", "|P|", "time(ms)", "Running time")
    visualize_optimality(hard, x, "hard")
    visualize_optimality(easy, x, "easy")
    saveplot(f"optimality_{stamp}", "|P|", "h/h_opt", "Optimality")
    visualize_overlaps(hard, x, "hard")
    visualize_overlaps(easy, x, "easy")
    saveplot(f"overlaps_{stamp}", "|P|", "#overlaps", "Overlaps")

visualize(time.strftime("%Y%m%d-%H%M%S"))
