class Speaker {
    Str name;
}

impl Speaker {
    .(Str name) {
        self.name = name;
    }

    fn greet(Str prefix) {
        (IO.out_str(prefix));
        (IO.out_str(" "));
        (IO.out_str(self.name));
    }
}

start {
    Speaker speaker;

    speaker = new Speaker("tinyS");
    (speaker.greet("hola"));
    (IO.out_str("\n"));
}
