class MathTools {}

impl MathTools {
    .() {}

    st fn Int add(Int left, Int right) {
        ret left + right;
    }

    st fn Int multiplyAndAdd(Int left, Int right, Int extra) {
        ret left * right + extra;
    }
}

start {
    Int result;

    result = MathTools.multiplyAndAdd(MathTools.add(1, 2), 4, 5 - 1);
    (IO.out_int(result));
    (IO.out_str("\n"));
}
