class ConstructorBox {
    Int value;
}

impl ConstructorBox {
    .() {
        self.value = 51;
    }

    fn Int getValue() {
        ret self.value;
    }
}

class BoxContainer {
    ConstructorBox box;
}

impl BoxContainer {
    // El constructor deja box con su valor predeterminado nil.
    .() {}

    fn Int getValue() {
        ret 0;
    }

    fn Int read() {
        ret self.box.getValue();
    }
}

start {
    BoxContainer container;

    container = new BoxContainer();
    (IO.out_int(container.read()));
}
