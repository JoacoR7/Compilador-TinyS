class A {
    Int secreto;
}
impl A {
    .() {}
}
class B : A {}
impl B {
    .() {}
    fn void acceder() {
        self.secreto = 10;
    }
}
start {
    B b;
    b = new B();
    (b.acceder());
}