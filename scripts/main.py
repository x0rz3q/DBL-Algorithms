import argparse
import os
import shutil
import glob
import re

parser = argparse.ArgumentParser(description='Handle files for momotor')
parser.add_argument('--path', required=True)
args = parser.parse_args()

os.mkdir("src")
os.chdir("src")

for root, dirs, files in os.walk(args.path):
    for file in files:
        if file.endswith(".java"):
            shutil.copyfile(os.path.join(root, file), file)

os.remove("MainWrapper.java")

re = re.compile('((package [a-zA-Z.*]+;)|import ((?!java)[a-zA-Z.*]+);)')

for file in glob.glob("*.java"):
    with open(file, 'r') as f:
        lines = f.read()

    with open(file, 'w') as f:
        result = re.sub("", lines)
        f.write(result)
