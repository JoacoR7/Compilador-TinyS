class Base : A{
    Int i1;
}
impl Base {
    .() { }

    fn m2() {}
    fn m1(Int i1) {}
}

class Sub : Base {
    Str s1;
}
impl Sub {
    .() { }
}

class A  {
    Bool b1;
}
impl A {
    .() { }
    fn m1(Int a4) {}
}

class Base : A {
    Int i2;
}

start {

}