class A {}
impl A {
    .() {}
    fn Int f() { ret 1; }
}
class B : A {}
impl B {
    .() {}
    fn Str f() { ret "1"; } // error: redefinición con tipo distinto
}
start {}