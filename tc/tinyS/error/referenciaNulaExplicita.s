class ExplicitBox {
    Int value;
}

impl ExplicitBox {
    .() {
        self.value = 11;
    }

    fn void touch() {
        self.value = self.value + 1;
    }
}

start {
    ExplicitBox box;

    // La llamada explicita intenta usar una referencia no inicializada.
    (box.touch());
}
