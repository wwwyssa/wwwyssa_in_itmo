import re


def solve(inp_string):
    result = len(re.findall(r"8-{P", inp_string))
    print(result)
    return result
