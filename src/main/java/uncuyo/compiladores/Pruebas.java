package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.*;
import uncuyo.compiladores.semantic.Class;

import java.io.IOException;
import java.util.Optional;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {
        Lexer lexer = new Lexer("C:\\Users\\AMD\\Desktop\\Compiladores2026\\testFiles\\semanticFiles\\success\\methods&Attributes.s");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
        semanticAnalyzer.program();
        SymbolTable symbolTable = semanticAnalyzer.getSymbolTable();
        StringBuilder sb = new StringBuilder();
        sb.append("\"classes\": [\n");

        int classCount = 0;
        int totalClasses = symbolTable.getClasses().size();

        for (Class currentClass : symbolTable.getClasses().values()) {

            sb.append("\t{\n");

            sb.append("\t\t\"name\": \"")
                    .append(currentClass.getToken().getLexeme())
                    .append("\",\n");

            sb.append("\t\t\"parent\": \"")
                    .append(currentClass.getParent())
                    .append("\",\n");

            sb.append("\t\t\"attributes\": [\n");

            int attrCount = 0;
            int totalAttrs = currentClass.getAttributes().size();

            for (Attribute currentAttribute : currentClass.getAttributesInOrder()) {

                sb.append("\t\t\t{\n");

                sb.append("\t\t\t\t\"name\": \"")
                        .append(currentAttribute.getToken().getLexeme())
                        .append("\",\n");

                sb.append("\t\t\t\t\"type\": \"")
                        .append(currentAttribute.getType().getLexeme())
                        .append("\",\n");

                sb.append("\t\t\t\t\"private\": ")
                        .append(currentAttribute.isPrivate())
                        .append(",\n");

                sb.append("\t\t\t\t\"position\": ")
                        .append(currentAttribute.getPosition())
                        .append("\n");

                sb.append("\t\t\t}");

                attrCount++;
                if (attrCount < totalAttrs) {
                    sb.append(",");
                }
                sb.append("\n");
            }

            sb.append("\t\t]\n");

            sb.append("\t\t\"methods\": [\n");

            int methodCount = 0;
            int totalMethods = currentClass.getMethods().size();

            for (Method currentMethod : currentClass.getMethodsInOrder()) {

                sb.append("\t\t\t{\n");

                sb.append("\t\t\t\t\"name\": \"")
                        .append(currentMethod.getToken().getLexeme())
                        .append("\",\n");

                sb.append("\t\t\t\t\"type\": \"")
                        .append(currentMethod.getType().getLexeme())
                        .append("\",\n");

                sb.append("\t\t\t\t\"static\": ")
                        .append(currentMethod.isStatic())
                        .append(",\n");

                sb.append("\t\t\t\t\"position\": ")
                        .append(currentMethod.getPosition())
                        .append("\n");

                sb.append("\t\t\t}");

                methodCount++;
                if (methodCount < totalMethods) {
                    sb.append(",");
                }
                sb.append("\n");
            }

            sb.append("\t\t]\n");
            sb.append("\t}");

            classCount++;
            if (classCount < totalClasses) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]\n");
        System.out.println(sb.toString());
    }
}
