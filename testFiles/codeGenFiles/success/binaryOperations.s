start {
    Int a;
    Int b;
    Int c;
    Bool t;
    Bool f;

    a = 20;
    b = 6;
    c = 3;

    t = true;
    f = false;


    // =====================================================
    // VALORES UTILIZADOS
    // =====================================================
    (IO.out_str("========================================\n"));
    (IO.out_str(" TEST DE OPERACIONES BINARIAS\n"));
    (IO.out_str("========================================\n"));

    (IO.out_str("Valores:\n"));

    (IO.out_str("a = "));
    (IO.out_int(a));
    (IO.out_str("\n"));

    (IO.out_str("b = "));
    (IO.out_int(b));
    (IO.out_str("\n"));

    (IO.out_str("c = "));
    (IO.out_int(c));
    (IO.out_str("\n\n"));


    // =====================================================
    // SUMA
    // =====================================================
    (IO.out_str("--- SUMA ---\n"));

    (IO.out_str("a(20) + b(6) = "));
    (IO.out_int(a + b));
    (IO.out_str("   esperado: 26\n"));

    (IO.out_str("b(6) + c(3) = "));
    (IO.out_int(b + c));
    (IO.out_str("   esperado: 9\n\n"));


    // =====================================================
    // RESTA
    // =====================================================
    (IO.out_str("--- RESTA ---\n"));

    (IO.out_str("a(20) - b(6) = "));
    (IO.out_int(a - b));
    (IO.out_str("   esperado: 14\n"));

    (IO.out_str("b(6) - a(20) = "));
    (IO.out_int(b - a));
    (IO.out_str("   esperado: -14\n\n"));


    // =====================================================
    // MULTIPLICACION
    // =====================================================
    (IO.out_str("--- MULTIPLICACION ---\n"));

    (IO.out_str("a(20) * b(6) = "));
    (IO.out_int(a * b));
    (IO.out_str("   esperado: 120\n"));

    (IO.out_str("b(6) * c(3) = "));
    (IO.out_int(b * c));
    (IO.out_str("   esperado: 18\n\n"));


    // =====================================================
    // DIVISION
    // =====================================================
    (IO.out_str("--- DIVISION ---\n"));

    (IO.out_str("a(20) / b(6) = "));
    (IO.out_int(a / b));
    (IO.out_str("   esperado: 3\n"));

    (IO.out_str("a(20) / c(3) = "));
    (IO.out_int(a / c));
    (IO.out_str("   esperado: 6\n"));

    (IO.out_str("b(6) / c(3) = "));
    (IO.out_int(b / c));
    (IO.out_str("   esperado: 2\n\n"));


    // =====================================================
    // IGUALDAD
    // =====================================================
    (IO.out_str("--- IGUALDAD == ---\n"));

    (IO.out_str("a(20) == a(20) = "));
    (IO.out_bool(a == a));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("a(20) == b(6) = "));
    (IO.out_bool(a == b));
    (IO.out_str("   esperado: false\n\n"));


    // =====================================================
    // DISTINTO
    // =====================================================
    (IO.out_str("--- DISTINTO != ---\n"));

    (IO.out_str("a(20) != b(6) = "));
    (IO.out_bool(a != b));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("a(20) != a(20) = "));
    (IO.out_bool(a != a));
    (IO.out_str("   esperado: false\n\n"));


    // =====================================================
    // MENOR
    // =====================================================
    (IO.out_str("--- MENOR < ---\n"));

    (IO.out_str("a(20) < b(6) = "));
    (IO.out_bool(a < b));
    (IO.out_str("   esperado: false\n"));

    (IO.out_str("b(6) < a(20) = "));
    (IO.out_bool(b < a));
    (IO.out_str("   esperado: true\n\n"));


    // =====================================================
    // MENOR O IGUAL
    // =====================================================
    (IO.out_str("--- MENOR O IGUAL <= ---\n"));

    (IO.out_str("a(20) <= b(6) = "));
    (IO.out_bool(a <= b));
    (IO.out_str("   esperado: false\n"));

    (IO.out_str("a(20) <= a(20) = "));
    (IO.out_bool(a <= a));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("b(6) <= a(20) = "));
    (IO.out_bool(b <= a));
    (IO.out_str("   esperado: true\n\n"));


    // =====================================================
    // MAYOR
    // =====================================================
    (IO.out_str("--- MAYOR > ---\n"));

    (IO.out_str("a(20) > b(6) = "));
    (IO.out_bool(a > b));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("b(6) > a(20) = "));
    (IO.out_bool(b > a));
    (IO.out_str("   esperado: false\n\n"));


    // =====================================================
    // MAYOR O IGUAL
    // =====================================================
    (IO.out_str("--- MAYOR O IGUAL >= ---\n"));

    (IO.out_str("a(20) >= b(6) = "));
    (IO.out_bool(a >= b));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("a(20) >= a(20) = "));
    (IO.out_bool(a >= a));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("b(6) >= a(20) = "));
    (IO.out_bool(b >= a));
    (IO.out_str("   esperado: false\n\n"));


    // =====================================================
    // AND
    // =====================================================
    (IO.out_str("--- AND && ---\n"));

    (IO.out_str("true && true = "));
    (IO.out_bool(t && t));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("true && false = "));
    (IO.out_bool(t && f));
    (IO.out_str("   esperado: false\n"));

    (IO.out_str("false && true = "));
    (IO.out_bool(f && t));
    (IO.out_str("   esperado: false\n"));

    (IO.out_str("false && false = "));
    (IO.out_bool(f && f));
    (IO.out_str("   esperado: false\n\n"));


    // =====================================================
    // OR
    // =====================================================
    (IO.out_str("--- OR || ---\n"));

    (IO.out_str("true || true = "));
    (IO.out_bool(t || t));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("true || false = "));
    (IO.out_bool(t || f));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("false || true = "));
    (IO.out_bool(f || t));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("false || false = "));
    (IO.out_bool(f || f));
    (IO.out_str("   esperado: false\n\n"));


    // =====================================================
    // EXPRESIONES BINARIAS ANIDADAS
    // Sirven especialmente para probar push/pop
    // =====================================================
    (IO.out_str("--- EXPRESIONES ANIDADAS ---\n"));

    (IO.out_str("a(20) + b(6) * c(3) = "));
    (IO.out_int(a + b * c));
    (IO.out_str("   esperado: 38\n"));

    (IO.out_str("(a(20) + b(6)) * c(3) = "));
    (IO.out_int((a + b) * c));
    (IO.out_str("   esperado: 78\n"));

    (IO.out_str("a(20) - b(6) - c(3) = "));
    (IO.out_int(a - b - c));
    (IO.out_str("   esperado: 11\n"));

    (IO.out_str("a(20) / b(6) + c(3) = "));
    (IO.out_int(a / b + c));
    (IO.out_str("   esperado: 6\n\n"));


    // =====================================================
    // COMPARACIONES + LOGICA ANIDADAS
    // =====================================================
    (IO.out_str("--- COMPARACIONES Y LOGICA ANIDADAS ---\n"));

    (IO.out_str("(a(20) > b(6)) && (b(6) > c(3)) = "));
    (IO.out_bool((a > b) && (b > c)));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("(a(20) < b(6)) || (b(6) > c(3)) = "));
    (IO.out_bool((a < b) || (b > c)));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("(a(20) == b(6)) || (b(6) == b(6)) = "));
    (IO.out_bool((a == b) || (b == b)));
    (IO.out_str("   esperado: true\n"));

    (IO.out_str("(a(20) != b(6)) && (b(6) != c(3)) = "));
    (IO.out_bool((a != b) && (b != c)));
    (IO.out_str("   esperado: true\n\n"));


    (IO.out_str("========================================\n"));
    (IO.out_str(" FIN TEST OPERACIONES BINARIAS\n"));
    (IO.out_str("========================================\n"));
}