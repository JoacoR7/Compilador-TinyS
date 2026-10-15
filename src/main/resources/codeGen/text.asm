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
    # ========================================
    # PROLOGUE length_str
    # ========================================

    # Fijo $fp en la posicion donde se guardara el $ra
    move $fp $sp

    # Guardo la direccion de retorno de la llamada
    sw $ra 0($fp)

    # $sp apunta al siguiente word libre del frame
    addiu $sp $sp -4

    # ========================================
    # BODY length_str
    # ========================================

    # Cargo el self
    lw $a0 12($fp)

    # Recupero la direccion del string del CIR
    lw $a0 4($a0)

    li $t1 0

    inicio_loop_length_str:

    # Cargo el byte actual del Str
    lb $t0 0($a0)

    # Comparo el byte con \0 (fin de str)
    beqz $t0 fin_loop_length_str

    # Avanzo una posicion
    addiu $a0 $a0 1
    addiu $t1 $t1 1

    j inicio_loop_length_str

    fin_loop_length_str:
    li $a0 8
    li $v0 9
    syscall

    sw $t1 4($v0)
    sw $v0 4($fp)

    # ========================================
    # EPILOGUE length_str
    # ========================================

    # Recupero la direccion de retorno
    lw $ra 0($fp)

    #
    # 12($fp) -> Self
    # 8($fp) -> $fp llamador
    #  4($fp) -> return
    #  0($fp) -> $ra
    #

    # Recupero el return
    lw $a0 4($fp)

    # Restauro $sp al primer word libre del caller
    addiu $sp $fp 12

    # Restauro el frame pointer del caller
    lw $fp 8($fp)

    # Vuelvo a la instruccion siguiente al jalr
    jr $ra

concat_str:
    # ========================================
    # PROLOGUE concat_str
    # ========================================

    # Fijo $fp en la posicion donde se guardara el $ra
    move $fp $sp

    # Guardo la direccion de retorno de la llamada
    sw $ra 0($fp)

    # $sp apunta al siguiente word libre del frame
    addiu $sp $sp -4

    # ========================================
    # BODY concat_str
    # ========================================

    # Cargo el self
    lw $a0 16($fp)

    # Guardo el CIR en donde iria el self del metodo length
    sw $a0 0($sp)
    addiu $sp $sp -4

    # Guardo el fp actual para que length lo recupere
    sw $fp 0($sp)
    addiu $sp $sp -8

    jal length_str

    # Length me devolvio en $a0 el CIR con la longitud, lo guardo en la pila
    sw $a0 0($sp)
    addiu $sp $sp -4

    # Cargo el parametro, el CIR a concatenar con self
    lw $a0 4($fp)

    # Guardo el parametro en el self del metodo length
    sw $a0 0($sp)
    addiu $sp $sp -4

    # Guardo el fp actual para que el metodo length lo recupere luego
    sw $fp 0($sp)
    addiu $sp $sp -8

    jal length_str

    # Cargo el CIR con el length del self
    lw $t0 4($sp)
    # Recupero el length
    lw $t0 4($t0)

    # Recupero el length del parametro
    lw $a0 4($a0)

    # Sumo self.length y param1.length
    add $a0 $a0 $t0
    # Sumo un byte extra para el fin de cadena
    addiu $a0 $a0 1

    # Reservo ese espacio
    li $v0 9
    syscall

    # Guardo la direccion del heap con esa memoria reservada
    sw $v0 0($sp)
    addiu $sp $sp -4

    # Guardo memoria en el heap para el nuevo CIR concatenado
    li $a0 8
    li $v0 9
    syscall

    # Obtengo la direccion del Str (aun vacio, solo tengo la memoria reservada)
    lw $a0 4($sp)
    # Guardo esa direccion en el nuevo CIR
    sw $a0 4($v0)

    # Recupero la pila
    addiu $sp $sp 4

    # Cargo el self
    lw $a0 16($fp)
    # Cargo el str self
    lw $a0 4($a0)

    # Obtengo la direccion del heap del Str
    lw $a1 4($v0)
    inicio_loop_concat_str_loop_1:
    lb $t0 0($a0)
    beqz $t0 fin_loop_concat_str_loop_1

    sb $t0 0($a1)
    addiu $a0 $a0 1
    addiu $a1 $a1 1

    j inicio_loop_concat_str_loop_1

    fin_loop_concat_str_loop_1:
    # Cargo el param1
    lw $a0 4($fp)
    # Cargo el str de param1
    lw $a0 4($a0)

    inicio_loop_concat_str_loop_2:
    lb $t0 0($a0)
    sb $t0 0($a1)
    beqz $t0 fin_loop_concat_str_loop_2

    addiu $a0 $a0 1
    addiu $a1 $a1 1

    j inicio_loop_concat_str_loop_2
    fin_loop_concat_str_loop_2:

    la $t0 vtable_str
    # Cargo en el nuevo CIR la vtable de Str
    sw $t0 0($v0)
    sw $v0 8($fp)

    # ========================================
    # EPILOGUE concat_str
    # ========================================

    # Recupero la direccion de retorno
    lw $ra 0($fp)

    # concat_str tiene 1 parametro
    # 16($fp) -> Self
    # 12($fp) -> $fp llamador
    #  8($fp) -> return
    #  4($fp) -> Param1
    #  0($fp) -> $ra

    # Recupero el return
    lw $a0 8($fp)

    # Restauro $sp al primer word libre del caller
    addiu $sp $fp 16

    # Restauro el frame pointer del caller
    lw $fp 12($fp)

    # Vuelvo a la instruccion siguiente al jalr
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

runtime_null:
    la $a0 runtime_null_msg
    li $v0 4
    syscall

    move $a0 $a2
    li $v0 1
    syscall

    la $a0 runtime_null_msg2
    li $v0 4
    syscall

    move $a0 $a3
    li $v0 1
    syscall

    j exit

runtime_out_of_range:
    la $a0 runtime_out_of_range_msg
    li $v0 4
    syscall

    j exit

runtime_negative_index:
    la $a0 runtime_negative_index_msg
    li $v0 4
    syscall

    j exit



#############################
#          FIN INIT         #
#############################

