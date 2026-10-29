// Caso exitoso (tinyS): Construccion, acceso e iteracion sobre un arreglo.
// Esperado: TS y AST validos, y codigo ASM ejecutable.
start {
    Array Int valores;
    valores = new Int[2];
    valores[0] = 1;
    valores[1] = 2;
    for (Int valor in valores) {
        (IO.out_int(valor));
    }
}
