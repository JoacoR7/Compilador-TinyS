class Divider {}

impl Divider {
    .() {}

    st fn Int divide(Int numerator, Int denominator) {
        ret numerator / denominator;
    }
}

start {
    (IO.out_int(Divider.divide(100, 0)));
}
