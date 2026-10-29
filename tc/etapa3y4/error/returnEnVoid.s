class A {}
impl A {
    .() {}
    fn void metodo() {
        ret 1; // error: método void no debe retornar valor
    }
}
start {}