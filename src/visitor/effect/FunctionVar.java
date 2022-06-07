package src.visitor.effect;

public class FunctionVar extends Status {
    private String varId;

    private boolean isVar;
    private int offset;

    public FunctionVar(String varId, boolean isVar, int offset) {
        super(isVar ? States.Bot : States.Init);

        this.varId = varId;
        this.isVar = isVar;
        this.offset = offset;
    }

    public String getId() {
        return varId;
    }

    public boolean isVar() {
        return isVar;
    }

}
