class Persona {
    Str nombre;
}

impl Persona {
    .(Str nombre){
        self.nombre = nombre;
    }

    fn Str getNombre() {
        ret nombre;
    }
}

start {
    Str a, b;
    a = "Hola";
    b = "Hola";

    (IO.out_bool(a == b));
}