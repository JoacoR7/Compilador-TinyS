start {
    Int a, b;

    a = 5;
    b = 10;

    (IO.out_str("5 <= 10: "));
    (IO.out_bool(a <= b));

    (IO.out_str("\n5 <= 5: "));
    (IO.out_bool(a <= 5));

    (IO.out_str("\n10 <= 5: "));
    (IO.out_bool(b <= a));

    (IO.out_str("\n"));
}