class PublicBox {
    pub Int value;
}

impl PublicBox {
    .() {
        self.value = 31;
    }
}

start {
    PublicBox box;

    // Escribir el atributo requiere desreferenciar box, que sigue siendo nil.
    box.value = 99;
}
