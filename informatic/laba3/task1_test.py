from solves import task1


class Test1:
    def test1(self):
        inp = '8-{P8-{P    8-{P'
        result = 3
        assert task1.solve(inp) == result

    def test2(self):
        inp = '8-{Pdvcs8-{Pq1vwrg'
        result = 2
        assert task1.solve(inp) == result

    def test3(self):
        inp = 'nvodniona'
        result = 0
        assert task1.solve(inp) == result

    def test4(self):
        inp = '8-1{P   '
        result = 0
        assert task1.solve(inp) == result

    def test5(self):
        inp = 'skdnv8-{Pnajfn'
        result = 1
        assert task1.solve(inp) == result
