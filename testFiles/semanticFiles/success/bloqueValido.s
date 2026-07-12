class A {}
impl A {
    .() {}
    fn Int doble(Int x) {
        Int resultado; // declaración local válida
        resultado = x * 2;
        ret resultado;
    }
}
start {
    A a;
    a = new A();
    (IO.out_int(a.doble(5)));
}