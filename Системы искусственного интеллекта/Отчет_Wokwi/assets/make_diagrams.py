from pathlib import Path
import subprocess

root = Path(__file__).parent
style = '''@startuml
skinparam defaultFontName Arial
skinparam defaultFontSize 16
skinparam backgroundColor white
skinparam shadowing false
skinparam sequenceMessageAlign center
skinparam sequence {
  ArrowColor #243647
  LifeLineBorderColor #777777
  ParticipantBackgroundColor #F2F4F6
  ParticipantBorderColor #555555
}
hide footbox
'''
first = style + '''participant "Процессор\\nSTM32C031C6" as CPU
participant "GPIO A и B\\nКлавиатура" as KEY
participant "Таймер\\nSysTick" as T
participant "Индикатор\\nTM1637" as D
participant "USART2\\nКонсоль Wokwi" as U

group Инициализация
CPU -> KEY: Тактирование GPIO A/B; PA5 = 0
CPU -> U: PA2/PA3: AF1; BRR = 417; TE и UE
CPU -> T: LOAD = 47999; включить счёт и прерывания
CPU -> KEY: Строки: открытый сток;\\nстолбцы: входы с подтяжкой вверх
CPU -> D: PA6 = CLK; PA7 = DIO; инициализация
CPU -> CPU: A = 0; B = 0; операция не задана;\\nсброс признаков ввода
CPU -> D: Очистить все разряды
CPU -> U: Calculator ready; приглашение к вводу
end

group Прерывание таймера каждые 1 мс
T -> CPU: Вызов osSystickHandler
CPU -> CPU: tickCount = tickCount + 1
end

loop Главный цикл программы
CPU -> CPU: scanKeyboard()
opt tickCount - lastScanTime > 100 мс
CPU -> CPU: Обновить lastScanTime
loop Строки R1 ... R4 до обнаружения клавиши
CPU -> KEY: Выбранная строка = 0; короткая задержка
KEY --> CPU: Чтение GPIOB IDR столбцов C1 ... C4
CPU -> KEY: Отпустить выбранную строку
end
alt Есть новая клавиша, отличная от lastKey
CPU -> U: Pressed: символ
CPU -> CPU: Запомнить lastKey; processKey(символ)\\nПродолжение на рисунке 2
else Все клавиши отпущены
CPU -> CPU: lastKey = пустой символ
else Удерживается прежняя клавиша
CPU -> CPU: Новое событие не формируется
end
end
end
@enduml
'''
second = style + '''participant "Процессор\\nSTM32C031C6" as CPU
participant "Индикатор\\nTM1637" as D
participant "USART2\\nКонсоль Wokwi" as U

group Обработка события processKey
alt Нажата цифра 0 ... 9
CPU -> CPU: После результата начать новый ввод;\\nдобавить цифру к A или B, если число ≤ 9999
CPU -> U: A/B = число или Maximum operand: 9999
opt Цифра принята
ref over CPU,D: Передача сегментов операнда
end
else Нажата операция +, -, * или /
CPU -> CPU: При наличии A до результата сохранить операцию
CPU -> U: Operation: знак или Enter the first number
else Нажата клавиша = и результат ещё не показан
CPU -> CPU: Проверить A, операцию и B; вычислить сотые
alt Данных недостаточно
CPU -> U: Enter: number, operation, number, =
else Деление на ноль
ref over CPU,D: Передача сегментов Err
CPU -> CPU: resultShown = true
CPU -> U: Error: division by zero
else Выражение корректно
ref over CPU,D: Передача сегментов результата или Err
CPU -> CPU: resultShown = true
CPU -> U: Выражение и результат с двумя знаками
end
else Нажата точка или повторное = после результата
CPU -> CPU: Состояние калькулятора не изменяется
end
end

group Передача подготовленных сегментов при обновлении экрана
CPU -> D: PA6/PA7: START; 0x40; STOP\\nАвтоматическое увеличение адреса
CPU -> D: START; 0xC0; 4 байта сегментов; STOP\\nЗапись четырёх разрядов слева направо
CPU -> D: START; 0x8F; STOP\\nВключение; максимальная яркость
end
@enduml
'''

for name, source in [('sequence_1', first), ('sequence_2', second)]:
    (root / (name + '.puml')).write_text(source, encoding='utf-8')
subprocess.run(['java', '-Djava.awt.headless=true', '-jar', str(root/'plantuml.jar'), '-charset', 'UTF-8', str(root/'sequence_1.puml'), str(root/'sequence_2.puml')], check=True)
