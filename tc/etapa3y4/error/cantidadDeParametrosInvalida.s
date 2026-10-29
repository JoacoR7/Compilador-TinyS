class Punto {
    Int x;
}
impl Punto {
    .(Int x, Str a) { self.x = x; }
}
start {
    Punto p;
    p = new Punto(1, 2); // error: constructor toma 1 parámetro, no 2
}