#include "tm1637.h"
#include "keyboard.h"
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>

// Эти переменные используются в keyboard.c и tm1637.c
volatile uint32_t tickCount = 0;
uint32_t last_display_update = 0;
uint16_t counter = 0;
char lastKey = '\0';
uint32_t lastScanTime = 0;

// Состояние калькулятора
static int operandA = 0;
static int operandB = 0;
static char operation = '\0';

static bool hasA = false;
static bool hasB = false;
static bool resultShown = false;

// Коды сегментов цифр 0–9
static const uint8_t digitSegments[10] = {
    0x3F, 0x06, 0x5B, 0x4F, 0x66,
    0x6D, 0x7D, 0x07, 0x7F, 0x6F
};

void osSystickHandler(void) {
    tickCount++;
}

void initGPIO(void) {
    RCC->AHBENR |= RCC_AHBENR_GPIOAEN
                | RCC_AHBENR_GPIOBEN;

    // PA5 — выход для светодиода
    GPIOA->MODER =
        (GPIOA->MODER & ~(3U << 10)) | (1U << 10);

    GPIOA->OTYPER &= ~(1U << 5);
    GPIOA->OSPEEDR |= (1U << 10);

    // Выключаем светодиод
    GPIOA->BSRR = 1U << (5 + 16);
}

void initUSART2(void) {
    RCC->APB1ENR |= RCC_APB1ENR_USART2EN;

    // PA2 и PA3 — альтернативная функция USART2
    GPIOA->MODER =
        (GPIOA->MODER & ~(0xFU << 4)) | (0xAU << 4);

    GPIOA->AFR[0] =
        (GPIOA->AFR[0] & ~(0xFFU << 8))
        | (1U << 8)
        | (1U << 12);

    USART2->BRR = 417; // 48 МГц / 115200
    USART2->CR1 = USART_CR1_TE | USART_CR1_UE;
}

void initSysTick(void) {
    SysTick->LOAD = 47999; // 1 мс при 48 МГц
    SysTick->VAL = 0;
    SysTick->CTRL = (1U << 2) | (1U << 1) | 1U;
}

int _write(int file, uint8_t *ptr, int len) {
    (void)file;

    for (int i = 0; i < len; i++) {
        while (!(USART2->ISR & USART_ISR_TXE)) {
        }

        USART2->TDR = ptr[i];
    }

    return len;
}

// Отправка четырёх разрядов слева направо.
// Используются существующие функции из tm1637.c.
static void displaySegments(const uint8_t data[4]) {
    tm1637_start();
    tm1637_write_byte(0x40); // Автоинкремент адреса
    tm1637_stop();

    tm1637_start();
    tm1637_write_byte(0xC0); // Первый разряд

    for (uint8_t i = 0; i < 4; i++) {
        tm1637_write_byte(data[i]);
    }

    tm1637_stop();

    tm1637_start();
    tm1637_write_byte(0x8F); // Включить, максимальная яркость
    tm1637_stop();
}

static void displayError(void) {
    const uint8_t data[4] = {
        0x79, // E
        0x50, // r
        0x50, // r
        0x00
    };

    displaySegments(data);
}

