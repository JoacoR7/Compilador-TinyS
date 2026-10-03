start {
    Int a;
    Int b;

    a = 10;
    b = 5;

    if (a < b) {

        (IO.out_int(1));

        if (b == 5) {
            (IO.out_int(2));
        } else {
            (IO.out_int(3));
        }

    } else {

        (IO.out_int(4));

        if (a == 10) {
            (IO.out_int(++5));
        } else {
            (IO.out_int(6));
        }
    }

    (IO.out_int(9));
}