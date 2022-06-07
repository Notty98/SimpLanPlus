package src.symbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public interface SymbolTableInterface {

    void newScope();

    void addDecl(String id, String type, int line);

    void addDecl(String id, String type, ArrayList<HashMap<String, String>> args, int line);

    String lookup(String id);

    String lookupDeclaration(String id);

    void exitScope();

}
