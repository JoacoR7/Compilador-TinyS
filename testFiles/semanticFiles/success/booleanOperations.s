class A {}
impl A {
    .() {}
    fn Bool esMayor(Int a, Int b) {
        ret a > b;
    }
    fn Bool esMenor(Int a, Int b) {
        ret a < b;
    }
    fn Bool esIgual(Int a, Int b) {
        ret a == b;
    }
}
start {
    A obj;
    obj = new A();
    (IO.out_bool(obj.esMayor(10, 5)));
    (IO.out_bool(obj.esMenor(10, 5)));
    (IO.out_bool(obj.esIgual(5, 5)));
}