class Pair {
    Int left;
    Int right;
}

impl Pair {
    .(Int left, Int right) {
        self.left = left;
        self.right = right;
    }

    fn Int sum() {
        ret self.left + self.right;
    }
}

start {
    Pair pair;

    pair = new Pair(2 + 3, 4 * 5);
    (IO.out_int(pair.sum()));
    (IO.out_str("\n"));
}
