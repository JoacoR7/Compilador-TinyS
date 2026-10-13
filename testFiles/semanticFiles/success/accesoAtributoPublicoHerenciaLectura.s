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
    fn Int leer() {
        Int copia;
        copia = self.valor;
        ret copia;
    }
}
start {
    Derivada derivada;
    derivada = new Derivada();
    (derivada.leer());
}
