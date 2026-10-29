class A {
    Int x;
}
class B : A {
    Int x; // error: x ya está definido en A
}
impl A { .() {} }
impl B { .() {} }
start {}