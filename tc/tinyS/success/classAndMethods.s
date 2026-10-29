// Caso exitoso (tinyS): Clase, constructor, parametro y retorno de metodo.
// Esperado: TS y AST validos, y codigo ASM ejecutable.
class Contador { pub Int valor; }
impl Contador {
    .(Int inicial) { valor = inicial; }
    fn Int obtener() { ret valor; }
}
start {
    Contador contador;
    contador = new Contador(3);
    (IO.out_int(contador.obtener()));
}
