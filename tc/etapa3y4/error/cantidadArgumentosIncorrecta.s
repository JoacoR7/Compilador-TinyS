class A {}
impl A {
    .() {}
    fn void hacer(Int x) { }
}
start {
    A a;
    a = new A();
    (a.hacer(1, 2)); // error: hacer toma 1 parámetro
}