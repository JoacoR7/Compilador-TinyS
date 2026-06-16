class Persona {
    pub Int edad;
    pub Persona padre;
    pub Mascota mascota;
}

impl Persona {
    .(){}
    fn saludar(Str nombre) {
        Str saludo;
        Auto auto;
        saludo = "Hola " + nombre;
        (IO.str_out(saludo));
    }

    fn Mascota obtenerMascota() {
        ret self.mascota;
    }
}

class Mascota {
    pub Int edad;
}

impl Mascota {
    .() {}
}

start {

    Persona hijo;
    Persona padre;
    Mascota mascota;
    /*
    Int edadMascota , edadAux;
    edadAux = 10;
    */
    hijo = new Persona();
    padre = new Persona();
    mascota = new Mascota();
    hijo.padre.mascota.edad = 10;
    /*
    edadMascota = hijo.padre.obtenerMascota().edad;
    edadMascota = hijo.padre.edad;
    hijo = padre;
    edadMascota = edadAux + 5;
    */
}