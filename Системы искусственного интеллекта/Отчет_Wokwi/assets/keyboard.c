#include "keyboard.h"

void initKeyboard(void) {
  RCC->AHBENR |= RCC_AHBENR_GPIOAEN | RCC_AHBENR_GPIOBEN;
  GPIOB->BSRR = (1U << 7) | (1U << 6) | (1U << 3);
  GPIOA->BSRR = (1U << 10);

  GPIOB->OTYPER |= (1U << 7) | (1U << 6) | (1U << 3);
  GPIOA->OTYPER |= (1U << 10);

  GPIOB->PUPDR &= ~(
    (3U << (7 * 2)) |
    (3U << (6 * 2)) |
    (3U << (3 * 2))
  );
  GPIOA->PUPDR &= ~(3U << (10 * 2));

  GPIOB->MODER = (GPIOB->MODER & ~(
    (3U << (7 * 2)) |
    (3U << (6 * 2)) |
    (3U << (3 * 2))
  )) |
    (1U << (7 * 2)) |
    (1U << (6 * 2)) |
    (1U << (3 * 2));

  GPIOA->MODER = (GPIOA->MODER & ~(3U << (10 * 2)))
                | (1U << (10 * 2));


  GPIOB->MODER &= ~(
    (3U << (10 * 2)) |
    (3U << (4 * 2))  |
    (3U << (5 * 2))  |
    (3U << (8 * 2))
  );

  GPIOB->PUPDR = (GPIOB->PUPDR & ~(
    (3U << (10 * 2)) |
    (3U << (4 * 2))  |
    (3U << (5 * 2))  |
    (3U << (8 * 2))
  )) |
    (1U << (10 * 2)) |
    (1U << (4 * 2))  |
    (1U << (5 * 2))  |
    (1U << (8 * 2));

  lastKey = '\0';
  lastScanTime = 0;
}

char readKey() {
  const uint8_t rows[] = {7, 6, 10, 3};    
  const uint8_t cols[] = {10, 4, 5, 8};
  char keymap[4][4] = {
    {'1', '2', '3', '+'},
    {'4', '5', '6', '-'},
    {'7', '8', '9', '*'},
    {'.', '0', '=', '/'}
  };

  for (uint8_t i = 0; i < 4; i++) {
    if (rows[i] != 10) {
      GPIOB->BSRR = (1 << (rows[i] + 16));
    } else {
      GPIOA->BSRR = (1 << (rows[i] + 16));
    }
    for (volatile int d = 0; d < 100; d++);

    for (uint8_t j = 0; j < 4; j++) {
      if ((GPIOB->IDR & (1 << cols[j])) == 0) {
        if (rows[i] != 10) {
          GPIOB->BSRR = (1 << rows[i]);
        } else {
          GPIOA->BSRR = (1 << rows[i]);
        }
        return keymap[i][j];
      }
    }

    if (rows[i] != 10) {
      GPIOB->BSRR = (1 << rows[i]);
    } else {
      GPIOA->BSRR = (1 << rows[i]);
    }
  }

  return '\0';
}

char scanKeyboard() { 
  // Сканируем клавиатуру каждые 100мс 
  if (tickCount - lastScanTime > 100) { 
    lastScanTime = tickCount; 
    char currentKey = readKey(); 
 
    if (currentKey != '\0' && currentKey != lastKey) { 
      printf("Pressed: %c\n", currentKey); 
      lastKey = currentKey;       
      return currentKey; 
    } else if (currentKey == '\0') { 
      lastKey = '\0'; 
    } 
  } 
  return '\0';

}