start {
    Str first;
    Str repeated;

    first = "linea 1\nlinea 2\tfin";
    repeated = "linea 1\nlinea 2\tfin";

    (IO.out_str(first));
    (IO.out_str("\n"));
    (IO.out_str("comillas: \"tinyS\"; barra: \\"));
    (IO.out_str("\n"));
    (IO.out_str(repeated));
    (IO.out_str("\n"));
}
