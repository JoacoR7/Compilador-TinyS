class A {
}

impl A {
    .(){}
    fn void procesar(Array Int datos) {
        Int i;
        for (Int i in datos) {
            (IO.out_int(i));
        }
    }
}

start {
    A a;
    Array Int data;
    data = new Int[2];
    a = new A();
    (a.procesar(data));
}
