start {
    Array Int numeros;
    Array Str textos;
    Array Bool booleanos;

    numeros = new Int[3];
    textos = new Str[3];
    booleanos = new Bool[3];

    // Array Int
    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;

    // Array Str
    textos[0] = "Hola";
    textos[1] = "Mundo";
    textos[2] = "TinyS";

    // Array Bool
    booleanos[0] = true;
    booleanos[1] = false;
    booleanos[2] = true;

    // Imprimir Int
    (IO.out_int(numeros[0]));
    (IO.out_str("\n"));
    (IO.out_int(numeros[1]));
    (IO.out_str("\n"));
    (IO.out_int(numeros[2]));
    (IO.out_str("\n"));

    // Imprimir Str
    (IO.out_str(textos[0]));
    (IO.out_str("\n"));
    (IO.out_str(textos[1]));
    (IO.out_str("\n"));
    (IO.out_str(textos[2]));
    (IO.out_str("\n"));

    // Imprimir Bool
    (IO.out_bool(booleanos[0]));
    (IO.out_str("\n"));
    (IO.out_bool(booleanos[1]));
    (IO.out_str("\n"));
    (IO.out_bool(booleanos[2]));
    (IO.out_str("\n"));
}