package src.codegen;

import src.codegen.codegenSTable.FunctionArg;
import src.codegen.codegenSTable.STable;
import src.parser.SimpLanPlusParser;
import src.utils.SimpLanUtils;
import src.visitor.SimpLanPlusBaseVisitor;
import src.visitor.type.BoolType;
import src.visitor.type.IntType;
import src.visitor.type.Type;
import src.visitor.type.VoidType;

import java.util.Objects;

public class VisitorCodeGen extends SimpLanPlusBaseVisitor<String> {

    private STable sTable;

    public VisitorCodeGen() {
        this.sTable = new STable();
    }

    @Override
    public String visitBlock(SimpLanPlusParser.BlockContext ctx) {
        this.sTable.addNestingLevel();
        //build offset
        for(int i = 0; i < ctx.declaration().size(); i++) {
            if(ctx.declaration(i).decVar() != null) {
                String name = ctx.declaration(i).decVar().ID().getText();
                Type type;
                if(Objects.equals(ctx.declaration(i).decVar().type().getText(), "int")) {
                    type = new IntType();
                } else {
                    type = new BoolType();
                }
                this.sTable.insertVariable(name, type, false);
            }
        }

        StringBuilder code = new StringBuilder("""
                addi $sp $sp -4
                sw $fp 0($sp)
                """);
        // add var dec (alloc)
        for(int i = ctx.declaration().size() - 1; i >= 0; i--) {
            if(ctx.declaration(i).decVar() != null) {
                String bytecodeDec = visit(ctx.declaration(i).decVar());
                code.append(bytecodeDec);
            }
            if(ctx.declaration(i).decFun() != null) {
                String bytecodeDec = visit(ctx.declaration(i).decFun());
                code.append(bytecodeDec);
            }
        }
        // push al
        code.append("move $al $fp\n").append("""
                addi $sp $sp -4
                sw $al 0($sp)\n""");
        code.append("move $fp $sp\n");
        // init dec var
        for(int i = ctx.declaration().size() - 1; i >= 0; i--) {
            if(ctx.declaration(i).decVar() != null && ctx.declaration(i).decVar().exp() != null) {
                String bytecodeDec = this.initVar(ctx.declaration(i).decVar());
                code.append(bytecodeDec);
            }
        }
        // visit stm
        for(SimpLanPlusParser.StatementContext stm: ctx.statement()) {
            code.append(visit(stm));
        }
        // vist dec fun
        //exit pop
        int total = this.sTable.calculateTotalOffset();
        code.append("addi $sp $sp 4").append('\n');
        code.append("addi $sp $sp ").append(total).append('\n');
        code.append("lw $fp 0($sp)").append('\n').append("addi $sp $sp 4").append('\n');
        //code.append("halt").append('\n');
        this.sTable.removeNestingLevel();
        return code.toString();
    }

