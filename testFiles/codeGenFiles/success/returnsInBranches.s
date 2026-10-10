class NumberTools {}

impl NumberTools {
    .() {}

    st fn Int absolute(Int value) {
        if (value < 0) {
            ret -value;
        } else {
            ret value;
        }
    }
}

start {
    (IO.out_int(NumberTools.absolute(-12)));
    (IO.out_str("\n"));
    (IO.out_int(NumberTools.absolute(8)));
    (IO.out_str("\n"));
}
