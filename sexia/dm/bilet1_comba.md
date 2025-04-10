# uБилет 1

## Основные понятия теории множеств: множество, элемент, подмножество. Основные операции над множествами.

> $\cdot$ Неформально, множество — это произвольная совокупность объектов. 
> $\cdot$ Объекты, из которых состоит множество, называются элементами. 
> $\cdot$ Элементами множества могут быть любые рассматриваемые в
> математике объекты: числа, точки, фигуры, а также другие множества. 
> $\cdot$ Принадлежность элемента x множеству Y обозначается $x \in Y$.
> $\cdot$ Множество, не содержащее ни одного элемента, называется пустым
> и обозначается ∅.

### Подмножество:

> Если все элементы множества Y принадлежат множеству X,
> то Y называется подмножеством множества X. Обозначение: $Y \subset X$. P(X) — множество всех подмножеств множества X.

### Основные операции над множествами:

> $\cdot$ Объединение множеств: $A \cup B= \{x | x \in A \lor x \in B\}$ 
> $\cdot$ Пересечение множеств: $A \cap B= \{x | x \in A \land x \in B\}$ 
> $\cdot$ Разность множеств: $A \setminus B= \{x | x \in A \land x \notin B\}$ 
> $\cdot$ Симметрическая разность множеств: $A \triangle B= \{x | (A \cup B) \setminus (A \cap B)\}$

> Пусть U — универсум (или объемлющее
> множество), т.е. множество, которое содержит все рассматриваемые
> множества. Тогда
> $A = \{x \in U | x \notin A\}$

> Декартово произведение множеств:
> $A \times B = \{(a, b) | a \in A \land b \in B\}$

# Билет 2

## Бинарные и n-арные отношения. Определения и примеры. Основные свойства отношений. Отношение эквивалентности. Отношение порядка.

### Бинарные отношения:

> Бинарным отношением между множествами X и Y называется
> произвольное подмножество их декартового произведения $R \subset X \times Y.$
>
> Пара (x,y), где $x \in X$ и $y \in Y$, удовлетворяет отношению R,
> если (x,y) ∈ R. Обозначение: xRy.

#### Замечание от пастора:

> Фактически, бинарное отношение — это свойство, которое для каждой
> пары (x,y) может либо выполняться, либо не выполняться (R — это
> множество тех пар, для которых данное свойство выполнено).

### n-арные отношения:

> • n-местным (или n-арным) отношением между множествами $X_1,...,X_n$
> называется произвольное подмножество $R \subset X_1 \times... \times X_n$

### Примеры:

> 1. Равенство (a = b) — бинарное отношение на R;
> 2. делимость $(a \vdots b)$ — бинарное отношение на Z;
> 3. пусть $G = (V,E)$ — граф, тогда– смежность — бинарное отношение на V,– инцидентность — бинарное отношение между V и E;
> 4. точки A,B,C лежат на одной прямой — трехместное отношение на
>    плоскости.

### Основные свойства отношений:

> Определение
> Бинарное отношение $R \subset X^2$ называется
>
> $\cdot$ рефлексивным, если $\forall x \in X: xRx$;
>
> $\cdot$ иррефлексивным, если если xRx не выполнено ни для каких x ∈ X;
>
> $\cdot$ симметричным, если $\forall x,y \in X: xRy \Rightarrow yRx$;
>
> $\cdot$ антисимметричным, если из xRy и yRx следует, x = y;
>
> $\cdot$ транзитивным, если из xRy и yRz следует xRz.

### Отношение эквивалентности:

> Бинарное отношение ∼ на множестве X называется отношением
> эквивалентности, если оно рефлексивно, симметрично и транзитивно.

> Замечание
> Отношение эквивалентности разбивает множество на классы эквивалентности так,
> что любые два элемента из одного класса эквивалентны, а любые два элемента из
> разных классов — нет

### Отношение порядка:

> Определение
> • Бинарное отношение $\prec$ на множестве X называется отношением
> частичного порядка, если оно антисимметрично и транзитивно.
>
> Если при этом отношение ≺ иррефлексивно, то оно называется
> отношением строгого частичного порядка
>
> А если оно рефлексивно — то отношением нестрогого частичного порядка.
>
> Множество, на котором задано отношение частичного порядка, называется
> частично упорядоченным.
>
> $\Rightarrow$ Формально, частично упорядоченное множество — это упорядоченная пара
> (X,$\prec$), где X — множество и $\prec$ — отношение частичного порядка на X.
>
> $\Rightarrow$ Вчастично упорядоченном множестве некоторые пары элементов могут быть
> несравнимы. То есть могут существовать такие $a,b \in X$, что ни одно из
> утверждений $a = b, a \prec b, b \prec$ a$ не выполнено.

### Отношение линейного порядка:

> • Бинарное отношение $\prec$ на множестве X называется отношением
> (строгого) линейного порядка, если оно является отношением частичного
> порядка и для любых a,b $\in$ X выполнено ровно одно из следующих трех
> утверждений: a = b, a ≺ b или b $\prec$ a.
> • В этом случае, пара (X,$\prec$) называется линейно упорядоченным
> множеством.

### Примеры:

> 1. a <b — отношение линейного порядка на R;
> 2. a $\vdots$ b — отношение частичного порядка на N.
> 3. Пусть X — множество.
>    Тогда A $\subset$ B — отношение частичного порядка на P(X).

# Билет 3

## Понятие отображения. Образ и прообраз элемента. Инъекция, сюръекция и биекция. Композиция отображений. Обратное отображение. Критерий обратимости.

### Отображения и функции:

> • Неформально, отображение (функция) из множества X в множество Y —
> это такое правило f , которое каждому элементу x ∈ X ставит в соответствие
> ровно один элемент y ∈ Y. Этот элемент обозначается f(x).
>
> • Формально понятие отображения можно определить в терминах
> отношений.

### Определение:

> • Бинарное отношение f ⊂ X ×Y называется отображением из множества
> X в множество Y, если любой элемент x ∈ X входит в качестве первого
> элемента ровно в одну пару (x,y) ∈ f.
>
> • Обозначение: f : X → Y.
>
> • Второй элемент пары (x,y) обозначают f(x) и называют образом элемента x при отображении f.
>
> • Если y = f(x), то x называют прообразом элемента y.
>
> • В отличии от образа, прообраз элемента существует не всегда;
> также прообраз может быть не единственным.

### Инъекция, сюръекция, биекция:

> Определение
> Отображение f : X → Y называется
>
> • инъекцией, если f (x)= f(y) при x= y;
>
> • сюръекцией, если у каждого элемента множества Y есть хотя бы один
> прообраз в множестве X;
>
> • биекцией, если f одновременно является инъекцией и сюръекцией.

### Композиция отображений и обратное отображение:

#### Определение

Композицией отображений f : X → Y и g: Y → Z называется отображение
g◦f : X →Z, задаваемое формулой (g◦f)(x) = g(f(x)).

#### Определение

> • Отображение g: Y → X называется обратным к отображению $f : X → Y$,
> если обе композиции $f ◦g и g ◦f$ являются тождественными
> отображениями.
>
> • Т.е. $g(f (x)) = x$, при всех x ∈ X, и $f(g(y)) = y$, при всех y ∈ Y.
>
> • Отображение f называется обратимым, если к нему есть обратное.
>
> • Отображение, обратное к $f$, обычно обозначается $f^{−1}$.

### Критерий обратимости

#### Теорема

Отображение f : X → Y обратимо ⇐⇒ f — биекция.

#### Доказательство:

Доказательство.

> “⇐”: Для каждого y ∈ Y обозначим через $f^{−1}$(y) единственный прообраз
> элемента y.
> • Тогда $f^{−1}$: Y → X — отображение, обратное к f.
>
> “⇒”: Пусть $f^{−1}$ — отображение, обратное к f.
> 
> • f — инъекция, поскольку если f(x) = f(y),
> то x =$f^{−1}$(f(x)) = $f^{−1}$(f(y)) = y.
> 
> • f — сюръекция, поскольку для любого y ∈ Y имеем y = f($f^{−1}$(y)).

# Билет 4

## Число элементов декартова произведения двух и нескольких множеств. Количество подмножеств данного множества.

> • Пусть X — конечное множество. Количество его элементов будем
> обозначать через |X|.

### Лемма (принцип произведения)

> Если |X| = m и |Y| = n, то |X ×Y| =mn.

> ### Доказательство.
>
> Каждый из m элементов множества X
> входит ровно в n пар с элементами множества Y.

