	.data

.org     0x200
code_buffer: .byte    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
tape: .word    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

	.text

_start:
	movea.l  0x80, A0
	movea.l  0x84, A1
	movea.l  0x200, A2
	movea.l  0x240, A3

	clr.l    D1 ; счетчик команд

read_code:
	cmp.l    64, D1
	bge      overflow_error

	move.b   (A0), D0
	cmp.b    10, D0
	beq      code_ready

	move.b   D0, (A2)+
	add.l    1, D1
	jmp      read_code

code_ready:
	move.l   D1, D2
	movea.l  0x200, A2
	clr.l    D1
	clr.l    D3

validate_brackets:
	cmp.l    D2, D1
	beq      validate_done

	move.b   0(A2, D1), D0
	cmp.b    '[', D0
	beq      validate_open
	cmp.b    ']', D0
	beq      validate_close
	jmp      validate_next

validate_open:
	add.l    1, D3
	jmp      validate_next

validate_close:
	sub.l    1, D3
	blt      error
	jmp      validate_next

validate_next:
	add.l    1, D1
	jmp      validate_brackets

validate_done:
	cmp.l    0, D3
	bne      error
	clr.l    D6 ; указатель ленты
	clr.l    D1
	movea.l  0x200, A2

exec_loop:
	cmp.l    D2, D1
	beq      done

	move.b   0(A2, D1), D0

	cmp.b    '>', D0
	beq      cmd_gt
	cmp.b    '<', D0
	beq      cmd_lt
	cmp.b    '+', D0
	beq      cmd_plus
	cmp.b    '-', D0
	beq      cmd_minus
	cmp.b    '.', D0
	beq      cmd_dot
	cmp.b    ',', D0
	beq      cmd_comma
	cmp.b    '[', D0
	beq      cmd_lbracket
	cmp.b    ']', D0
	beq      cmd_rbracket
	cmp.b    ' ', D0
	beq      next_cmd
	cmp.b    9, D0
	beq      next_cmd
	cmp.b    13, D0
	beq      next_cmd

	jmp      error

cmd_gt:
	add.l    4, D6
	cmp.l    120, D6
	bge      error
	jmp      next_cmd

cmd_lt:
	sub.l    4, D6
	blt      error
	jmp      next_cmd

cmd_plus:
	move.l   0(A3, D6), D0
	add.l    1, D0
	move.l   D0, 0(A3, D6)
	jmp      next_cmd

cmd_minus:
	move.l   0(A3, D6), D0
	sub.l    1, D0
	move.l   D0, 0(A3, D6)
	jmp      next_cmd

cmd_dot:
	move.l   0(A3, D6), D0
	move.l   D0, (A1)
	jmp      next_cmd

cmd_comma:
	move.b   (A0), D0
	move.l   0(A3, D6), D5
	or.l     D0, D5
	move.l   D5, 0(A3, D6)
	jmp      next_cmd

cmd_lbracket:
	move.l   0(A3, D6), D0
	cmp.l    0, D0
	bne      next_cmd

	move.l   D1, D3
	add.l    1, D3
	move.l   1, D4

scan_fwd:
	cmp.l    D2, D3
	bge      error

	move.b   0(A2, D3), D0
	cmp.b    '[', D0
	beq      scan_fwd_inc
	cmp.b    ']', D0
	beq      scan_fwd_dec

scan_fwd_step:
	add.l    1, D3
	jmp      scan_fwd

scan_fwd_inc:
	add.l    1, D4
	jmp      scan_fwd_step

scan_fwd_dec:
	sub.l    1, D4
	beq      scan_fwd_found
	jmp      scan_fwd_step

scan_fwd_found:
	move.l   D3, D1
	jmp      next_cmd

cmd_rbracket:
	move.l   0(A3, D6), D0
	cmp.l    0, D0
	beq      next_cmd

	move.l   D1, D3
	sub.l    1, D3
	move.l   1, D4

scan_back:
	cmp.l    0, D3
	blt      error

	move.b   0(A2, D3), D0
	cmp.b    ']', D0
	beq      scan_back_inc
	cmp.b    '[', D0
	beq      scan_back_dec

scan_back_step:
	sub.l    1, D3
	jmp      scan_back

scan_back_inc:
	add.l    1, D4
	jmp      scan_back_step

scan_back_dec:
	sub.l    1, D4
	beq      scan_back_found
	jmp      scan_back_step

scan_back_found:
	move.l   D3, D1
	jmp      next_cmd

next_cmd:
	add.l    1, D1
	jmp      exec_loop

done:
	halt

error:
	move.l   -1, D0
	move.l   D0, (A1)
	halt

overflow_error:
	move.l   0xCCCCCCCC, D0
	move.l   D0, (A1)
	halt
