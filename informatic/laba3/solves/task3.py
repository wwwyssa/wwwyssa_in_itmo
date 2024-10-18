import re


def solve(inp):
    inp = inp.replace('\n', ' ')
    filtered = re.search(r'<meta name="daily_price" content="(\W+\w+)*\W+Bitcoin([^/>])*/>', inp)
    price = re.search(r'₽\S+?([\s]|r)', filtered.group(), flags=re.IGNORECASE)
    return price.group()[1:-1]


s = """
<meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Мы обновляем
нашу цену BTC к RUB в режиме реального времени."/> <meta name="daily_price"
content=" Price Bitcoin today is ₽5,45  RUB."/><meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/>
"""

s1 = '''
<meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/> <meta name="daily_price" content=" Цена Bitcoin в реальном времени сегодня составляет ₽5,797,806.88
RUB."/>
'''