    @Override
    public String visitDecFun(SimpLanPlusParser.DecFunContext ctx) {
        Type type;
        if(ctx.type() == null) {
            type = new VoidType();
        } else {
            if(Objects.equals(ctx.type().getText(), "int")) {
                type = new IntType();
            } else {
                type = new BoolType();
            }
        }
        this.sTable.insertFunction(ctx.ID().getText(), type);
        String endFunctionDec = SimpLanUtils.generateLabel("endFunctionDec");
        StringBuilder code = new StringBuilder("branch ").append(endFunctionDec).append('\n');
        code.append(sTable.getFunctionLabel(ctx.ID().getText())).append('\n');
        //visit function block
        this.sTable.addNestingLevel();
        for(int i = 0; i < ctx.arg().size(); i++) {
            Type typeArg;
            if(Objects.equals(ctx.arg(i).type().getText(), "int")) {
                typeArg = new IntType();
            } else {
                typeArg = new BoolType();
            }
            this.sTable.insertVariable(ctx.arg(i).ID().getText(), typeArg, ctx.arg(i).getText().startsWith("var"));
            FunctionArg arg = new FunctionArg(ctx.arg(i).ID().getText(), ctx.arg(i).getText().startsWith("var"),typeArg);
            this.sTable.insertArg(ctx.ID().getText(), arg);
        }
        // visit dec var
        for(int i = 0; i < ctx.block().declaration().size(); i++) {
            if(ctx.block().declaration(i).decVar() != null) {
                String name = ctx.block().declaration(i).decVar().ID().getText();
                Type typeVar;
                if(Objects.equals(ctx.block().declaration(i).decVar().type().getText(), "int")) {
                    typeVar = new IntType();
                } else {
                    typeVar = new BoolType();
                }
                this.sTable.insertVariable(name, typeVar, false);
            }
        }
        //add to stack
        for(int i = ctx.block().declaration().size() - 1; i >= 0; i--) {
            if(ctx.block().declaration(i).decVar() != null) {
                String bytecodeDec = visit(ctx.block().declaration(i).decVar());
                code.append(bytecodeDec);
            }
            /*if(ctx.declaration(i).decFun() != null) {
                String bytecodeDec = visit(ctx.declaration(i).decFun());
                code.append(bytecodeDec);
            }*/
        }
        //push al ($t2)
        code.append("""
                addi $sp $sp -4
                sw $t2 0($sp)\n""");
        // update fp
        code.append("move $fp $sp\n");
        // push ra
        code.append("""
                addi $sp $sp -4
                sw $ra 0($sp)\n""");
        //visit stm
        for(int i = 0; i < ctx.block().statement().size(); i++) {
            code.append(visit(ctx.block().statement(i)));
        }
        // pop ra
        code.append("lw $ra 0($sp)\n").append("""
                addi $sp $sp 4\n""");
        // pop al
        code.append("addi $sp $sp 4\n");
        // pop k
        int totalOffset = this.sTable.calculateTotalOffset();
        code.append("addi $sp $sp ").append(totalOffset).append('\n');
        // pop fp
        code.append("lw $fp 0($sp)\n").append("""
                addi $sp $sp 4\n""");
        code.append("jr $ra\n");

        code.append(endFunctionDec).append('\n');
        this.sTable.removeNestingLevel();
        return code.toString();
    }

    @Override
    public String visitDecVar(SimpLanPlusParser.DecVarContext ctx) {
        Type type;
        if(Objects.equals(ctx.type().getText(), "int")) {
            type = new IntType();
        } else {
            type = new BoolType();
        }
        StringBuilder bytecode;
        if(type instanceof IntType) {
            bytecode = new StringBuilder("""
                    addi $sp $sp -4
                    """);
        } else {
            bytecode = new StringBuilder("""
                    addi $sp $sp -1
                    """);
        }
        /*if(ctx.exp() != null) {
            String expBytecode = visit(ctx.exp());
            if(type instanceof IntType) {
                bytecode = new StringBuilder("""
                    addi $sp $sp -4
                    """);
                //bytecode.append(expBytecode);
                //bytecode.append("sw $a0 0($sp)").append('\n');
            } else {
                bytecode = new StringBuilder("""
                    addi $sp $sp -1
                    """);
                //bytecode.append(expBytecode);
                //bytecode.append("sw_b $a0 0($sp)").append('\n');
            }
        } else {

        }*/
        return bytecode.toString();
    }

    @Override
    public String visitBoolExp(SimpLanPlusParser.BoolExpContext ctx) {
        String value = ctx.BOOL().getText();
        if(value.equals("false")) {
            return "lb $a0 0\n";
        } else {
            return "lb $a0 1\n";
        }
    }

    @Override
    public String visitValExp(SimpLanPlusParser.ValExpContext ctx) {
        String value = ctx.NUMBER().getText();
        return "li $a0 " + value + "\n";
    }

