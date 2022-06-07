package src.main;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import src.codegen.VisitorCodeGen;
import src.interpreter.ExecuteBytecode;
import src.interpreter.bytecodeLexer;
import src.interpreter.bytecodeParser;
import src.interpreter.bytecodeVisitorInterpreter;
import src.lexer.SimpLanPlusLexer;
import src.listener.CustomErrorListener;
import src.listener.SimpLanPlusBaseListener;
import src.parser.SimpLanPlusParser;
import src.utils.ErrorLogger;
import src.visitor.SimpLanPlusVisitorEffect;
import src.visitor.SimpLanPlusVisitorTypeCheck;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Main {

    private static final String pathInput = "src/input";
    private static final String inputFile = "test";
    private static final Path inputPath = Paths.get(pathInput, inputFile);
    private static final String pathOutput = "src/output/";
    private static final String outputFileName = "errorLogger_";
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    private static final Path bytecodeFile = Paths.get("src", "interpreter", "bytecode.asm");

    public static void main(String[]args) {
        String pathToOpen = inputPath.normalize().toAbsolutePath().toString();
        ErrorLogger errorLogger;

        try {
            CharStream inputFile = CharStreams.fromFileName(pathToOpen);

            SimpLanPlusLexer lexer = new SimpLanPlusLexer(inputFile);

            SimpLanPlusParser parser = new SimpLanPlusParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String outputFile = outputFileName + sdf3.format(timestamp) + ".log";

            errorLogger = new ErrorLogger(pathOutput,outputFile);
            CustomErrorListener listener = new CustomErrorListener(errorLogger);

            parser.addErrorListener(listener);

            ParseTree tree = parser.block();

            if(!listener.isCorrect()) {
                listener.writeErrors();
                return;
            }

            // SEMANTIC ERROR

            SimpLanPlusBaseListener myListener = new SimpLanPlusBaseListener(errorLogger);
            ParseTreeWalker.DEFAULT.walk(myListener, tree);

            ArrayList<String> errors = myListener.getSemanticErrors();
            if(errors.size() > 0) {
                myListener.writeErrors();
                System.out.println("Number of errors: " + errors.size());
                for (String error: errors) {
                    System.out.println("\t" + error);
                }
                return;
            }

            // Type check

            SimpLanPlusVisitorTypeCheck simpLanPlusVisitorTypeCheck = new SimpLanPlusVisitorTypeCheck(errorLogger);
            simpLanPlusVisitorTypeCheck.visit(tree);

            errors = simpLanPlusVisitorTypeCheck.getTypeErrors();
            if(errors.size() > 0) {
                simpLanPlusVisitorTypeCheck.writeErrors();
                System.out.println("Number of errors: " + errors.size());
                for (String error: errors) {
                    System.out.println("\t" + error);
                }
                return;
            }

            // Effect
            SimpLanPlusVisitorEffect simpLanPlusVisitorEffect = new SimpLanPlusVisitorEffect(errorLogger);
            simpLanPlusVisitorEffect.visit(tree);

            errors = simpLanPlusVisitorEffect.getEffectErrors();
            if(errors.size() > 0) {
                simpLanPlusVisitorEffect.writeErrors();
                System.out.println("Number of errors: " + errors.size());
                for (String error: errors) {
                    System.out.println("\t" + error);
                }
                return;
            }

            VisitorCodeGen visitorCodeGen = new VisitorCodeGen();
            String code = visitorCodeGen.visit(tree);
            code = code + "halt\n";
            System.out.println(code);

            //String bytecodeToOpen = bytecodeFile.normalize().toAbsolutePath().toString();
            CharStream testBytecode = CharStreams.fromString(code);

            bytecodeLexer bytecodeLexer = new bytecodeLexer(testBytecode);
            CommonTokenStream tokenBytecode = new CommonTokenStream(bytecodeLexer);
            bytecodeParser bytecodeParser = new bytecodeParser(tokenBytecode);

            ParseTree bytecodeTree = bytecodeParser.assembly();

            bytecodeVisitorInterpreter bytecodeVisitor = new bytecodeVisitorInterpreter();
            bytecodeVisitor.visit(bytecodeTree);

            ExecuteBytecode executeBytecode = new ExecuteBytecode(bytecodeVisitor.code, bytecodeVisitor.getLabelRef());
            executeBytecode.cpu();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}