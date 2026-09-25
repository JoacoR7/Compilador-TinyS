.data
    #############################
    #       COMIENZO INIT       #
    #############################

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
        .word out_array_int
        .word out_array_str
        .word out_array_bool
        .word in_str
        .word in_int
        .word in_bool

    #############################
    #          FIN INIT         #
    #############################

    str_0:
        .asciiz "hola mundo"

.text
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

    #############################
    #          FIN INIT         #
    #############################


    .globl main

    main:
        # Prologue de start
        move $fp, $sp
        sw $ra, 0($fp)
        addiu $sp, $sp, -20
        # Inicializacion variables locales de start
        # Inicializacion de a
        li $a0, 8
        li $v0, 9
        syscall
        sw $zero, 0($v0)
        sw $zero, 4($v0)
        addiu $a0, $fp, -4
        sw $v0, 0($a0)
        # Inicializacion de b
        li $a0, 8
        li $v0, 9
        syscall
        sw $zero, 0($v0)
        sw $zero, 4($v0)
        addiu $a0, $fp, -8
        sw $v0, 0($a0)
        # Inicializacion de c
        li $a0, 8
        li $v0, 9
        syscall
        la $t0, empty_str
        sw $t0, 4($v0)
        la $t0, vtable_str
        sw $t0, 0($v0)
        addiu $a0, $fp, -12
        sw $v0, 0($a0)
        # Inicializacion de d
        li $a0, 8
        li $v0, 9
        syscall
        la $t0, empty_str
        sw $t0, 4($v0)
        la $t0, vtable_str
        sw $t0, 0($v0)
        addiu $a0, $fp, -16
        sw $v0, 0($a0)
        # Fin Prologue de start
        # Body de start
        li $a0 42
        li $a0 1
        la $a0 str_0
        la $a0 empty_str
        li $v0 10
        syscall

