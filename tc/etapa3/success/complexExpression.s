class A {}
impl A {
    .() {}
    fn Int calcular(Int a, Int b, Int c) {
        ret a + b * c - a / b;
    }
}
start {
    A obj;
    Int resultado;
    obj = new A();
    resultado = obj.calcular(10, 5, 3);
    (IO.out_int(resultado));
}