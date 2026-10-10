#############################
#       COMIENZO INIT       #
#############################

out_str:
    # ========================================
    # PROLOGUE out_str
    # ========================================

    # Fijo $fp en la posicion donde se guardara el $ra
    move $fp $sp

    # Guardo la direccion de retorno de la llamada
    sw $ra 0($fp)

    # $sp apunta al siguiente word libre del frame
    addiu $sp $sp -4


    # ========================================
    # BODY out_str
    # ========================================

    # Cargo el Param 1.
    # En out_str, Param 1 contiene la direccion del CIR a imprimir
    lw $a0 4($fp)

    # Recupero la direccion del string del CIR
    lw $a0 4($a0)

    # Syscall 4: imprimir string
    li $v0 4
    syscall


    # ========================================
    # EPILOGUE out_str
    # ========================================

    # Recupero la direccion de retorno
    lw $ra 0($fp)

    # out_str tiene 1 parametro:
    #
    # 16($fp) -> Self
    # 12($fp) -> $fp llamador
    #  8($fp) -> return
    #  4($fp) -> Param 1
    #  0($fp) -> $ra
    #
    # Restauro $sp al primer word libre del caller
    addiu $sp $fp 16

    # Restauro el frame pointer del caller
    lw $fp 12($fp)

    # Vuelvo a la instruccion siguiente al jalr
    jr $ra



out_int:
    # ========================================
    # PROLOGUE out_int
    # ========================================

    # Fijo $fp en la posicion donde se guardara el $ra
    move $fp $sp

    # Guardo la direccion de retorno de la llamada
    sw $ra 0($fp)

    # $sp apunta al siguiente word libre del frame
    addiu $sp $sp -4


    # ========================================
    # BODY out_int
    # ========================================

    # Cargo el Param 1.
    # En out_int, Param 1 contiene el CIR a imprimir
    lw $a0 4($fp)

    # Recupero el int del CIR
    lw $a0 4($a0)

    # Syscall 1: imprimir entero
    li $v0 1
    syscall


    # ========================================
    # EPILOGUE out_int
    # ========================================

    # Recupero la direccion de retorno
    lw $ra 0($fp)

    # out_int tiene 1 parametro:
    #
    # 16($fp) -> Self
    # 12($fp) -> $fp llamador
    #  8($fp) -> return
    #  4($fp) -> Param 1
    #  0($fp) -> $ra
    #
    # Restauro $sp al primer word libre del caller
    addiu $sp, $fp, 16

    # Restauro el frame pointer del caller
    lw $fp, 12($fp)

    # Vuelvo a la instruccion siguiente al jalr
    jr $ra

out_bool:
    # ========================================
    # PROLOGUE out_bool
    # ========================================

    # Fijo $fp en la posicion donde se guardara el $ra
    move $fp $sp

    #Guardo la direccion de retorno de la llamada
    sw $ra 0($fp)

    # $sp apunta al siguiente word libre del frame
    addiu $sp $sp -4

    # ========================================
    # BODY out_bool
    # ========================================

    # Cargo el Param 1.
    # En out_int, Param 1 contiene CIR a imprimir
    lw $t0 4($fp)

    # Recupero el booleano (0 o 1) a imprimir
    lw $t0 4($t0)

    # Se evalua si es 0 o 1 para cargar el string correcto a imprimir
    la $a0 true_str
    beqz $t0 load_false

    out_bool_syscall:

    # Syscall 4: imprimir string
    li $v0 4
    syscall

    # ========================================
    # EPILOGUE out_bool
    # ========================================

    # Recupero la direccion de retorno
    lw $ra, 0($fp)

    # out_bool tiene 1 parametro:
    #
    # 16($fp) -> Self
    # 12($fp) -> $fp llamador
    #  8($fp) -> return
    #  4($fp) -> Param 1
    #  0($fp) -> $ra
    #
    # Restauro $sp al primer word libre del caller
    addiu $sp, $fp, 16

    # Restauro el frame pointer del caller
    lw $fp, 12($fp)

    # Vuelvo a la instruccion siguiente al jalr
    jr $ra

load_false:
    la $a0 false_str
    j out_bool_syscall

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

