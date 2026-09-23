from pathlib import Path
from html import escape
import base64
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Image, Table, TableStyle, PageBreak, KeepTogether
from reportlab.lib import colors
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY, TA_LEFT
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.lib.units import mm
from PIL import Image as PILImage
import pypdfium2 as pdfium

ROOT = Path(__file__).resolve().parent.parent
A = ROOT/'assets'
for name, filename in [('Times','times.ttf'),('Times-Bold','timesbd.ttf'),('Times-Italic','timesi.ttf')]:
    pdfmetrics.registerFont(TTFont(name, str(Path('C:/Windows/Fonts')/filename)))
pdfmetrics.registerFontFamily('Times', normal='Times', bold='Times-Bold', italic='Times-Italic', boldItalic='Times-Bold')
styles = {
 'body': ParagraphStyle('body',fontName='Times',fontSize=12,leading=16.5,alignment=TA_JUSTIFY,spaceAfter=8),
 'h1': ParagraphStyle('h1',fontName='Times-Bold',fontSize=15,leading=19,spaceAfter=13),
 'h2': ParagraphStyle('h2',fontName='Times-Bold',fontSize=12.5,leading=17,spaceBefore=8,spaceAfter=7),
 'caption': ParagraphStyle('caption',fontName='Times',fontSize=11,leading=14,alignment=TA_CENTER,spaceBefore=7,spaceAfter=10),
 'small': ParagraphStyle('small',fontName='Times',fontSize=10.5,leading=13.5,spaceAfter=7),
 'cell': ParagraphStyle('cell',fontName='Times',fontSize=10.5,leading=13),
 'center': ParagraphStyle('center',fontName='Times',fontSize=14,leading=21,alignment=TA_CENTER,spaceAfter=8),
 'title': ParagraphStyle('title',fontName='Times-Bold',fontSize=19,leading=26,alignment=TA_CENTER,spaceAfter=16),
}
pages=[]
def page(*items): pages.append(list(items))
def p(t,style='body'): return ('p',t,style)
def h(t):return p(t,'h1')
def sub(t):return p(t,'h2')
def img(file,width,caption):return ('img',file,width,caption)
def tbl(rows,widths): return ('table',rows,widths)
def gap(v):return ('space',v)

page(
 p('[Название образовательной организации]','center'),
 p('[Институт или факультет]','center'),
 p('[Кафедра или образовательная программа]','center'),gap(65),
 p('ОТЧЁТ ПО ЛАБОРАТОРНОЙ РАБОТЕ','title'),
 p('Калькулятор на микроконтроллере STM32\nс матричной клавиатурой и индикатором TM1637','title'),
 p('Дисциплина: [название дисциплины]','center'),
 p('Лабораторная работа № [номер]','center'),
 p('Вариант № [номер варианта]','center'),gap(60),
 p('Выполнил(и): [ФИО исполнителя или исполнителей]'),
 p('Группа: [номер группы]'),
 p('Проверил: [ФИО преподавателя]'),gap(100),
 p('[Город] — 2026','center')
)

