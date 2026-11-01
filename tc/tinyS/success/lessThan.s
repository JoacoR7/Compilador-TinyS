start {
    Int a, b;

    a = 3 * 2;

    (IO.out_str("Valor de a: "));
    (IO.out_int(a));

    b = 2;

    (IO.out_str("\nValor de b: "));
    (IO.out_int(b));

    (IO.out_str("\n\nPruebas de operador <:"));

    (IO.out_str("\na < b: "));
    (IO.out_bool(a < b));

    (IO.out_str("\nb < a: "));
    (IO.out_bool(b < a));

    (IO.out_str("\na < 10: "));
    (IO.out_bool(a < 10));

    (IO.out_str("\n10 < a: "));
    (IO.out_bool(10 < a));

    (IO.out_str("\n2 < 3 * a: "));
    (IO.out_bool(2 < 3 * a));

    (IO.out_str("\n"));
}