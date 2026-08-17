class A {
}
impl A {
    .() {}
    pub fn Int foo() { ret 1; }
}
start {
    A a;
    a = new A();
}