    @Override
    public String visitDerExp(SimpLanPlusParser.DerExpContext ctx) {
        String name = ctx.ID().getText();
        Type type = this.sTable.getVariableType(name);
        StringBuilder code = new StringBuilder("move $al $fp\n");
        int currentNestingLevel = this.sTable.getCurrentNestingLevel();
        int variableNL = this.sTable.getNestingLevel(name);
        for (int i = 0; i < currentNestingLevel - variableNL; i++) {
            code.append("lw $al 0($al)").append('\n');
        }

        int offset = this.sTable.getVariableOffset(name);
        if(this.sTable.isVarByName(name)) {
            //code.append("addi $al $al ").append(offset).append('\n');
            code.append("lw $al ").append(offset).append("($al)").append('\n');
            offset = 0;
        }
        if(type instanceof IntType) {
            code.append("lw $a0 ").append(offset).append("($al)").append('\n');
        } else {
            code.append("lw_b $a0 ").append(offset).append("($al)").append('\n');
        }
        return code.toString();


    }

    @Override
    public String visitAssignment(SimpLanPlusParser.AssignmentContext ctx) {
        String name = ctx.ID().getText();
        String exp = visit(ctx.exp());
        StringBuilder code = new StringBuilder(exp);
        Type type = this.sTable.getVariableType(name);
        int variableNL = this.sTable.getNestingLevel(name);
        int currentNestingLevel = this.sTable.getCurrentNestingLevel();
        code.append("move $al $fp\n");
        for (int i = 0; i < currentNestingLevel - variableNL; i++) {
            code.append("lw $al 0($al)").append('\n');
        }

        int offset = this.sTable.getVariableOffset(name);
        if(this.sTable.isVarByName(name)) {
            code.append("lw $al ").append(offset).append("($al)").append('\n');
            offset = 0;
        }
        if(type instanceof IntType) {
            code.append("sw $a0 ").append(offset).append("($al)").append('\n');
        } else {
            code.append("sw_b $a0 ").append(offset).append("($al)").append('\n');
        }


        return code.toString();
    }

