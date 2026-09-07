.data
    empty_str: .asciiz ""
    true_str: .asciiz "true"
    false_str: .asciiz "false"

    vtable_str:
        .word length_str
        .word concat_str

    vtable_array_int:
        .word length_array
        .word has_next_array
        .word next_int_array

    vtable_array_str:
        .word length_array
        .word has_next_array
        .word next_str_array

    vtable_array_bool:
        .word length_array
        .word has_next_array
        .word next_bool_array

    vtable_IO:
        .word out_str
        .word out_int
        .word out_bool
        .word out_array_str
        .word out_array_int
        .word out_array_bool
        .word in_str
        .word in_int
        .word in_bool