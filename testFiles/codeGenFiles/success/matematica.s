class Matematica {}

impl Matematica {
    .() {}

    st fn void sumar(Int a, Int b) {
        (IO.out_int(2+3));
    }

    st fn void restar(Int a, Int b) {
        (IO.out_int(2-3));
    }
}

start {
    (Matematica.sumar(2,3));
    (IO.out_str("\n"));
    (Matematica.restar(2,3));
}