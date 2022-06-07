package src.listener;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import src.utils.ErrorLogger;

import java.util.ArrayList;

public class CustomErrorListener extends BaseErrorListener {

    private final ArrayList<String> errors;
    private final ErrorLogger logger;

    public CustomErrorListener(ErrorLogger logger) {
        this.errors = new ArrayList<>();
        this.logger = logger;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);

        this.errors.add("line " + line + ": " + msg);
    }

    public boolean isCorrect() {
        return this.errors.size() == 0;
    }

    public void writeErrors() {
        this.logger.write("=====================LEXER ERRORS==========================");
        this.logger.write("Number of errors: " + this.errors.size());
        for (String error: errors) {
            this.logger.write("\t" + error);
        }
        this.logger.write("=====================LEXER ERRORS==========================");
    }
}