// Число передаётся в сотых:
// 350 = 3.50, -700 = -7.00, 8100 = 81.00.
// Функция рассчитана на результаты операций с цифрами 0–9.
static void displayResult(int64_t result100) {
    uint8_t data[4] = {0, 0, 0, 0};

    bool negative = result100 < 0;
    int64_t magnitude = negative ? -result100 : result100;

    int64_t whole = magnitude / 100;
    int fraction = (int)(magnitude % 100);

    // Два разряда до двоеточия, два после.
    // При отрицательном числе один разряд занимает минус.
    bool fractionFits = negative ? whole <= 9 : whole <= 99;

    if (fraction != 0 && fractionFits) {
        if (negative) {
            data[0] = 0x40;
        } else if (whole >= 10) {
            data[0] = digitSegments[(int)(whole / 10)];
        }

        data[1] = digitSegments[(int)(whole % 10)] | 0x80;
        data[2] = digitSegments[fraction / 10];
        data[3] = digitSegments[fraction % 10];

        displaySegments(data);
        return;
    }

    // Если дробь не помещается, округляем до целого
    int64_t integer = (magnitude + 50) / 100;

    if ((!negative && integer > 9999) ||
        (negative && integer > 999)) {
        displayError();
        printf("Result does not fit the display\n");
        return;
    }

    int position = 3;
    int64_t remaining = integer;

    do {
        data[position--] =
            digitSegments[(int)(remaining % 10)];

        remaining /= 10;
    } while (remaining > 0);

    if (negative && integer != 0) {
        data[position] = 0x40;
    }

    displaySegments(data);
}
static void resetCalculator(void) {
    operandA = 0;
    operandB = 0;
    operation = '\0';

    hasA = false;
    hasB = false;
    resultShown = false;
}

static void calculate(void) {
    if (!hasA || operation == '\0' || !hasB) {
        printf("Enter: number, operation, number, =\n");
        return;
    }

    // 64 бита нужны, например, для 9999 * 9999,
    // если результат хранится в сотых.
    int64_t result100 = 0;

    switch (operation) {
        case '+':
            result100 = ((int64_t)operandA + operandB) * 100;
            break;

        case '-':
            result100 = ((int64_t)operandA - operandB) * 100;
            break;

        case '*':
            result100 = (int64_t)operandA * operandB * 100;
            break;

        case '/':
            if (operandB == 0) {
                displayError();
                printf("Error: division by zero\n");
                resultShown = true;
                return;
            }

            result100 =
                ((int64_t)operandA * 100 + operandB / 2)
                / operandB;
            break;

        default:
            return;
    }

    displayResult(result100);
    resultShown = true;

    int64_t magnitude = result100 < 0
                        ? -result100
                        : result100;

    // Для операндов 0–9999 целая часть помещается
    // в unsigned long на этой платформе.
    printf(
        "%d %c %d = %s%lu.%02lu\n",
        operandA,
        operation,
        operandB,
        result100 < 0 ? "-" : "",
        (unsigned long)(magnitude / 100),
        (unsigned long)(magnitude % 100)
    );
}

static void processKey(char key) {
    if (key >= '0' && key <= '9') {
        if (resultShown) {
            resetCalculator();
        }

        int digit = key - '0';

        // Выбираем, какой операнд сейчас вводится
        int *operand = (operation == '\0')
                       ? &operandA
                       : &operandB;

        // Проверяем ограничение ДО добавления цифры
        if (*operand > (9999 - digit) / 10) {
            printf("Maximum operand: 9999\n");
            return;
        }

        // Например: было 12, нажали 3 -> получаем 123
        *operand = *operand * 10 + digit;

        if (operation == '\0') {
            hasA = true;
            printf("A = %d\n", operandA);
        } else {
            hasB = true;
            printf("B = %d\n", operandB);
        }

        tm1637_display_number(*operand);
        return;
    }

    if (key == '+' || key == '-' ||
        key == '*' || key == '/') {

        if (!hasA || resultShown) {
            printf("Enter the first number\n");
            return;
        }

        operation = key;
        printf("Operation: %c\n", operation);
        return;
    }

    if (key == '=') {
        if (!resultShown) {
            calculate();
        }

        return;
    }

    // Кнопка '.' здесь не используется:
    // операнды вводятся как целые числа
}


int main(void) {
    initGPIO();
    initUSART2();
    initSysTick();
    initKeyboard();
    tm1637_init();

    resetCalculator();
    tm1637_clear();

    printf("Calculator ready\n");
    printf("Enter: digit, + - * /, digit, =\n");

    while (1) {
        char key = scanKeyboard();

        if (key != '\0') {
            processKey(key);
        }
    }
}