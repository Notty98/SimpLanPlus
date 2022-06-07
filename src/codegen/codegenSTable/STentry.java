package src.codegen.codegenSTable;

import src.visitor.type.Type;

public class STentry {

    private String name;
    private Type type;
    private int offset;
    private int nestingLevel;
    private boolean isVar;

    public STentry(String name, int offset, Type type, int nestingLevel) {
        this.name = name;
        this.type = type;
        this.offset = offset;
        this.nestingLevel = nestingLevel;
    }
    public STentry(String name, int offset, Type type, int nestingLevel, boolean isVar) {
        this.name = name;
        this.type = type;
        this.offset = offset;
        this.nestingLevel = nestingLevel;
        this.isVar = isVar;
    }

    public STentry(String name, Type type, int nestingLevel) {
        this.name = name;
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    public int getOffset() {
        return this.offset;
    }

    public Type getType() {
        return this.type;
    }
    public boolean isVar() {
        return this.isVar;
    }
    public void setIsVar(boolean value) {
        this.isVar = value;
    }
    public int getNestingLevel() {
        return this.nestingLevel;
    }

}
