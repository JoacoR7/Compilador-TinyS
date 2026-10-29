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
    caja = new Caja();
    (IO.out_int(caja.valor));
}
