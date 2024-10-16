import re


def solve(s):
    split_s = re.split(r'\W+', s)
    pos = 0
    result = []
    filtered = re.finditer(r'(ВТ)(\W+\w+){0,4}\W+(ИТМО)', s)
    for e in filtered:
        tmp = e.group()
        t = re.sub(r"\W+", r' ', tmp)
        result.append(t)
    return '\n'.join(result)
