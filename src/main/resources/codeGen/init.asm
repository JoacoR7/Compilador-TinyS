.data
    empty_str: .asciiz ""
    true_str: .asciiz "true"
    false_str: .asciiz "false"

    vtable_str:
        .word length_str
        .word concat_str

    vtable_array_int:
        .word length_array
        .word has_next_array
        .word next_int_array

    vtable_array_str:
        .word length_array
        .word has_next_array
        .word next_str_array

    vtable_array_bool:
        .word length_array
        .word has_next_array
        .word next_bool_array

    vtable_IO:
        .word out_str
        .word out_int
        .word out_bool
        .word out_array_str
        .word out_array_int
        .word out_array_bool
        .word in_str
        .word in_int
        .word in_bool

.text
    out_int:
        # Empiezo out_int
        move $fp $sp
        sw $ra 0($fp)
        addiu $sp $sp -4

        # Se carga el parámetro int en $a0
        lw $a0 4($fp)
        li $v0 1
        syscall

        # Se restaura el $ra
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

    out_bool:
        # Empiezo out_int
        move $fp $sp
        sw $ra 0($fp)
        addiu $sp $sp -4

        # Se carga el parámetro bool en $a0
        lw $t0 4($fp)
        la $a0 true_str
        beqz $t0 load_false
        li $v0 4
        syscall

        # Se restaura el $ra
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

    load_false:
        la $a0 false_str
        jr $ra


