class A {
    pub Int x;
}
impl A {
    .() {}
    fn Int getX() { ret self.x; }
}
class B : A {}
impl B {
    .() {}
}
start {
    B b;
    b = new B();
    b.x = 10;
    (IO.out_int(b.getX()));
}