> ### Следствие
>
> Если $|X_i|$ = $m_i$, где i $\in$ [1..k], то $|X_1 \times ... \times X_k|$ = $m_1 ·...· m_k$.

> ### Доказательство.
>
> Доказательство. Индукция по k. 
> База (k = 2): см. предыдущую лемму. 
> Переход (k → k +1): 
> $|X_1 ×... ×X_k ×X_{k+1}|$ = $|(X_1 ×...×X_k)×X_{k+1}|$ = $|X_1 ×...×X_k|·|X_{k+1}|$ = $(m_1 ·...·m_k)·m_{k+1} = m_1 ·...·m_k ·m_{k+1}.$

### Количество подмножеств данного множества:

#### Теорема: Если |X| = m, то |P(X)| = $2^m$.

> ### Доказательство.
>
> Доказательство. Пусть X = ${x_1,...,x_m}.$ 
> • Для каждого из i ∈ [1..m] есть два варианта: $x_i$ можно либо включить в подмножество, либо не включать. 
> • Итого, есть $2^m$ способов выбрать подмножество.

> ### Замечание
>
> • Фактически, мы построили следующую биекцию между множествами P(X) и $\{0,1\}^m$

# Билет 5

## Число отображений из одного множества в другое. Число инъекций. Число перестановок данного множества. Размещения и размещения с повторениями.

### Теорема

> Пусть |X| = k и |Y| = n. Тогда
>
> 1. число отображений из X в Y равно nk;
> 2. число инъекций из X в Y равно n(n−1)...(n−k +1).

### Доказательство.

> Пусть $X = {x_1,...,x_k}$.
>
> 1. Для каждого элемента xi ∈ X можно n способами выбрать его образ.
> 2. Образ $x_1$ можно выбрать n способами. После этого останется n −1
>    способ выбрать образ $x_2$, ..., n − k +1 способ выбрать образ $x_k$.

### Замечание

> При n ≥k имеем n(n−1)...(n−k +1) = $\frac{n!}{(n−k)!}$

### Конечные множества: перестановки и размещения:

Перестановкой на множестве X называется произвольная биекция σ: X → X.

### Следствие

Если |X| = n, то число перестановок на множестве X равно n!.

### Определение

> Число инъекций f : [1..k] → [1..n] называется числом размещений из n
> элементов по k и обозначается $A^k_n$.
>
> Число отображений f : [1..k] → [1..n] называется числом размещений с
> повторениями из n элементов по k и обозначается $\bar{A^k_n}$

1. $A^k_n$ = n(n−1)...(n−k +1) = $\frac{n!}{(n−k)!}$
2. $\bar{A^k_n}$ = $n^k$

# Билет 6

## Счётные множества. Определение и примеры. Счётность декартова произведения счётных множеств.

### Определение

Множество X называется счётным, если существует биекция $f : X → N$.

### Замечание

> • Это означает, что элементы множества X можно занумеровать
> натуральными числами: для элемента a ∈ X число f(a) будет его номером. 
> • Элементы счётного множества можно записать в виде
> последовательности: $X = \{x_1,x_2,x_3,...\}$, где $x_k = f^{-1}(k)$.

### Примеры

> • $2N =\{2n | n ∈N\}$ — множество всех чётных чисел.
> Доказательство. $f(x) = x/2$ — биекция из $2N$ в $N.$ \
>
> $$
> f(x) = \begin{cases} x, x > 0, \\ 1 - x, x < 0. \end{cases}
> $$

### Счётность декартова произведения счётных множеств.

#### Теорема

Множество N×N — счётно.

#### Доказательство.

> f(x, y) = $\frac{(x+y-1)(x+y-2)}{2} + y$ - биекция из N×N в N.

#### Замечание

> • Эта функция перечисляет клетки бесконечной таблицы “по диагоналям”.
> • Другой пример биекции из N×N в N: g(x,y) = $2^{x−1}(2y −1).$

#### Следствие

Пусть $X_1,...,X_n$ — счётные множества.
Тогда множество $X_1 \times ... \times X_n$ — также счётно.

