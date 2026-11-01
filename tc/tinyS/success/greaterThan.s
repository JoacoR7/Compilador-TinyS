start {
    Int a, b;

    a = 10;
    b = 5;

    (IO.out_str("10 > 5: "));
    (IO.out_bool(a > b));

    (IO.out_str("\n5 > 10: "));
    (IO.out_bool(b > a));

    (IO.out_str("\n10 > 10: "));
    (IO.out_bool(a > 10));

    (IO.out_str("\na > b: "));
    (IO.out_bool(a > b));

    (IO.out_str("\n"));
}