package src.symbolTable;

import src.visitor.effect.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionNode extends Node {
    private ArrayList<HashMap<String, String>> args;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status;

    public FunctionNode(String type, ArrayList<HashMap<String, String>> args) {
        super(type);
        this.args = args;
    }

    public ArrayList<HashMap<String, String>> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<HashMap<String, String>> args) {
        this.args = args;
    }

}
