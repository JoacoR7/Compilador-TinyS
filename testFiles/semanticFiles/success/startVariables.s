/*
class Persona {
    pub Int edad;
    pub Persona padre;
    Mascota mascota;
}

impl Persona {
    .(){}
    fn saludar(Str nombre) {
        Str saludo;
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
*/
start {
    /*
    Persona hijo;
    Persona padre;
    Mascota mascota;
    */
    Int edadMascota , edadAux;
    edadAux = 10;
    /*
    hijo = new Persona();
    padre = new Persona();
    mascota = new Mascota();
    //edadMascota = hijo.padre.obtenerMascota().edad;
    edadMascota = hijo.padre.edad;
    hijo = padre;
    */
    edadMascota = edadAux + 5;
}