class Persona {
    Str nombre;
    Str apellido;
}

impl Persona {
    fn saludar() {
        (IO.out_str("Hola"));
    }
}

start {
    Persona persona = new Persona();
    persona.saludar();
}
