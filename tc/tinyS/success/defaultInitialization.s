class Defaults {
    Int number;
    Bool flag;
    Str text;
}

impl Defaults {
    .() {}

    fn Int getNumber() {
        ret self.number;
    }

    fn Bool getFlag() {
        ret self.flag;
    }

    fn Str getText() {
        ret self.text;
    }
}

start {
    Defaults values;

    values = new Defaults();
    (IO.out_int(values.getNumber()));
    (IO.out_str("\n"));
    (IO.out_bool(values.getFlag()));
    (IO.out_str("\n"));
    (IO.out_str(values.getText()));
    (IO.out_str("fin\n"));
}
