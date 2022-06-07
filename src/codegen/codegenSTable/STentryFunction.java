package src.codegen.codegenSTable;

import src.visitor.type.Type;

import java.util.ArrayList;
import java.util.HashMap;

public class STentryFunction extends STentry{

    private final String label;
    private final ArrayList<FunctionArg> listType;

    public STentryFunction(String name, Type type, int nestingLevel) {
        super(name, type, nestingLevel);
        this.label = name + nestingLevel;
        this.listType = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public void addArg(FunctionArg arg) {
        this.listType.add(arg);
    }

    public Type getArgTypeByIndex(int index) {
        return this.listType.get(index).getType();
    }

    public boolean getIsVarByIndex(int index) {
        return this.listType.get(index).isVar();
    }

}
