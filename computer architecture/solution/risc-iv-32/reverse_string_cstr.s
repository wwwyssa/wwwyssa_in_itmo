.data
buffer:          .byte  '________________________________'


.text
    .org 0x88

_start:
    lui      sp, 1                         ; sp = 0x1000 
    addi     t0, zero, 0x80                ; t0 = 0x80 
    addi     t1, zero, 0x84                ; t1 = 0x84 
    addi     t5, zero, 0                   ; t5 = 0 
    addi     a0, zero, 0                   ; a0 = 0 
    addi     t3, zero, 10                  ; t3 = 10 '\n'
    addi     t4, zero, 31                  ; t4 = 31
    addi     s0, zero, 1                   ; s0 = 1 
    addi     s1, zero, 0                   ; s1 = 0 

read_loop:
    lb       t2, 0(t0)
    beq      t2, t3, start_pop_loop        ; '\n' -> stop read -> reverse
    beqz     t2, found_nul                 ; '\0' -> s1=1 -> reverse -> copy_past
    beq      a0, t4, overflow              ; of

    addi     sp, sp, -4
    sw       t2, 0(sp)                     
    addi     a0, a0, 1                     ; a++
    j        read_loop

found_nul:
    addi     s1, zero, 1

start_pop_loop:
pop_loop:
    beqz     a0, read_done                 ; a=0 -> break
    lw       t2, 0(sp)                     
    addi     sp, sp, 4                     
    addi     a0, a0, -1                    ; st.pop() -> t2
    
    beqz     s0, skip
    sb       t2, 0(t1)                     ; t2 -> out

skip:
    sb       t2, 0(t5)                     
    addi     t5, t5, 1                     
    j        pop_loop                      

read_done:
    beqz     s1, end
    
    sb       zero, 0(t5)                   
    addi     t5, t5, 1


copy_past:
    lb       t2, 0(t0)                     
    beq      t2, t3, end
    sb       t2, 0(t5)                     
    addi     t5, t5, 1                     
    j        copy_past

end:
    sb       zero, 0(t5)                  
    halt                                   

overflow:
    lui     t2, %hi(0xCCCCCCCC)
    addi    t2, t2, %lo(0xCCCCCCCC)             
    sw      t2, 0(t1)                     
    j       end                                  