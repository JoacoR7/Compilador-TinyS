class A {
    Int x;
}
impl A {
    .() {}
    fn Int sumar(Int a, Int b) {
        ret a + b;
    }
}
start {
    A a;
    a = new A();
    (a.sumar(3, 5));
}