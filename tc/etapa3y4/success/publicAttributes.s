class Persona {
    pub Int edad;
}
impl Persona {
    .() {}
    fn Int getEdad() {
        ret self.edad;
    }
}
start {
    Persona p;
    p = new Persona();
    p.edad = 25;
    (IO.out_int(p.getEdad()));
}