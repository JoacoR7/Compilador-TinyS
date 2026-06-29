class A {}
impl A {
    .() {}
    st fn void cosa() { (IO.out_str("hola")); }
}
start {
    A a;
    a = new A();
    (a.cosa()); // error: cosa es estático, debe llamarse A.cosa()
}