# парсит JSON в словарь
class ParserJSON:
    def __init__(self, input_string):
        self.input_string = input_string
        self.position = 0

    def parse(self):
        self.skip_whitespace()
        if self.position < len(self.input_string):
            return self.parse_value()
        else:
            raise ValueError("Empty input")

    def parse_value(self):
        self.skip_whitespace()
        char = self.peek()

        if char == '{':
            return self.parse_object()
        elif char == '[':
            return self.parse_array()
        elif char == '"':
            return self.parse_string()
        elif char.isdigit() or char == '-':
            return self.parse_number()
        elif self.input_string[self.position:self.position + 4] == "true":
            self.position += 4
            return True
        elif self.input_string[self.position:self.position + 5] == "false":
            self.position += 5
            return False
        elif self.input_string[self.position:self.position + 4] == "null":
            self.position += 4
            return None
        else:
            raise ValueError(f"in parse_value")

    def parse_object(self):
        obj = {}
        self.position += 1  # скипаем {
        self.skip_whitespace()

        if self.peek() == '}':
            self.position += 1
            return obj

        while True:
            self.skip_whitespace()
            key = self.parse_string()
            self.skip_whitespace()
            if self.peek() != ':':
                raise ValueError("in parse_object")
            self.position += 1  # скипаем :
            value = self.parse_value()
            obj[key] = value

            self.skip_whitespace()
            if self.peek() == '}':
                break
            elif self.peek() == ',':
                self.position += 1  # скипаем ,
            else:
                raise ValueError("in parse_object")

        self.position += 1  # скипаем }
        return obj

    def parse_array(self):
        array = []
        self.position += 1  # скипаем [
        self.skip_whitespace()

        if self.peek() == ']':
            self.position += 1
            return array

        while True:
            array.append(self.parse_value())
            self.skip_whitespace()
            if self.peek() == ']':
                break
            elif self.peek() == ',':
                self.position += 1  # скипаем ,
            else:
                raise ValueError(f"in parse_array")

        self.position += 1  # скипаем ]
        return array

    def parse_string(self):
        self.position += 1  # скипаем  "
        start = self.position
        while self.peek() != '"':
            self.position += 1
        result = self.input_string[start:self.position]
        self.position += 1  # скипаем "
        return result

    def parse_number(self):
        start = self.position
        flag = False
        while self.peek().isdigit() or self.peek() in '.-':
            self.position += 1
            if self.peek() in '.-':
                flag = True
        if flag:
            return float(self.input_string[start:self.position])
        return int(self.input_string[start:self.position])

    def peek(self):
        return self.input_string[self.position] if self.position < len(self.input_string) else ''

    def skip_whitespace(self):
        while self.peek() in ' \n\t\r':
            self.position += 1
