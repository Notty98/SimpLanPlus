package src.interpreter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class ExecuteBytecode {
    private static final int CODESIZE = 10000;
    private static final int STACKSIZE = 10000;

    private HashMap<Integer, bytecodeParser.InstructionContext>[] code;
    private byte[] stack = new byte[STACKSIZE];

    private int ip = 0;

    private int sp = STACKSIZE; // stack pointer
    private int fp = STACKSIZE; // frame pointer
    private int ra; // return address
    private int al; // access link
    private int a0; // $a0
    private int t1;
    private int t2;

    private HashMap<String, Integer> labelRef;

    public ExecuteBytecode(HashMap<Integer, bytecodeParser.InstructionContext>[] code, HashMap<String, Integer> labelRef) {
        this.code = code;
        this.labelRef = labelRef;
    }

    public int getReg(String reg) {
        switch (reg) {
            case "$sp" -> {
                return this.sp;
            }
            case "$fp" -> {
                return this.fp;
            }
            case "$ra" -> {
                return this.ra;
            }
            case "$al" -> {
                return this.al;
            }
            case "$a0" -> {
                return this.a0;
            }
            case "$t1" -> {
                return this.t1;
            }
            case "$t2" -> {
                return this.t2;
            }
            default -> {
                return -1;
            }
        }
    }

    public void setREg(String reg, int value) {
        switch (reg) {
            case "$sp" -> {
                this.sp = value;
            }
            case "$fp" -> {
                this.fp = value;
            }
            case "$ra" -> {
                this.ra = value;
            }
            case "$al" -> {
                this.al = value;
            }
            case "$a0" -> {
                this.a0 = value;
            }
            case "$t1" -> {
                this.t1 = value;
            }
            case "$t2" -> {
                this.t2 = value;
            }
        }
    }

    public void cpu() {
        while (true) {
            if (sp > STACKSIZE) {
                System.out.println("\nError: Out of memory");
                return;
            }
            HashMap<Integer, bytecodeParser.InstructionContext> bytecode = code[ip++];
            int bytecodeType = (int) bytecode.keySet().toArray()[0];
            switch (bytecodeType) {
                case bytecodeParser.LW: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.LW);
                    int r2 = getReg(lwContext.r2.getText()); //src reg
                    int offset = Integer.parseInt(lwContext.offset.getText());
                    byte[] myInt = new byte[4];
                    for(int i = 0; i<4; i++) {
                        myInt[i] = this.stack[r2 + offset +i];
                    }
                    ByteBuffer buffer = ByteBuffer.wrap(myInt);
                    setREg(lwContext.r1.getText(), buffer.getInt());
                    break;
                }
                case bytecodeLexer.LW_B: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.LW_B);
                    int r2 = getReg(lwContext.r2.getText()); //src reg
                    int offset = Integer.parseInt(lwContext.offset.getText());

                    byte myBool = this.stack[r2 + offset];
                    setREg(lwContext.r1.getText(), myBool);
                    break;
                }
                case bytecodeLexer.ADD: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.ADD);
                    int r2 = getReg(lwContext.r2.getText()); //src reg
                    int r3 = getReg(lwContext.r3.getText());

                    setREg(lwContext.r1.getText(), r2+r3);
                    break;
                }
                case bytecodeLexer.SUB: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.SUB);
                    int r2 = getReg(lwContext.r2.getText()); //src reg
                    int r3 = getReg(lwContext.r3.getText());

                    setREg(lwContext.r1.getText(), r2-r3);
                    break;
                }
                case bytecodeLexer.DIV: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.DIV);
                    int r2 = getReg(lwContext.r2.getText()); //src reg
                    int r3 = getReg(lwContext.r3.getText());

                    setREg(lwContext.r1.getText(), r2/r3);
                    break;
                }
                case bytecodeLexer.MULT: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.MULT);
                    int r2 = getReg(lwContext.r2.getText()); //src reg
                    int r3 = getReg(lwContext.r3.getText());

                    setREg(lwContext.r1.getText(), r2*r3);
                    break;
                }
                case bytecodeLexer.SW: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.SW);
                    int r1 = getReg(lwContext.r1.getText()); //src reg
                    int r2 = getReg(lwContext.r2.getText());
                    int offset = Integer.parseInt(lwContext.offset.getText());

                    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
                    byteBuffer.putInt(r1);
                    System.arraycopy(byteBuffer.array(), 0, this.stack, (r2 + offset), 4);
                    break;
                }
                case bytecodeLexer.SW_B: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.SW_B);
                    int r1 = getReg(lwContext.r1.getText()); //src reg
                    int r2 = getReg(lwContext.r2.getText());
                    int offset = Integer.parseInt(lwContext.offset.getText());

                    byte myValue = (byte) r1;
                    this.stack[r2 + offset] = myValue;
                    break;
                }
                case bytecodeLexer.ADDI: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.ADDI);
                    int r2 = getReg(lwContext.r2.getText());

                    setREg(lwContext.r1.getText(), r2 + Integer.parseInt(lwContext.n.getText()));
                    break;
                }
                case bytecodeLexer.LI: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.LI);
                    setREg(lwContext.r1.getText(), Integer.parseInt(lwContext.n.getText()));
                    break;
                }
                case bytecodeLexer.LB: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.LB);
                    setREg(lwContext.r1.getText(), Integer.parseInt(lwContext.n.getText()));
                    break;
                }
                case bytecodeLexer.MOVE: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.MOVE);
                    int r2 = getReg(lwContext.r2.getText());
                    setREg(lwContext.r1.getText(), r2);
                    break;
                }
                case bytecodeLexer.BEQ: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.BEQ);
                    int r1 = getReg(lwContext.r1.getText());
                    int r2 = getReg(lwContext.r2.getText());
                    if(r1 == r2) {
                        ip = this.labelRef.get(lwContext.label.getText());
                    }
                    break;
                }
                case bytecodeLexer.BGT: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.BGT);
                    int r1 = getReg(lwContext.r1.getText());
                    int r2 = getReg(lwContext.r2.getText());
                    if(r1 > r2) {
                        ip = this.labelRef.get(lwContext.label.getText());
                    }
                    break;
                }
                case bytecodeLexer.BGE: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.BGE);
                    int r1 = getReg(lwContext.r1.getText());
                    int r2 = getReg(lwContext.r2.getText());
                    if(r1 >= r2) {
                        ip = this.labelRef.get(lwContext.label.getText());
                    }
                    break;
                }
                case bytecodeLexer.BEQ_B: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.BEQ_B);
                    int r1 = getReg(lwContext.r1.getText());
                    int r2 = getReg(lwContext.r2.getText());
                    if(r1 == r2) {
                        ip = this.labelRef.get(lwContext.label.getText());
                    }
                    break;
                }
                case bytecodeLexer.BRANCH: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.BRANCH);
                    ip = this.labelRef.get(lwContext.label.getText());
                    break;
                }
                case bytecodeLexer.JTL: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.JTL);
                    this.ra = ip;
                    ip = this.labelRef.get(lwContext.label.getText());
                    break;
                }
                case bytecodeLexer.JR: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.JR);
                    ip = this.getReg(lwContext.reg.getText());
                    break;
                }
                case bytecodeLexer.PRINT: {
                    bytecodeParser.InstructionContext lwContext = bytecode.get(bytecodeParser.PRINT);
                    int reg = getReg(lwContext.reg.getText());
                    System.out.println(reg);
                    break;
                }
                case bytecodeLexer.HALT: {
                    System.out.println("Halt");
                    System.exit(0);
                    break;
                }
            }
        }
    }

}
