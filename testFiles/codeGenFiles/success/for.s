
start {
    Array Int a;
    Int b;

    a = new Int[5];
    b = 3;

    a[0] = 0;
    a[1] = 2;
    a[2] = 4;
    a[3] = 6;
    a[4] = 8;

    (IO.out_str("================================\n"));
    (IO.out_str("        PRIMER FOR\n"));
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

    (IO.out_str("\n"));
    (IO.out_str("================================\n"));
    (IO.out_str("        SEGUNDO FOR\n"));
    (IO.out_str("================================\n"));

    for (Int i in a) {
        (IO.out_str("i = "));
        (IO.out_int(i));
        (IO.out_str("\n"));
    }

    (IO.out_str("================================\n"));
    (IO.out_str("        FIN DEL TEST\n"));
    (IO.out_str("================================\n"));
}
