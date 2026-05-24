package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.JsonSerializable;

public abstract class Node implements JsonSerializable {

    public abstract <T> T accept(Visitor<T> visitor);

    public abstract String toJson(String indent);
}
