class Base : A{
    Int i1;
}
impl Base {
    .() { }
}

class Sub : Base { }
impl Sub {
    .() { }
}

class A  { }
impl A {
    .() { }
}

class Base : A {
    Int i2;
}

start {

}