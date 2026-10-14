class ChildBox {
    Int value;
}

impl ChildBox {
    .() {
        self.value = 41;
    }

    fn Int getValue() {
        ret self.value;
    }
}

class ParentBox {
    ChildBox child;
}

impl ParentBox {
    .() {}

    fn Int getValue() {
        ret 0;
    }

    fn Int readChild() {
        ret self.child.getValue();
    }
}

start {
    ParentBox parent;

    parent = new ParentBox();
    // El atributo child tiene su valor predeterminado nil.
    (IO.out_int(parent.readChild()));
}
