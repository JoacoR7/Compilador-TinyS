class Persona {
    Str nombre;
    Int edad;
}

impl Persona {
    .(Int edad, Str nombre) {
        self.edad = edad;
        self.nombre = nombre;
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
    persona1 = new Persona(30, "Lucas");
    (persona1.saludar("Tomas"));


}