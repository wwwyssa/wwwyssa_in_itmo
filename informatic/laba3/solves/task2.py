import re


def solve(s):
    cleared_s = re.sub(r'\W+', r' ', s)
    filtered = re.findall(r'ВТ\b(?:\W+\w+){0,4}\W+ИТМО\b', cleared_s)
    result = '\n'.join(filtered)
    print(result)
    return result
