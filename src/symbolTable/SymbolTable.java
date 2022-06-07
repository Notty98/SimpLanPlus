package src.symbolTable;

import src.visitor.effect.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable implements SymbolTableInterface {
    private final ArrayList<HashMap<String, Node>> environments = new ArrayList<>();

    @Override
    public void newScope() {
        this.environments.add(new HashMap<>());
    }

    @Override
    public void addDecl(String id, String type, int line) {
        int index = this.environments.size() - 1;
        if(index < 0) {
            return;
        }
        this.environments.get(index).put(id, new Node(type));
    }

    @Override
    public void addDecl(String id, String type, ArrayList<HashMap<String, String>> args, int line) {
        int index = this.environments.size() - 1;
        if(index < 0) {
            return;
        }
        this.environments.get(index).put(id, new FunctionNode(type, args));
    }

    public Node lookupNode(String id) {
        int index = this.environments.size() - 1;
        while(index >= 0) {
            HashMap<String, Node> item = this.environments.get(index);
            Node type = item.get(id);
            if(type != null) {
                return type;
            }
            index = index -1;
        }
        return null;
    }

    @Override
    public String lookup(String id) {
        int index = this.environments.size() - 1;
        while(index >= 0) {
            HashMap<String, Node> item = this.environments.get(index);
            Node type = item.get(id);
            if(type != null) {
                return type.getType();
            }
            index = index -1;
        }
        return null;
    }

    @Override
    public String lookupDeclaration(String id) {
        int index = this.environments.size() - 1;
        HashMap<String, Node> item = this.environments.get(index);
        Node type = item.get(id);
        if(type != null) {
            return type.getType();
        }
        return null;
    }

    @Override
    public void exitScope() {
        int index = this.environments.size() - 1;
        if(index < 0) {
            return;
        }
        this.environments.remove(index);
    }

    public Status searchStatusFun(String funId) {
        int index = this.environments.size() - 1;
        while(index >= 0) {
            HashMap<String, Node> item = this.environments.get(index);
            Node type = item.get(funId);
            if(type instanceof FunctionNode) {
                return ((FunctionNode) type).getStatus();
            }
            index = index -1;
        }
        return null;
    }

    public void setStatus(String funId, Status status) {
        int index = this.environments.size() - 1;
        while(index >= 0) {
            Node node = this.environments.get(index).get(funId);
            if(node instanceof FunctionNode) {
                ((FunctionNode) node).setStatus(status);
                this.environments.get(index).put(funId, node);
            }
            index = index - 1;
        }
    }

    public void printTableState() {
        System.out.println(this.environments.size());
        for(HashMap<String, Node> item: this.environments) {
            System.out.println("---------BEGIN SCOPE-------------");
            for(String id: item.keySet()) {
                Node value = item.get(id);
                StringBuilder print = new StringBuilder(id + ": ");
                if(value instanceof FunctionNode) {
                    print.append(" (");
                    for(HashMap<String, String> formalPar: ((FunctionNode) value).getArgs()) {
                        for(String argId: formalPar.values()) {
                            print.append(argId);
                        }
                    }
                    print.append(") -> ");
                    print.append(value.getType());
                } else {
                    String type = value.getType();
                    print.append(type);
                }
                System.out.println(print);
            }
            System.out.println("--------- END SCOPE--------------");
        }
    }

}
