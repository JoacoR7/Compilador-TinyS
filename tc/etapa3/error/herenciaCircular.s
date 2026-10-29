class A : B {}
class B : A {} // error: ciclo A → B → A
impl A { .() {} }
impl B { .() {} }
start {}