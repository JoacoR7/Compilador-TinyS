class Caja {
    pub Int valor;
}
impl Caja {
    .() {
        self.valor = 42;
    }
}
start {
    Caja caja;
    Int copia;
    caja = new Caja();
    copia = caja.valor;
}
