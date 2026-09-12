    .data

inp_addr:        .word  0x80               ; Input address where n is stored
out_addr:        .word  0x84               ; Output address for the result
n:               .word  0x00               ; Input number
sum:             .word  0x00               ; Sum of odd numbers
counter:         .word  0x01               ; Loop counter starting at 1
const_1:         .word  0x01               ; Constant 1
const_2:         .word  0x02               ; Constant 2

    .text

_start:

    load         inp_addr
    load_acc
    store        n                           ; mem[n] = inp_val

    load_imm     0
    store        sum                         ; mem[sum] = 0

    load_imm     1
    store        counter                     ; mem[counter] = 1

loop:
    load         counter                     ; 
    sub          n                           ; 
    bgt          end                         ; if counter > n -> end 

    load         counter                     
    and          const_1                     ; 
    beqz         loop_next                   ; if (counter && 1 != 1) skip

    
    load         sum                         
    add          counter                     
    store        sum                         ; sum = sum + counter

loop_next:
    ; ++counter
    load         counter                     
    add          const_1                    
    store        counter                     
    jmp          loop                        

end:
    load         sum                         
    store_ind    out_addr                    
    halt