page(
 h('2 Цель и задачи работы'),
 p('Цель работы — реализовать и исследовать взаимодействие процессора STM32C031C6 с матричной клавиатурой, четырёхразрядным индикатором TM1637 и последовательным интерфейсом USART2 на примере калькулятора в симуляторе Wokwi.'),
 p('Задачи работы: настроить GPIO, USART2 и системный таймер SysTick; организовать опрос клавиатуры; реализовать ввод целых операндов и четыре арифметические операции; обеспечить вывод результата и сообщений об ошибках; проверить работу устройства в симуляторе.'),
 sub('2.1 Состав устройства'),
 p('Используется плата NUCLEO-C031C6 с микроконтроллером STM32C031C6 на базе ядра ARM Cortex-M0+ с частотой 48 МГц [2]. Периферия проекта включает клавиатуру 4 × 4, индикатор TM1637, внешний зелёный светодиод и резистор 1 кОм. Схема и исходные файлы размещены в проекте Wokwi [1].'),
 sub('2.2 Подключение периферии'),
 tbl([
 ['Устройство или сигнал','Выводы STM32','Назначение'],
 ['Клавиатура R1, R2, R3, R4','PB7, PB6, PA10, PB3','Выбор строки при опросе'],
 ['Клавиатура C1, C2, C3, C4','PB10, PB4, PB5, PB8','Чтение столбцов'],
 ['TM1637 CLK / DIO','PA6 / PA7','Тактирование / данные'],
 ['TM1637 VCC / GND','3V3 / GND','Питание индикатора'],
 ['USART2 TX / RX','PA2 / PA3','Связь с Serial Monitor'],
 ['Внешний светодиод','PA5 → LED → 1 кОм → GND','В программе выключен'],
 ],[48,55,62]),
 p('PA3 соединён с передатчиком Serial Monitor, однако программа включает только передачу USART2. Ввод выражения выполняется с клавиатуры, а не через консоль. Светодиод PA5 и функция счётчика tm1637_update в вычислениях не используются.','small')
)

page(h('3 Схема взаимодействия процессора и периферии'),
 img('sequence_1.png',144,'Рисунок 1 — Инициализация, отсчёт времени и опрос клавиатуры'),
 p('Прерывания SysTick продолжаются в течение работы программы. На схеме показан один типовой вызов обработчика; процессор возвращается из него к прерванному участку программы.','small')
)
page(h('3 Схема взаимодействия процессора и периферии'),
 img('sequence_2.png',144,'Рисунок 2 — Обработка клавиши и обмен с индикатором'),
 p('Рамка «Передача сегментов» раскрыта в нижней части рисунка. Она вызывается только при обновлении операнда, результата или сообщения Err; при вводе операции экран сохраняет прежнее число.','small')
)

page(h('4 Описание процессов на диаграмме'),
 sub('4.1 Инициализация и отсчёт времени'),
 p('После запуска процессор разрешает тактирование портов GPIOA и GPIOB и задаёт низкий уровень PA5. Для USART2 выводы PA2 и PA3 переводятся в альтернативную функцию AF1. Значение BRR = 417 задаёт скорость около 115200 бод при частоте 48 МГц. Устанавливаются биты разрешения USART и передатчика.'),
 p('SysTick получает значение LOAD = 47999. При частоте 48 МГц это соответствует периоду 1 мс. Обработчик osSystickHandler увеличивает tickCount. После настройки клавиатуры и индикатора состояние калькулятора сбрасывается, разряды очищаются, в консоль выводятся сообщение готовности и подсказка.'),
 sub('4.2 Сканирование клавиатуры'),
 p('Строки клавиатуры настроены как выходы с открытым стоком, столбцы — как входы с подтяжкой к питанию. Процессор поочерёдно активирует строку низким уровнем и считывает GPIOB IDR. Нулевой уровень столбца указывает на замкнутую клавишу. После чтения строка освобождается, а пара «строка–столбец» преобразуется в символ.'),
 p('Опрос выполняется при разности tickCount и lastScanTime больше 100, то есть не чаще чем примерно раз в 101 мс. Новое событие выдаётся, если символ отличается от lastKey. При отпускании всех клавиш lastKey очищается. Такой механизм подавляет повтор одного удерживаемого символа, но не является полноценной проверкой устойчивости контактов. Для повторного ввода одной цифры необходимо отпустить клавишу между нажатиями.'),
 sub('4.3 Ввод выражения и вычисление'),
 p('Вводимые цифры последовательно дописываются к A до выбора операции и к B после него. До добавления цифры проверяется предел 9999; лишняя цифра игнорируется. Флаги hasA и hasB различают отсутствие ввода и введённый ноль. Клавиши +, −, * и / задают операцию, а = запускает вычисление при наличии всех данных.'),
 p('Результат хранится как целое 64-разрядное значение в сотых. Сложение, вычитание и умножение дают точное целое значение, умноженное на 100. При делении положительных целых операндов результат округляется до сотых. Нулевой делитель обрабатывается отдельно: индикатор показывает Err, консоль сообщает Error: division by zero.'),
 p('После результата или ошибки деления на ноль следующая цифра начинает новое выражение. Повторное = игнорируется; операция после результата не продолжает предыдущий расчёт. Клавиша точки не изменяет состояние калькулятора.')
)

