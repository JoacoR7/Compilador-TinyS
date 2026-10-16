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
    runtime_null_msg: .asciiz "Error: referencia a una instancia no inicializada en linea "
    runtime_null_msg2: .asciiz ", columna "
    runtime_out_of_range_msg: .asciiz "Error: indice fuera de rango\n"
    runtime_negative_index_msg: .asciiz "Error: indice negativo\n"
    runtime_div_zero_msg: .asciiz "Error: division por cero\n"
    runtime_invalid_bool_msg: .asciiz "Error: ingreso booleano no valido\n"

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

    str_0:
        .asciiz "========================================\n"
    str_1:
        .asciiz " TEST DE OPERACIONES BINARIAS\n"
    str_2:
        .asciiz "Valores:\n"
    str_3:
        .asciiz "a = "
    str_4:
        .asciiz "\n"
    str_5:
        .asciiz "b = "
    str_6:
        .asciiz "c = "
    str_7:
        .asciiz "\n\n"
    str_8:
        .asciiz "--- SUMA ---\n"
    str_9:
        .asciiz "a(20) + b(6) = "
    str_10:
        .asciiz "   esperado: 26\n"
    str_11:
        .asciiz "b(6) + c(3) = "
    str_12:
        .asciiz "   esperado: 9\n\n"
    str_13:
        .asciiz "--- RESTA ---\n"
    str_14:
        .asciiz "a(20) - b(6) = "
    str_15:
        .asciiz "   esperado: 14\n"
    str_16:
        .asciiz "b(6) - a(20) = "
    str_17:
        .asciiz "   esperado: -14\n\n"
    str_18:
        .asciiz "--- MULTIPLICACION ---\n"
    str_19:
        .asciiz "a(20) * b(6) = "
    str_20:
        .asciiz "   esperado: 120\n"
    str_21:
        .asciiz "b(6) * c(3) = "
    str_22:
        .asciiz "   esperado: 18\n\n"
    str_23:
        .asciiz "--- DIVISION ---\n"
    str_24:
        .asciiz "a(20) / b(6) = "
    str_25:
        .asciiz "   esperado: 3\n"
    str_26:
        .asciiz "a(20) / c(3) = "
    str_27:
        .asciiz "   esperado: 6\n"
    str_28:
        .asciiz "b(6) / c(3) = "
    str_29:
        .asciiz "   esperado: 2\n\n"
    str_30:
        .asciiz "--- IGUALDAD == ---\n"
    str_31:
        .asciiz "a(20) == a(20) = "
    str_32:
        .asciiz "   esperado: true\n"
    str_33:
        .asciiz "a(20) == b(6) = "
    str_34:
        .asciiz "   esperado: false\n\n"
    str_35:
        .asciiz "--- DISTINTO != ---\n"
    str_36:
        .asciiz "a(20) != b(6) = "
    str_37:
        .asciiz "a(20) != a(20) = "
    str_38:
        .asciiz "--- MENOR < ---\n"
    str_39:
        .asciiz "a(20) < b(6) = "
    str_40:
        .asciiz "   esperado: false\n"
    str_41:
        .asciiz "b(6) < a(20) = "
    str_42:
        .asciiz "   esperado: true\n\n"
    str_43:
        .asciiz "--- MENOR O IGUAL <= ---\n"
    str_44:
        .asciiz "a(20) <= b(6) = "
    str_45:
        .asciiz "a(20) <= a(20) = "
    str_46:
        .asciiz "b(6) <= a(20) = "
    str_47:
        .asciiz "--- MAYOR > ---\n"
    str_48:
        .asciiz "a(20) > b(6) = "
    str_49:
        .asciiz "b(6) > a(20) = "
    str_50:
        .asciiz "--- MAYOR O IGUAL >= ---\n"
    str_51:
        .asciiz "a(20) >= b(6) = "
    str_52:
        .asciiz "a(20) >= a(20) = "
    str_53:
        .asciiz "b(6) >= a(20) = "
    str_54:
        .asciiz "--- AND && ---\n"
    str_55:
        .asciiz "true && true = "
    str_56:
        .asciiz "true && false = "
    str_57:
        .asciiz "false && true = "
    str_58:
        .asciiz "false && false = "
    str_59:
        .asciiz "--- OR || ---\n"
    str_60:
        .asciiz "true || true = "
    str_61:
        .asciiz "true || false = "
    str_62:
        .asciiz "false || true = "
    str_63:
        .asciiz "false || false = "
    str_64:
        .asciiz "--- EXPRESIONES ANIDADAS ---\n"
    str_65:
        .asciiz "a(20) + b(6) * c(3) = "
    str_66:
        .asciiz "   esperado: 38\n"
    str_67:
        .asciiz "(a(20) + b(6)) * c(3) = "
    str_68:
        .asciiz "   esperado: 78\n"
    str_69:
        .asciiz "a(20) - b(6) - c(3) = "
    str_70:
        .asciiz "   esperado: 11\n"
    str_71:
        .asciiz "a(20) / b(6) + c(3) = "
    str_72:
        .asciiz "   esperado: 6\n\n"
    str_73:
        .asciiz "--- COMPARACIONES Y LOGICA ANIDADAS ---\n"
    str_74:
        .asciiz "(a(20) > b(6)) && (b(6) > c(3)) = "
    str_75:
        .asciiz "(a(20) < b(6)) || (b(6) > c(3)) = "
    str_76:
        .asciiz "(a(20) == b(6)) || (b(6) == b(6)) = "
    str_77:
        .asciiz "(a(20) != b(6)) && (b(6) != c(3)) = "
    str_78:
        .asciiz " FIN TEST OPERACIONES BINARIAS\n"

