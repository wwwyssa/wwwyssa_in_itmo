import re


def solve(s):
    split_s = re.sub(r'\W+', r' ', s)
    filtered = re.findall(r'ВТ\b(?:\W+\w+){0,4}\W+ИТМО\b', split_s)
    result = '\n'.join(filtered)
    return result
