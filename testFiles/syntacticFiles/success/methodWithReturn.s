class A {
}
impl A {
    .() {}
    fn Int foo() { ret 42; }
}
start {
    A a;
    a = new A();
    (a.foo());
}