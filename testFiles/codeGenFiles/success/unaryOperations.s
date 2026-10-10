start {
    Int positive;
    Int negative;
    Int incremented;
    Int decremented;
    Bool negated;

    positive = +7;
    negative = -7;
    incremented = ++7;
    decremented = --7;
    negated = !false;

    (IO.out_int(positive));
    (IO.out_str("\n"));
    (IO.out_int(negative));
    (IO.out_str("\n"));
    (IO.out_int(incremented));
    (IO.out_str("\n"));
    (IO.out_int(decremented));
    (IO.out_str("\n"));
    (IO.out_bool(negated));
    (IO.out_str("\n"));
}
