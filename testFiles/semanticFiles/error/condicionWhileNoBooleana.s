start {
    Int i;
    i = 0;
    while (i) { // error: condición debe ser Bool, no Int
        (++i);
    }
}