page(h('4 Описание процессов на диаграмме'),
 sub('4.4 Формирование изображения на TM1637'),
 p('Связь с TM1637 реализована программным переключением GPIO: PA6 формирует CLK, PA7 передаёт DIO. Это собственный двухпроводный протокол TM1637, несовместимый с I²C [3]. Передача включает START, команду 0x40 для автоматического увеличения адреса, STOP; затем START, адрес 0xC0, четыре байта сегментов и STOP; в конце передаётся команда 0x8F для включения индикатора на максимальной яркости.'),
 p('Биты каждого байта передаются младшим вперёд. После восьми битов формируется такт подтверждения и линия DIO освобождается. В данной реализации значение ACK не считывается, поэтому подтверждение приёма и отсутствие устройства программой не контролируются.'),
 p('При вводе операнд отображается без ведущих нулей. Если результат имеет ненулевую дробную часть и помещается в формат, два правых разряда показывают сотые; центральное двоеточие играет роль десятичного разделителя. Например, 3:50 означает 3,50. Для дробного положительного результата доступны две цифры целой части, для отрицательного — одна цифра и знак минус.'),
 p('Если дробный формат не помещается, значение на индикаторе округляется до целого. Целый результат допустим в пределах от −999 до 9999; вне этого диапазона выводится Err и сообщение Result does not fit the display. В консоли сохраняется результат с двумя знаками после точки. Например, 1234 × 9 даёт 11106.00 в консоли и Err на индикаторе.'),
 sub('4.5 Передача диагностических сообщений'),
 p('Функция printf передаёт символы через переопределённую функцию _write. Для каждого символа процессор ожидает флаг готовности TXE и записывает байт в USART2 TDR. В Serial Monitor отображаются нажатия клавиш, ввод A и B, выбранная операция, результат и сообщения об ошибках. Передача выполняется с ожиданием готовности, без DMA.'),
 sub('4.6 Состояния калькулятора'),
 tbl([
 ['Состояние','Действие пользователя','Переход'],
 ['Ввод A','Цифра / операция','Дополнение A / переход к B'],
 ['Ввод B','Цифра / =','Дополнение B / расчёт'],
 ['Результат или Err','Цифра','Сброс и ввод нового A'],
 ['Незавершённый ввод','Перезапуск симуляции','Полная очистка состояния'],
 ],[48,61,56]),
 p('Отдельная клавиша очистки не предусмотрена. Смена знака операции до вычисления возможна, но уже введённое B при этом не очищается. Поэтому для полного исправления незавершённого выражения следует перезапустить симуляцию.','small')
)

page(h('5 Руководство пользователя'),
 sub('5.1 Запуск и подготовка'),
 p('Открыть проект по ссылке из раздела 6 и нажать зелёную кнопку Start the simulation. После запуска в Serial Monitor должны появиться строки Calculator ready и Enter: digit, + - * /, digit, =. Индикатор очищен и ожидает ввода. Несмотря на слово digit в подсказке, программа поддерживает многозначные целые операнды.'),
 img('01_ready.png',156,'Рисунок 3 — Схема устройства и сообщение готовности'),
 sub('5.2 Порядок ввода'),
 p('Нажимать виртуальные клавиши в порядке: первый операнд → операция → второй операнд → =. Нажатие следует удерживать дольше одного периода опроса, например около 0,2 с. Между повторениями одинаковой клавиши нужно отпускать её также примерно на 0,2 с. Это помогает избежать пропуска короткого нажатия.'),
 p('Для нового расчёта после результата достаточно ввести первую цифру следующего числа. Для сброса незавершённого ввода используется Restart the simulation. Кнопка Stop завершает симуляцию.','small')
)

