class A {
}
impl A {
    .() {}
}
start {
    A a;
    a = new A();
    if (true)
        (IO.out_str("si"));
    else
        (IO.out_str("no"));
}