> ### Доказательство.
>
> Рассмотрим биекции 
> $f_i : X_i → N$ (где i ∈ [1..n]) и $g: N \times N → N$.
> База (n = 2): отображение $h_2: X_1 \times X_2 → N$, задаваемое формулой
> $h_2(x_1,x_2) = g(f_1(x_1), f_2(x_2))$,
> является биекцией.
> Переход (n −1 → n)
> • Множество $X_1 \times ...\times X_{n−1}$ счётно по индукционному предположению.
> • Множество $X_n$ счётно по условию.
> • Тогда множество $X_1 \times ... \times X_{n−1} \times X_n$ = $(X_1 \times ... \times X_{n−1}) \times X_n$
> является декартовым произведением двух счётных множеств.
> • По доказанному в базе, оно счётно.

# Билет 7

## Теорема о бесконечном подмножестве счётного множества. Понятие не более чем счётного множества и их основные свойства.

### Теорема

> Бесконечное подмножество счётного множества — счётно.

### Доказательство.

> Пусть X — счётное множество и $A \subset X$ — его бесконечное подмножество. 
> • Рассмотрим биекцию f : X → N.
> • Тогда g(x) = |{a $\in$ A | f(a) ≤ f(x)}| — инъекция из A в N.

### Не более чем счетные множества

> ### Определение
>
> • Множество X — не более чем счётно, если X либо конечно, либо счётно. 
> • Множество X — несчётно, если X не является ни конечным, ни счётным.

### Основные свойства не более чем счётных множеств:

> Пусть X= ∅. Тогда следующие условия равносильны:
>
> 1. множество X не более чем счётно;
> 2. существует инъекция f : X → N;
> 3. существует сюръекция g : N → X.

> ### Доказательство.
>
> “1. ⇒ 3.”: Пусть множество X не более чем счётно. 
> • Если X бесконечно, то оно счётно. 
> ▶ Тогда существует биекция f : X → N. 
> ▶ Следовательно, $f^{−1}$: N → X — сюръекция. 
> • Если X конечно, то обозначим |X| = n. 
> ▶ Тогда существует биекция f : X → [1..n]. 
> пусть 
>
> $$
> g(y) = \begin{cases} f^{-1}(y), y < n \\ f^{-1}(n), y > n \end{cases}
> $$
>
>
> ▶ Легко видеть, что g: N → X — сюръекция.
>
> “3. ⇒ 2.”: Пусть g: N → X — сюръекция. 
> • Тогда у каждого элемента x ∈ X есть хотя бы один прообраз. 
> • Выберем из всех прообразов x наименьший. 
> • То есть положим f(x) = min{y ∈ N|g(y) = x}. 
> • Легко видеть, что f : X → N — инъекция.
>
> “2. ⇒ 1.”: Пусть f : X → N — инъекция. 
> • Рассмотрим множество f(X) = {f(x) | x ∈ X}. 
> • Тогда f : X →f(X) — биекция. 
> • Поскольку f(X) ⊂ N, множество f(X) не более чем счётно. 
> • Если f(X) — конечно, то и X — конечно. 
> • Если f(X) — счётно, то и X — счётно.

### Следствие 1

Если f : X →Y — инъекция и множество Y — счётно,
то множество X — не более чем счётно.

> ### Доказательство.
>
> • Пусть g: Y →N — биекция. 
> • Тогда g ◦f : X →N — инъекция.

### Следствие 2

Если g: Y →X — сюръекция и множество Y — счётно,
то множество X — не более чем счётно.

> ### Доказательство.
>
> • Пусть f : N → Y — биекция.
> • Тогда g ◦f : N →X — сюръекция.

# Билет 8

## Счётность множества рациональных чисел.

### Теорема

Множество Q всех рациональных чисел — счётно.

> Доказательство. Рассмотрим отображение g : Z × N → Q, задаваемое формулой g(a,b) = $\frac{a}{b}.$ 
> • Очевидно, что g — сюръекция. 
> • Следовательно, Q — не более, чем счётно. 
> • Но при этом множество Q — бесконечно. 
> • Таким образом, Q — счётно

# Билет 9

### Теорема

Объединение не более, чем счётного множества не более, чем счётных
множеств — не более чем счётно.

> Замечание
> То есть, если нам дана конечная или бесконечная последовательность множеств 
> $A_1,A_2,...,$ каждое из которых не более чем счётно, то множество $B = \cup A_i$ также не более чем счётно.

