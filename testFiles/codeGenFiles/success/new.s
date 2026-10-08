class Persona {
    Str nombre;
    Int edad;
}

impl Persona {
    .(){}

    fn setEdad(Int edad) {
        self.edad = edad;
    }

    fn Int getEdad() {
        ret self.edad;
    }
    /*
    fn setNombre(Str nuevoNombre) {
        self.nombre = nuevoNombre;
    }

    fn saludar(Str nombre) {
        (IO.out_str("Hola "));
        (IO.out_str(nombre));
    }
    */
}

start {
    Persona persona1;
    persona1 = new Persona();
    /*
    (persona1.saludar("Joaquin"));
    (IO.out_str("\n"));
    (persona1.saludar("Tomas"));
    */
    (persona1.setEdad(2));
    (IO.out_int(persona1.getEdad()));

}