class Persona {
    pub Int edad;
    pub Str nombre;
    pub Persona padre;
    pub Mascota mascota;
}

impl Persona {

    .(){}
    /*
    .(Str nombre){
        self.nombre = "Victor";
    }

    fn saludar(Str nombre) {
        Str saludo;
        Auto auto;
        saludo = "Hola " + nombre;
        (IO.str_out(saludo));
    }
    */
    fn Mascota obtenerMascota() {
        ret self.mascota;
    }

    /*
    fn nuevoSaludo(Str nombre) {
        Str nombre;
        (IO.str_out(nombre));
    }
    */

    fn mascotaPadre(){
        Array Int arrayPersona;
        arrayPersona = new Int [5];

        for (Int edad in arrayPersona){
        }
        for (Str edad in arrayPersona){
        }
        /*
        while (self.padre.mascota.edad > 10){
            self.padre.mascota.edad = 10;
        }
        */

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
    //hijo.padre.mascota.edad = 10;
    /*
    edadMascota = hijo.padre.obtenerMascota().edad;
    edadMascota = hijo.padre.edad;
    hijo = padre;
    edadMascota = edadAux + 5;
    */
}