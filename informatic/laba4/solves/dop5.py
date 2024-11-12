import bson

from solves.ParserJSON import ParserJSON

# конвертация в бинарный файл bson


input_file = r'E:\wwwyssa_in_itmo\informatic\laba4\data\in.json'
with open(input_file, 'r', encoding='utf-8') as f:
    json_data = f.read()
parser = ParserJSON(json_data)
parsed_data = parser.parse()
bson_data = bson.dumps(parsed_data)

# Запись в BSON файл
with open('E:\wwwyssa_in_itmo\informatic\laba4\data\out_dop_5.bson', 'wb') as file:
    file.write(bson_data)

# https://mcraiha.github.io/tools/BSONhexToJSON/bsonfiletojson.html
# ссылка, чтобы просмотреть файл
