class Matematica {}

impl Matematica {
    .() {}

    st fn void sumar(Int a, Int b) {
        (IO.out_int(2+3));
    }

    st fn Int restar(Int a, Int b) {
        ret a - b;
    }
}

start {
    Int a, b;
    a = 2;
    b = 3;
    (Matematica.sumar(2,3));
    (IO.out_str("\n"));
    (IO.out_int(Matematica.restar(a,3)));
}