> ### Доказательство.
>
> Пусть $f_i$ : $A_i$ → N — инъекции. 
> • Для каждого x ∈ B пусть $s(x) = min\{n ∈ N|x ∈A_n\}$. 
> • Рассмотрим отображение h: B → N×N, задаваемое формулой 
> h(x)  = $(s(x),f_s(x)(x))$. 
> • Очевидно, что h — инъекция. 
> • Следовательно, B — не более, чем счётно.

> Замечание 
> В частности, объединение любого конечного или счётного набора счётных
> множеств всегда счётно.

# Билет 10

## Пример несчётного множества. Существование трансцендентных чисел.

> • Вещественное число α называется алгебраическим, если α — корень ненулевого
> многочлена с рациональными коэффициентами. 
> • В противном случае, число α называется *трансцендентным*.

### Пример несчётного множества

### Теорема

Множество [0,1) несчётно.

> ### Доказательство.
>
> Пусть f : N → [0,1) — биекция. 
> $\cdot$ Тогда [0,1) = $\{\alpha_1,\alpha_2,...\}$, где $\alpha_i$ = f(i). 
> $\cdot$ Запишем все $\alpha_i$ в виде бесконечных десятичных дробей: $\alpha_i = \sum^{\inf}_{j=1}\frac{a_{ij}}{10^j},$ \
> где $a_{ij} ∈ \{0,1,...,9\}$.
>
>  $\cdot$ Рассмотрим число $\beta = \sum^{\inf}_{i=1}\frac{b_i}{10^i},$ где $b_i = \begin{cases} 4, a_{ii}= 7 \\ 7, a_{ii} \ne 7  \end{cases} $ \
> $\cdot$ Тогда $\beta$ ∈ [0,1), но при этом $\beta$ не совпадает ни с одним из $\alpha_i$ (десятичные
 записи чисел $\beta$ и $\alpha_i$ отличаются в i-м разряде). Противоречие.


> ### Следствие
> Существуют трансцендентные числа (т.е. вещественные числа, не
 являющиеся алгебраическими).

# Билет 11

## Понятие мощности множества. Теорема о счётном подмножестве бесконечного множества.


###  Определение
 Множества X и Y называются равномощными (или эквивалентными), если
 существует биекция f : X → Y. Обозначение: X ∼ Y.
 
> $\cdot$ Мощностью конечного множества называется число его элементов. \
> $\cdot$ Множество X имеет мощность континуума, если X ∼ [0,1].

###  Теорема
В любом бесконечном множестве есть счётное подмножество.

> ### Доказательство.
> Пусть $X = X_0$ — бесконечное множество. \
> • Будем последовательно выбирать из $X_0$ элементы счётного
 подмножества A. \
> • Пусть $a_1 \in X_0$ и $X_1$= $X_0$ \ {$a_1$}. \
> ▶ Множество $X_1$ бесконечно, т.к. иначе X = $X_1$ $\cup$ {$a_1$} было бы конечно.
> • На k-м шаге из бесконечного множества $X_{k−1} \subset X$ выбираем элемент $a_k$ и полагаем $X_k$ = $X_{k−1}$ \ {$a_k$}. \
> ▶ Аналогично предыдущему, множество $X_k$ бесконечно. \
> • Продолжая этот процесс, получим последовательность ($a_i$) элементов множества X. \
> Тогда, множество A = {$a_1,a_2,...$}, составленное из членов построенной последовательности, будет счётным подмножеством X.


# Билет 12

## Формулировка аксиомы выбора. Примеры теорем, которые невозможно доказать без использования этой аксиомы.

### Аксиома выбора
>  Пусть S — множество непустых множеств, ∪S — объединение всех входящих в S множеств. Тогда существует функция f : S →∪S, такая, что f(x) ∈ x для каждого x ∈ S.

### Примеры теорем, которые невозможно доказать без использования этой аксиомы.

>• Фактически, мы уже использовали аксиому выбора в теореме о счётном объединении счётных множеств. \
> • Эти две теоремы (равно как и многие другие) невозможно доказать без аксиомы выбора
> 


# Билет 13

## Следствия об объединении и разности бесконечного множества и счётного множества. Примеры множеств мощности континуума.

### Следствие 1
 Если X бесконечно и Y не более чем счётно, то X ∪Y ∼ X.
 
