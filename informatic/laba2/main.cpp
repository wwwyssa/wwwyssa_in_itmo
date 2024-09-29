#include <iostream>
#include <vector>
#include <string>
using namespace std;

int check_input(string input) {
    if (input.length() != 7) {
        return 0;
    }
    for (char c : input) {
        if (!(c == '0' || c == '1')) {
            return 0;
        }
    }
    return 1;
}

bool check(int r, int a, int b, int c) {
    return r ^ a ^ b ^ c;
}

string correct_message(int wrong_bit, string input) {
    if (input[wrong_bit-1] == '1') {
        input[wrong_bit-1] = '0';
    }else {
        input[wrong_bit-1] = '1';
    }
    return input;
}

void print_answer(string answer) {
    cout << answer[2] << answer[4] << answer[5] << answer[6];
}

int main() {
    string input;
    cin >> input;
    if (!check_input(input)) {
        cout << "Wrong input" << endl;
        return 0;
    }
    if (input.length() > 7) {
        cout << "Incorrect input" << endl;
    }
    vector<int> v(7);
    for(int i = 0; i < 7; i++) {
        v[i] = input[i] - '0';
    }
    int s1 = 0, s2 = 0, s3 = 0;
    s1 = check(v[0], v[2], v[4], v[6]);
    s2 = check(v[1], v[2], v[5], v[6]);
    s3 = check(v[3], v[4], v[5], v[6]);
    int wrong_bit = s3 * 2 * 2 + s2 * 2 + s1;
    cout << s1 << s2 << s3 << wrong_bit << endl;
    string answer = correct_message(wrong_bit, input);
    print_answer(answer);
    return 0;
}
//0100011
//1111101
//1001000
//0001100
