from solves.ParserJSON import ParserJSON


def dict_to_xml(data: dict, root_element: str):
    def dict_to_xml_recursive(data: dict):
        xml_string = ""
        for key, value in data.items():
            if isinstance(value, dict):
                xml_string += f"<{key}>{dict_to_xml_recursive(value)}</{key}>"
            elif isinstance(value, list):
                for item in value:
                    xml_string += f"<{key}>{dict_to_xml_recursive(item)}</{key}>"
            else:
                xml_string += f"<{key}>{value}</{key}>"
        return xml_string

    xml_content = f"<{root_element}>{dict_to_xml_recursive(data)}</{root_element}>"
    return xml_content


def convert(filename: str):
    with open(filename, 'r', encoding='utf-8') as f:
        json_data = f.read()
    parser = ParserJSON(json_data)
    parsed_data = parser.parse()
    xml = dict_to_xml(parsed_data, 'root')
    return xml


if __name__ == '__main__':
    input_file = r'E:\wwwyssa_in_itmo\informatic\laba4\data\in.json'
    with open(r'E:\wwwyssa_in_itmo\informatic\laba4\data\out_dop_3.xml', 'w', encoding='utf-8') as f:
        f.write(convert(input_file))
