input_file = r'E:\wwwyssa_in_itmo\informatic\laba4\data\in.json'
import re


def json_to_xml(filename: str):
    with open(filename, 'r', encoding='utf-8') as f:
        data = f.read()
    data = data[1:-1]
    data = data.split('\n')
    result = ""
    stack = []
    for e in data:
        s = e.strip()
        if len(s) == 0:
            continue
        if s[0] == '"':
            key = re.search(r'"\S*":', s).group()[1:-2]
            if '{' not in s:
                value = re.search(r': "(.*)"|(?<=:) (true)|(?<=:) (false)|(?<=:) (\d+)', s)
                if value:
                    value = value.group()
                    value = re.sub(r': "', r'', value)
                    if '"' in value:
                        value = re.sub('"', '', value)
                    pattern = "<key>value</key>"
                    tmp_str = re.sub("key", key, pattern)
                    tmp_str = re.sub("value", value, tmp_str)
                    result += tmp_str
        if '{' in s:
            key = s[1:s[1:].index('"') + 1]
            result += f"<{key}>"
            stack.append(key)
        if '}' in s:
            result += f'</{stack[-1]}>'
            stack.pop()
    return "<root>" + result + "</root>"


if __name__ == '__main__':
    with open(r'E:\wwwyssa_in_itmo\informatic\laba4\data\out_dop_2.xml', 'w', encoding='utf-8') as f:
        f.write(json_to_xml(input_file))
