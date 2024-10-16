from solves import task3


class Test2:
    def test1(self):
        inp = '''
        <meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Мы обновляем
нашу цену BTC к RUB в режиме реального времени."/> <meta name="daily_price"
content=" Цена Bitcoin в реальном времени сегодня составляет ₽5,797,806.88
RUB."/><meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/>
        '''
        result = '5,797,806.88'
        assert task3.solve(inp) == result

    def test2(self):
        # RUB слитно с ценой
        inp = '''
        <meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Мы обновляем
нашу цену BTC к RUB в режиме реального времени."/> <meta name="daily_price"
content=" Цена Bitcoin в реальном времени сегодня составляет ₽5,797,806.88RUB."/><meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/>
        '''
        result = '5,797,806.88'
        assert task3.solve(inp) == result

    def test3(self):
        # Цена маленькая, RUB через \n + пробел
        inp = '''
        <meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Мы обновляем
нашу цену BTC к RUB в режиме реального времени."/> <meta name="daily_price"
content=" Цена Bitcoin в реальном времени сегодня составляет ₽5,797 
 RUB."/><meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/>
        '''
        result = '5,797'
        assert task3.solve(inp) == result

    def test4(self):
        inp = '''
        <meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/> <meta name="daily_price" content=" Цена Bitcoin в реальном времени сегодня составляет ₽5,797,806.88
RUB."/>
        '''
        result = '5,797,806.88'
        assert task3.solve(inp) == result

    def test5(self):
        inp = '''
        <meta name="daily_volume" content="В суточным объемом торгов
₽2,835,029,974,960.63 RUB."/> <meta name="daily_price" content="Мы обновляем
нашу цену BTC к RUB в режиме реального времени."/> <meta name="daily_price"
content=" Price Bitcoin today is ₽5 RUB."/><meta name="daily_price" content="Ethereum стоит на данный момент
₽229,590,78 RUB."/>
        '''
        result = '5'
        assert task3.solve(inp) == result
