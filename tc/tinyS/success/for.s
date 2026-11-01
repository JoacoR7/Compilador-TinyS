
start {
    Array Int a;
    Array Str cadenas;
    Array Bool booleanos;

    Int b;

    a = new Int[5];
    cadenas = new Str[4];
    booleanos = new Bool[4];

    b = 3;

    // ========================================
    // INICIALIZACION ARRAY INT
    // ========================================

    a[0] = 0;
    a[1] = 2;
    a[2] = 4;
    a[3] = 6;
    a[4] = 8;

    // ========================================
    // INICIALIZACION ARRAY STR
    // ========================================

    cadenas[0] = "Hola";
    cadenas[1] = "Mundo";
    cadenas[2] = "TinyS";
    cadenas[3] = "Compiladores";

    // ========================================
    // INICIALIZACION ARRAY BOOL
    // ========================================

    booleanos[0] = true;
    booleanos[1] = false;
    booleanos[2] = true;
    booleanos[3] = false;


    // ========================================
    // TEST 1 - ARRAY INT
    // ========================================

    (IO.out_str("================================\n"));
    (IO.out_str("        ARRAY INT\n"));
    (IO.out_str("================================\n"));

    for (Int i in a) {
        (IO.out_str("\n"));
        (IO.out_str(">>> FOR EXTERIOR: i = "));
        (IO.out_int(i));
        (IO.out_str("\n"));

        for (Int j in a) {
            (IO.out_str("    i = "));
            (IO.out_int(i));

            (IO.out_str(" | j = "));
            (IO.out_int(j));

            (IO.out_str(" | i * j = "));
            (IO.out_int(i * j));

            (IO.out_str("\n"));
        }

        (IO.out_str("<<< FIN INTERIOR, i sigue valiendo: "));
        (IO.out_int(i));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 2 - REUTILIZACION ARRAY INT
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        SEGUNDO FOR INT\n"));
    (IO.out_str("================================\n"));

    for (Int i in a) {
        (IO.out_str("i = "));
        (IO.out_int(i));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 3 - ARRAY STR
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        ARRAY STR\n"));
    (IO.out_str("================================\n"));

    for (Str s in cadenas) {
        (IO.out_str("Cadena: "));
        (IO.out_str(s));

        (IO.out_str(" | Longitud: "));
        (IO.out_int(s.length()));

        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 4 - FOR ANIDADOS STR
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        FOR ANIDADOS STR\n"));
    (IO.out_str("================================\n"));

    for (Str s in cadenas) {
        (IO.out_str("\n"));
        (IO.out_str(">>> EXTERIOR: "));
        (IO.out_str(s));
        (IO.out_str("\n"));

        for (Str t in cadenas) {
            (IO.out_str("    s = "));
            (IO.out_str(s));

            (IO.out_str(" | t = "));
            (IO.out_str(t));

            (IO.out_str(" | concat = "));
            (IO.out_str(s.concat(t)));

            (IO.out_str("\n"));
        }

        (IO.out_str("<<< FIN INTERIOR, s = "));
        (IO.out_str(s));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 5 - REUTILIZACION ARRAY STR
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        SEGUNDO FOR STR\n"));
    (IO.out_str("================================\n"));

    for (Str s in cadenas) {
        (IO.out_str("s = "));
        (IO.out_str(s));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 6 - ARRAY BOOL
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        ARRAY BOOL\n"));
    (IO.out_str("================================\n"));

    for (Bool valor in booleanos) {
        (IO.out_str("Valor booleano: "));
        (IO.out_bool(valor));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 7 - FOR ANIDADOS BOOL
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        FOR ANIDADOS BOOL\n"));
    (IO.out_str("================================\n"));

    for (Bool x in booleanos) {
        (IO.out_str("\n"));
        (IO.out_str(">>> EXTERIOR x = "));
        (IO.out_bool(x));
        (IO.out_str("\n"));

        for (Bool y in booleanos) {
            (IO.out_str("    x = "));
            (IO.out_bool(x));

            (IO.out_str(" | y = "));
            (IO.out_bool(y));

            (IO.out_str(" | x && y = "));
            (IO.out_bool(x && y));

            (IO.out_str(" | x || y = "));
            (IO.out_bool(x || y));

            (IO.out_str("\n"));
        }

        (IO.out_str("<<< FIN INTERIOR, x = "));
        (IO.out_bool(x));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 8 - REUTILIZACION ARRAY BOOL
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        SEGUNDO FOR BOOL\n"));
    (IO.out_str("================================\n"));

    for (Bool valor in booleanos) {
        (IO.out_str("valor = "));
        (IO.out_bool(valor));
        (IO.out_str("\n"));
    }


    // ========================================
    // TEST 9 - FOR CON TRES TIPOS
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        FOR MIXTOS\n"));
    (IO.out_str("================================\n"));

    for (Int i in a) {
        (IO.out_str("\n"));
        (IO.out_str("INT EXTERIOR = "));
        (IO.out_int(i));
        (IO.out_str("\n"));

        for (Str s in cadenas) {
            (IO.out_str("  STR INTERMEDIO = "));
            (IO.out_str(s));
            (IO.out_str("\n"));

            for (Bool x in booleanos) {
                (IO.out_str("    i = "));
                (IO.out_int(i));

                (IO.out_str(" | s = "));
                (IO.out_str(s));

                (IO.out_str(" | x = "));
                (IO.out_bool(x));

                (IO.out_str("\n"));
            }
        }
    }


    // ========================================
    // TEST 10 - ARRAYS DESPUES DE FOR MIXTOS
    // ========================================

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        VERIFICACION FINAL\n"));
    (IO.out_str("================================\n"));

    (IO.out_str("INT:\n"));

    for (Int i in a) {
        (IO.out_int(i));
        (IO.out_str("\n"));
    }

    (IO.out_str("STR:\n"));

    for (Str s in cadenas) {
        (IO.out_str(s));
        (IO.out_str("\n"));
    }

    (IO.out_str("BOOL:\n"));

    for (Bool x in booleanos) {
        (IO.out_bool(x));
        (IO.out_str("\n"));
    }


    // ========================================
    // FIN
    // ========================================

    (IO.out_str("================================\n"));
    (IO.out_str("        FIN DEL TEST\n"));
    (IO.out_str("================================\n"));
}
