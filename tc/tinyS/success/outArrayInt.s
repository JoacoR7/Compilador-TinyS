start {
    Array Int numeros;
    Array Str nombres;
    Array Bool booleanos;

    numeros = new Int[3];
    nombres = new Str[5];
    booleanos = new Bool[3];

    // Array Int
    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;

    nombres[0] = "Tomas";
    nombres[1] = "Victor";
    nombres[2] = "Paulina";
    nombres[3] = "Joaquin";
    nombres[4] = "Cecilia";

    booleanos[0] = true;
    booleanos[1] = false;
    booleanos[2] = false;

    (IO.out_array_int(numeros));
    (IO.out_array_str(nombres));
    (IO.out_array_bool(booleanos));
}