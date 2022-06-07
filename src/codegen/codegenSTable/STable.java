package src.codegen.codegenSTable;

import src.visitor.type.BoolType;
import src.visitor.type.IntType;
import src.visitor.type.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class STable {

    private int nestingLevel;
    private int offsetEnv = 0;
    private final ArrayList<LinkedHashMap<String, STentry>> stable;

    public STable() {
        this.stable = new ArrayList<>();
        this.nestingLevel = -1;
    }

    public void addNestingLevel() {
        this.offsetEnv = 0;
        this.nestingLevel = this.nestingLevel + 1;
        this.stable.add(new LinkedHashMap<>());
    }

    public void removeNestingLevel() {
        this.nestingLevel = this.nestingLevel - 1;
        this.stable.remove(this.stable.size() - 1);
    }

    public void insertVariable(String name, Type type, boolean isVar) {
        if(type instanceof IntType) {
            LinkedHashMap<String, STentry> topEnv = this.stable.get(this.stable.size() - 1);
            topEnv.put(name, new STentry(name, offsetEnv, type, this.nestingLevel, isVar));
            this.offsetEnv = this.offsetEnv + 4;
        } else {
            LinkedHashMap<String, STentry> topEnv = this.stable.get(this.stable.size() - 1);
            topEnv.put(name, new STentry(name, offsetEnv, type, this.nestingLevel, isVar));
            this.offsetEnv = this.offsetEnv + 1;
        }
    }

    public void insertFunction(String name, Type type) {
        LinkedHashMap<String, STentry> topEnv = this.stable.get(this.stable.size() - 1);
        topEnv.put(name, new STentryFunction(name, type, this.nestingLevel));
    }

    public int getNestingLevel(String name) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name)) {
                    return env.get(key).getNestingLevel();
                }
            }
            index--;
        }

        return -1;

    }

    public int getVariableOffset(String name) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name)) {
                    return env.get(key).getOffset() + 4; // offset al -> 4
                }
            }
            index--;
        }

        return -1;

    }

    public Type getVariableType(String name) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name)) {
                    return env.get(key).getType();
                }
            }
            index--;
        }

        return null;

    }

    public void insertArg(String name, FunctionArg arg) {
        System.out.println(name);
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name) && env.get(key) instanceof STentryFunction) {
                    ((STentryFunction) env.get(key)).addArg(arg);
                    return;
                }
            }
            index--;
        }
    }

    public Type getFunctionArgByIndex(String name, int i) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name) && env.get(key) instanceof STentryFunction) {
                    return ((STentryFunction) env.get(key)).getArgTypeByIndex(i);
                }
            }
            index--;
        }
        return null;
    }

    public boolean getFunctionIsVarByIndex(String name, int i) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name) && env.get(key) instanceof STentryFunction) {
                    System.out.println("here");
                    return ((STentryFunction) env.get(key)).getIsVarByIndex(i);
                }
            }
            index--;
        }
        return false;
    }

    public boolean isVarByName(String name) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name)) {
                    return env.get(key).isVar();
                }
            }
            index--;
        }
        return false;
    }

    public String getFunctionLabel(String name) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name) && env.get(key) instanceof STentryFunction) {
                    return ((STentryFunction) env.get(key)).getLabel();
                }
            }
            index--;
        }

        return "";

    }

    public int getCurrentNestingLevel() {
        return this.nestingLevel;
    }

    public int calculateTotalOffset() {
        HashMap<String, STentry> currEnv = this.stable.get(this.stable.size() - 1);
        int total = 0;
        for(String key: currEnv.keySet()) {
            STentry item = currEnv.get(key);
            if(item instanceof STentryFunction) {
                continue;
            }
            if(item.getType() instanceof IntType) {
                total = total + 4;
            } else {
                if(item.getType() instanceof BoolType) {
                    total = total + 1;
                }
            }
        }
        return total;
    }

    public void updateIsVar(String name, boolean newState) {
        System.out.println("name->" + name);
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, name)) {
                    System.out.println("updated!");
                    env.get(name).setIsVar(newState);
                }
            }
            index--;
        }
    }

    /*public Type getTypeIndex(String fun, int i) {
        int index = this.stable.size() - 1;
        while(index >= 0) {
            LinkedHashMap<String, STentry> env = this.stable.get(index);
            for(String key: env.keySet()) {
                if(Objects.equals(key, fun) && env.get(key) instanceof STentryFunction) {
                    return ((STentryFunction) env.get(key)).getLabel();
                }
            }
            index--;
        }

        return "";
    }*/

}
