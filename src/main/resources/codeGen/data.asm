#############################
#       COMIENZO INIT       #
#############################

# ========================= #
#     STRINGS PREDEFINIDOS  #
# ========================= #

empty_str: .asciiz ""
true_str: .asciiz "true"
false_str: .asciiz "false"
runtime_null_msg: .asciiz "Error: referencia nula\n"
runtime_bounds_msg: .asciiz "Error: indice fuera de rango\n"
runtime_div_zero_msg: .asciiz "Error: division por cero\n"

# ========================= #
#          VTABLES          #
# ========================= #

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
    .word out_array_int
    .word out_array_str
    .word out_array_bool
    .word in_str
    .word in_int
    .word in_bool

#############################
#          FIN INIT         #
#############################
