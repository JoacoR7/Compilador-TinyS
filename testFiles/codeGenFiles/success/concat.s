class Persona {
    Str nombre;
    Int edad;
}

impl Persona {
    .(Str nombre, Int edad) {
        self.nombre = nombre;
        self.edad = edad;
    }

    fn Str getNombre() {
        ret self.nombre;
    }

    fn Int getEdad() {
        ret self.edad;
    }
}

start {
    Str a,b;
    Persona p1;
    a = "Hello";
    b = "world";
    p1 = new Persona("Joaquin", 25);
    (IO.out_str(a.concat(" ").concat(b).concat(", mi nombre es ").concat(p1.getNombre()).concat(", y tengo ")));
    (IO.out_int(p1.getEdad()));
    (IO.out_str(" años"));

}