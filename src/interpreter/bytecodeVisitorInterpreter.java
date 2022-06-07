package src.interpreter;

import java.util.ArrayList;
import java.util.HashMap;

public class bytecodeVisitorInterpreter extends bytecodeBaseVisitor<Void> {

    private int i = 0;
    //public int[] code = new int[10000];
    public HashMap<Integer, bytecodeParser.InstructionContext>[] code;
    public HashMap<String, Integer> labelRef;

    public bytecodeVisitorInterpreter() {
        //this.code = code;
        this.code = (HashMap<Integer, bytecodeParser.InstructionContext>[]) new HashMap<?,?>[10000];
        this.labelRef = new HashMap<>();
    }

    public HashMap<String, Integer> getLabelRef() {
        return this.labelRef;
    }


    @Override
    public Void visitAssembly(bytecodeParser.AssemblyContext ctx) {
        visitChildren(ctx);
        // handle label
        return null;
    }

    @Override
    public Void visitInstruction(bytecodeParser.InstructionContext ctx) {
        HashMap<Integer, bytecodeParser.InstructionContext> item = new HashMap<>();
        switch(ctx.getStart().getType()) {
            case bytecodeLexer.LW: {
                item.put(bytecodeParser.LW, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.LW_B: {
                item.put(bytecodeParser.LW_B, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.ADD: {
                item.put(bytecodeParser.ADD, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.SUB: {
                item.put(bytecodeParser.SUB, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.DIV: {
                item.put(bytecodeParser.DIV, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.MULT: {
                item.put(bytecodeParser.MULT, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.SW: {
                item.put(bytecodeParser.SW, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.SW_B: {
                item.put(bytecodeParser.SW_B, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.ADDI: {
                item.put(bytecodeParser.ADDI, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.LI: {
                item.put(bytecodeParser.LI, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.LB: {
                item.put(bytecodeParser.LB, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.MOVE: {
                item.put(bytecodeParser.MOVE, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.BEQ: {
                item.put(bytecodeParser.BEQ, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.BGT: {
                item.put(bytecodeParser.BGT, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.BGE: {
                item.put(bytecodeParser.BGE, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.BEQ_B: {
                item.put(bytecodeParser.BEQ_B, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.BRANCH: {
                item.put(bytecodeParser.BRANCH, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.LABEL: {
                this.labelRef.put(ctx.getText(), i);
                break;
            }
            case bytecodeLexer.JTL: {
                item.put(bytecodeParser.JTL, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.JR: {
                item.put(bytecodeParser.JR, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.PRINT: {
                item.put(bytecodeParser.PRINT, ctx);
                code[i++] = item;
                break;
            }
            case bytecodeLexer.HALT: {
                item.put(bytecodeParser.HALT, ctx);
                code[i++] = item;
                break;
            }
            default: {
                System.out.println("Invalid instruction");
                break;
            }
        }
        return null;
    }
}
