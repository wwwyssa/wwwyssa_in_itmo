subroutine BISECT(a, b, eps, f, x0, k)
    ! Интерфейс для функции f
    interface
        function f(x)
            real(16) :: f
            real(16), intent(in) :: x
        end function f
    end interface

    ! Объявление переменных
    real(16) :: x0, eps, a, b, an, bn, r, y
    integer :: k

    ! Инициализация
    k = 0          ! Счетчик итераций
    an = a         ! Начальная левая граница интервала
    bn = b         ! Начальная правая граница интервала
    r = f(a)       ! Значение функции в точке a

    do
        ! Вычисление середины интервала
        x0 = 0.5_16 * (an + bn)
        y = f(x0)

        ! Проверка, найден ли корень или интервал достаточно мал
        if ((y == 0.0_16) .or. ((bn - an) <= (2.0_16 * eps))) exit

        ! Обновление счетчика итераций и границ интервала
        k = k + 1
        if (sign(1.0_16, r) * sign(1.0_16, y) < 0.0_16) then
            bn = x0  ! Обновление правой границы интервала
        else
            an = x0  ! Обновление левой границы интервала
            r = y    ! Обновление значения функции в новой левой границе
        end if
    end do

end subroutine BISECT

program main
    ! Интерфейс для функции f
    interface
        function f(x)
            real(16) :: f
            real(16), intent(in) :: x
        end function f
    end interface

    ! Объявление переменных
    real(16) :: a, b, eps, x
    integer :: k

    ! Ввод данных с клавиатуры
    print *, "Введите начальную левую границу интервала (a):"
    read *, a
    print *, "Введите начальную правую границу интервала (b):"
    read *, b
    print *, "Введите точность вычислений (eps):"
    read *, eps

    ! Вызов подпрограммы BISECT для нахождения корня
    call BISECT(a, b, eps, f, x, k)

    ! Вывод результата
    print *, "Корень уравнения найден: "
    print *, "x =", x
    print *, "Число итераций:", k

end program main

function f(x)
    implicit none
    real(16) :: f
    real(16), intent(in) :: x

    ! Вычисление значения функции
    f = atan(x) - 1.0_16 / x
    return
end function f