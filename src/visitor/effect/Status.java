package src.visitor.effect;

import java.io.*;
import java.util.ArrayList;

public class Status implements Serializable {

    public enum States {
        Bot, // declared variable
        Init, // initialized (sx expr)
        RW, // dx expr
        Top // error
    }

    private States state;

    public Status(States state) {
        this.state = state;
    }
    public Status() {
        this.state = States.Bot;
    }

    public void update(States newStatus) {
        if(this.state == States.Bot && newStatus == States.RW) {
            this.state = States.Top;
            return;
        }
        // if rules take the max of two
        this.state = this.max(this.state, newStatus);
    }

    public States max(States state1, States state2) {
        if(state1 == States.Top || state2 == States.Top) {
            return States.Top;
        }
        if(state1 == States.RW || state2 ==States.RW) {
            return States.RW;
        }
        if(state1 == States.Init || state2 ==States.Init) {
            return States.Init;
        }
        return States.Bot;
    }

    public States getState() {
        return state;
    }
    public void setState(States state) {
        this.state = state;
    }

    public Status deepCopy()
    {
        try
        {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(bo);
            o.writeObject(this);

            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream i = new ObjectInputStream(bi);

            return (Status)i.readObject();
        }
        catch(Exception e)
        {
            return null;
        }
    }

}
