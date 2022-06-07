package src.visitor;

import src.parser.SimpLanPlusParser;
import src.symbolTable.FunctionNode;
import src.symbolTable.Node;
import src.symbolTable.SymbolTable;
import src.utils.Logger;
import src.visitor.type.BoolType;
import src.visitor.type.IntType;
import src.visitor.type.Type;
import src.visitor.type.VoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SimpLanPlusVisitorTypeCheck extends SimpLanPlusBaseVisitor<Type>{

    private final SymbolTable symbolTable;
    private ArrayList<HashMap<String, String>> argToPush = null;
    private ArrayList<String> typeErrors = null;
    private Logger logger;

    public SimpLanPlusVisitorTypeCheck(Logger logger) {
        this.symbolTable = new SymbolTable();
        this.typeErrors = new ArrayList<>();
        this.logger = logger;
    }

    public ArrayList<String> getTypeErrors() {
        return this.typeErrors;
    }

    public void writeErrors() {
        this.logger.write("=====================TYPE CHECK ERRORS=====================");
        this.logger.write("Number of errors: " + this.typeErrors.size());
        for (String error: this.typeErrors) {
            this.logger.write("\t" + error);
        }
        this.logger.write("=====================TYPE CHECK ERRORS=====================");
    }

    @Override
    public Type visitBlock(SimpLanPlusParser.BlockContext ctx) {
        this.symbolTable.newScope();
        // push function arg
        if(this.argToPush != null) {
            for(HashMap<String, String> arg: this.argToPush) {
                for(String id: arg.keySet()) {
                    this.symbolTable.addDecl(id, arg.get(id), ctx.getStart().getLine());
                }
            }
        }
        this.argToPush = null;
        for(SimpLanPlusParser.DeclarationContext item: ctx.declaration()){
            visit(item);
        }
        for(SimpLanPlusParser.StatementContext item: ctx.statement()) {
            visit(item);
        }
        this.symbolTable.exitScope();
        return new VoidType();
        //return super.visitBlock(ctx);
    }

    @Override
    public Type visitDeclaration(SimpLanPlusParser.DeclarationContext ctx) {
        if(ctx.decFun() == null) {
            return visit(ctx.decVar());
        } else {
            return visit(ctx.decFun());
        }
    }

    @Override
    public Type visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
        Type type;
        if(ctx.type() == null) {
            type = new VoidType();
        } else {
            type = visit(ctx.type());
        }
        //load parameter
        this.argToPush = new ArrayList<>();
        for(SimpLanPlusParser.ArgContext item: ctx.arg()) {
            HashMap<String, String> arg = new HashMap<>();
            arg.put(item.ID().getText(), item.type().getText());
            this.argToPush.add(arg);
        }
        this.symbolTable.addDecl(ctx.ID().getText(), type.getType(), this.argToPush, ctx.getStart().getLine());
        return visit(ctx.block());
    }

    @Override
    public Type visitRet(SimpLanPlusParser.RetContext ctx) {
        if(ctx.exp() != null) {
            return visit(ctx.exp());
        }
        return new VoidType();
    }

    @Override
    public Type visitCall(SimpLanPlusParser.CallContext ctx) {
        Node node = this.symbolTable.lookupNode(ctx.ID().getText());
        if(!ctx.exp().isEmpty()) {
            ArrayList<String> argList = new ArrayList<>();
            for(SimpLanPlusParser.ExpContext arg: ctx.exp()) {
                Type type = visit(arg);
                argList.add(type.getType());
            }
            if(node instanceof FunctionNode) {
                ArrayList<HashMap<String, String>> formalArg = ((FunctionNode) node).getArgs();
                if(argList.size() != formalArg.size()) {
                    this.typeErrors.add("line " + ctx.getStart().getLine() + ": the invocation of function " + ctx.ID().getText() + " doesn't match with the number of parameter in declaration");
                } else {
                    for(int i = 0; i < argList.size(); i++) {
                        ArrayList<String> argType = new ArrayList<>(formalArg.get(i).values());
                        if(!argList.get(i).equals(argType.get(0))) {
                            this.typeErrors.add("line " + ctx.getStart().getLine() + ": the invocation of function " + ctx.ID().getText() + " doesn't match with the type of parameter in declaration");
                            break;
                        }
                    }
                }

            }
        }
        if(node.getType().equals("bool")) {
            return new BoolType();
        } else {
            if(node.getType().equals("int")) {
                return new IntType();
            } else {
                return new VoidType();
            }

        }
    }

    @Override
    public Type visitDecVar(SimpLanPlusParser.DecVarContext ctx) {
        String name = ctx.ID().getText();
        Type type =  visit(ctx.type());
        this.symbolTable.addDecl(name, type.getType(), ctx.getStart().getLine());
        if(ctx.exp() != null) {
            Type typeExp = visit(ctx.exp());
            System.out.println(name +" "+ typeExp.getType());
            if(!type.getType().equals(typeExp.getType())) {
                this.typeErrors.add("line " + ctx.getStart().getLine() + ": the variable " + name + " is declared " + type.getType());
            }
        }
        return type;
    }

    @Override
    public Type visitType(SimpLanPlusParser.TypeContext ctx) {
        if(Objects.equals(ctx.getText(), "int")) {
            return new IntType();
        } else {
            return new BoolType();
        }
    }

    @Override
    public Type visitAssignment(SimpLanPlusParser.AssignmentContext ctx) {
        Type type =  visit(ctx.exp());
        String name = ctx.ID().getText();
        String varType = this.symbolTable.lookup(name);
        if(varType != null && !type.getType().equals(varType)) {
            System.out.println(name + " " + varType);
            this.typeErrors.add("line " + ctx.getStart().getLine() + ": the variable " + name + " is declared " + varType);
        }
        return new VoidType();
    }

    @Override
    public Type visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) {
        return new BoolType();
    }

    @Override
    public Type visitValExp(SimpLanPlusParser.ValExpContext ctx) {
        return new IntType();
    }

    @Override
    public Type visitBaseExp(SimpLanPlusParser.BaseExpContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public Type visitNegExp(SimpLanPlusParser.NegExpContext ctx) {
        Type type = visit(ctx.exp());
        if(type.getType().equals("int")) {
            this.typeErrors.add("line " + ctx.getStart().getLine() + ": the neg expression needs an integer");
        }
        return type;
    }

    @Override
    public Type visitNotExp(SimpLanPlusParser.NotExpContext ctx) {
        Type type = visit(ctx.exp());
        if(type.getType().equals("bool")) {
            this.typeErrors.add("line " + ctx.getStart().getLine() + ": the not expression needs a bool");
        }
        return type;
    }

    @Override
    public Type visitPrint(SimpLanPlusParser.PrintContext ctx) {
        visit(ctx.exp());
        return new VoidType();
    }

    @Override
    public Type visitStatement(SimpLanPlusParser.StatementContext ctx) {
        if(ctx.assignment() != null) {
            return visit(ctx.assignment());
        }
        if(ctx.print() != null) {
            return visit(ctx.print());
        }
        if(ctx.ret() != null) {
            return visit(ctx.ret());
        }
        if(ctx.ite() != null) {
            return visit(ctx.ite());
        }
        if(ctx.call() != null) {
            return visit(ctx.call());
        }
        if(ctx.block() != null) {
            return visit(ctx.block());
        }
        return null;
    }

    @Override
    public Type visitBinExp(SimpLanPlusParser.BinExpContext ctx) {
        String textOp = ctx.op.getText();
        if(textOp.equals("*") || textOp.equals("/") || textOp.equals("+") || textOp.equals("-")) {
            Type leftType = visit(ctx.left);
            Type right = visit(ctx.right);
            if(!(leftType.getType().equals("int") && right.getType().equals("int"))) {
                this.typeErrors.add("line " + ctx.getStart().getLine() + ": the operator " + textOp + " needs two integers");
            }
            return new IntType();
        }
        if(textOp.equals("||") || textOp.equals("&&")) {
            Type leftType = visit(ctx.left);
            Type right = visit(ctx.right);
            if(!(leftType.getType().equals("bool") && right.getType().equals("bool"))) {
                this.typeErrors.add("line " + ctx.getStart().getLine() + ": the operator " + textOp + " needs two bool");
            }
            return new BoolType();
        }

        if(textOp.equals("<") || textOp.equals("<=") || textOp.equals(">") || textOp.equals(">=")) {
            Type leftType = visit(ctx.left);
            Type right = visit(ctx.right);
            if(!(leftType.getType().equals("int") && right.getType().equals("int"))) {
                System.out.println("line " + ctx.getStart().getLine() + ": the operator " + textOp + " needs two int");
                this.typeErrors.add("line " + ctx.getStart().getLine() + ": the operator " + textOp + " needs two int");
            }
            return new BoolType();
        }

        // != and ==
        Type leftType = visit(ctx.left);
        Type right = visit(ctx.right);
        if(!(leftType.getType().equals(right.getType()))) {
            this.typeErrors.add("line " + ctx.getStart().getLine() + ": the operator " + textOp + " needs the same type");
        }
        return new BoolType();
    }

    @Override
    public Type visitIte(SimpLanPlusParser.IteContext ctx) {
        Type exp = visit(ctx.exp());
        System.out.println("IT: " + exp);
        if(!exp.getType().equals("bool")) {
            this.typeErrors.add("line " + ctx.getStart().getLine() + ": the condition of if must be a bool!");
        }
        Type thenType = visit(ctx.statement(0));
        if(ctx.statement().size() > 1) {
            Type elseType = visit(ctx.statement(1));
            if(!thenType.getType().equals(elseType.getType())) {
                this.typeErrors.add("line " + ctx.getStart().getLine() + ": the then and else branches of if must have the same type!");
            }

        }
        return thenType;
    }

    @Override
    public Type visitDerExp(SimpLanPlusParser.DerExpContext ctx) {
        String type =  this.symbolTable.lookup(ctx.ID().getText());
        return Objects.equals(type, "int") ? new IntType() : new BoolType();
    }
}
