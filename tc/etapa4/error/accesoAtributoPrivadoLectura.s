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
    Int copia;
    caja = new Caja();
    copia = caja.secreto;
}
