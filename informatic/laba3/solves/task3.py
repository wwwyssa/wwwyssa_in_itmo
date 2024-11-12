import re


def solve(inp):
    inp = inp.replace('\n', ' ')
    filtered = re.search(r'<meta name="daily_price" content="(\W+\w+)*\W+Цена Bitcoin([^/>])*/>', inp)
    price = re.search(r'(?<=₽)\S+(?=[\sr])', filtered.group(), flags=re.IGNORECASE)
    result = price.group()

    return result

