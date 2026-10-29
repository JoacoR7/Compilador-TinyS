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
    fn Int leer() {
        Int copia;
        copia = self.secreto;
        ret copia;
    }
}
start {
    Derivada derivada;
    derivada = new Derivada();
    (derivada.leer());
}
