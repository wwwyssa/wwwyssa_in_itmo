import json
import xml.etree.ElementTree as ET


def convert(filename: str):
    with open(filename) as json_file:
        json_data = json.load(json_file)
        xml_data = json_to_xml(json_data)
    return xml_data


def json_to_xml(json_obj):
    root = ET.Element("root")

    def build_tree(element, data):
        if isinstance(data, dict):
            for key, value in data.items():
                sub_elem = ET.SubElement(element, key)
                build_tree(sub_elem, value)
        elif isinstance(data, list):
            for item in data:
                item_elem = ET.SubElement(element, "item")
                build_tree(item_elem, item)
        else:
            element.text = str(data)

    build_tree(root, json_obj)

    return ET.tostring(root, encoding="utf-8").decode("utf-8")


if __name__ == '__main__':
    input_file = r"E:\wwwyssa_in_itmo\informatic\laba4\data\in.json"
    with open(r"E:\wwwyssa_in_itmo\informatic\laba4\data\out_dop_1.xml", "w") as xml_file:
        xml_file.write(convert(input_file))