    @Override
    public String visitBinExp(SimpLanPlusParser.BinExpContext ctx) {
        StringBuilder code = new StringBuilder(visit(ctx.exp(0)));
        if(Objects.equals(ctx.op.getText(), "||")) {
            String trueBranch = SimpLanUtils.generateLabel("trueBranch");
            String exit = SimpLanUtils.generateLabel("exit");
            code.append("lb $t1 1").append('\n').append("beq_b $a0 $t1 ").append(trueBranch).append('\n');
            code.append(visit(ctx.exp(1)));
            code.append("lb $t1 1").append('\n').append("beq_b $a0 $t1 ").append(trueBranch).append('\n');
            code.append("lb $a0 0").append('\n').append("branch ").append(exit).append('\n');
            code.append(trueBranch).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
            return code.toString();

        }
        if(Objects.equals(ctx.op.getText(), "&&")) {
            String falseBranch = SimpLanUtils.generateLabel("falseBranch");
            String exit = SimpLanUtils.generateLabel("exit");
            code.append("lb $t1 0").append('\n').append("beq_b $a0 $t1 ").append(falseBranch).append('\n');
            code.append(visit(ctx.exp(1)));
            code.append("lb $t1 0").append('\n').append("beq_b $a0 $t1 ").append(falseBranch).append('\n');
            code.append("lb $a0 1").append('\n').append("branch ").append(exit).append('\n');
            code.append(falseBranch).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
            return code.toString();

        }
        if(Objects.equals(ctx.op.getText(), "==")) {
            String equalBranch = SimpLanUtils.generateLabel("equalBranch");
            String exit = SimpLanUtils.generateLabel("exitEqual");
            code.append("addi $sp $sp -4").append('\n').append("sw $a0 0($sp)").append('\n');
            code.append(visit(ctx.exp(1)));
            code.append("lw $t1 0($sp)").append('\n').append("beq $a0 $t1 ").append(equalBranch).append('\n');
            code.append("lb $a0 0").append('\n').append("branch ").append(exit).append('\n');
            code.append(equalBranch).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
            code.append("addi $sp $sp 4").append('\n'); //pop
            return code.toString();
        }
        if(Objects.equals(ctx.op.getText(), "!=")) {
            String notEqualFalseBranch = SimpLanUtils.generateLabel("notEqualBranch");
            String exit = SimpLanUtils.generateLabel("exitNotEqual");
            code.append("addi $sp $sp -4").append('\n').append("sw $a0 0($sp)").append('\n');
            code.append(visit(ctx.exp(1)));
            code.append("lw $t1 0($sp)").append('\n').append("beq $a0 $t1 ").append(notEqualFalseBranch).append('\n');
            code.append("lb $a0 1").append('\n').append("branch ").append(exit).append('\n');
            code.append(notEqualFalseBranch).append('\n').append("lb $a0 0").append('\n').append(exit).append('\n');
            code.append("addi $sp $sp 4").append('\n'); //pop
            return code.toString();
        }
        if(Objects.equals(ctx.op.getText(), ">") || Objects.equals(ctx.op.getText(), ">=") || Objects.equals(ctx.op.getText(), "<") || Objects.equals(ctx.op.getText(), "<=")) {
            code.append("addi $sp $sp -4").append('\n').append("sw $a0 0($sp)").append('\n');
            code.append(visit(ctx.exp(1)));
            if(Objects.equals(ctx.op.getText(), ">")) {
                String greater = SimpLanUtils.generateLabel("greater");
                String exit = SimpLanUtils.generateLabel("exitGreater");
                code.append("lw $t1 0($sp)").append('\n').append("bgt $t1 $a0 ").append(greater).append('\n');
                code.append("lb $a0 0").append('\n').append("branch ").append(exit).append('\n');
                code.append(greater).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
                code.append("addi $sp $sp 4").append('\n'); //pop
                return code.toString();
            }
            if(Objects.equals(ctx.op.getText(), ">=")) {
                String greaterThan = SimpLanUtils.generateLabel("greaterThan");
                String exit = SimpLanUtils.generateLabel("exitGreaterThan");
                code.append("lw $t1 0($sp)").append('\n').append("bge $t1 $a0 ").append(greaterThan).append('\n');
                code.append("lb $a0 0").append('\n').append("branch ").append(exit).append('\n');
                code.append(greaterThan).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
                code.append("addi $sp $sp 4").append('\n'); //pop
                return code.toString();
            }
            if(Objects.equals(ctx.op.getText(), "<")) {
                String less = SimpLanUtils.generateLabel("less");
                String exit = SimpLanUtils.generateLabel("exitLess");
                code.append("lw $t1 0($sp)").append('\n').append("bgt $a0 $t1").append(less).append('\n');
                code.append("lb $a0 0").append('\n').append("branch ").append(exit).append('\n');
                code.append(less).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
                code.append("addi $sp $sp 4").append('\n'); //pop
                return code.toString();
            }
            if(Objects.equals(ctx.op.getText(), "<=")) {
                String lessThan = SimpLanUtils.generateLabel("lessThan");
                String exit = SimpLanUtils.generateLabel("exitLess");
                code.append("lw $t1 0($sp)").append('\n').append("bge $a0 $t1").append(lessThan).append('\n');
                code.append("lb $a0 0").append('\n').append("branch ").append(exit).append('\n');
                code.append(lessThan).append('\n').append("lb $a0 1").append('\n').append(exit).append('\n');
                code.append("addi $sp $sp 4").append('\n'); //pop
                return code.toString();
            }
        }
        if(Objects.equals(ctx.op.getText(), "*")) {
            //code.append(visit(ctx.exp(0)));
            code.append("move $t1 $a0").append("\n");
            code.append(visit(ctx.exp(1)));
            code.append("mult $a0 $t1 $a0").append("\n");
            return code.toString();
        }
        if(Objects.equals(ctx.op.getText(), "/")) {
            //code.append(visit(ctx.exp(0)));
            code.append("move $t1 $a0").append("\n");
            code.append(visit(ctx.exp(1)));
            code.append("div $a0 $t1 $a0").append("\n");
            return code.toString();
        }
        if(Objects.equals(ctx.op.getText(), "+")) {
            //code.append(visit(ctx.exp(0)));
            code.append("move $t1 $a0").append("\n");
            code.append(visit(ctx.exp(1)));
            code.append("add $a0 $t1 $a0").append("\n");
            return code.toString();
        }
        if(Objects.equals(ctx.op.getText(), "-")) {
            //code.append(visit(ctx.exp(0)));
            code.append("move $t1 $a0").append("\n");
            code.append(visit(ctx.exp(1)));
            code.append("sub $a0 $t1 $a0").append("\n");
            return code.toString();
        }

        return "";
    }

