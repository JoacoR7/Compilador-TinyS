// Caso exitoso (tinyS): Asignaciones, operaciones aritmeticas, logicas y relacionales.
// Esperado: TS y AST validos, y codigo ASM ejecutable.
start {
    Int numero;
    Bool activo;
    numero = (1 + 2) * 3;
    activo = numero > 0 && !false;
    (IO.out_int(numero));
}