page(h('5 Руководство пользователя'),
 sub('5.3 Сложение и ввод многозначного числа'),
 p('Последовательно нажать 1, 2, +, 3, =. При вводе первых двух цифр формируется A = 12; после выбора операции вводится B = 3. Индикатор показывает 15, в консоли выводится строка 12 + 3 = 15.00.'),
 img('02_add.png',165,'Рисунок 4 — Проверка сложения 12 + 3'),
 p('После завершения выражения нажать цифру нового операнда. Предыдущие A, B и операция будут сброшены автоматически. При выборе операции сам знак не выводится на семисегментный индикатор; его можно проверить по строке Operation в консоли.'),
 p('Вычитание и умножение вводятся таким же способом. В ходе проверки получены результаты 2 − 9 = −7.00 и 9 × 9 = 81.00. Соответствующие значения на индикаторе: −7 и 81.')
)

page(h('5 Руководство пользователя'),
 sub('5.4 Деление и отображение дробной части'),
 p('После предыдущего результата нажать 7, /, 2, =. На индикаторе отображается 3:50, а в консоли — 7 / 2 = 3.50. Двоеточие разделяет целую часть и сотые и не обозначает время.'),
 img('03_fraction.png',165,'Рисунок 5 — Дробный результат 7 / 2 = 3,50'),
 p('Ввод десятичных операндов не предусмотрен: клавиша точки не выполняет действие. Дробная часть появляется только в результате деления. Если число не помещается в дробный формат индикатора, на нём выводится округлённое целое значение; результат с сотыми следует смотреть в консоли.'),
 p('Пример поведения по алгоритму: для 9999 / 2 внутренний результат равен 4999,50, в консоли будет 4999.50, а на четырёхразрядном индикаторе — 5000. Этот пример иллюстрирует правило форматирования и не входит в приведённый ниже перечень выполненных испытаний.','small')
)

page(h('5 Руководство пользователя'),
 sub('5.5 Ошибки и ограничения ввода'),
 p('Для проверки деления на ноль нажать 8, /, 0, =. Индикатор показывает Err, а консоль — Error: division by zero. Для восстановления работы достаточно ввести первую цифру нового выражения.'),
 img('06_divzero.png',165,'Рисунок 6 — Обработка деления на ноль'),
 p('Попытка ввести 12345 останавливается на числе 1234: пятая цифра отклоняется, а консоль сообщает Maximum operand: 9999. Индикатор сохраняет допустимый операнд.'),
 p('При вычислении 1234 × 9 результат превышает вместимость индикатора. На экране появляется Err, в консоли — Result does not fit the display и 1234 * 9 = 11106.00. Это ошибка отображения, а не ошибка арифметического вычисления.')
)

