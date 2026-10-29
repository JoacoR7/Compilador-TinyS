class Base {
    Int secreto;
}
impl Base {
    .() {
        self.secreto = 42;
    }
}
class Derivada : Base {}
impl Derivada {
    .() {}
    fn mostrar() {
        (IO.out_int(self.secreto));
    }
}
start {
    Derivada derivada;
    derivada = new Derivada();
    (derivada.mostrar());
}
