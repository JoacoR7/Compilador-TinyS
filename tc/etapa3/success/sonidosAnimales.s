class Animal {}
impl Animal {
    .() {}
    fn Str sonido() { ret "..."; }
}
class Perro : Animal {}
impl Perro {
    .() {}
    fn Str sonido() { ret "guau"; }
}
start {
    Perro p;
    p = new Perro();
    (IO.out_str(p.sonido()));
}