    @Override
    public String visitBaseExp(SimpLanPlusParser.BaseExpContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public String visitPrint(SimpLanPlusParser.PrintContext ctx) {
        return visit(ctx.exp()) + "print $a0" + '\n';
    }

    @Override
    public String visitStatement(SimpLanPlusParser.StatementContext ctx) {
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
    public String visitNegExp(SimpLanPlusParser.NegExpContext ctx) {
        String exp = visit(ctx.exp());
        StringBuilder code = new StringBuilder("li $t1 -1 \n");
        code.append(exp);
        code.append("mult $a0 $a0 $t1 \n");
        return code.toString();
    }

    @Override
    public String visitNotExp(SimpLanPlusParser.NotExpContext ctx) {
        String ifTrue = SimpLanUtils.generateLabel("ifTrue");
        String exit = SimpLanUtils.generateLabel("exitNot");
        String exp = visit(ctx.exp());
        StringBuilder code = new StringBuilder("lb $t1 1\n");
        code.append(exp);
        code.append("beq $a0 $t1 ifTrue\n").append("lb $a0 1\n").append("branch ").append(exit).append('\n').append(ifTrue).append('\n').append("lb $a0 0\n").append(exit).append('\n');
        return code.toString();
    }

    @Override
    public String visitIte(SimpLanPlusParser.IteContext ctx) {
        String codeExp = visit(ctx.exp());
        String exitIf = SimpLanUtils.generateLabel("exitIf");
        StringBuilder code = new StringBuilder(codeExp);
        if(ctx.statement().size() > 1) {
            //if then else
            String elseLabel = SimpLanUtils.generateLabel("elseSTM");
            code.append("lb $t1 0").append('\n');
            code.append("beq_b $a0 $t1 ").append(elseLabel).append('\n');
            code.append(visit(ctx.statement(0)));
            code.append("branch ").append(exitIf).append('\n');
            code.append(elseLabel).append('\n');
            code.append(visit(ctx.statement(1)));
            code.append(exitIf).append('\n');

        } else {
            // if then
            code.append("lb $t1 0").append('\n');
            code.append("beq_b $a0 $t1 ").append(exitIf).append('\n');
            code.append(visit(ctx.statement(0)));
            code.append(exitIf).append('\n');
        }
        return code.toString();
    }

    @Override
    public String visitRet(SimpLanPlusParser.RetContext ctx) {
        StringBuilder code = new StringBuilder();
        if(ctx.exp() != null) {
            String expCode = visit(ctx.exp());
            code.append(expCode);
        }
        return code.toString();
    }

    @Override
    public String visitCall(SimpLanPlusParser.CallContext ctx) {
        StringBuilder code = new StringBuilder("addi $sp $sp -4")
                .append('\n')
                .append("sw $fp 0($sp)")
                .append('\n');
        for(int i = ctx.exp().size() - 1; i >= 0; i--) {
            //set new state of i-arg
            if(this.sTable.getFunctionIsVarByIndex(ctx.ID().getText(), i) && ctx.exp(i) instanceof SimpLanPlusParser.DerExpContext) {
                this.sTable.updateIsVar(((SimpLanPlusParser.DerExpContext) ctx.exp(i)).ID().getText(), true);
                code.append(this.pushVarAddress((SimpLanPlusParser.DerExpContext) ctx.exp(i)));
            } else {
                code.append(visit(ctx.exp(i)));
            }
            if(this.sTable.getFunctionArgByIndex(ctx.ID().getText(), i) instanceof IntType || this.sTable.getFunctionIsVarByIndex(ctx.ID().getText(), i)) {
                code.append("addi $sp $sp -4").append('\n')
                        .append("sw $a0 0($sp)").append('\n');
            } else {
                code.append("addi $sp $sp -1").append('\n')
                        .append("sw $a0 0($sp)").append('\n');
            }
        }
        code.append("move $al $fp\n");
        // load $al $t2
        int currentNestingLevel = this.sTable.getCurrentNestingLevel();
        int variableNL = this.sTable.getNestingLevel(ctx.ID().getText());
        for(int i = 0; i < currentNestingLevel - variableNL; i++) {
            code.append("lw $al 0($al)").append('\n');
        }
        code.append("move $t2 $al").append('\n');
        code.append("jtl ").append(this.sTable.getFunctionLabel(ctx.ID().getText())).append('\n');

        for(int i = ctx.exp().size() - 1; i >= 0; i--) {
            //set new state of i-arg
            if(this.sTable.getFunctionIsVarByIndex(ctx.ID().getText(), i) && ctx.exp(i) instanceof SimpLanPlusParser.DerExpContext) {
                this.sTable.updateIsVar(((SimpLanPlusParser.DerExpContext) ctx.exp(i)).ID().getText(), false);
            }
        }

        return code.toString();
    }

    public String initVar(SimpLanPlusParser.DecVarContext ctx) {
        Type type;
        if(Objects.equals(ctx.type().getText(), "int")) {
            type = new IntType();
        } else {
            type = new BoolType();
        }
        StringBuilder bytecode = new StringBuilder(visit(ctx.exp()));
        int variableNL = this.sTable.getNestingLevel(ctx.ID().getText());
        int currentNestingLevel = this.sTable.getCurrentNestingLevel();
        bytecode.append("move $al $fp\n");
        for(int i = 0; i < currentNestingLevel - variableNL; i++) {
            bytecode.append("lw $al 0($al)").append('\n');
        }
        int offset = this.sTable.getVariableOffset(ctx.ID().getText());
        if(ctx.exp() != null) {
            if(type instanceof IntType) {
                bytecode.append("sw $a0 ").append(offset).append("($al)").append('\n');
            } else {
                bytecode.append("sw_b $a0 ").append(offset).append("($al)").append('\n');
            }
            return bytecode.toString();
        }
        return "";
    }

    public String pushVarAddress(SimpLanPlusParser.DerExpContext ctx) {
        System.out.println("push arg!!");
        String name = ctx.ID().getText();
        Type type = this.sTable.getVariableType(name);
        StringBuilder code = new StringBuilder("move $al $fp\n");
        int currentNestingLevel = this.sTable.getCurrentNestingLevel();
        int variableNL = this.sTable.getNestingLevel(name);
        for (int i = 0; i < currentNestingLevel - variableNL; i++) {
            code.append("lw $al 0($al)").append('\n');
        }

        int offset = this.sTable.getVariableOffset(name);
        if(this.sTable.isVarByName(name)) {
            code.append("addi $al $al ").append(offset).append('\n');
            code.append("move $a0 $al").append('\n');
        }
        return code.toString();


    }


}
