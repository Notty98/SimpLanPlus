package src.codegen.codegenSTable;

import src.visitor.type.Type;

public class FunctionArg {

    private String name;
    private boolean isVar;

    public Type getType() {
        return type;
    }

    private Type type;

    public FunctionArg(String name, boolean isVar, Type type) {
        this.name = name;
        this.isVar = isVar;
        this.type = type;
    }

    public boolean isVar() {
        return this.isVar;
    }


}
