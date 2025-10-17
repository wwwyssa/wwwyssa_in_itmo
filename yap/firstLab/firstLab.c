#include <stdio.h>
#include <stdlib.h>

const int ALPH_SIZE = 26;

char letterCrypt(char letter, int shift, int alph_size, char StartLetter) {
        return (letter - StartLetter + shift) % alph_size + StartLetter;
}

void encrypt(char* word, int shift, int alph_size) {
    shift = (shift % alph_size + alph_size) % alph_size;
    for(int i =0; word[i] != '\0'; ++i){
       char curChar = word[i];
       word[i] = letterCrypt(curChar, shift, alph_size, (curChar >= 'a' && curChar <= 'z') ? 'a' : 'A');
    }
}


void decrypt(char *word, int shift, int alph_size) {
    shift = (shift % alph_size + alph_size) % alph_size;
    for (int i = 0; word[i] != '\0'; i++) {
        char c = word[i];
        if (c >= 'a' && c <= 'z')
            word[i] = ((c - 'a' - shift + alph_size) % alph_size) + 'a';
        else if (c >= 'A' && c <= 'Z')
            word[i] = ((c - 'A' - shift + alph_size) % alph_size) + 'A';
    }
}



int main(int argc, char* argv[]) {
    
    if (argc != 3){
        return 2;
    }

    char* word = argv[1];
    int shift = atoi(argv[2]);
    

    
    encrypt(word, shift, ALPH_SIZE);
    
    printf("Result: %s", word);

    decrypt(word, shift, ALPH_SIZE);

    printf("\nDecrypted: %s\n", word);
}