page(h('5 Руководство пользователя'),
 sub('5.6 Результаты проверки в Wokwi'),
 p('Перечисленные сценарии выполнены в симуляторе на исходном проекте. Между завершёнными выражениями новый ввод начинался с цифры; строки консоли использовались для проверки результата.'),
 tbl([
 ['Проверка','Последовательность','Наблюдаемый результат'],
 ['Запуск','Start the simulation','Calculator ready; разряды очищены'],
 ['Многозначный ввод и сложение','1 2 + 3 =','15; 12 + 3 = 15.00'],
 ['Дробное деление','7 / 2 =','3:50; 7 / 2 = 3.50'],
 ['Отрицательный результат','2 − 9 =','−7; 2 - 9 = -7.00'],
 ['Умножение','9 * 9 =','81; 9 * 9 = 81.00'],
 ['Деление на ноль','8 / 0 =','Err; Error: division by zero'],
 ['Ограничение операнда','1 2 3 4 5','1234; Maximum operand: 9999'],
 ['Переполнение индикатора','После 1234: * 9 =','Err; предупреждение и 11106.00'],
 ],[46,43,76]),
 p('Проверка подтверждает работу четырёх операций, накопления многозначного ввода, вывода дробных и отрицательных результатов, а также обработки ограничений. Поведение физической платы и электрические параметры за пределами симулятора в рамках этих испытаний не проверялись.','small'),
 h('6 Ссылка на репозиторий с кодом'),
 p('Репозиторий: [вставить ссылку на GitHub или GitLab].'),
 p('Рабочий проект Wokwi с исходными файлами и схемой:'),
 p('<link href="https://wokwi.com/projects/475321255208815617" color="#174A78">https://wokwi.com/projects/475321255208815617</link>','small'),
 p('Основные файлы проекта: main.c и main.h; keyboard.c и keyboard.h; tm1637.c и tm1637.h; diagram.json. Исходный код в текст отчёта не включён.','small'),
 sub('Использованные источники'),
 p('[1] Проект Wokwi. Адрес указан выше.','small'),
 p('[2] <link href="https://docs.wokwi.com/parts/board-st-nucleo-c031c6" color="#174A78">Wokwi Docs — STM32 Nucleo64 C031C6</link>.','small'),
 p('[3] <link href="https://docs.wokwi.com/parts/wokwi-tm1637-7segment" color="#174A78">Wokwi Docs — TM1637 7-segment display</link>.','small'),
 p('[4] <link href="https://docs.wokwi.com/parts/wokwi-membrane-keypad" color="#174A78">Wokwi Docs — Membrane keypad</link>.','small'),
 p('[5] <link href="https://plantuml.com/ru/sequence-diagram" color="#174A78">PlantUML — Диаграмма последовательностей</link>.','small')
)

def as_para(txt, sty): return Paragraph(txt.replace('\n','<br/>'),styles[sty])
story=[]
W=165*mm
for pn,items in enumerate(pages):
    if pn: story.append(PageBreak())
    for item in items:
        if item[0]=='p': story.append(as_para(item[1],item[2]))
        elif item[0]=='space': story.append(Spacer(1,item[1]))
        elif item[0]=='img':
            _,file,width,cap=item
            w,h0=PILImage.open(A/file).size
            picture=Image(str(A/file),width=width*mm,height=width*mm*h0/w)
            story.append(KeepTogether([picture,as_para(cap,'caption')]))
        else:
            _,rows,widths=item
            t=Table([[as_para(escape(c),'cell') for c in r] for r in rows],colWidths=[v*mm for v in widths],repeatRows=1,hAlign='CENTER')
            t.setStyle(TableStyle([('BACKGROUND',(0,0),(-1,0),colors.HexColor('#E8EDF1')),('GRID',(0,0),(-1,-1),0.5,colors.HexColor('#D9D9D9')),('VALIGN',(0,0),(-1,-1),'MIDDLE'),('LEFTPADDING',(0,0),(-1,-1),7),('RIGHTPADDING',(0,0),(-1,-1),7),('TOPPADDING',(0,0),(-1,-1),6),('BOTTOMPADDING',(0,0),(-1,-1),6)]))
            story.extend([t,Spacer(1,9)])

def footer(canvas,doc):
    if doc.page>1:
        canvas.setFont('Times',10)
        canvas.drawCentredString(105*mm,13*mm,str(doc.page))

target=ROOT/'Отчет_калькулятор_STM32.pdf'
doc=SimpleDocTemplate(str(target),pagesize=(210*mm,297*mm),rightMargin=20*mm,leftMargin=25*mm,topMargin=18*mm,bottomMargin=20*mm,title='Калькулятор на STM32 с клавиатурой и индикатором TM1637',author='')
doc.build(story,onFirstPage=footer,onLaterPages=footer)

