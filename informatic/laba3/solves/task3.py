import re


def solve(inp):
    inp = inp.replace('\n', ' ')
    filtered = re.search(r'<meta name="daily_price" content="(\W+\w+)*\W+Bitcoin([^/>])*/>', inp)
    price = re.search(r'₽\S+?([\s]|r)', filtered.group(), flags=re.IGNORECASE)
    result = price.group()[1:-1]
    return result
