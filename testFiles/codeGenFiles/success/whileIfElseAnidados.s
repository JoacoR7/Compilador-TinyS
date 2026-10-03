start {
    Int i;
    Int j;
    Int suma;

    i = 0;
    suma = 0;

    while (i < 3) {

        j = 0;

        while (j < 4) {

            if ((i + j) < 3) {
                (IO.out_int(1));
            } else {
                (IO.out_int(0));
            }

            j = j + 1;
        }

        (IO.out_str("\n"));

        suma = suma + i;

        if (suma > 2) {
            (IO.out_int(9));
        } else {
            (IO.out_int(8));
        }

        (IO.out_str("\n"));

        i = i + 1;
    }

    (IO.out_str("FIN\n"));
}