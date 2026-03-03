    .data
    .org 0x256


inp_addr:        .word  0x80               ; Input address where n is stored
out_addr:        .word  0x84               ; Output address for the result
n:               .word  0x00               ; Input number
kol:             .word  0x00               ; Number of terms
an:              .word  0x00               ; last number in sum
s:               .word  0x00               ;    
const_1:         .word  0x01               ; Constant 1
const_2:         .word  0x02               ; Constant 2

    .text

_start:

    load                inp_addr
    load_acc
    store               n                         ; mem[n] = inp_val
    load                n                         ; load n to check if n > 0
    ble                 er                        ; if n <= 0, error
    beqz                er

all_ok:
    load                n                         ; reload n for parity check
    and                 const_1
    beqz                even                      ; if even, go to even

odd:
    load                n
    add                 const_1
    div                 const_2
    store               kol                       ; kol = (n + 1) / 2
    load                n
    store               an                        ; an = n
    jmp                 outif


even:
    load                n
    div                 const_2
    store               kol                       ; kol = n / 2
    load                n
    sub                 const_1
    store               an                        ; an = n - 1


outif:
    load                an

    add                 const_1

    div                 const_2

    mul                 kol                       ; acc = (1 + an) / 2 * kol
    bvs    of_error                               ; check of
    store_ind           out_addr                  ; out answer
    halt

er:
    load_imm            -1
    store_ind           out_addr                    
    halt


of_error:
    load_imm            0xCCCCCCCC
    store_ind           out_addr                    
    halt