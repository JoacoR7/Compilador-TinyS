.text
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