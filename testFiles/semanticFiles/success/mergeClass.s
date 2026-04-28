class Base : A{
    Int i1;
}
impl Base {
    .() { }
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
}

class Base : A {
    Int i2;
}

start {

}