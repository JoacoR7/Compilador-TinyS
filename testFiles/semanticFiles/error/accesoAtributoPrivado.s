class Caja {
    pub Int secreto;
}
impl Caja {
    .() { self.secreto = 42; }
}
start {
    Caja c;
    c = new Caja();
    c.secreto = 10; // error: secreto es privado
}