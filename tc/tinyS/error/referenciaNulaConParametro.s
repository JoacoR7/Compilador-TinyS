class ParameterBox {
    Int value;
}

impl ParameterBox {
    .(Int value) {
        self.value = value;
    }

    fn Int add(Int amount) {
        ret self.value + amount;
    }
}

start {
    ParameterBox box;

    // La llamada pasa un parametro, pero su receptor conserva nil.
    (IO.out_int(box.add(5)));
}
