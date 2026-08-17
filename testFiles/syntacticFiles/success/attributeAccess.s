class A {
    Int x;
}
impl A {
    .() {}
}
start {
    A a;
    a = new A();
    a.x = 5;
}