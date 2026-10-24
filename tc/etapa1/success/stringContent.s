// Caso exitoso: aceptar simbolos de comentario, tildes y escapes en cadenas.
// Esperado: STRING_CONSTANT para cada cadena; los delimitadores internos no abren comentarios.
"// esto es texto"
"/* esto tambien es texto */"
"áéíóú ÁÉÍÓÚ ñ Ñ ¿¡?"
"comillas: \"hola\" y barra: \\"
