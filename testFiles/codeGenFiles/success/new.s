class Persona {
    Str nombre;
    Int edad;
}

impl Persona {
    .() {
        self.edad = 25;
        self.nombre = "Joaquin";
    }

    fn setEdad(Int edad) {
        self.edad = edad;
    }

    fn Int getEdad() {
        ret self.edad;
    }

    fn setNombre(Str nuevoNombre) {
        self.nombre = nuevoNombre;
    }

    fn Str getNombre() {
        ret self.nombre;
    }

    fn saludar(Str nombre) {
        (IO.out_str("Hola "));
        (IO.out_str(nombre));
        (IO.out_str(", soy "));
        (IO.out_str(self.nombre));
        (IO.out_str(" y tengo "));
        (IO.out_int(self.edad));
        (IO.out_str(" años"));
    }

}

start {
    Persona persona1;
    persona1 = new Persona();
    /*
    (persona1.saludar("Joaquin"));
    (IO.out_str("\n"));
    (persona1.saludar("Tomas"));
    */
    //(IO.out_str(persona1.getNombre()));
    (persona1.saludar("Tomas"));
    (IO.out_str("\nuwu"));
    //a = 3;

}