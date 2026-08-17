class A {}
impl A {
    .() {}
    st fn void foo() {}
}
class B : A {}
impl B {
    .() {}
    st fn Int foo() { ret 1; }
}
start {}