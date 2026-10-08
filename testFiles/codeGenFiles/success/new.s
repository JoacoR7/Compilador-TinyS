class Persona {
    Str nombre;
}

impl Persona {
    .(){}

    fn setNombre(Str nuevoNombre) {
        self.nombre = nuevoNombre;
    }

    fn saludar(Str nombre) {
        (IO.out_str("Hola "));
        (IO.out_str(nombre));
    }
}

start {
    Persona persona1;
    persona1 = new Persona();
    (persona1.saludar("Joaquin"));
    (IO.out_str("\n"));
    (persona1.saludar("Tomas"));
}