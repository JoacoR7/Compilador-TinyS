class A {}
class B {
    pub A miA;
}
impl A {
    .() {}
    fn Str saludo() { ret "hola"; }
}
impl B {
    .() { self.miA = new A(); }
}
start {
    B b;
    A a;
    Str s;
    b = new B();
    s = a.saludo();
    (IO.out_str(b.miA.saludo())); // encadenado válido
}