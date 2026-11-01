start {
    Int a, b;

    a = 10;
    b = 5;

    (IO.out_str("a >= b: "));
    (IO.out_bool(a >= b));

    (IO.out_str("\na >= 10: "));
    (IO.out_bool(a >= 10));

    (IO.out_str("\nb >= a: "));
    (IO.out_bool(b >= a));

    (IO.out_str("\n"));
}