class Contador {
    pub Int valor;
}
impl Contador {
    .() { self.valor = 0; }
    fn void incrementar() {
        self.valor = self.valor + 1;
    }
    fn Int getValor() {
        ret self.valor;
    }
}
start {
    Contador c;
    c = new Contador();
    (c.incrementar());
    (c.incrementar());
    (c.incrementar());
    (IO.out_int(c.getValor()));
}