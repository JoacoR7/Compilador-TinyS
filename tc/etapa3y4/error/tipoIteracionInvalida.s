start {
    Array Int a;
    a = new Int[3];
    for (Bool i in a) { // error: a es Int, i es Bool
        (IO.out_bool(i));
    }
}