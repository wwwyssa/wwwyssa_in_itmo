input_file = r'E:\wwwyssa_in_itmo\informatic\laba4\data\in.json'


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
            key = s[1:s[1:].index('"') + 1]
            if '{' not in s:
                value = s[s.index(':') + 2:]
                value = value.replace(',', '')
                if '"' in value:
                    value = value[1:-1]
                result += f"<{key}>{value}</{key}>"
        if '{' in s:
            key = s[1:s[1:].index('"') + 1]
            result += f"<{key}>"
            stack.append(key)
        if '}' in s:
            result += f'</{stack[-1]}>'
            stack.pop()
    return "<root>" + result + "</root>"


if __name__ == '__main__':
    with open(r'E:\wwwyssa_in_itmo\informatic\laba4\data\out.xml', 'w', encoding='utf-8') as f:
        f.write(json_to_xml(input_file))