.text
    .globl main

    main:

        # ========================================
        # PROLOGUE start
        # ========================================
        # Registro de activacion del metodo:
        # self         -> 12($fp)
        # $fp llamador -> 8($fp)
        # return       -> 4($fp)
        # $ra          -> 0($fp)
        # Var 1        -> -4($fp)
        # Var 2        -> -8($fp)
        # Var 3        -> -12($fp)
        # Var 4        -> -16($fp)
        # Var 5        -> -20($fp)

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 24 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -24


        # --- INICIALIZACION DE VARIABLES LOCALES ---
        # Inicializacion variables locales de start
        # Inicializacion de a
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -4($fp)
        # Inicializacion de b
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -8($fp)
        # Inicializacion de c
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -12($fp)
        # Inicializacion de t
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -16($fp)
        # Inicializacion de f
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -20($fp)

        # $sp queda apuntando al primer word libre del frame

        # Body de start
        # Asignacion a "a" en 8_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 20
        li $a0 20
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -4
        # Checkeo acceso nulo
        li $a2 8
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "b" en 9_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 6
        li $a0 6
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -8
        # Checkeo acceso nulo
        li $a2 9
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "c" en 10_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 3
        li $a0 3
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -12
        # Checkeo acceso nulo
        li $a2 10
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "t" en 12_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal true
        li $a0 1
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -16
        # Checkeo acceso nulo
        li $a2 12
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "f" en 13_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal false
        li $a0 0
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -20
        # Checkeo acceso nulo
        li $a2 13
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)

        # --- Comienzo llamada 19_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "========================================\n"
        la $a0 str_0
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 20_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal " TEST DE OPERACIONES BINARIAS\n"
        la $a0 str_1
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 21_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "========================================\n"
        la $a0 str_0
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 23_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "Valores:\n"
        la $a0 str_2
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 25_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a = "
        la $a0 str_3
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 26_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 27_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "\n"
        la $a0 str_4
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 29_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b = "
        la $a0 str_5
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 30_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 31_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "\n"
        la $a0 str_4
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 33_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "c = "
        la $a0 str_6
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 34_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 35_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "\n\n"
        la $a0 str_7
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 41_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- SUMA ---\n"
        la $a0 str_8
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 43_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) + b(6) = "
        la $a0 str_9
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 44_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria + en 44_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Sumo los operandos
        add $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 45_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 26\n"
        la $a0 str_10
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 47_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) + c(3) = "
        la $a0 str_11
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 48_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria + en 48_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Sumo los operandos
        add $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 49_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 9\n\n"
        la $a0 str_12
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 55_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- RESTA ---\n"
        la $a0 str_13
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 57_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) - b(6) = "
        la $a0 str_14
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 58_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria - en 58_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Resto el operando derecho al izquierdo
        sub $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 59_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 14\n"
        la $a0 str_15
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 61_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) - a(20) = "
        la $a0 str_16
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 62_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria - en 62_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Resto el operando derecho al izquierdo
        sub $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 63_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: -14\n\n"
        la $a0 str_17
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 69_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- MULTIPLICACION ---\n"
        la $a0 str_18
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 71_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) * b(6) = "
        la $a0 str_19
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 72_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria * en 72_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Multiplico los operandos
        mul $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 73_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 120\n"
        la $a0 str_20
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 75_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) * c(3) = "
        la $a0 str_21
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 76_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria * en 76_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Multiplico los operandos
        mul $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 77_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 18\n\n"
        la $a0 str_22
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 83_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- DIVISION ---\n"
        la $a0 str_23
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 85_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) / b(6) = "
        la $a0 str_24
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 86_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria / en 86_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Checkeo division por 0
        beqz $a0 runtime_div_zero
        # Divido el operando izquierdo por el derecho
        div $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 87_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 3\n"
        la $a0 str_25
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 89_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) / c(3) = "
        la $a0 str_26
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 90_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria / en 90_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Checkeo division por 0
        beqz $a0 runtime_div_zero
        # Divido el operando izquierdo por el derecho
        div $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 91_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 6\n"
        la $a0 str_27
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 93_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) / c(3) = "
        la $a0 str_28
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 94_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria / en 94_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Checkeo division por 0
        beqz $a0 runtime_div_zero
        # Divido el operando izquierdo por el derecho
        div $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 95_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 2\n\n"
        la $a0 str_29
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 101_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- IGUALDAD == ---\n"
        la $a0 str_30
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 103_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) == a(20) = "
        la $a0 str_31
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 104_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria == en 104_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son iguales
        seq $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 105_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 107_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) == b(6) = "
        la $a0 str_33
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 108_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria == en 108_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son iguales
        seq $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 109_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n\n"
        la $a0 str_34
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 115_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- DISTINTO != ---\n"
        la $a0 str_35
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 117_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) != b(6) = "
        la $a0 str_36
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 118_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria != en 118_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son distintos
        sne $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 119_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 121_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) != a(20) = "
        la $a0 str_37
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 122_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria != en 122_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son distintos
        sne $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 123_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n\n"
        la $a0 str_34
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 129_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- MENOR < ---\n"
        la $a0 str_38
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 131_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) < b(6) = "
        la $a0 str_39
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 132_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria < en 132_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es menor que el derecho
        slt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 133_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n"
        la $a0 str_40
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 135_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) < a(20) = "
        la $a0 str_41
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 136_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria < en 136_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es menor que el derecho
        slt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 137_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n\n"
        la $a0 str_42
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 143_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- MENOR O IGUAL <= ---\n"
        la $a0 str_43
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 145_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) <= b(6) = "
        la $a0 str_44
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 146_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria <= en 146_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es menor o igual que el derecho
        sle $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 147_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n"
        la $a0 str_40
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 149_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) <= a(20) = "
        la $a0 str_45
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 150_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria <= en 150_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es menor o igual que el derecho
        sle $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 151_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 153_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) <= a(20) = "
        la $a0 str_46
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 154_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria <= en 154_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es menor o igual que el derecho
        sle $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 155_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n\n"
        la $a0 str_42
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 161_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- MAYOR > ---\n"
        la $a0 str_47
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 163_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) > b(6) = "
        la $a0 str_48
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 164_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria > en 164_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor que el derecho
        sgt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 165_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 167_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) > a(20) = "
        la $a0 str_49
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 168_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria > en 168_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor que el derecho
        sgt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 169_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n\n"
        la $a0 str_34
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 175_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- MAYOR O IGUAL >= ---\n"
        la $a0 str_50
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 177_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) >= b(6) = "
        la $a0 str_51
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 178_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria >= en 178_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor o igual que el derecho
        sge $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 179_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 181_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) >= a(20) = "
        la $a0 str_52
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 182_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria >= en 182_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor o igual que el derecho
        sge $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 183_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 185_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "b(6) >= a(20) = "
        la $a0 str_53
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 186_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria >= en 186_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor o igual que el derecho
        sge $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 187_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n\n"
        la $a0 str_34
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 193_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- AND && ---\n"
        la $a0 str_54
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 195_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "true && true = "
        la $a0 str_55
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 196_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria && en 196_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo AND logico entre los operandos
        and $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 197_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 199_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "true && false = "
        la $a0 str_56
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 200_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria && en 200_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo AND logico entre los operandos
        and $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 201_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n"
        la $a0 str_40
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 203_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "false && true = "
        la $a0 str_57
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 204_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria && en 204_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo AND logico entre los operandos
        and $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 205_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n"
        la $a0 str_40
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 207_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "false && false = "
        la $a0 str_58
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 208_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria && en 208_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo AND logico entre los operandos
        and $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 209_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n\n"
        la $a0 str_34
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 215_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- OR || ---\n"
        la $a0 str_59
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 217_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "true || true = "
        la $a0 str_60
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 218_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria || en 218_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo OR logico entre los operandos
        or $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 219_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 221_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "true || false = "
        la $a0 str_61
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 222_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria || en 222_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo OR logico entre los operandos
        or $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 223_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 225_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "false || true = "
        la $a0 str_62
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 226_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria || en 226_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -16($fp)
        # Checkeo acceso nulo
        li $a2 5
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo OR logico entre los operandos
        or $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 227_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 229_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "false || false = "
        la $a0 str_63
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 230_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria || en 230_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 6
        li $a3 10
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo OR logico entre los operandos
        or $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 231_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: false\n\n"
        la $a0 str_34
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 238_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- EXPRESIONES ANIDADAS ---\n"
        la $a0 str_64
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 240_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) + b(6) * c(3) = "
        la $a0 str_65
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 241_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria + en 241_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Comienzo expresion binaria * en 241_23
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Multiplico los operandos
        mul $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Sumo los operandos
        add $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 242_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 38\n"
        la $a0 str_66
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 244_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "(a(20) + b(6)) * c(3) = "
        la $a0 str_67
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 245_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria * en 245_25
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria + en 245_20
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Sumo los operandos
        add $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Multiplico los operandos
        mul $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 246_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 78\n"
        la $a0 str_68
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 248_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) - b(6) - c(3) = "
        la $a0 str_69
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 249_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria - en 249_23
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria - en 249_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Resto el operando derecho al izquierdo
        sub $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Resto el operando derecho al izquierdo
        sub $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 250_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 11\n"
        la $a0 str_70
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 252_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "a(20) / b(6) + c(3) = "
        la $a0 str_71
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 253_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_int ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria + en 253_23
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria / en 253_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Checkeo division por 0
        beqz $a0 runtime_div_zero
        # Divido el operando izquierdo por el derecho
        div $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Sumo los operandos
        add $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 254_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: 6\n\n"
        la $a0 str_72
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 260_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "--- COMPARACIONES Y LOGICA ANIDADAS ---\n"
        la $a0 str_73
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 262_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "(a(20) > b(6)) && (b(6) > c(3)) = "
        la $a0 str_74
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 263_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria && en 263_26
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria > en 263_21
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor que el derecho
        sgt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Comienzo expresion binaria > en 263_32
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor que el derecho
        sgt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo AND logico entre los operandos
        and $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 264_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 266_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "(a(20) < b(6)) || (b(6) > c(3)) = "
        la $a0 str_75
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 267_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria || en 267_26
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria < en 267_21
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es menor que el derecho
        slt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Comienzo expresion binaria > en 267_32
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si el operando izquierdo es mayor que el derecho
        sgt $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo OR logico entre los operandos
        or $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 268_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 270_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "(a(20) == b(6)) || (b(6) == b(6)) = "
        la $a0 str_76
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 271_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria || en 271_27
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria == en 271_21
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son iguales
        seq $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Comienzo expresion binaria == en 271_33
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son iguales
        seq $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo OR logico entre los operandos
        or $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 272_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n"
        la $a0 str_32
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 274_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "(a(20) != b(6)) && (b(6) != c(3)) = "
        la $a0 str_77
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 275_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_bool ---
        # Evaluo parametro n° 1
        # Comienzo expresion binaria && en 275_27
        # Obtengo el valor del nodo izquierdo
        # Comienzo expresion binaria != en 275_21
        # Obtengo el valor del nodo izquierdo
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 2
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son distintos
        sne $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Comienzo expresion binaria != en 275_33
        # Obtengo el valor del nodo izquierdo
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 3
        li $a3 9
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 4
        li $a3 9
        beqz $a0 runtime_null
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Comparo si los operandos son distintos
        sne $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Obtengo el valor del CIR
        lw $a0 4($a0)
        # Recupero el valor del nodo izquierdo
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Obtengo el valor del CIR
        lw $t0 4($t0)
        # Realizo AND logico entre los operandos
        and $a0 $t0 $a0
        # Guardo en la pila el valor
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero nuevo CIR
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Recupero el valor de la expresion binaria
        # Guardo en el CIR el valor
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 276_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "   esperado: true\n\n"
        la $a0 str_42
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 279_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "========================================\n"
        la $a0 str_0
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 280_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal " FIN TEST OPERACIONES BINARIAS\n"
        la $a0 str_78
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 281_6 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL out_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de out_str ---
        # Evaluo parametro n° 1
        # Genero el literal "========================================\n"
        la $a0 str_0
        # Guardo el literal en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un CIR para el literal
        li $a0 8
        li $v0 9
        syscall
        la $t0 vtable_str
        sw $t0 0($v0)
        la $t0 empty_str
        sw $t0 4($v0)
        # Recupero el literal de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el literal en el CIR
        sw $t0 4($v0)
        move $a0 $v0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0
        li $v0 10
        syscall

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
        # ========================================
        # PROLOGUE length_array
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

        # Como la longitud en el CIR del array es solo un literal, genero un nuevo CIR para int
        li $a0 8
        li $v0 9
        syscall

        # Cargo el self
        lw $a0 12($fp)

        # Cargo el valor de la longitud
        lw $a0 4($a0)

        # Lo guardo en el nuevo CIR
        sw $a0 4($v0)

        # Guardo la direccion del CIR en el return
        sw $v0 4($fp)

        # ========================================
        # EPILOGUE length_array
        # ========================================

        # Recupero la direccion de retorno
        lw $ra 0($fp)

        #
        # 12($fp) -> Self
        # 8($fp) -> $fp llamador
        #  4($fp) -> return
        #  0($fp) -> $ra

        # Recupero el return
        lw $a0 4($fp)

        # Restauro $sp al primer word libre del caller
        addiu $sp $fp 12

        # Restauro el frame pointer del caller
        lw $fp 8($fp)

        # Vuelvo a la instruccion siguiente al jalr
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
        # ========================================
        # PROLOGUE in_str
        # ========================================

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp $sp

        # Guardo la direccion de retorno de la llamada
        sw $ra 0($fp)

        # $sp apunta al siguiente word libre del frame
        addiu $sp $sp -4

        # ========================================
        # BODY in_str
        # ========================================

        # Reservo 1026 bytes en el heap (1204 caracteres y \n\0)
        li $a0 1026
        li $v0 9
        syscall

        # Guardo la direccion para el syscall de lectura de STR
        move $a0 $v0

        # 1026 bytes para el buffer
        li $a1 1026
        li $v0 8
        syscall

        # Guardo el valor leido en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4

        # Genero el CIR
        li $a0 8
        li $v0 9
        syscall

        # Recupero el valor leido de la pila
        lw $a0 4($sp)
        addiu $sp $sp 4

        # Guardo el valor leido en el CIR
        sw $a0 4($v0)

        # Guardo la vtable en el CIR
        la $a0 vtable_str
        sw $a0 0($v0)

        # Guardo el CIR en el return
        sw $v0 4($fp)

        # ========================================
        # EPILOGUE in_str
        # ========================================

        # Recupero la direccion de retorno
        lw $ra 0($fp)

        # 12($fp) -> Self
        # 8($fp) -> $fp llamador
        #  4($fp) -> return
        #  0($fp) -> $ra

        # Recupero el return
        lw $a0 4($fp)

        # Restauro $sp al primer word libre del caller
        addiu $sp, $fp, 16

        # Restauro el frame pointer del caller
        lw $fp, 12($fp)

        # Vuelvo a la instruccion siguiente al jalr
        jr $ra


    in_int:
        # ========================================
        # PROLOGUE in_int
        # ========================================

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp $sp

        # Guardo la direccion de retorno de la llamada
        sw $ra 0($fp)

        # $sp apunta al siguiente word libre del frame
        addiu $sp $sp -4


        # ========================================
        # BODY in_int
        # ========================================

        # Cargo el syscall para leer int
        li $v0 5
        syscall

        # Guardo el resultado en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4

        # Genero un CIR entero
        li $a0 8
        li $v0 9
        syscall

        # Recupero la lectura de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4

        # Lo guardo en la posicion value del CIR
        sw $t0 4($v0)

        # Guardo la direccion del CIR en el return
        sw $v0 4($fp)

        # ========================================
        # EPILOGUE in_int
        # ========================================

        # Recupero la direccion de retorno
        lw $ra 0($fp)

        # 12($fp) -> Self
        # 8($fp) -> $fp llamador
        #  4($fp) -> return
        #  0($fp) -> $ra

        # Recupero el return
        lw $a0 4($fp)

        # Restauro $sp al primer word libre del caller
        addiu $sp, $fp, 12

        # Restauro el frame pointer del caller
        lw $fp, 8($fp)

        # Vuelvo a la instruccion siguiente al jalr
        jr $ra

    in_bool:
        # ========================================
        # PROLOGUE in_bool
        # ========================================

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp $sp

        # Guardo la direccion de retorno de la llamada
        sw $ra 0($fp)

        # $sp apunta al siguiente word libre del frame
        addiu $sp $sp -4

        # ========================================
        # BODY in_bool
        # ========================================

        # Reservo 7 bytes en el heap (false\n\0)
        li $a0 7
        li $v0 9
        syscall

        # Guardo la direccion para el syscall de lectura de STR
        move $a0 $v0

        # 7 bytes para el buffer
        li $a1 7
        li $v0 8
        syscall

        # Guardo el valor leido en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4

        # Cargo true_str para comparar
        la $a1 true_str

        # t2 sera el resultado de la comparacion, si es true se queda en 1, si es false pasa a ser 0
        li $t2 1

        # Comparo si lo ingresado es vacio
        lb $t0 0($a0)
        li $t1 10
        beq $t0 $t1 error_comparacion

        comparacion_true:

        # Cargo el byte actual de la posicion
        lb $t0 0($a0)
        lb $t1 0($a1)

        # Si true_str ya llego a fin de linea, comparo salto de linea
        beqz $t1 comparacion_salto_linea

        # Adelanto una posicion en el puntero
        addiu $a0 $a0 1
        addiu $a1 $a1 1

        beq $t0 $t1 comparacion_true

        # Cargo el string de false
        la $a1 false_str
        # $t2 pasa a ser 0 por si es false el input
        li $t2 0

        # Recupero el input de la fila
        lw $a0 4($sp)
        addiu $sp $sp 4

        comparacion_false:
        # Obtengo el byte actual
        lb $t0 0($a0)
        lb $t1 0($a1)

        # Si false_str ya llego a fin de linea, comparo salto de linea
        beqz $t1 comparacion_salto_linea

        addiu $a0 $a0 1
        addiu $a1 $a1 1

        beq $t0 $t1 comparacion_false

        comparacion_salto_linea:
        # Comparo con salto de linea
        li $t1 10
        beq $t0 $t1 comparacion_fin_str

        error_comparacion:
        la $a0 runtime_invalid_bool_msg
        li $v0 4
        syscall

        j exit

        comparacion_fin_str:
        # Si el string ingresado termino, se carga el t2 (1 true - 0 false)
        addiu $a0 $a0 1
        lb $t0 0($a0)
        li $t1 0
        # Si no termino, booleano invalido (no ingreso true o false)
        bne $t0 $t1 error_comparacion

        # Genero CIR para bool
        li $a0 8
        li $v0 9
        syscall

        # Cargo el valor (0 o 1) en el CIR
        sw $t2 4($v0)

        # Guardo el valor en el return
        sw $v0 4($fp)

        # ========================================
        # EPILOGUE in_bool
        # ========================================

        # Recupero la direccion de retorno
        lw $ra 0($fp)

        # 12($fp) -> Self
        # 8($fp) -> $fp llamador
        #  4($fp) -> return
        #  0($fp) -> $ra

        # Recupero el return
        lw $a0 4($fp)

        # Restauro $sp al primer word libre del caller
        addiu $sp, $fp, 12

        # Restauro el frame pointer del caller
        lw $fp, 8($fp)

        # Vuelvo a la instruccion siguiente al jalr
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


