from solves import task2


class Test2:
    def test1(self):
        inp = 'А ты знал, что ВТ – лучшая кафедра в ИТМО?'
        result = 'ВТ лучшая кафедра в ИТМО'
        assert task2.solve(inp) == result

    def test2(self):
        inp = 'кто ВТ а а а а ИТМО где'
        result = 'ВТ а а а а ИТМО'
        assert task2.solve(inp) == result

    def test3(self):
        inp = f'ВТ ИТМО крутая штука'
        result = 'ВТ ИТМО'
        assert task2.solve(inp) == result

    def test4(self):
        inp = 'ВТ 1 2 3 4 5 ИТМО'
        result = ''
        assert task2.solve(inp) == result

    def test5(self):
        inp = 'ВТ 1 2 3 4 ИТМО ВТ 1 ИТМО'
        result = 'ВТ 1 2 3 4 ИТМО\nВТ 1 ИТМО'
        assert task2.solve(inp) == result
