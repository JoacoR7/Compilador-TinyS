class A {
    pub Int x;
}
impl A {
    .(){ self.x = 10; }

    fn Str setX(Int v) {
     self.x = v;
    }
}
start {
    A a;
    a = new A();
    (a.setX(42).length().x); // length retorna Int, no tiene el atributo x
}