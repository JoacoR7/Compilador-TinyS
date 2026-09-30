.data
    #############################
    #       COMIENZO INIT       #
    #############################

    # ========================= #
    #     STRINGS PREDEFINIDOS  #
    # ========================= #

    empty_str: .asciiz ""
    true_str: .asciiz "true"
    false_str: .asciiz "false"
    runtime_null_msg: .asciiz "Error: referencia nula\n"
    runtime_bounds_msg: .asciiz "Error: indice fuera de rango\n"
    runtime_div_zero_msg: .asciiz "Error: division por cero\n"

    # ========================= #
    #          VTABLES          #
    # ========================= #

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
        .word out_array_int
        .word out_array_str
        .word out_array_bool
        .word in_str
        .word in_int
        .word in_bool

    #############################
    #          FIN INIT         #
    #############################


.text
    .globl main

    main:
        # Prologue de start
        move $fp, $sp
        sw $ra, 0($fp)
        addiu $sp, $sp, -8
        # Inicializacion variables locales de start
        # Inicializacion de a
        li $a0, 8
        li $v0, 9
        syscall
        sw $zero, 0($v0)
        sw $zero, 4($v0)
        addiu $a0, $fp, -4
        sw $v0, 0($a0)
        # Fin Prologue de start
        # Body de start
        li $a0 5
        sw $a0 0($sp)
        addiu $sp $sp -4
        li $a0 3
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Checkeo division por 0
        beqz $a0 runtime_div_zero
        div $a0 $t0 $a0
        lw $t0, -4($fp)
        sw $a0, 4($t0)
        li $v0 10
        syscall

    #############################
    #       COMIENZO INIT       #
    #############################

    out_str:
        # Empiezo out_str
        move $fp $sp
        sw $ra 0($fp)
        addiu $sp $sp -4

        # Se carga la direccion de memoria del string en $a0
        lw $a0 4($fp)
        li $v0 4
        syscall

        # Se restaura el $ra
        lw $ra 0($fp)
        addiu $sp $sp 4
        jr $ra

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
        # Empiezo out_bool
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

    length_str:
        jr $ra

    concat_str:
        jr $ra

    length_array:
        jr $ra

    has_next_array:
        jr $ra

    next_int_array:
        jr $ra

    next_str_array:
        jr $ra

    next_bool_array:
        jr $ra

    out_array_int:
        jr $ra

    out_array_str:
        jr $ra

    out_array_bool:
        jr $ra

    in_str:
        jr $ra

    in_int:
        jr $ra

    in_bool:
        jr $ra


    # ========================= #
    #          UTILS            #
    # ========================= #
    exit:
        li $v0, 10
        syscall

    # ========================= #
    #     RUNTIME EXCEPTIONS    #
    # ========================= #

    runtime_div_zero:
        la $a0, runtime_div_zero_msg
        li $v0, 4
        syscall

        j exit

    #############################
    #          FIN INIT         #
    #############################


