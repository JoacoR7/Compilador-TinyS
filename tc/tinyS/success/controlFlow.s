// Caso exitoso (tinyS): Orden de sentencias, while e if/else anidados.
// Esperado: TS y AST validos, y codigo ASM ejecutable.
start {
    Int indice;
    indice = 0;
    while (indice < 3) {
        if (indice == 1) {
            (IO.out_str("medio"));
        } else {
            (IO.out_int(indice));
        }
        indice = indice + 1;
    }
}
