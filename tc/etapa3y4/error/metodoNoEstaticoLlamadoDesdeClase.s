class A {}
impl A {
    .() {}
    fn void cosa() { (IO.out_str("hola")); }
}
start {
    A a;
    a = new A();
    (A.cosa()); // error: cosa es estático, debe llamarse A.cosa()
}