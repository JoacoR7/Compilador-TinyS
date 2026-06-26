class A {
}

impl A{
    .(){}
    fn Int sumar(Int x, Int y) {
        ret x + y;
    }
}

start {
    A a;
    Int aux;
    a = new A();
    aux = a.sumar(42,12,17);
}
