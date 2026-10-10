class Box {
    Int value;
}

impl Box {
    .() {
        self.value = 7;
    }

    fn Int getValue() {
        ret self.value;
    }
}

start {
    Box box;

    // box conserva su valor predeterminado nil.
    (IO.out_int(box.getValue()));
}
