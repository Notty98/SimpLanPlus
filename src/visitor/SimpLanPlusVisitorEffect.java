package src.visitor;

import src.parser.SimpLanPlusParser;
import src.utils.ErrorLogger;
import src.visitor.effect.Effect;
import src.visitor.effect.FunctionNode;
import src.visitor.effect.FunctionVar;
import src.visitor.effect.Status;

import java.util.*;

public class SimpLanPlusVisitorEffect extends SimpLanPlusBaseVisitor<Object> {

    private Effect environment;
    private LinkedHashMap<String, FunctionVar> argsToPush;
    private String funId;
    private ErrorLogger logger;

    private ArrayList<String> effectErrors;

    public void writeErrors() {
        this.logger.write("=====================EFFECT ANALYSIS=====================");
        this.logger.write("Number of errors: " + this.effectErrors.size());
        for (String error: this.effectErrors) {
            this.logger.write("\t" + error);
        }
        this.logger.write("=====================EFFECT ANALYSIS=====================");
    }

    public SimpLanPlusVisitorEffect(ErrorLogger logger) {
        this.environment = new Effect();
        this.effectErrors = new ArrayList<>();
        this.logger = logger;
    }

    @Override
    public Effect visitBlock(SimpLanPlusParser.BlockContext ctx) {
        String funVisited = null;
        if(funId != null) {
            this.environment.setArgsToPush(this.argsToPush);
            funVisited = funId;
            this.argsToPush = null;
            this.funId = null;
        }
        this.environment.newEnvironment();
        for(SimpLanPlusParser.DeclarationContext item: ctx.declaration()){
            visit(item);
        }
        for(SimpLanPlusParser.StatementContext item: ctx.statement()) {
            visit(item);
        }
        LinkedHashMap<String, Status> oldEnv = this.environment.pop();
        for(String id: oldEnv.keySet()) {
            Status status = oldEnv.get(id);
            if(status instanceof FunctionNode || status instanceof FunctionVar) {
                continue;
            }
            if(status.getState() == Status.States.Bot || status.getState() == Status.States.Init) {
                this.effectErrors.add("line " + ctx.getStart().getLine() + ": Warning the variable " + id + " is declared but never used");
            }
            if(status.getState() == Status.States.Top) {
                this.effectErrors.add("line " + ctx.getStart().getLine() + ": Attention invalid status (top) for the variable " + id);
            }
        }
        if(funVisited != null) {
            this.environment.updateFunctionEnv(funVisited, oldEnv);
        }
        return this.environment;
    }

    @Override
    public Status.States visitDecVar(SimpLanPlusParser.DecVarContext ctx) {
        this.environment.addVarDeclaration(ctx.ID().getText());
        if(ctx.exp() != null) {
            Status.States state = (Status.States) visit(ctx.exp());
            if(state == Status.States.Bot || state == Status.States.Top) {
                this.effectErrors.add("line " + ctx.getStart().getLine() + ": Attention the variable " + ctx.ID().getText() + " is not initialized");
                this.environment.update(ctx.ID().getText(), Status.States.Top);
                return null;
            }
            this.environment.update(ctx.ID().getText(), Status.States.Init);
        }
        return null;
    }

