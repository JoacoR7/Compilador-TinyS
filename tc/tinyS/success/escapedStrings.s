// Caso exitoso (tinyS): Exportacion de comillas, barras y secuencias de escape.
// Esperado: TS y AST validos, y codigo ASM ejecutable.
start {
    (IO.out_str("comillas: \"hola\", barra: \\, linea:\nfin\t!"));
}
