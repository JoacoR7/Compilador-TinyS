start {
    Array Int numeros;
    Array Str textos;
    Array Bool booleanos;
    Array Int vacio;

    numeros = new Int[3];
    textos = new Str[2];
    booleanos = new Bool[4];

    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;

    textos[0] = "Hola";
    textos[1] = "Mundo";

    booleanos[0] = true;
    booleanos[1] = false;
    booleanos[2] = true;
    booleanos[3] = false;

    (IO.out_int(numeros.length()));
    (IO.out_str("\n"));

    (IO.out_int(textos.length()));
    (IO.out_str("\n"));

    (IO.out_int(booleanos.length()));
    (IO.out_str("\n"));
}