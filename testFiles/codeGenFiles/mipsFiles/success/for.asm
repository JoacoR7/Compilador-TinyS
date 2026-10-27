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
        .ascii "Hola"
        .byte 0
    str_1:
        .ascii "Mundo"
        .byte 0
    str_2:
        .ascii "TinyS"
        .byte 0
    str_3:
        .ascii "Compiladores"
        .byte 0
    str_4:
        .ascii "================================"
        .byte 10
        .byte 0
    str_5:
        .ascii "        ARRAY INT"
        .byte 10
        .byte 0
    str_6:
        .byte 10
        .byte 0
    str_7:
        .ascii ">>> FOR EXTERIOR: i = "
        .byte 0
    str_8:
        .ascii "    i = "
        .byte 0
    str_9:
        .ascii " | j = "
        .byte 0
    str_10:
        .ascii " | i * j = "
        .byte 0
    str_11:
        .ascii "<<< FIN INTERIOR, i sigue valiendo: "
        .byte 0
    str_12:
        .ascii "        SEGUNDO FOR INT"
        .byte 10
        .byte 0
    str_13:
        .ascii "i = "
        .byte 0
    str_14:
        .ascii "        ARRAY STR"
        .byte 10
        .byte 0
    str_15:
        .ascii "Cadena: "
        .byte 0
    str_16:
        .ascii " | Longitud: "
        .byte 0
    str_17:
        .ascii "        FOR ANIDADOS STR"
        .byte 10
        .byte 0
    str_18:
        .ascii ">>> EXTERIOR: "
        .byte 0
    str_19:
        .ascii "    s = "
        .byte 0
    str_20:
        .ascii " | t = "
        .byte 0
    str_21:
        .ascii " | concat = "
        .byte 0
    str_22:
        .ascii "<<< FIN INTERIOR, s = "
        .byte 0
    str_23:
        .ascii "        SEGUNDO FOR STR"
        .byte 10
        .byte 0
    str_24:
        .ascii "s = "
        .byte 0
    str_25:
        .ascii "        ARRAY BOOL"
        .byte 10
        .byte 0
    str_26:
        .ascii "Valor booleano: "
        .byte 0
    str_27:
        .ascii "        FOR ANIDADOS BOOL"
        .byte 10
        .byte 0
    str_28:
        .ascii ">>> EXTERIOR x = "
        .byte 0
    str_29:
        .ascii "    x = "
        .byte 0
    str_30:
        .ascii " | y = "
        .byte 0
    str_31:
        .ascii " | x && y = "
        .byte 0
    str_32:
        .ascii " | x || y = "
        .byte 0
    str_33:
        .ascii "<<< FIN INTERIOR, x = "
        .byte 0
    str_34:
        .ascii "        SEGUNDO FOR BOOL"
        .byte 10
        .byte 0
    str_35:
        .ascii "valor = "
        .byte 0
    str_36:
        .ascii "        FOR MIXTOS"
        .byte 10
        .byte 0
    str_37:
        .ascii "INT EXTERIOR = "
        .byte 0
    str_38:
        .ascii "  STR INTERMEDIO = "
        .byte 0
    str_39:
        .ascii " | s = "
        .byte 0
    str_40:
        .ascii " | x = "
        .byte 0
    str_41:
        .ascii "        VERIFICACION FINAL"
        .byte 10
        .byte 0
    str_42:
        .ascii "INT:"
        .byte 10
        .byte 0
    str_43:
        .ascii "STR:"
        .byte 10
        .byte 0
    str_44:
        .ascii "BOOL:"
        .byte 10
        .byte 0
    str_45:
        .ascii "        FIN DEL TEST"
        .byte 10
        .byte 0

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

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 20 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -20


        # --- INICIALIZACION DE VARIABLES LOCALES ---
        # Inicializacion variables locales de start
        # Inicializacion de a
        move $v0 $zero
        sw $v0 -4($fp)
        # Inicializacion de cadenas
        move $v0 $zero
        sw $v0 -8($fp)
        # Inicializacion de booleanos
        move $v0 $zero
        sw $v0 -12($fp)
        # Inicializacion de b
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -16($fp)

        # $sp queda apuntando al primer word libre del frame

        # Body de start
        # Asignacion a "a" en 9_5
        # Evalúo el lado derecho de la asignacion

        # --- Constructor array linea 9, columna 13 ---
        # Evaluo expresion de constructor array
        # Genero el literal 5
        li $a0 5
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
        # Recupero el valor del CIR
        lw $a0 4($a0)
        # Guardo la longitud del array en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Necesito la cantidad de elementos del array mas 8 bytes: 4 para la direccion de la vtable y 4 para la longitud
        li $t0 4
        mul $a0 $a0 $t0
        addiu $a0 $a0 8
        li $v0 9
        syscall
        # El array es de tipo Int, por lo tanto guardo su vtable correspondiente
        la $a0 vtable_array_int
        sw $a0 0($v0)
        # Recupero la longitud del array de la pila en $t0
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la longitud del array
        sw $t0 4($v0)
        # Inicializacion de elementos del array
        # $t1 va a ser el contador
        li $t1 0
        # $t2 contendra la direccion a la vtable correspondiente
        li $t2 0
        # $t3 contendrá la direccion del primer elemento del array
        addiu $t3 $v0 8
        # Guardo la direccion del array en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Como el array es de tipo Int, guardo un 0 en $t4
        li $t4 0
        # $a0 valdra 8, que son los bytes necesarios para cada elemento
        li $a0 8
        inicializacion_array_9_13:
        # Hago el syscall para almacenar el espacio
        li $v0 9
        syscall
        # Guardo la vtable correspondiente
        sw $t2 0($v0)
        # Como el array es de tipo Int, guardo un 0
        sw $t4 4($v0)
        # Guardo el CIR en la posicion correspondiente
        sw $v0 0($t3)
        # Avanzo de posicion
        addiu $t3 $t3 4
        # Incremento el contador
        addiu $t1 $t1 1
        # Comparo el contador con la longitud para saber si salgo del loop
        bne $t1 $t0 inicializacion_array_9_13
        # Recupero la direccion del array
        lw $t0 4($sp)
        addiu $sp $sp 4
        move $a0 $t0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Cargo la direccion de a
        addiu $a0 $fp -4
        # Checkeo acceso nulo
        li $a2 9
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "cadenas" en 10_5
        # Evalúo el lado derecho de la asignacion

        # --- Constructor array linea 10, columna 19 ---
        # Evaluo expresion de constructor array
        # Genero el literal 4
        li $a0 4
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
        # Recupero el valor del CIR
        lw $a0 4($a0)
        # Guardo la longitud del array en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Necesito la cantidad de elementos del array mas 8 bytes: 4 para la direccion de la vtable y 4 para la longitud
        li $t0 4
        mul $a0 $a0 $t0
        addiu $a0 $a0 8
        li $v0 9
        syscall
        # El array es de tipo Str, por lo tanto guardo su vtable correspondiente
        la $a0 vtable_array_str
        sw $a0 0($v0)
        # Recupero la longitud del array de la pila en $t0
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la longitud del array
        sw $t0 4($v0)
        # Inicializacion de elementos del array
        # $t1 va a ser el contador
        li $t1 0
        # $t2 contendra la direccion a la vtable correspondiente
        la $t2 vtable_str
        # $t3 contendrá la direccion del primer elemento del array
        addiu $t3 $v0 8
        # Guardo la direccion del array en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Como el array es de tipo Str, guardo la direccion al str vacio
        la $t4 empty_str
        # $a0 valdra 8, que son los bytes necesarios para cada elemento
        li $a0 8
        inicializacion_array_10_19:
        # Hago el syscall para almacenar el espacio
        li $v0 9
        syscall
        # Guardo la vtable correspondiente
        sw $t2 0($v0)
        # Como el array es de tipo Str, guardo  la direccion al str vacio
        sw $t4 4($v0)
        # Guardo el CIR en la posicion correspondiente
        sw $v0 0($t3)
        # Avanzo de posicion
        addiu $t3 $t3 4
        # Incremento el contador
        addiu $t1 $t1 1
        # Comparo el contador con la longitud para saber si salgo del loop
        bne $t1 $t0 inicializacion_array_10_19
        # Recupero la direccion del array
        lw $t0 4($sp)
        addiu $sp $sp 4
        move $a0 $t0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Cargo la direccion de cadenas
        addiu $a0 $fp -8
        # Checkeo acceso nulo
        li $a2 10
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "booleanos" en 11_5
        # Evalúo el lado derecho de la asignacion

        # --- Constructor array linea 11, columna 21 ---
        # Evaluo expresion de constructor array
        # Genero el literal 4
        li $a0 4
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
        # Recupero el valor del CIR
        lw $a0 4($a0)
        # Guardo la longitud del array en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Necesito la cantidad de elementos del array mas 8 bytes: 4 para la direccion de la vtable y 4 para la longitud
        li $t0 4
        mul $a0 $a0 $t0
        addiu $a0 $a0 8
        li $v0 9
        syscall
        # El array es de tipo Bool, por lo tanto guardo su vtable correspondiente
        la $a0 vtable_array_bool
        sw $a0 0($v0)
        # Recupero la longitud del array de la pila en $t0
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la longitud del array
        sw $t0 4($v0)
        # Inicializacion de elementos del array
        # $t1 va a ser el contador
        li $t1 0
        # $t2 contendra la direccion a la vtable correspondiente
        li $t2 0
        # $t3 contendrá la direccion del primer elemento del array
        addiu $t3 $v0 8
        # Guardo la direccion del array en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Como el array es de tipo Bool, guardo un 0 en $t4
        li $t4 0
        # $a0 valdra 8, que son los bytes necesarios para cada elemento
        li $a0 8
        inicializacion_array_11_21:
        # Hago el syscall para almacenar el espacio
        li $v0 9
        syscall
        # Guardo la vtable correspondiente
        sw $t2 0($v0)
        # Como el array es de tipo Bool, guardo un 0
        sw $t4 4($v0)
        # Guardo el CIR en la posicion correspondiente
        sw $v0 0($t3)
        # Avanzo de posicion
        addiu $t3 $t3 4
        # Incremento el contador
        addiu $t1 $t1 1
        # Comparo el contador con la longitud para saber si salgo del loop
        bne $t1 $t0 inicializacion_array_11_21
        # Recupero la direccion del array
        lw $t0 4($sp)
        addiu $sp $sp 4
        move $a0 $t0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Cargo la direccion de booleanos
        addiu $a0 $fp -12
        # Checkeo acceso nulo
        li $a2 11
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "b" en 13_5
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
        # Cargo la direccion de b
        addiu $a0 $fp -16
        # Checkeo acceso nulo
        li $a2 13
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "a" en 19_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 0
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 0
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 19
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 19
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "a" en 20_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 2
        li $a0 2
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 1
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 20
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 20
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "a" en 21_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 4
        li $a0 4
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 2
        li $a0 2
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 21
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 21
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "a" en 22_5
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
        # Evaluo la expresion de acceso a array
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 22
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 22
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "a" en 23_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal 8
        li $a0 8
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 4
        li $a0 4
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 23
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 23
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "cadenas" en 29_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal "Hola"
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
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Evaluo la expresion de acceso a array
        # Genero el literal 0
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 29
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 29
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "cadenas" en 30_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal "Mundo"
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
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Evaluo la expresion de acceso a array
        # Genero el literal 1
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 30
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 30
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "cadenas" en 31_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal "TinyS"
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
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Evaluo la expresion de acceso a array
        # Genero el literal 2
        li $a0 2
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 31
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 31
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "cadenas" en 32_5
        # Evalúo el lado derecho de la asignacion
        # Genero el literal "Compiladores"
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
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Evaluo la expresion de acceso a array
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 32
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 32
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "booleanos" en 38_5
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 0
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 38
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 38
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "booleanos" en 39_5
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 1
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 39
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 39
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "booleanos" en 40_5
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
        # Evaluo la expresion de acceso a array
        # Genero el literal 2
        li $a0 2
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 40
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 40
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "booleanos" en 41_5
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
        # Evaluo la expresion de acceso a array
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
        sw $a0 0($sp)
        addiu $sp $sp -4
        lw $a0 -12($fp)
        # Checkeo acceso nulo
        li $a2 41
        li $a3 5
        beqz $a0 runtime_null
        # Recupero la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        lw $t0 4($t0)
        # Recupero la longitud del array para hacer checkeo de indice
        lw $t1 4($a0)
        # Checkeo indice negativo
        bltz $t0 runtime_negative_index
        # Checkeo indice fuera de rango
        slt $t2 $t0 $t1
        beqz $t2 runtime_out_of_range
        # Multiplico por 4 y sumo 8 para acceder al elemento correcto
        sll $t0 $t0 2
        addiu $t0 $t0 8
        add $a0 $a0 $t0
        # Checkeo acceso nulo
        li $a2 41
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)

        # --- Comienzo llamada 48_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        ARRAY INT\n"
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

        # --- Comienzo llamada 50_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 52_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -4($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_52_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_52_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_int_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 53_10 ---
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

        # --- Comienzo llamada 54_10 ---
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
        # Genero el literal ">>> FOR EXTERIOR: i = "
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

        # --- Comienzo llamada 55_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 52
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 56_10 ---
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

        # ========================================
        # Comienzo for 58_18
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -4($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_58_18:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_58_18
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_int_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -28($fp)

        # --- Comienzo llamada 59_14 ---
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
        # Genero el literal "    i = "
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

        # --- Comienzo llamada 60_14 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 52
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 62_14 ---
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
        # Genero el literal " | j = "
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

        # --- Comienzo llamada 63_14 ---
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
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 58
        li $a3 18
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 65_14 ---
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
        # Genero el literal " | i * j = "
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

        # --- Comienzo llamada 66_14 ---
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
        # Comienzo expresion binaria * en 66_27
        # Obtengo el valor del nodo izquierdo
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 52
        li $a3 14
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 58
        li $a3 18
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

        # --- Comienzo llamada 68_14 ---
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
        j for_58_18
        end_for_58_18:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 71_10 ---
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
        # Genero el literal "<<< FIN INTERIOR, i sigue valiendo: "
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

        # --- Comienzo llamada 72_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 52
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 73_10 ---
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
        j for_52_14
        end_for_52_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 81_6 ---
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

        # --- Comienzo llamada 82_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        SEGUNDO FOR INT\n"
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

        # --- Comienzo llamada 84_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 86_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -4($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_86_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_86_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_int_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 87_10 ---
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
        # Genero el literal "i = "
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

        # --- Comienzo llamada 88_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 86
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 89_10 ---
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
        j for_86_14
        end_for_86_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 97_6 ---
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

        # --- Comienzo llamada 98_6 ---
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
        # Genero el literal "================================\n"
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

        # --- Comienzo llamada 99_6 ---
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
        # Genero el literal "        ARRAY STR\n"
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

        # --- Comienzo llamada 100_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 102_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -8($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_102_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_102_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_str_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 103_10 ---
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
        # Genero el literal "Cadena: "
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

        # --- Comienzo llamada 104_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 102
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 106_10 ---
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
        # Genero el literal " | Longitud: "
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

        # --- Comienzo llamada 107_10 ---
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
        # Guardo en $a0 la direccion del CIR que invoca al metodo
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 107
        li $a3 21
        beqz $a0 runtime_null

        # ========================================
        # PRECALL length
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Como el metodo length es un metodo de instancia, obtengo el self
        lw $a0 12($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo length en la vtable
        lw $a0 0($a0)
        jalr $a0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 109_10 ---
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
        j for_102_14
        end_for_102_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

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
        # Genero el literal "\n"
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

        # --- Comienzo llamada 118_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        FOR ANIDADOS STR\n"
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

        # --- Comienzo llamada 120_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 122_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -8($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_122_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_122_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_str_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 123_10 ---
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

        # --- Comienzo llamada 124_10 ---
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
        # Genero el literal ">>> EXTERIOR: "
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

        # --- Comienzo llamada 125_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 122
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 126_10 ---
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

        # ========================================
        # Comienzo for 128_18
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -8($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_128_18:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_128_18
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_str_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -28($fp)

        # --- Comienzo llamada 129_14 ---
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
        # Genero el literal "    s = "
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

        # --- Comienzo llamada 130_14 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 122
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 132_14 ---
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
        # Genero el literal " | t = "
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

        # --- Comienzo llamada 133_14 ---
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
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 128
        li $a3 18
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 135_14 ---
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
        # Genero el literal " | concat = "
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

        # --- Comienzo llamada 136_14 ---
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
        # Guardo en $a0 la direccion del CIR que invoca al metodo
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 136
        li $a3 25
        beqz $a0 runtime_null

        # ========================================
        # PRECALL concat
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


        # --- Evaluo parametros de concat ---
        # Evaluo parametro n° 1
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 128
        li $a3 18
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo concat es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo concat en la vtable
        lw $a0 4($a0)
        jalr $a0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 138_14 ---
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
        j for_128_18
        end_for_128_18:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 141_10 ---
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
        # Genero el literal "<<< FIN INTERIOR, s = "
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

        # --- Comienzo llamada 142_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 122
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 143_10 ---
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
        j for_122_14
        end_for_122_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

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
        # Genero el literal "\n"
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

        # --- Comienzo llamada 152_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        SEGUNDO FOR STR\n"
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

        # --- Comienzo llamada 154_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 156_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -8($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_156_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_156_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_str_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 157_10 ---
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
        # Genero el literal "s = "
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

        # --- Comienzo llamada 158_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 156
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 159_10 ---
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
        j for_156_14
        end_for_156_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

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
        # Genero el literal "\n"
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

        # --- Comienzo llamada 168_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        ARRAY BOOL\n"
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

        # --- Comienzo llamada 170_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 172_15
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -12($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_172_15:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_172_15
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_bool_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 173_10 ---
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
        # Genero el literal "Valor booleano: "
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

        # --- Comienzo llamada 174_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 172
        li $a3 15
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 175_10 ---
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
        j for_172_15
        end_for_172_15:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

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
        # Genero el literal "\n"
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

        # --- Comienzo llamada 184_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        FOR ANIDADOS BOOL\n"
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

        # --- Comienzo llamada 186_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 188_15
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -12($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_188_15:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_188_15
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_bool_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 189_10 ---
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

        # --- Comienzo llamada 190_10 ---
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
        # Genero el literal ">>> EXTERIOR x = "
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

        # --- Comienzo llamada 191_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 188
        li $a3 15
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 192_10 ---
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

        # ========================================
        # Comienzo for 194_19
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -12($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_194_19:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_194_19
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_bool_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -28($fp)

        # --- Comienzo llamada 195_14 ---
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
        # Genero el literal "    x = "
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

        # --- Comienzo llamada 196_14 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 188
        li $a3 15
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 198_14 ---
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
        # Genero el literal " | y = "
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

        # --- Comienzo llamada 199_14 ---
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
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 194
        li $a3 19
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 201_14 ---
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
        # Genero el literal " | x && y = "
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

        # --- Comienzo llamada 202_14 ---
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
        # Comienzo expresion binaria && en 202_28
        # Obtengo el valor del nodo izquierdo
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 188
        li $a3 15
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 194
        li $a3 19
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

        # --- Comienzo llamada 204_14 ---
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
        # Genero el literal " | x || y = "
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

        # --- Comienzo llamada 205_14 ---
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
        # Comienzo expresion binaria || en 205_28
        # Obtengo el valor del nodo izquierdo
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 188
        li $a3 15
        beqz $a0 runtime_null
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 194
        li $a3 19
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

        # --- Comienzo llamada 207_14 ---
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
        j for_194_19
        end_for_194_19:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 210_10 ---
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
        # Genero el literal "<<< FIN INTERIOR, x = "
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

        # --- Comienzo llamada 211_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 188
        li $a3 15
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 212_10 ---
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
        j for_188_15
        end_for_188_15:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 220_6 ---
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
        # Genero el literal "================================\n"
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

        # --- Comienzo llamada 222_6 ---
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
        # Genero el literal "        SEGUNDO FOR BOOL\n"
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 225_15
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -12($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_225_15:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_225_15
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_bool_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 226_10 ---
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
        # Genero el literal "valor = "
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

        # --- Comienzo llamada 227_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 225
        li $a3 15
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 228_10 ---
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
        j for_225_15
        end_for_225_15:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 236_6 ---
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

        # --- Comienzo llamada 237_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        FOR MIXTOS\n"
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

        # --- Comienzo llamada 239_6 ---
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
        # Genero el literal "================================\n"
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

        # ========================================
        # Comienzo for 241_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -4($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_241_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_241_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_int_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 242_10 ---
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

        # --- Comienzo llamada 243_10 ---
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
        # Genero el literal "INT EXTERIOR = "
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

        # --- Comienzo llamada 244_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 241
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 245_10 ---
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

        # ========================================
        # Comienzo for 247_18
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -8($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_247_18:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_247_18
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_str_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -28($fp)

        # --- Comienzo llamada 248_14 ---
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
        # Genero el literal "  STR INTERMEDIO = "
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

        # --- Comienzo llamada 249_14 ---
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
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 247
        li $a3 18
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 250_14 ---
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

        # ========================================
        # Comienzo for 252_23
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -12($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_252_23:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_252_23
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_bool_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -36($fp)

        # --- Comienzo llamada 253_18 ---
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
        # Genero el literal "    i = "
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

        # --- Comienzo llamada 254_18 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 241
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 256_18 ---
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
        # Genero el literal " | s = "
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

        # --- Comienzo llamada 257_18 ---
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
        # Busco la variable en el RA
        lw $a0 -28($fp)
        # Checkeo acceso nulo
        li $a2 247
        li $a3 18
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 259_18 ---
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
        # Genero el literal " | x = "
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

        # --- Comienzo llamada 260_18 ---
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
        # Busco la variable en el RA
        lw $a0 -36($fp)
        # Checkeo acceso nulo
        li $a2 252
        li $a3 23
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 262_18 ---
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
        j for_252_23
        end_for_252_23:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4
        j for_247_18
        end_for_247_18:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4
        j for_241_14
        end_for_241_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

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
        # Genero el literal "\n"
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

        # --- Comienzo llamada 273_6 ---
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
        # Genero el literal "================================\n"
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
        # Genero el literal "        VERIFICACION FINAL\n"
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

        # --- Comienzo llamada 275_6 ---
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
        # Genero el literal "================================\n"
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

        # --- Comienzo llamada 277_6 ---
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
        # Genero el literal "INT:\n"
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

        # ========================================
        # Comienzo for 279_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -4($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_279_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_279_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_int
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_int_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 280_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 279
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 281_10 ---
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
        j for_279_14
        end_for_279_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 284_6 ---
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
        # Genero el literal "STR:\n"
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

        # ========================================
        # Comienzo for 286_14
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -8($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_286_14:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_286_14
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_str
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_str_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 287_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 286
        li $a3 14
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_str es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 0($a0)
        jalr $a0

        # --- Comienzo llamada 288_10 ---
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
        j for_286_14
        end_for_286_14:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 291_6 ---
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
        # Genero el literal "BOOL:\n"
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

        # ========================================
        # Comienzo for 293_15
        # ========================================
        # Guardo la nueva variable en el RA
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Cargo el array
        lw $a0 -12($fp)
        # Lo guardo en la pila
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Genero un objeto iterator
        li $a0 8
        li $v0 9
        syscall
        # Recupero el array
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo el array en el primer espacio del CIR de iterator
        sw $t0 0($v0)
        # Cargo indice -1
        li $t0 -1
        # Guardo el indice en la segunda posicion del CIR del iterator
        sw $t0 4($v0)
        # Guardo el iterator en la pila
        sw $v0 0($sp)
        addiu $sp $sp -4
        # Comienzo loop for
        for_293_15:
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL hasNext
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a hasNext
        jal has_next_array
        # Tengo en $a0 el CIR booleano
        # Recupero el valor del booleano
        lw $a0 4($a0)
        beqz $a0 end_for_293_15
        # Cargo el objeto iterator
        lw $a0 4($sp)

        # ========================================
        # PRECALL next_bool
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 12($sp)
        # $fp llamador -> 8($sp)
        # return      -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -12
        # Guardo Self/contexto
        sw $a0, 12($sp)
        # Guardo $fp del llamador
        sw $fp, 8($sp)
        # Inicializo return
        sw $zero, 4($sp)

        # Salto a next
        jal next_bool_array
        # Tengo en $a0 el del elemento actual
        # Guardo el valor en la posicion correspondiente de la variable
        sw $a0 -20($fp)

        # --- Comienzo llamada 294_10 ---
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
        # Busco la variable en el RA
        lw $a0 -20($fp)
        # Checkeo acceso nulo
        li $a2 293
        li $a3 15
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_bool es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 8($a0)
        jalr $a0

        # --- Comienzo llamada 295_10 ---
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
        j for_293_15
        end_for_293_15:
        # Quito el cir iterator de la pila
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Quito la variable del contexto for del RA
        lw $t0 4($sp)
        addiu $sp $sp 4

        # --- Comienzo llamada 303_6 ---
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
        # Genero el literal "================================\n"
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

        # --- Comienzo llamada 304_6 ---
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
        # Genero el literal "        FIN DEL TEST\n"
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

        # --- Comienzo llamada 305_6 ---
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
        # Genero el literal "================================\n"
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
        # BODY length_array
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
        # ========================================
        # PROLOGUE has_next_array
        # ========================================

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp $sp

        # Guardo la direccion de retorno de la llamada
        sw $ra 0($fp)

        # $sp apunta al siguiente word libre del frame
        addiu $sp $sp -4


        # ========================================
        # BODY has_next_array
        # ========================================

        # Cargo el objeto iterator guardado en self
        lw $a0 12($fp)

        # Cargo el indice actual
        lw $t0 4($a0)

        # Cargo el array
        lw $t1 0($a0)

        # Cargo la longitud del array
        lw $t1 4($t1)

        # Incremento en uno el valor del indice actual para ver si entra en el rango de la longitud
        addiu $t0 $t0 1

        # Comparo el indice con la longitud -> indice < longitud ? 1 : 0
        slt $t0 $t0 $t1

        # Genero un CIR booleano
        li $a0 8
        li $v0 9
        syscall

        # Guardo el valor en el CIR
        sw $t0 4($v0)

        # Guardo el CIR booleano en el return
        sw $v0 4($fp)

        # ========================================
        # EPILOGUE has_next_array
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

    next_int_array:
        li $t2 0
        j next_array
        nop

    next_str_array:
        la $t2, vtable_str
        j next_array
        nop

    next_bool_array:
        li $t2 0
        j next_array
        nop

    next_array:

        # ========================================
        # PROLOGUE next_array
        # ========================================

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp

        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # $sp apunta al siguiente word libre del frame
        addiu $sp, $sp, -4


        # ========================================
        # BODY next_array
        # ========================================

        # Cargo el objeto iterator guardado en self
        lw $a0, 12($fp)

        # Cargo el indice actual
        lw $t0, 4($a0)

        # Incremento en uno el valor del indice actual
        addiu $t0, $t0, 1

        # Guardo el nuevo indice en el iterator
        sw $t0, 4($a0)

        # Multiplico por 4 el indice para acceder al elemento correspondiente
        sll $t0, $t0, 2

        # Cargo el array
        lw $t1, 0($a0)

        # Avanzo hasta la posicion del primer elemento del array
        addiu $t1, $t1, 8

        # Avanzo a la posicion correspondiente
        addu $t1, $t1, $t0

        # Cargo la direccion del CIR del elemento
        lw $t1, 0($t1)

        # Reservo 8 bytes para el nuevo CIR
        li $a0, 8
        li $v0, 9
        syscall

        # Guardo la vtable en caso de string, en otro caso es 0
        sw $t2, 0($v0)

        # Obtengo el valor del elemento actual
        lw $t0, 4($t1)

        # Guardo el valor en el nuevo CIR
        sw $t0, 4($v0)

        # Guardo el CIR en el return
        sw $v0, 4($fp)


        # ========================================
        # EPILOGUE
        # ========================================

        # Recupero la direccion de retorno
        lw $ra, 0($fp)

        # Registro de activacion:
        #
        # 12($fp) -> Self
        #  8($fp) -> $fp llamador
        #  4($fp) -> return
        #  0($fp) -> $ra

        # Recupero el return
        lw $a0, 4($fp)

        # Restauro $sp al primer word libre del caller
        addiu $sp, $fp, 12

        # Restauro el frame pointer del caller
        lw $fp, 8($fp)

        # Vuelvo a la instruccion siguiente al jalr
        jr $ra
        nop

    out_array_int:
        li $t0 1
        li $t4 0
        j out_array


    out_array_str:
        li $t0 4
        li $t4 0
        j out_array

    out_array_bool:
        li $t0 4
        li $t4 1

    out_array:
        # ========================================
        # PROLOGUE out_array
        # ========================================

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp $sp

        # Guardo la direccion de retorno de la llamada
        sw $ra 0($fp)

        # $sp apunta al siguiente word libre del frame
        addiu $sp $sp -4

        # ========================================
        # BODY out_array
        # ========================================

        # $t0 contiene el syscall correspondiente para imprimir int, str o bool
        # $t4 indica si es bool o no

        # Cargo el Param
        lw $t1 4($fp)

        # Cargo la longitud del array
        lw $t2 4($t1)

        # Inicio un contador en 0
        li $t3 0

        # Avanzo al primer elemento del array
        addiu $t1 $t1 8

        # Imprimo '['
        li $a0 91
        li $v0 11
        syscall

        loop_out_array:
        # Imprimo el elemento actual
        lw $a0 0($t1)
        lw $a0 4($a0)

        bnez $t4 cargar_bool
        fin_cargar_bool:

        move $v0 $t0
        syscall

        # Aumento contadores y posiciones
        addiu $t1 $t1 4
        addiu $t3 $t3 1

        # Si la longitud es igual al contador, termino el loop
        beq $t2 $t3 fin_loop_out_array

        # Imprimo ','
        li $a0 44
        li $v0 11
        syscall

        # Imprimo ' '
        li $a0 32
        syscall

        j loop_out_array

        fin_loop_out_array:
        # Imprimo ']'
        li $a0 93
        li $v0 11
        syscall

        # Imprimo '\n'
        li $a0 10
        syscall

        # ========================================
        # EPILOGUE out_array
        # ========================================

        # Recupero la direccion de retorno
        lw $ra 0($fp)

        #
        # 16($fp) -> Self
        # 12($fp) -> $fp llamador
        #  8($fp) -> return
        #  4($fp) -> Param1
        #  0($fp) -> $ra

        # Restauro $sp al primer word libre del caller
        addiu $sp $fp 16

        # Restauro el frame pointer del caller
        lw $fp 12($fp)

        # Vuelvo a la instruccion siguiente al jalr
        jr $ra

    cargar_bool:
        # $a0 contiene el valor del Bool (0 o 1)
        beqz $a0 cargar_false

        # Si es true (1)
        la $a0 true_str
        j fin_cargar_bool

    cargar_false:
        # Si es false (0)
        la $a0 false_str
        j fin_cargar_bool

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
        addiu $sp, $fp, 12

        # Restauro el frame pointer del caller
        lw $fp, 8($fp)

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


