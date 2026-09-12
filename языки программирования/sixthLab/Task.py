
import random
import ctypes
from dataclasses import dataclass
from pathlib import Path

DATA_FILE = Path("pairs.txt")

class Point:
    x: float
    y: float

    def __init__(self, x: float, y: float):
        self.x = float(x)
        self.y = float(y)

    def __repr__(self) -> str:
        return f"Point(x={self.x}, y={self.y})"


def gf(path: Path, count: int = 1000):
    rng = random.Random()
    lines = []
    for _ in range(count):
        x1, y1 = rng.randint(0, 100), rng.randint(0, 100)
        x2, y2 = rng.randint(0, 100), rng.randint(0, 100)
        lines.append(f"{x1},{y1} {x2},{y2}")
    path.write_text("\n".join(lines), encoding="utf-8")

def read_file(path: Path):
    pairs: list[tuple[Point, Point]] = []
    for line in path.read_text(encoding="utf-8").strip().splitlines():
        a, b = line.strip().split()
        x1, y1 = map(float, a.split(","))
        x2, y2 = map(float, b.split(","))
        pairs.append((Point(x1, y1), Point(x2, y2)))
    return pairs



def compute_distances(pairs, lib):
    func = lib.pair_distances
    func.argtypes = [ctypes.POINTER(ctypes.c_double), ctypes.c_size_t, ctypes.POINTER(ctypes.c_double)]
    func.restype = None
    n = len(pairs)
    data = (ctypes.c_double * (n * 4))()
    out = (ctypes.c_double * n)()
    for i, (p1, p2) in enumerate(pairs):
        base = i * 4
        data[base] = p1.x
        data[base + 1] = p1.y
        data[base + 2] = p2.x
        data[base + 3] = p2.y
    func(data, n, out)
    return out[::]

def main():
    gf(DATA_FILE)
    pairs = read_file(DATA_FILE)
    lib = ctypes.CDLL("./distance.dll")
    distances = compute_distances(pairs, lib)
    for i, d in enumerate(distances):
        print(f"{i+1}: {pairs[i][0]} {pairs[i][1]} {d:.6f}")


if __name__ == "__main__":
    main()