package src.visitor.effect;

import src.symbolTable.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Effect implements Serializable {

    private ArrayList<LinkedHashMap<String, Status>> environment;
    private LinkedHashMap<String, FunctionVar> argsToPush = null;
    private String funId;

    public String getFunId() {
        return funId;
    }

    public void setFunId(String funId) {
        this.funId = funId;
    }

    public Effect() {
        environment = new ArrayList<>();
    }

    public void newEnvironment() {
        LinkedHashMap<String, Status> myEnv = new LinkedHashMap<>();

        if(this.argsToPush != null) {
            for(String item: this.argsToPush.keySet()) {
                myEnv.put(item, this.argsToPush.get((item)));
            }
            this.setFunEnv(this.funId, this.argsToPush);
            this.argsToPush = null;
            this.funId = null;
        }
        this.environment.add(myEnv);
    }

    public void addVarDeclaration(String id) {
        int index = this.environment.size() - 1;
        LinkedHashMap<String, Status> myEnv = this.environment.get(index);
        myEnv.put(id, new Status());
        this.environment.set(index, myEnv);
    }

    public void addFunctionDeclaration(String id, boolean isVoid) {
        int index = this.environment.size() - 1;
        LinkedHashMap<String, Status> myEnv = this.environment.get(index);
        myEnv.put(id, new FunctionNode(id, isVoid));
        this.environment.set(index, myEnv);
    }

    public void setArgsToPush(LinkedHashMap<String,FunctionVar> argsToPush) {
        this.argsToPush = argsToPush;
    }

    public Status.States getState(String value) {

        for(int i = 0; i < this.environment.size(); i++) {
            HashMap<String, Status> item = this.environment.get(i);
            Status state = item.get(value);
            if(state != null) {
                return state.getState();
            }
        }
        return Status.States.Top;
    }

    public Status search(String id) {
        for(int i = 0; i < this.environment.size(); i++) {
            HashMap<String, Status> item = this.environment.get(i);
            Status state = item.get(id);
            if(state != null) {
                return state;
            }
        }
        return null;
    }

    public void update(String value, Status.States newState) {
        for(int i = 0; i < this.environment.size(); i++) {
            LinkedHashMap<String, Status> item = this.environment.get(i);
            Status status = item.get(value);
            if(status != null) {
                status.update(newState);
                item.put(value, status);
                this.environment.set(i, item);
            }
        }
    }

    public void setNewState(String value, Status.States newState) {
        for(int i = 0; i < this.environment.size(); i++) {
            LinkedHashMap<String, Status> item = this.environment.get(i);
            Status status = item.get(value);
            if(status != null) {
                status.setState(newState);
                item.put(value, status);
                this.environment.set(i, item);
            }
        }
    }

    public LinkedHashMap<String, Status> pop() {
        int index = this.environment.size() - 1;
        if(index < 0) {
            return null;
        }
        return this.environment.remove(index);
    }

    public void updateFunctionEnv(String id, LinkedHashMap<String, Status> env) {
        for(int i = 0; i < this.environment.size(); i++) {
            LinkedHashMap<String, Status> item = this.environment.get(i);
            Status state = item.get(id);
            if(state instanceof FunctionNode) {
                LinkedHashMap<String, FunctionVar> list = ((FunctionNode) state).getArgs();
                for(String argId: list.keySet()) {
                    FunctionVar arg = list.get(argId);
                    arg.update(env.get(argId).getState());
                    list.put(argId, arg);
                }
            }
        }
    }

    public void setFunEnv(String id, LinkedHashMap<String, FunctionVar> env) {
        for(int i = 0; i < this.environment.size(); i++) {
            LinkedHashMap<String, Status> item = this.environment.get(i);
            Status state = item.get(id);
            if(state instanceof FunctionNode) {
                ((FunctionNode) state).setArgs(env);
                item.put(id, state);
                this.environment.set(i, item);
            }
        }
    }

    public void printState() {
        System.out.println(this.environment.size());
        for(HashMap<String, Status> item: this.environment) {
            System.out.println("---------BEGIN ENV-------------");
            for(String id: item.keySet()) {
                Status value = item.get(id);
                StringBuilder print = new StringBuilder(id + ": " + value.getState());
                System.out.println(print);
            }
            System.out.println("--------- END ENV--------------");
        }
    }

    public void max(Effect env1, Effect env2) {
        for(LinkedHashMap<String, Status> newEnv: this.environment) {
            for(String id: newEnv.keySet()) {
                Status state = newEnv.get(id);
                state.update(state.max(env1.search(id).getState(), env2.search(id).getState()));
                newEnv.put(id, state);
            }
        }
    }

    public Effect deepCopyUsingSerialization()
    {
        try
        {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(bo);
            o.writeObject(this);

            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream i = new ObjectInputStream(bi);

            return (Effect)i.readObject();
        }
        catch(Exception e)
        {
            System.out.println("Error!");
            System.out.println(e.getMessage());
            return null;
        }
    }

}
