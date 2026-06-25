class Punto {
    Int x;
}
impl Punto {
    .(Int x) { self.x = x; }
}
start {
    Punto p;
    p = new Punto(true); // error: se espera Int, se recibe Bool
}