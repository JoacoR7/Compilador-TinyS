class A {}
impl A { .() {} }

start {
    A x;
    A y;
    Bool r;
    x = new A();
    y = new A();
    r = x == y;
}