# Self-contained editable document: images remain embedded when the file is moved.
html=['''<!doctype html><html lang="ru"><head><meta charset="utf-8"><title>Отчёт по калькулятору STM32</title><style>
*{box-sizing:border-box}body{margin:0;background:#e7e9eb;color:#000;font-family:"Times New Roman",serif}
.toolbar{position:sticky;top:0;background:#223344;color:white;padding:12px 20px;font:15px Arial,sans-serif;z-index:5;display:flex;gap:14px;align-items:center}.toolbar button{padding:8px 14px;cursor:pointer}main{padding:22px}.page{width:210mm;min-height:297mm;background:white;margin:0 auto 20px;padding:18mm 20mm 20mm 25mm;box-shadow:0 3px 12px #0002}p{font-size:12.5pt;line-height:1.4;margin:0 0 8pt;text-align:justify}.h1{font-size:15pt;font-weight:bold;line-height:1.27;margin-bottom:13pt}.h2{font-weight:bold;margin-top:8pt;margin-bottom:7pt}.small{font-size:10.5pt;line-height:1.28}.center{text-align:center;font-size:14pt;line-height:1.5}.title{text-align:center;font-size:19pt;font-weight:bold;line-height:1.37;margin-bottom:16pt}figure{margin:0 auto 10pt;text-align:center;break-inside:avoid}figure img{max-width:100%;height:auto}figcaption{font-size:11pt;line-height:1.27;margin-top:7pt}table{border-collapse:collapse;width:100%;margin-bottom:9pt}td{font-size:10.5pt;line-height:1.24;border:1px solid #d9d9d9;padding:6pt 7pt;vertical-align:middle}tr:first-child{background:#e8edf1}a{color:#174a78}.page:focus{outline:2px solid #7da7ca}@page{size:A4;margin:0}@media print{body{background:white}.toolbar{display:none}main{padding:0}.page{margin:0;box-shadow:none;page-break-after:always;min-height:297mm}.page:last-child{page-break-after:auto}.page:focus{outline:none}}
</style></head><body><div class="toolbar"><span>Текст можно редактировать прямо на странице. Заполните поля в квадратных скобках.</span><button onclick="window.print()">Печать или PDF</button><button onclick="saveDocument()">Сохранить изменения</button></div><main>''']
import re
for items in pages:
    html.append('<section class="page" contenteditable="true">')
    for item in items:
        if item[0]=='p':
            text=item[1].replace('\n','<br>')
            text=re.sub(r'<link href="([^"]+)" color="[^"]+">',r'<a href="\1">',text).replace('</link>','</a>')
            html.append(f'<p class="{item[2]}">{text}</p>')
        elif item[0]=='space':html.append(f'<div style="height:{item[1]}pt"></div>')
        elif item[0]=='img':
            _,file,width,cap=item
            data=base64.b64encode((A/file).read_bytes()).decode()
            html.append(f'<figure><img style="width:{width}mm" src="data:image/png;base64,{data}" alt="{escape(cap)}"><figcaption>{escape(cap)}</figcaption></figure>')
        else:
            _,rows,widths=item
            html.append('<table><colgroup>'+''.join(f'<col style="width:{v}mm">' for v in widths)+'</colgroup>')
            for row in rows: html.append('<tr>'+''.join('<td>'+escape(c)+'</td>' for c in row)+'</tr>')
            html.append('</table>')
    html.append('</section>')
html.append('''</main><script>function saveDocument(){const source='<!doctype html>\\n'+document.documentElement.outerHTML;const b=new Blob([source],{type:'text/html;charset=utf-8'});const a=document.createElement('a');a.href=URL.createObjectURL(b);a.download='Отчет_калькулятор_STM32.html';a.click();setTimeout(()=>URL.revokeObjectURL(a.href),1000)}</script></body></html>''')
(ROOT/'Отчет_калькулятор_STM32.html').write_text(''.join(html).replace('p{font-size:12.5pt;line-height:1.4','p{font-size:12pt;line-height:1.375'),encoding='utf-8')

qa=ROOT/'assets'/'qa';qa.mkdir(exist_ok=True)
pdf=pdfium.PdfDocument(str(target))
print('PDF pages:',len(pdf),'planned:',len(pages))
for i in range(len(pdf)):
    pdf[i].render(scale=1.5).to_pil().save(qa/f'page-{i+1}.png')
    text=pdf[i].get_textpage().get_text_range()
    print(i+1, len(text),text[:85].replace('\r\n',' '))
