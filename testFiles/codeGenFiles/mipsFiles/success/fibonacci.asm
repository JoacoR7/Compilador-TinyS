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
        .asciiz "f_"
    str_1:
        .asciiz "="
    str_2:
        .asciiz "\n"

    vtable_Fibonacci:
        .word Fibonacci_sucesion_fib
        .word Fibonacci_imprimo_numero
        .word Fibonacci_imprimo_sucesion
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

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 12 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -12


        # --- INICIALIZACION DE VARIABLES LOCALES ---
        # Inicializacion variables locales de start
        # Inicializacion de fib
        move $v0 $zero
        sw $v0 -4($fp)
        # Inicializacion de n
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -8($fp)

        # $sp queda apuntando al primer word libre del frame

        # Body de start
        # Asignacion a "fib" en 71_5
        # Evalúo el lado derecho de la asignacion

        # ========================================
        # Generacion de CIR de Fibonacci
        # ========================================
        # Necesita 4 espacios para sus atributos y la vtable
        li $a0 16
        li $v0 9
        syscall
        # Guardo la direccion del heap en el stack para inicializar las variables
        sw $v0 0($sp)
        addiu $sp $sp -4

        # --- Inicializacion atributos de Fibonacci ---
        # Inicializacion de atributo suma de tipo Int
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Guardo la direccion del atributo suma
        lw $t0 4($sp)
        sw $v0 4($t0)
        # Inicializacion de atributo i de tipo Int
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Guardo la direccion del atributo i
        lw $t0 4($sp)
        sw $v0 8($t0)
        # Inicializacion de atributo j de tipo Int
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        # Guardo la direccion del atributo j
        lw $t0 4($sp)
        sw $v0 12($t0)
        # Guardo la direccion de la vtable de Fibonacci
        la $a0 vtable_Fibonacci
        sw $a0 0($t0)
        # Recupero la direccion de la instancia de clase de Fibonacci
        lw $t0 4($sp)
        addiu $sp $sp 4
        move $a0 $t0

        # ========================================
        # PRECALL Constructor Fibonacci
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

        la $t0 Fibonacci_constructor
        jalr $t0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -4
        # Checkeo acceso nulo
        li $a2 71
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "n" en 72_5
        # Evalúo el lado derecho de la asignacion

        # --- Comienzo llamada 72_9 ---
        # Llamada estática sobre IO
        # Guardo la vtable de IO
        la $a0, vtable_IO

        # ========================================
        # PRECALL in_int
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

        
        # Como el metodo in_int es estatico, guardo la vtable en self
        lw $a0 12($sp)
        lw $a0 28($a0)
        jalr $a0
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -8
        # Checkeo acceso nulo
        li $a2 72
        li $a3 5
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)

        # --- Comienzo llamada 74_6 ---
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
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 74
        li $a3 17
        beqz $a0 runtime_null

        # ========================================
        # PRECALL sucesion_fib
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de sucesion_fib ---
        # Evaluo parametro n° 1
        lw $a0 -8($fp)
        # Checkeo acceso nulo
        li $a2 69
        li $a3 9
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo sucesion_fib es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo sucesion_fib en la vtable
        lw $a0 0($a0)
        jalr $a0
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
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



    Fibonacci_sucesion_fib:

        # ========================================
        # PROLOGUE sucesion_fib
        # ========================================
        # Registro de activacion del metodo:
        # self         -> 16($fp)
        # $fp llamador -> 12($fp)
        # return       -> 8($fp)
        # Par 1        -> 4($fp)
        # $ra          -> 0($fp)
        # Var 1        -> -4($fp)

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 8 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -8


        # --- INICIALIZACION DE VARIABLES LOCALES ---
        # Inicializacion variables locales de sucesion_fib
        # Inicializacion de anterior
        li $a0 8
        li $v0 9
        syscall
        sw $zero 0($v0)
        sw $zero 4($v0)
        sw $v0 -4($fp)

        # $sp queda apuntando al primer word libre del frame

        # Body de sucesion_fib
        # Asignacion a "i" en 12_9
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
        # Obtengo el self (instancia actual)
        lw $a0 16($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 8
        # Checkeo acceso nulo
        li $a2 12
        li $a3 9
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "j" en 13_9
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
        # Obtengo el self (instancia actual)
        lw $a0 16($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 12
        # Checkeo acceso nulo
        li $a2 13
        li $a3 9
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "suma" en 14_9
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
        # Obtengo el self (instancia actual)
        lw $a0 16($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 4
        # Checkeo acceso nulo
        li $a2 14
        li $a3 9
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)

        # ========================================
        # while_16_9
        # ========================================
        while_16_9:
        # Evaluo expresion de while_16_9
        # Comienzo expresion binaria <= en 16_18
        # Obtengo el valor del nodo izquierdo
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 16
        li $a3 16
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 4($fp)
        # Checkeo acceso nulo
        li $a2 16
        li $a3 21
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
        lw $a0 4($a0)
        beqz $a0 endwhile_16_9

        # --- Cuerpo de while_16_9 ---

        # ========================================
        # if_17_13
        # ========================================
        # Evaluo expresion de if_17_13
        # Comienzo expresion binaria == en 17_19
        # Obtengo el valor del nodo izquierdo
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 17
        li $a3 17
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
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
        lw $a0 4($a0)
        beqz $a0 else_17_13
        # Cuerpo de if_17_13
        if_17_13:
        # Accedo al self
        lw $a0 16($fp)

        # ========================================
        # PRECALL imprimo_numero
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de imprimo_numero ---
        # Evaluo parametro n° 1
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 18
        li $a3 38
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo imprimo_numero es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo imprimo_numero en la vtable
        lw $a0 4($a0)
        jalr $a0
        # Accedo al self
        lw $a0 16($fp)

        # ========================================
        # PRECALL imprimo_sucesion
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de imprimo_sucesion ---
        # Evaluo parametro n° 1
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 19
        li $a3 40
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo imprimo_sucesion es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo imprimo_sucesion en la vtable
        lw $a0 8($a0)
        jalr $a0
        j endif_17_13
        # Cuerpo de else_17_13
        else_17_13:

        # ========================================
        # if_22_17
        # ========================================
        # Evaluo expresion de if_22_17
        # Comienzo expresion binaria == en 22_23
        # Obtengo el valor del nodo izquierdo
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 22
        li $a3 21
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
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
        lw $a0 4($a0)
        beqz $a0 else_22_17
        # Cuerpo de if_22_17
        if_22_17:
        # Accedo al self
        lw $a0 16($fp)

        # ========================================
        # PRECALL imprimo_numero
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de imprimo_numero ---
        # Evaluo parametro n° 1
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 23
        li $a3 42
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo imprimo_numero es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo imprimo_numero en la vtable
        lw $a0 4($a0)
        jalr $a0
        # Asignacion a "suma" en 24_21
        # Evalúo el lado derecho de la asignacion
        # Comienzo expresion binaria + en 24_33
        # Obtengo el valor del nodo izquierdo
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 24
        li $a3 28
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 24
        li $a3 35
        beqz $a0 runtime_null
        addiu $a0 $a0 8
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
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el self (instancia actual)
        lw $a0 16($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 4
        # Checkeo acceso nulo
        li $a2 24
        li $a3 21
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Accedo al self
        lw $a0 16($fp)

        # ========================================
        # PRECALL imprimo_sucesion
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de imprimo_sucesion ---
        # Evaluo parametro n° 1
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 25
        li $a3 44
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo imprimo_sucesion es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo imprimo_sucesion en la vtable
        lw $a0 8($a0)
        jalr $a0
        j endif_22_17
        # Cuerpo de else_22_17
        else_22_17:
        # Accedo al self
        lw $a0 16($fp)

        # ========================================
        # PRECALL imprimo_numero
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de imprimo_numero ---
        # Evaluo parametro n° 1
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 28
        li $a3 42
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo imprimo_numero es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo imprimo_numero en la vtable
        lw $a0 4($a0)
        jalr $a0
        # Asignacion a "anterior" en 35_21
        # Evalúo el lado derecho de la asignacion
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 35
        li $a3 32
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        addiu $a0 $fp -4
        # Checkeo acceso nulo
        li $a2 35
        li $a3 21
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "suma" en 36_21
        # Evalúo el lado derecho de la asignacion
        # Comienzo expresion binaria + en 36_33
        # Obtengo el valor del nodo izquierdo
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 36
        li $a3 28
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el valor del nodo derecho
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 36
        li $a3 35
        beqz $a0 runtime_null
        addiu $a0 $a0 12
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
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el self (instancia actual)
        lw $a0 16($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 4
        # Checkeo acceso nulo
        li $a2 36
        li $a3 21
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "j" en 37_21
        # Evalúo el lado derecho de la asignacion
        lw $a0 -4($fp)
        # Checkeo acceso nulo
        li $a2 10
        li $a3 13
        beqz $a0 runtime_null
        # Guardo en la pila el resultado de la expresion
        sw $a0 0($sp)
        addiu $sp $sp -4
        # Obtengo el self (instancia actual)
        lw $a0 16($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 12
        # Checkeo acceso nulo
        li $a2 37
        li $a3 21
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Accedo al self
        lw $a0 16($fp)

        # ========================================
        # PRECALL imprimo_sucesion
        # ========================================
        # Layout del registro de activacion:
        # Self        -> 16($sp)
        # $fp llamador -> 12($sp)
        # return      -> 8($sp)
        # Param 1     -> 4($sp)
        # $ra          -> 0($sp) [lo guarda el callee]

        # Reservo el registro de activacion del caller
        addiu $sp, $sp, -16
        # Guardo Self/contexto
        sw $a0, 16($sp)
        # Guardo $fp del llamador
        sw $fp, 12($sp)
        # Inicializo return
        sw $zero, 8($sp)


        # --- Evaluo parametros de imprimo_sucesion ---
        # Evaluo parametro n° 1
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 39
        li $a3 44
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        # Guardo parametro n° 1
        sw $a0 4($sp)
        # Como el metodo imprimo_sucesion es un metodo de instancia, obtengo el self
        lw $a0 16($sp)
        # Busco la vtable de la instancia
        lw $a0 0($a0)
        # Busco el metodo imprimo_sucesion en la vtable
        lw $a0 8($a0)
        jalr $a0
        endif_22_17:
        endif_17_13:
        # Comienzo expresion unaria ++ en 43_14
        # Obtengo el valor de la expresion
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 43
        li $a3 16
        beqz $a0 runtime_null
        addiu $a0 $a0 8
        # Incremento el operando en 1
        li $t0 1
        lw $t1 4($a0)
        add $t0 $t1 $t0
        sw $t0 4($a0)
        j while_16_9
        endwhile_16_9:
        lw $a0 16($fp)
        # Checkeo acceso nulo
        li $a2 46
        li $a3 13
        beqz $a0 runtime_null
        addiu $a0 $a0 4
        # Guardo el return
        sw $a0 8($fp)
        la $t0 epilogue_Fibonacci_sucesion_fib
        jr $t0

        # --- Epilogue de sucesion_fib ---

    epilogue_Fibonacci_sucesion_fib:
        # Recupero el $ra anterior
        lw $ra 0($fp)
        # Recupero el valor del return
        lw $a0 8($fp)
        # Recupero el $fp del llamador
        lw $fp 12($fp)
        # Recupero el espacio usado
        addiu $sp $sp 24
        jr $ra

    Fibonacci_constructor:

        # ========================================
        # PROLOGUE Constructor Fibonacci
        # ========================================
        # Registro de activacion del metodo:
        # self         -> 12($fp)
        # $fp llamador -> 8($fp)
        # return       -> 4($fp)
        # $ra          -> 0($fp)

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 4 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -4

        # $sp queda apuntando al primer word libre del frame

        # Body de constructor
        # Asignacion a "i" en 50_9
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
        # Obtengo el self (instancia actual)
        lw $a0 12($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 8
        # Checkeo acceso nulo
        li $a2 50
        li $a3 9
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "j" en 51_9
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
        # Obtengo el self (instancia actual)
        lw $a0 12($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 12
        # Checkeo acceso nulo
        li $a2 51
        li $a3 9
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)
        # Asignacion a "suma" en 52_9
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
        # Obtengo el self (instancia actual)
        lw $a0 12($fp)
        # Obtengo la direccion del atributo del self
        addiu $a0 $a0 4
        # Checkeo acceso nulo
        li $a2 52
        li $a3 9
        beqz $a0 runtime_null
        # Recupero de la pila el resultado de la expresion
        lw $t0 4($sp)
        addiu $sp $sp 4
        # Guardo la expresión en la direccion del lado izquierdo
        sw $t0 0($a0)

        # --- Epilogue de . ---

    epilogue_Fibonacci_constructor:
        # Recupero el $ra anterior
        lw $ra 0($fp)
        # Como es un constructor, guardo en return el self (CIR)
        lw $t0 12($fp)
        sw $t0 4($fp)
        # Recupero el valor del return
        lw $a0 4($fp)
        # Recupero el $fp del llamador
        lw $fp 8($fp)
        # Recupero el espacio usado
        addiu $sp $sp 16
        jr $ra

    Fibonacci_imprimo_numero:

        # ========================================
        # PROLOGUE imprimo_numero
        # ========================================
        # Registro de activacion del metodo:
        # self         -> 16($fp)
        # $fp llamador -> 12($fp)
        # return       -> 8($fp)
        # Par 1        -> 4($fp)
        # $ra          -> 0($fp)

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 4 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -4

        # $sp queda apuntando al primer word libre del frame

        # Body de imprimo_numero

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
        # Genero el literal "f_"
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

        # --- Comienzo llamada 57_10 ---
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
        lw $a0 4($fp)
        # Checkeo acceso nulo
        li $a2 57
        li $a3 21
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 58_10 ---
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
        # Genero el literal "="
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

        # --- Epilogue de imprimo_numero ---

    epilogue_Fibonacci_imprimo_numero:
        # Recupero el $ra anterior
        lw $ra 0($fp)
        # Recupero el $fp del llamador
        lw $fp 12($fp)
        # Recupero el espacio usado
        addiu $sp $sp 20
        jr $ra

    Fibonacci_imprimo_sucesion:

        # ========================================
        # PROLOGUE imprimo_sucesion
        # ========================================
        # Registro de activacion del metodo:
        # self         -> 16($fp)
        # $fp llamador -> 12($fp)
        # return       -> 8($fp)
        # Par 1        -> 4($fp)
        # $ra          -> 0($fp)

        # Fijo $fp en la posicion donde se guardara el $ra
        move $fp, $sp
        # Guardo la direccion de retorno de la llamada
        sw $ra, 0($fp)

        # Reservo 4 bytes para variables locales y el siguiente word libre
        addiu $sp, $sp, -4

        # $sp queda apuntando al primer word libre del frame

        # Body de imprimo_sucesion

        # --- Comienzo llamada 62_10 ---
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
        lw $a0 4($fp)
        # Checkeo acceso nulo
        li $a2 62
        li $a3 21
        beqz $a0 runtime_null
        # Guardo parametro n° 1
        sw $a0 4($sp)
        
        # Como el metodo out_int es estatico, guardo la vtable en self
        lw $a0 16($sp)
        lw $a0 4($a0)
        jalr $a0

        # --- Comienzo llamada 63_10 ---
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

        # --- Epilogue de imprimo_sucesion ---

    epilogue_Fibonacci_imprimo_sucesion:
        # Recupero el $ra anterior
        lw $ra 0($fp)
        # Recupero el $fp del llamador
        lw $fp 12($fp)
        # Recupero el espacio usado
        addiu $sp $sp 20
        jr $ra
