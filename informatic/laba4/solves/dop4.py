import time
import mainTask
import dop1
import dop2
import dop3

input_file = r'E:\wwwyssa_in_itmo\informatic\laba4\data\in.json'

first_start = time.time()
print(f"Start first algorithm 1000 times")
for i in range(1000):
    _ = mainTask.json_to_xml(input_file)
print(f"First algorithm end, time need {time.time() - first_start}sec")
print()

second_start = time.time()
print(f"Start second algorithm 1000 times")
for i in range(1000):
    _ = dop1.convert(input_file)
print(f"Second algorithm end, time need {time.time() - second_start}sec")
print()

third_start = time.time()
print(f"Start third first algorithm 1000 times")
for i in range(1000):
    _ = dop2.json_to_xml(input_file)
print(f"Third algorithm end, time need {time.time() - third_start}sec")
print()

fourth_start = time.time()
print(f"Start fourth first algorithm 1000 times")
for i in range(1000):
    _ = dop3.convert(input_file)
print(f"Fourth algorithm end, time need {time.time() - fourth_start}sec")
print()
