class Caja {
    Int secreto;
}
impl Caja {
    .() {
        self.secreto = 42;
    }
}
start {
    Caja caja;
    caja = new Caja();
    (IO.out_int(caja.secreto));
}
