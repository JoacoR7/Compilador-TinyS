class Base {
    pub Int valor;
}
impl Base {
    .() {
        self.valor = 42;
    }
}
class Derivada : Base {}
impl Derivada {
    .() {}
    fn mostrar() {
        (IO.out_int(self.valor));
    }
}
start {
    Derivada derivada;
    derivada = new Derivada();
    (derivada.mostrar());
}
