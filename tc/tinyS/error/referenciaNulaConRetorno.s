class ReassignedBox {
    Int value;
}

impl ReassignedBox {
    .(Int value) {
        self.value = value;
    }

    fn Int getValue() {
        ret self.value;
    }

}

start {
    ReassignedBox box;

    // La referencia conserva el valor nil con el que fue inicializada.
    (IO.out_int(box.getValue()));
}
