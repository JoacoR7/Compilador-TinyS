class A {
    Array Int datos;
}

impl A {
    .(){
        self.datos = new Int[2];
    }

    fn Int obtener() {
        ret self.datos[0];
    }

    fn Int procesar(Array Int arr) {
        Int suma;
        suma = arr[0] + arr[1];
        ret suma;
    }
}

start {
}
