class Persona {
}

impl Persona {
    .() {
    }

    fn Int getNumero() {
        ret 10;
    }
}

class Empleado : Persona {
}

impl Empleado {
    .() {
    }
    /*
    fn Int getNumero() {
        ret 20;
    }
    */



}

start {
    Empleado e;

    e = new Empleado();

    (IO.out_int(e.getNumero()));
}