"""Пересчёт таблиц и сборка исходников отчёта по варианту 10.

python build_report.py
Затем в report_variant10: xelatex main.tex (дважды).
"""

import contextlib
import io
import json
from pathlib import Path
import shutil

import main as stats
import sequence_analysis as sequence
import distribution_approximation as approximation
import generated_sequence as generator


ROOT = Path(__file__).resolve().parent
REPORT = ROOT / "report_variant10"
FIGURES = REPORT / "figures"
TEMPLATE = Path(r"C:\Users\grisa\.codex\attachments\319191af-215b-4907-b20c-d34dbab9bc45\pasted-text.txt")


def num(value, digits=4):
    return f"{value:.{digits}f}".replace(".", "{,}")


def signed(value, reference):
    return (value - reference) / abs(reference) * 100


def form_table(data):
    rows = [
        ("Мат. ожидание", stats.average, False),
        ("Дов. инт. (0,90)", lambda x: stats.epsilon(x, 0.9), True),
        ("Дов. инт. (0,95)", lambda x: stats.epsilon(x, 0.95), True),
        ("Дов. инт. (0,99)", lambda x: stats.epsilon(x, 0.99), True),
        ("Дисперсия", stats.dispersion, False),
        ("С. к. о.", stats.std, False),
        ("Коэф. вариации", stats.variation_coefficient, False),
    ]
    lines = [r"\begin{center}\begingroup\fontsize{9}{11}\selectfont",
             r"\setlength{\tabcolsep}{3pt}\renewcommand{\arraystretch}{1.3}",
             r"\begin{tabular}{@{}lcrrrrrr@{}}\toprule",
             r"\multirow{2}{*}{Характеристика} & & \multicolumn{6}{c}{Количество случайных величин}\\",
             r"\cmidrule(l){3-8} & & 10 & 20 & 50 & 100 & 200 & 300\\\midrule"]
    for index, (label, calculate, interval) in enumerate(rows):
        values = [calculate(data[:n]) for n in stats.SAMPLE_SIZES]
        cells = [(r"\pmv{" + num(v) + "}") if interval else num(v) for v in values]
        deviations = [num(signed(v, values[-1])) for v in values[:-1]] + ["---"]
        lines.append(r"\multirow{2}{*}{" + label + "} & Знач. & " + " & ".join(cells) + r"\\")
        lines.append(r" & \% & " + " & ".join(deviations) + r"\\")
        if index < len(rows) - 1:
            lines.append(r"\midrule")
    lines.append(r"\bottomrule\end{tabular}\endgroup\end{center}")
    return "\n".join(lines)


def acf_table(values):
    return (r"\begin{center}\begingroup\fontsize{8.5}{10}\selectfont\setlength{\tabcolsep}{3pt}" "\n"
            r"\begin{tabular}{@{}l*{10}{r}@{}}\toprule" "\n"
            "Сдвиг & " + " & ".join(str(k) for k in range(1, 11)) + r"\\\midrule" "\n"
            "Коэф. АК & " + " & ".join(num(v, 4) for v in values[:10]) + r"\\\bottomrule" "\n"
            r"\end{tabular}\endgroup\end{center}")


