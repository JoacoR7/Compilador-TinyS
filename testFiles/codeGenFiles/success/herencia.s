class Persona {
    Int edad;
}

impl Persona {
    .() {
    }

    fn void setEdad(Int nuevaEdad) {
        edad = nuevaEdad;
    }

    fn Int getEdad() {
        ret edad;
    }
}

class Empleado : Persona {
    Int sueldo;
}

impl Empleado {
    .() {
    }

    fn void setSueldo(Int nuevoSueldo) {
        sueldo = nuevoSueldo;
    }

    fn Int getSueldo() {
        ret sueldo;
    }
}

class Gerente : Empleado {
    Int bono;
}

impl Gerente {
    .() {
        bono = 500;
    }

    fn Int getBono() {
        ret bono;
    }
}

start {
    Gerente g;

    g = new Gerente();

    (g.setEdad(25));
    (g.setSueldo(1000));

    (IO.out_int(g.getEdad()));
    (IO.out_str("\n"));

    (IO.out_int(g.getSueldo()));
    (IO.out_str("\n"));

    (IO.out_int(g.getBono()));
}