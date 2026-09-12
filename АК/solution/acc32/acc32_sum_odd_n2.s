    .data
    .org 0x256


inp_addr:        .word  0x80               
out_addr:        .word  0x84                
n:               .word  0x00               
kol:             .word  0x00               ; кол-во элементов в сумме
an:              .word  0x00               ; последнее число суммы              
const_1:         .word  0x01               
const_2:         .word  0x02               

    .text

_start:

    load                inp_addr
    load_acc
    store               n                                                
    ble                 er                        ; if n <= 0 => error
    beqz                er

all_ok:                        
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
    jmp finish

er:
    load_imm            -1
    jmp finish


of_error:
    load_imm            0xCCCCCCCC
    jmp finish

finish:
    store_ind           out_addr                  ; out answer
    halt