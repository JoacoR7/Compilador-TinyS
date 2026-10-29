class A {}
impl A {
    .() {}
    fn Int valor() {
        ret 42;
    }
    fn void nada() {

    }
}
start {
    A a;
    a = new A();
    (a.nada());
    (IO.out_int(a.valor()));
}