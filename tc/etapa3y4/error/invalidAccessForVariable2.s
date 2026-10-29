start {
    Array Int a;
    Array Int b;

    a = new Int[2];
    a[0] = 1;
    a[1] = 2;

    b = new Int[2];
    b[0] = 1;
    b[1] = 2;

    for (Int i in a) {
        for (Int j in b){
            (IO.out_int(i));
            (IO.out_int(j));
        }

        for (Int k in b){
            (IO.out_int(i));
            (IO.out_int(k));
            (IO.out_int(j));
        }
        (IO.out_int(k));
    }
}