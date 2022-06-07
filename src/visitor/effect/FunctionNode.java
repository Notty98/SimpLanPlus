package src.visitor.effect;

import java.util.LinkedHashMap;

public class FunctionNode extends Status {
    private String id;

    private boolean isVoid;

    private LinkedHashMap<String, FunctionVar> args;

    public FunctionNode(String id, boolean isVoid) {
        super(isVoid ? States.Bot : States.Init);
        this.id = id;
        this.isVoid = isVoid;
    }

    public LinkedHashMap<String, FunctionVar> getArgs() {
        return args;
    }

    public void setArgs(LinkedHashMap<String, FunctionVar> args) {
        this.args = args;
    }
    public String searchByIndex(int index) {
        return (String) this.args.keySet().toArray()[index];
    }

    public boolean isVoid() {
        return isVoid;
    }

}
