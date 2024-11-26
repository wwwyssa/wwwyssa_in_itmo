import bson

from solves.ParserJSON import ParserJSON

input_file = r'E:\wwwyssa_in_itmo\informatic\laba4\data\in.json'
with open(input_file, 'r', encoding='utf-8') as f:
    json_data = f.read()
parser = ParserJSON(json_data)
parsed_data = parser.parse()
bson_data = bson.dumps(parsed_data)

with open('E:\wwwyssa_in_itmo\informatic\laba4\data\out_dop_5.bson', 'wb') as file:
    file.write(bson_data)

# https://mcraiha.github.io/tools/BSONhexToJSON/bsonfiletojson.html
# ссылка, чтобы просмотреть файл
