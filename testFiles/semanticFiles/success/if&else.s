start {
    Int a;
    a = IO.in_int();
    if (a >= 18)
        (IO.out_str("Mayor de edad"));

    if (a > 10)
        (IO.out_str("Adolescente"));
    else
        (IO.out_str("Niño"));
}