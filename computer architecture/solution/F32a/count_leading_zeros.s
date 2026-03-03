    .data

input_addr:      .word  0x80
output_addr:     .word  0x84

    .text

_start:
    @p input_addr a! @ \ input_addr -> [] -> a

    solution

    @p output_addr a! ! \output_addr -> [] -> a -> out
    halt



solution:
    dup                  \ duplicate input
    if zero              \ if 0 -> zero
    dup                  \ duplicate input
    -if positive         
    negative ;
    
positive:
    dup
    lit 0 a!             \ 0 -> a

loop:
    2*                   \ Left Shift
    dup                  \ duplicate top
    -if continue         \ if 32s bit != 1 continue
    
    drop                 \ delete duplicate 
    a lit 1 +            \ ++a -> st.top()
    ;                    \ return
    
continue:
    a lit 1 + a!         \ inc a
    loop ;               

zero:
    drop                \delete duplicate
    lit 32
    ;

negative:
    drop
    lit 0
    ;