def main():
    FIGURES.mkdir(parents=True, exist_ok=True)
    original = stats.read_data(stats.DATA_PATH)[:300]
    params = approximation.fit_hyperexponential(original)
    generated = generator.generate_sequence(params, 300, 42)
    saved = [float(s) for s in (ROOT / "generated_results/seed_42/generated_data.txt").read_text().splitlines()]
    assert generated == saved, "Сохранённая генерация не соответствует текущим данным и seed=42"
    lags = list(range(1, 201))
    original_acf = [sequence.autocorrelation(original, k) for k in lags]
    generated_acf = [sequence.autocorrelation(generated, k) for k in lags]
    edges, counts = sequence.histogram_frequencies(original)
    shared_edges, cx, cy = generator.common_histogram(original, generated)
    with contextlib.redirect_stdout(io.StringIO()):
        sequence.OUTPUT_DIR = FIGURES
        sequence.draw_plots(original, lags, original_acf, edges, counts)
        for old, new in [("sequence.png", "source-sequence.png"),
                         ("autocorrelation.png", "source-autocorrelation.png"),
                         ("histogram.png", "source-histogram.png")]:
            (FIGURES / old).replace(FIGURES / new)
        approximation.OUTPUT_PATH = FIGURES / "approximation.png"
        approximation.plot_approximation(original, params)
        generator.plot_comparison(original, generated, lags, original_acf, generated_acf,
                                  shared_edges, cx, cy, FIGURES, 42)
    for name in ("main.py", "sequence_analysis.py", "distribution_approximation.py", "generated_sequence.py", "data.txt"):
        (REPORT / "code").mkdir(exist_ok=True)
        shutil.copy2(ROOT / name, REPORT / "code" / name)
    (REPORT / "code/generated_data.txt").write_text("\n".join(map(repr, generated)) + "\n", encoding="utf-8")

    source = TEMPLATE.read_text(encoding="utf-8")
    prefix = source[:source.index(r"\clearpage" + "\n" + r"\section{Ход работы}")]
    prefix = prefix[prefix.index(r"\documentclass"):]
    prefix = prefix.replace(r"\usepackage{array,booktabs,multirow,tabularx}", r"\usepackage{array,booktabs,multirow,tabularx,longtable}")
    prefix = prefix.replace("Барсуков Максим Андреевич; Горляков Даниил Петрович", "Марьин Григорий Алексеевич")
    prefix = prefix.replace("Вариант 16", "Вариант 10")
    prefix = prefix.replace(r"\section{Задание}",
                            r"\begingroup\small\setstretch{1.05}" + "\n"
                            + r"\setlist[enumerate]{itemsep=1pt,topsep=2pt,parsep=0pt}" + "\n"
                            + r"\section{Задание}")
    prefix += "\n" + r"\endgroup" + "\n"
    intro = "% !TeX program = xelatex\n% !TeX encoding = UTF-8\n% Вариант 10. Таблицы пересчитаны по data.txt; генерация H2, seed=42.\n"
    sections = [r"\clearpage\section{Ход работы}", r"\stage{1}{Числовые характеристики исходной последовательности}"]
    sections.append(r"\begingroup\small\setstretch{1.05}")
    sections.append(r"""Исследованы первые $N=300$ значений файла \texttt{data.txt} в исходном
порядке. В файле 301 число; последнее не используется, поскольку заданием
предусмотрена максимальная выборка из 300 элементов. Для каждого $n$ берутся
первые $n$ значений. Расчёты выполнены на Python.
\[
\bar x=\frac1n\sum_{i=1}^n x_i,\quad
s^2=\frac1{n-1}\sum_{i=1}^n(x_i-\bar x)^2,\quad
s=\sqrt{s^2},\quad V=\frac{s}{|\bar x|}.
\]
Доверительный интервал записывается как $\bar x\pm\varepsilon_p$, где
\[
\varepsilon_p=t_p\frac{s}{\sqrt n},\qquad
t_{0{,}90}=1{,}643,\quad t_{0{,}95}=1{,}960,\quad t_{0{,}99}=2{,}576.
\]
Коэффициенты взяты из таблицы 1 методических материалов. Это нормальное
приближение, а не распределение Стьюдента; для малых выборок точность
приближения ограничена.

\noindent\textbf{Форма 1.} Характеристики исходной последовательности.
""")
    sections.append(form_table(original))
    sections.append(r"""\noindent Строки «Дов. инт.» содержат полуинтервал $\pm\varepsilon_p$;
границы равны $\bar x-\varepsilon_p$ и $\bar x+\varepsilon_p$.
Проценты рассчитаны со знаком: $\Delta_A=(A_n-A_{300})/|A_{300}|\cdot100\%$.
Для доверительных интервалов сравниваются $\varepsilon_{p,n}$ и
$\varepsilon_{p,300}$ при одинаковом $p$. Прочерк при $n=300$ означает нулевое
отклонение. В отличие от абсолютных отклонений в консольных таблицах программы,
здесь сохранено знаковое представление шаблона.
\conclusion{1}{Оценки изменяются немонотонно при расширении выборки.
Для $n=300$ среднее равно $70{,}1692$, исправленная дисперсия --- $5621{,}2380$,
СКО --- $74{,}9749$, коэффициент вариации --- $1{,}0685$.
В данной последовательности доверительные полуинтервалы уменьшаются
при увеличении $n$, а при фиксированном $n$ расширяются с ростом $p$.}
""")
    with contextlib.redirect_stdout(io.StringIO()):
        a, b = sequence.linear_trend(original)
    index_corr = sequence.correlation(list(range(1, 301)), original)
    sections.append(r"\endgroup\clearpage\stage{2}{График исходной числовой последовательности}")
    sections.append(r"\reportfigure{source-sequence.png}{1}{График 1 --- Исходная последовательность, среднее и линейный тренд}")
    sections.append(r"Номер наблюдения отложен по горизонтали, значение --- по вертикали. "
                    r"Линейный тренд, рассчитанный методом наименьших квадратов:" + "\n"
                    + r"\[\widehat x_i=" + num(a, 6) + "+(" + num(b, 6) + r")i.\]" + "\n"
                    + r"Корреляция значения с номером наблюдения равна $" + num(index_corr, 6) + r"$." + "\n"
                    + r"\conclusion{2}{Последовательность содержит как повышения, так и снижения; "
                    r"монотонности нет. Наклон тренда мал относительно разброса значений. "
                    r"Выраженного роста, убывания и устойчивого повторяющегося рисунка визуально "
                    r"не обнаружено. Подтверждённый период по графику не установлен.}")
    sections.append(r"\clearpage\stage{3}{Автокорреляционный анализ исходной последовательности}")
    sections.append(r"""Для сдвига $k$ сравниваются два среза длиной $N-k$:
\[
(x_1,\ldots,x_{N-k})\quad\text{и}\quad(x_{1+k},\ldots,x_N).
\]
Коэффициент Пирсона рассчитывается по формуле:
\[
r_X(k)=\frac{\sum_{i=1}^{N-k}(x_i-\bar x_L)(x_{i+k}-\bar x_R)}
{\sqrt{\sum_{i=1}^{N-k}(x_i-\bar x_L)^2\sum_{i=1}^{N-k}(x_{i+k}-\bar x_R)^2}}.
\]
Средние $\bar x_L$ и $\bar x_R$ вычисляются отдельно для двух срезов.
Исследованы сдвиги $1\le k\le200$. Ниже приведены первые 10 коэффициентов;
полная форма 3 для обеих последовательностей находится в приложении.
""")
    sections.append(acf_table(original_acf))
    sections.append(r"\reportfigure{source-autocorrelation.png}{1}{Автокорреляция исходной последовательности при сдвигах 1--200}")
    max_index = max(range(200), key=lambda i: abs(original_acf[i]))
    sections.append(r"Наибольший по модулю коэффициент: $r_X(" + str(max_index+1) + ")=" + num(original_acf[max_index],6) + r"$. "
                    r"При $k=200$ остаётся 100 пар, поэтому оценки на больших сдвигах менее устойчивы." + "\n"
                    r"\conclusion{3}{Коэффициенты колеблются около нуля, устойчивой серии периодических "
                    r"пиков не установлено. При $k=1$ коэффициент равен $0{,}000692$. "
                    r"Результат совместим со случайной последовательностью. Отдельные пики "
                    r"требуют осторожной интерпретации; отсутствие выраженной автокорреляционной "
                    r"структуры не доказывает независимость или отсутствие нелинейной зависимости.}")
    sections.append(r"\clearpage\stage{4}{Гистограмма распределения частот}")
    sections.append(r"Число интервалов выбрано по правилу Стерджеса: $B=\lceil1+\log_2 300\rceil=10$. "
                    r"Интервалы имеют вид $[a_j,a_{j+1})$, последний включает правую границу.")
    sections.append(r"\begin{center}\begin{tabular}{rrrr}\toprule № & Левая граница & Правая граница & Частота\\\midrule")
    for i, count in enumerate(counts):
        sections.append(f"{i+1} & {num(edges[i])} & {num(edges[i+1])} & {count}" + r"\\")
    sections.append(r"\midrule\multicolumn{3}{r}{Всего} & 300\\\bottomrule\end{tabular}\end{center}")
    sections.append(r"\reportfigure{source-histogram.png}{0.95}{График 2 --- Гистограмма частот исходной последовательности}")
    sections.append(r"\conclusion{4}{В первый интервал $[0{,}0710;54{,}7066)$ попали 163 значения "
                    r"из 300, то есть $54{,}33\%$. Гистограмма асимметрична: основная масса "
                    r"сосредоточена при небольших значениях, справа наблюдается длинный хвост. "
                    r"Форма гистограммы сама по себе не определяет единственный закон распределения.}")
    sections.append(r"\clearpage\stage{5}{Параметры аппроксимирующего закона распределения}")
    sections.append(r"""Метод моментов согласует первые два начальных момента модели с выборочными:
\[
m_1=\frac1N\sum_{i=1}^N x_i=70{,}169167,\qquad
m_2=\frac1N\sum_{i=1}^N x_i^2=10526{,}212451.
\]
Для подбора закона используется
\[
D_*=m_2-m_1^2=5602{,}500500,\qquad
V_* = \frac{\sqrt{D_*}}{m_1}=1{,}066706>1.
\]
В отличие от формы 1, здесь дисперсия имеет знаменатель $N$:
$D_*=(N-1)s^2/N$. Это обеспечивает точное согласование именно начальных
моментов. Поэтому $V_*$ немного отличается от исправленного выборочного КВ.

Выбран гиперэкспоненциальный закон $H_2$. Его плотность:
\[
f(x)=p\lambda_1e^{-\lambda_1x}+(1-p)\lambda_2e^{-\lambda_2x},\quad x\ge0;
\qquad f(x)=0,\quad x<0.
\]
Для него
\[
E[X]=\frac p{\lambda_1}+\frac{1-p}{\lambda_2},\qquad
E[X^2]=\frac{2p}{\lambda_1^2}+\frac{2(1-p)}{\lambda_2^2}.
\]
Два момента не определяют три параметра однозначно. Использовано
дополнительное условие равных вкладов компонент в математическое ожидание:
\[
\frac p{\lambda_1}=\frac{1-p}{\lambda_2}=\frac{m_1}{2}.
\]
Отсюда
\[
p=\frac12\left(1+\sqrt{\frac{V_*^2-1}{V_*^2+1}}\right)=0{,}626970,
\]
\[
\lambda_1=\frac{2p}{m_1}=0{,}01787024,\qquad
\lambda_2=\frac{2(1-p)}{m_1}=0{,}01063230.
\]
Средние компонент равны $1/\lambda_1=55{,}958949$ и
$1/\lambda_2=94{,}052986$. Подстановка параметров в формулы $E[X]$ и $E[X^2]$
воспроизводит $m_1$ и $m_2$ с точностью вычислений.
\conclusion{5}{Гиперэкспоненциальная модель согласована по двум начальным
моментам. Выбор обусловлен $V_*>1$; совпадение моментов не означает
совпадения всего распределения и требует отдельного сравнения с данными.}
""")
    sections.append(r"\clearpage\subsection*{Плотность модели и исходная гистограмма}")
    sections.append(r"\reportfigure{approximation.png}{1}{График 3 --- Плотность $H_2$ и нормированная исходная гистограмма}")
    sections.append(r"""Для сравнения с плотностью высота столбца гистограммы равна
$h_j=n_j/(N\Delta_j)$, где $\Delta_j$ --- ширина интервала. Поэтому сумма
площадей столбцов равна единице. Плотность модели нормирована на всей
полуоси $[0,+\infty)$; на рисунке показан диапазон наблюдений.

Кривая воспроизводит общее убывание частот и правый хвост. Высота столбца
отражает среднюю плотность в интервале, поэтому кривая не обязана проходить
через его верхнюю середину. Визуальное соответствие является качественной
оценкой; формальный критерий согласия в этой работе не применялся.
""")
    sections.append(r"\clearpage\stage{6}{Алгоритм формирования случайной последовательности}")
    sections.append(r"""Генератор реализован на Python в файле \texttt{generated\_sequence.py}.
На каждое значение используются два отдельных равномерных псевдослучайных
числа $U_1,U_2\in[0,1)$. Сначала выбирается компонента, затем применяется
обратная функция экспоненциального распределения:
\[
Y=G(U_1,U_2)=\begin{cases}
-\ln(1-U_2)/\lambda_1,&U_1<p,\\
-\ln(1-U_2)/\lambda_2,&U_1\ge p.
\end{cases}
\]
Из $U_2=1-e^{-\lambda Y}$ следует $Y=-\ln(1-U_2)/\lambda$.
Фрагмент реализации:
\begin{Verbatim}[fontsize=\small,frame=single,framesep=3mm]
rng = random.Random(seed)
values = []
for _ in range(300):
    u1 = rng.random()
    rate = lambda1 if u1 < p else lambda2
    u2 = rng.random()
    values.append(-math.log(1 - u2) / rate)
\end{Verbatim}
\texttt{random.Random} используется только для равномерных чисел;
преобразование в $H_2$ написано явно. Статистические характеристики и
корреляции также рассчитываются вручную. Matplotlib сохраняет графики в PNG.

В отчёте используется одна реализация из 300 значений при
\texttt{seed=42}. Первые пять значений:
$2{,}382253$; $14{,}134457$; $106{,}202093$; $8{,}554345$; $1{,}692768$.
Данные не сортируются, не масштабируются после генерации и не отбираются
по степени сходства с исходной выборкой.

Для демонстрации на защите:
\begin{Verbatim}[fontsize=\small,frame=single,framesep=3mm]
python generated_sequence.py --seed 42
python generated_sequence.py --seed 7
\end{Verbatim}
При одинаковых исходных данных и seed эксперимент воспроизводится;
изменение seed даёт другую реализацию той же модели.
\conclusion{6}{Реализован генератор смеси двух экспоненциальных законов.
Он формирует требуемые 300 значений и сохраняет данные, параметры,
числовые таблицы и графики. Работа иллюстрируется повторным запуском
с фиксированным и изменённым seed.}
""")
    sections.append(r"\clearpage\stage{7}{Сравнение исходной и сгенерированной последовательностей}")
    sections.append(r"\noindent\textbf{Форма 2.} Характеристики сгенерированной последовательности ($H_2$, seed=42).")
    sections.append(form_table(generated))
    sections.append(r"""\noindent В строках «\%» эталоном является характеристика
сгенерированной выборки из 300 элементов. Для доверительных интервалов
сравниваются соответствующие полуинтервалы. Подбор закона выполнялся
по исходным данным; моменты конечной сгенерированной выборки могут отличаться
от теоретических из-за случайных колебаний.

\noindent\textbf{Сопоставление при $N=300$:}
\begin{center}\begin{tabular}{lrrr}\toprule
Характеристика & Исходная & Сгенерированная & Отклонение, \%\\\midrule
""")
    sx, sy = generator.characteristics(original), generator.characteristics(generated)
    for key in sx:
        sections.append(f"{key} & {num(sx[key])} & {num(sy[key])} & {num(signed(sy[key],sx[key]))}" + r"\\")
    sections.append(r"\bottomrule\end{tabular}\end{center}")
    sections.append(r"""При $p=0{,}95$ доверительные интервалы среднего равны
$[61{,}6850;78{,}6534]$ для исходной и $[62{,}7519;80{,}0842]$ для
сгенерированной выборки. Их перекрытие согласуется с близостью средних,
но не является самостоятельным доказательством совпадения распределений.
""")
    sections.append(r"\clearpage\subsection*{Сравнение характеристик при одинаковом объёме выборки}")
    sections.append(r"Процентное отличие вычислено как $\Delta_{YX,n}=(A_{Y,n}-A_{X,n})/|A_{X,n}|\cdot100\%$. "
                    r"В отличие от формы 2, здесь эталоном служит исходная выборка того же объёма.")
    sections.append(r"\begin{center}\begin{tabular}{lrrrrrr}\toprule Характеристика & 10 & 20 & 50 & 100 & 200 & 300\\\midrule")
    for key in sx:
        values = [signed(generator.characteristics(generated[:n])[key], generator.characteristics(original[:n])[key]) for n in stats.SAMPLE_SIZES]
        sections.append(key + " & " + " & ".join(num(v) for v in values) + r"\\")
    sections.append(r"\bottomrule\end{tabular}\end{center}")
    sections.append(r"\reportfigure{sequences.png}{1}{Графики исходной и сгенерированной последовательностей; шкалы одинаковы}")
    sections.append(r"На малых выборках расхождения сильнее. При $N=300$ отличие средних "
                    r"составляет $1{,}7799\%$, дисперсий --- $4{,}3348\%$, СКО --- $2{,}1444\%$, "
                    r"КВ --- $0{,}3582\%$. Совпадение положения отдельных пиков не требуется: "
                    r"модель воспроизводит распределение значений, а не исходный порядок наблюдений.")
    sections.append(r"\clearpage\subsection*{Сравнение распределений частот}")
    sections.append(r"\reportfigure{histograms.png}{1}{Гистограммы исходной и сгенерированной выборок на общих интервалах}")
    sections.append(r"Обе выборки содержат по 300 значений, поэтому сравниваются абсолютные "
                    r"частоты. Синий столбчатый график соответствует исходным данным, "
                    r"малиновый ступенчатый контур --- сгенерированным. Границы едины для обеих выборок.")
    sections.append(r"\begin{center}\begin{tabular}{rrrrr}\toprule № & Левая граница & Правая граница & Исходная & Генерация\\\midrule")
    for i in range(len(cx)):
        sections.append(f"{i+1} & {num(shared_edges[i])} & {num(shared_edges[i+1])} & {cx[i]} & {cy[i]}" + r"\\")
    sections.append(r"\midrule\multicolumn{3}{r}{Всего} & 300 & 300\\\bottomrule\end{tabular}\end{center}")
    sections.append(r"Гистограммы имеют сходную общую форму, но отдельные частоты различаются. "
                    r"Это ожидаемо при конечной случайной выборке. Сходство гистограмм "
                    r"поддерживает пригодность модели для приближённого описания распределения.")
    sections.append(r"\clearpage\subsection*{Автокорреляция сгенерированной последовательности}")
    sections.append(acf_table(generated_acf))
    sections.append(r"\reportfigure{autocorrelation.png}{1}{Сравнение автокорреляций исходной и сгенерированной последовательностей}")
    sections.append(r"При $k=1$ для генерации $r_Y(1)=-0{,}012227$. Наибольший модуль "
                    r"наблюдается при $k=179$: $r_Y(179)=0{,}325124$, расчёт выполнен по 121 паре. "
                    r"Отдельный пик не устанавливает период. При просмотре многих сдвигов "
                    r"и уменьшении числа пар возможны заметные случайные отклонения. "
                    r"Генератор не использует предыдущее значение при формировании следующего; "
                    r"его математическая модель предполагает независимые испытания.")
    sections.append(r"\clearpage\subsection*{Корреляция исходной и сгенерированной последовательностей}")
    sections.append(r"""Для пар с одинаковыми номерами рассчитан коэффициент Пирсона:
\[
r_{XY}=\frac{\sum_{i=1}^{n}(x_i-\bar x)(y_i-\bar y)}
{\sqrt{\sum_{i=1}^{n}(x_i-\bar x)^2\sum_{i=1}^{n}(y_i-\bar y)^2}}.
\]
\begin{center}\begin{tabular}{rrrrrrr}\toprule
$n$ & 10 & 20 & 50 & 100 & 200 & 300\\\midrule
""")
    sections.append(r"$r_{XY}$ & " + " & ".join(num(sequence.correlation(original[:n],generated[:n]),6) for n in stats.SAMPLE_SIZES) + r"\\\bottomrule\end{tabular}\end{center}")
    sections.append(r"""Для полных выборок $r_{XY}=0{,}002370$, что указывает на практически
отсутствующую попарную линейную связь. Высокая корреляция не является целью:
сходство распределений не требует совпадения значений на одинаковых позициях.
Малый коэффициент не доказывает полную статистическую независимость.

\conclusion{7}{При $N=300$ характеристики исходной и сгенерированной
последовательностей близки: среднее отличается на $1{,}7799\%$, исправленная
дисперсия --- на $4{,}3348\%$. Гистограммы согласуются по общей форме,
попарная линейная корреляция близка к нулю. Полученная модель пригодна
как приближённое описание данных по первым двум моментам; статистическое
доказательство полного совпадения законов не проводилось.}

\clearpage\section{Выводы}
В работе обработаны первые 300 значений последовательности варианта 10.
Для шести объёмов выборки рассчитаны среднее, исправленная дисперсия,
СКО, коэффициент вариации и приближённые доверительные интервалы.
График значений не показывает выраженного тренда и подтверждённого периода.
Автокорреляционный анализ не выявил устойчивого периодического рисунка,
однако не является доказательством независимости.

По двум начальным моментам подобрана гиперэкспоненциальная модель $H_2$
с $p=0{,}626970$, $\lambda_1=0{,}01787024$, $\lambda_2=0{,}01063230$.
Реализован генератор методом выбора компоненты и обратного преобразования.
Для воспроизводимой реализации при seed=42 получены близкие числовые
характеристики и форма гистограммы. Качество аппроксимации оценено по
моментам и визуальному сопоставлению, без формального критерия согласия.

\subsection*{Использованные материалы и воспроизводимость}
\begin{enumerate}
\item «Статистическая обработка результатов измерений», учебные материалы
по дисциплине «Моделирование», ИТМО.
\item «Обработка результатов имитационного моделирования», методические
материалы к УИР 1, формулы (1), (4), (12), (13), таблица 1.
\item O. J. Boxma. \textit{Stochastic Performance Modelling}, раздел 2.3:
\url{https://iadan.win.tue.nl/4t400/DictaatPart1.pdf}.
\item Исходные данные и Python-файлы приложены в папке \texttt{code}.
Для графиков требуется Matplotlib. Генерация: \texttt{seed=42}; $N=300$.
\end{enumerate}

\clearpage\section{Приложение. Полная форма 3}
Коэффициенты автокорреляции исходной ($X$) и сгенерированной ($Y$)
последовательностей. В обеих выборках $N=300$; число пар равно $300-k$.
\begingroup\fontsize{9}{10.5}\selectfont\renewcommand{\arraystretch}{1.03}
\begin{longtable}{rrrr}
\toprule Сдвиг $k$ & Число пар & $r_X(k)$ & $r_Y(k)$\\\midrule\endfirsthead
\toprule Сдвиг $k$ & Число пар & $r_X(k)$ & $r_Y(k)$\\\midrule\endhead
\midrule\multicolumn{4}{r}{Продолжение на следующей странице}\\\endfoot
\bottomrule\endlastfoot
""")
    for k, rx, ry in zip(lags, original_acf, generated_acf):
        sections.append(f"{k} & {300-k} & {num(rx,6)} & {num(ry,6)}" + r"\\")
    sections.append(r"\end{longtable}\endgroup\end{document}")
    tex = intro + prefix + "\n" + "\n\n".join(sections) + "\n"
    (REPORT / "main.tex").write_text(tex, encoding="utf-8")
    (REPORT / "README.txt").write_text(
        "Вариант 10. Марьин Григорий Алексеевич, P3312.\n"
        "Сборка: xelatex main.tex (дважды); в Overleaf выбрать XeLaTeX.\n"
        "Загрузить main.tex вместе с папкой figures. Папка code содержит программы и данные.\n"
        "Числовые результаты: первые 300 значений data.txt, генератор H2, seed=42.\n"
        "Проценты в отчёте знаковые; интервалы представлены полуинтервалами.\n",
        encoding="utf-8",
    )
    print(json.dumps({"report": str(REPORT), "source_max_acf": [max_index+1, original_acf[max_index]],
                      "figures": len(list(FIGURES.glob('*.png')))}, ensure_ascii=False))


if __name__ == "__main__":
    main()