> ###  Доказательство. Пусть B ⊂ X — счётное множество;
> • A=X \B иC =Y \X. \
> • Тогда X = A ∪ B и X ∪Y =A ∪ B ∪ C; \
> • при этом A ∩ B =A ∩ C =∅, \
> • множество C не более чем счётно и B ∪ C счётно. \
> • Рассмотрим биекцию f : B → B ∪C. \
> • Тогда отображение g: X → X ∪Y, задаваемое формулой \
> $g(x) = \begin{cases} f(x), x ∈ B \\ x, x ∈ A \end{cases}$ - Биекция 

### Следствие 2
Если X несчётно и Y не более чем счётно, то X \ Y ∼ X.
>### Доказательство. 
> Заметим, что X = (X \ Y) ∪ (X ∩ Y); \
> • множество X \ Y бесконечно и X ∩ Y — не более чем счётно.


### Примеры множеств мощности континуума
> ### Утверждение
> Пусть a,b ∈ R и a < b. Тогда множества \
> [a, b], [a, b), (a,b] и (a,b) имеют мощность континуума.

> ### Доказательство.
> • Функция f(x) = (b −a)x +a задает биекцию f : [0,1] → [a,b]. \
> • Множества [a,b), (a,b] и (a,b) равномощны [a,b], поскольку получаются удалением конечного числа элементов.

### Утверждение
 Множество R имеет мощность континуума.
>### Доказательство.
 Функция tg(x) задает биекцию из $(\frac{−\Pi}{2}, \frac{\Pi}{2})$на R
 
# Билет 14

##  Сравнение мощностей. Определение, теорема Кантора-Бернштейна (формулировка), континуум-гипотеза. Теорема Кантора о мощности множества всех подмножеств.

### Сравнение мощностей

#### Определение
> Мощность множества X больше мощности множества Y, если эти
 множества неравномощны и в X есть подмножество, равномощное Y.
 Обозначение: |X| > |Y|

### Теорема Кантора-Бернштейна
> Теорема (Г.Кантор, Ф.Бернштейн)
 Если существуют подмножества $X_0 \subset X$ и $Y_0 \subset Y$, такие, что $X_0 ∼ Y$ и
 $Y_0 ∼X$, то $X ∼Y$. (б/д)


 • Тем самым, утверждения |X| > |Y| и |Y| > |X| не могут быть выполнены
 одновременно. То есть сравнение мощностей корректно.
 • При помощи аксиомы выбора можно доказать, что любые две мощности
 сравнимы. То есть для любых X и Y выполнено ровно одно из следующих
 трех утверждений: либо |X| > |Y|, либо |Y| > |X|, либо X ∼ Y.
 
> • Мощность конечного множества тем больше, чем больше в нем элементов.
> • Мощность счётного множества больше мощности любого конечного множества.
> • Обратно, если |X| < a, то X конечно.
> • Мощность континуума больше мощности счетного множества.
> • Континуум-гипотеза. Не существует такого множества X, что a < |X| < c. (Мощность счётного множества обозначается через a. Мощность континуума обозначается через c.)
> • К.Гёдель и П.Коэн доказали, что континуум-гипотезу невозможно ни доказать ни опровергнуть в рамках формальной теории множеств.


### Теорема Кантора о мощности множества всех подмножеств

> ### Теорема
>  Теорема (Г.Кантор)
 Для любого множества X выполнено |P(X)| > |X|.

> ### Доказательство. 
> Пусть Y = {{x} | x ∈ X}. \
> • Очевидно, что Y ∼ X и Y ⊂P(X). \
> • Пусть f : X →P(X) — биекция. \
> • Рассмотрим множество $Z = \{x \in X |x \notin f(x)\}$. \
> • Пусть $z = f^{−1}(Z)$. Верно ли, что $z \in Z$? \
> ▶ Если $z \in Z$, то $z \in f(z)$, откуда $z \notin Z$; \
> ▶ если $z \notin Z$, то $z \notin f(z)$, откуда $z \in Z$. \
> • В любом случае получаем противоречие.


> ### Замечание
> • Из этой теоремы следует, что не существует множества наибольшей мощности. Следовательно, нет и множества всех множеств.
> • Для конечных множеств это означает, что $2^n$ всегда больше $n$.