    @Override
    public Status.States visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) {
        return Status.States.Init;
    }

    @Override
    public Status.States visitValExp(SimpLanPlusParser.ValExpContext ctx) {
        return Status.States.Init;
    }

    @Override
    public Status.States visitCall(SimpLanPlusParser.CallContext ctx) {
        String funId = ctx.ID().getText();

        for(int i = 0; i < ctx.exp().size(); i++) {
            if(ctx.exp(i) instanceof SimpLanPlusParser.DerExpContext) {
                String var = ((SimpLanPlusParser.DerExpContext) ctx.exp(i)).ID().getText();
                FunctionNode fun = (FunctionNode) this.environment.search(funId);
                FunctionVar functionVar = fun.getArgs().get(fun.searchByIndex(i));
                if(functionVar.isVar()) {
                    this.environment.setNewState(var, functionVar.getState());
                } else {
                    visit(ctx.exp(i));
                }
            } else {
                visit(ctx.exp(i));
            }

        }

        FunctionNode node = (FunctionNode) this.environment.search(funId);
        if(node != null) {
            if(node.isVoid()) {
                return Status.States.Bot;
            }
            return Status.States.Init;
        } else {
            System.out.println("Error fun not found!");
        }
        return Status.States.Bot;
    }

    @Override
    public Status.States visitBinExp(SimpLanPlusParser.BinExpContext ctx) {
        Status.States left = (Status.States) visit(ctx.left);
        Status.States right = (Status.States) visit(ctx.right);
        if(left == Status.States.Bot || right == Status.States.Bot || left == Status.States.Top || right == Status.States.Top) {
            return Status.States.Top;
        }
        return Status.States.Init;
    }

    @Override
    public Status.States visitDerExp(SimpLanPlusParser.DerExpContext ctx) {
        this.environment.update(ctx.ID().getText(), Status.States.RW);
        return this.environment.getState(ctx.ID().getText());
    }

    @Override
    public Status.States visitNotExp(SimpLanPlusParser.NotExpContext ctx) {
        return (Status.States) visit(ctx.exp());
    }

    @Override
    public Status.States visitBaseExp(SimpLanPlusParser.BaseExpContext ctx) {
        return (Status.States) visit(ctx.exp());
    }

    @Override
    public Status.States visitNegExp(SimpLanPlusParser.NegExpContext ctx) {
        return (Status.States) visit(ctx.exp());
    }

    @Override
    public Status.States visitRet(SimpLanPlusParser.RetContext ctx) {
        if(ctx.exp() != null) {
            Status.States state = (Status.States) visit(ctx.exp());
            if(state == Status.States.Bot || state == Status.States.Top) {
                this.effectErrors.add("line " + ctx.getStart().getLine() + ": Attention the expression inside the return statement is not initialized  ");
            }
        }
        return null;
    }

    @Override
    public Status.States visitPrint(SimpLanPlusParser.PrintContext ctx) {
        Status.States state = (Status.States) visit(ctx.exp());
        if(state == Status.States.Top || state == Status.States.Bot) {
            this.effectErrors.add("line " + ctx.getStart().getLine() + ": Attention the expression inside the print statement is not initialized ");
        }
        return null;
    }

    @Override
    public Status.States visitAssignment(SimpLanPlusParser.AssignmentContext ctx) {
        Status.States state = (Status.States) visit(ctx.exp());
        if(state == Status.States.Top || state == Status.States.Bot) {
            this.effectErrors.add("line " + ctx.getStart().getLine() + ": Attention invalid assignment of " + ctx.ID().getText());
            this.environment.update(ctx.ID().getText(), Status.States.Top);
        } else {
            this.environment.update(ctx.ID().getText(), Status.States.Init);
        }
        return null;
    }

    @Override
    public Status.States visitDeclaration(SimpLanPlusParser.DeclarationContext ctx) {
        if(ctx.decFun() == null) {
            visit(ctx.decVar());
        } else {
            visit(ctx.decFun());
        }
        return null;
    }

    @Override
    public Object visitStatement(SimpLanPlusParser.StatementContext ctx) {
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
    public Status.States visitIte(SimpLanPlusParser.IteContext ctx) {
        Status.States exp = (Status.States) visit(ctx.exp());
        if(exp == Status.States.Bot || exp == Status.States.Top) {
            this.effectErrors.add("line " + ctx.getStart().getLine() + ": Attention the expression inside the if statement is not initialized ");
        }
        if(ctx.statement().size() > 1 && ctx.statement(0).block() != null && ctx.statement(1).block() != null) {
            Effect cache = this.environment.deepCopyUsingSerialization();
            Effect cache_else = this.environment.deepCopyUsingSerialization();
            Effect env1 = (Effect) visit(ctx.statement(0));
            this.environment = cache_else;
            Effect env2 = (Effect) visit(ctx.statement(1));
            this.environment = cache;
            this.environment.max(env1, env2);
            return null;
        }

        visit(ctx.statement(0));
        if(ctx.statement().size() > 1) {
            visit(ctx.statement(1));
        }

        return null;
    }

    @Override
    public Status.States visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
        this.funId = ctx.ID().getText();
        //load parameter
        if(ctx.arg() != null) {
            LinkedHashMap<String, FunctionVar> argsToPush = new LinkedHashMap<>();
            for(int i = 0; i < ctx.arg().size(); i++) {
                boolean isBool = ctx.arg().get(i).getText().startsWith("var");
                argsToPush.put(ctx.arg().get(i).ID().getText(), new FunctionVar(ctx.arg().get(i).ID().getText(), isBool, i));
            }
            this.environment.setFunId(this.funId);
            this.environment.addFunctionDeclaration(funId, ctx.type() == null);
            this.environment.setArgsToPush(argsToPush);
            this.argsToPush = argsToPush;
        }
        visit(ctx.block());
        return null;
    }

    public ArrayList<String> getEffectErrors() {
        return effectErrors;
    }

}
