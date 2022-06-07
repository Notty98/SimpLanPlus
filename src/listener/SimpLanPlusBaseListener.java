package src.listener;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import src.parser.SimpLanPlusParser;
import src.symbolTable.SymbolTable;
import src.utils.ErrorLogger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides an empty implementation of {@link SimpLanPlusListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
public class SimpLanPlusBaseListener implements SimpLanPlusListener {

	private final SymbolTable symbolTable;
	private final ArrayList<String> errors;
	private final ErrorLogger logger;
	private ArrayList<HashMap<String, String>> argsToPush = null;

	public SimpLanPlusBaseListener(ErrorLogger logger) {
		super();
		symbolTable = new SymbolTable();
		errors = new ArrayList<>();
		this.logger = logger;
	}

	public ArrayList<String> getSemanticErrors() {
		return this.errors;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBlock(SimpLanPlusParser.BlockContext ctx) {
		symbolTable.newScope();
		if(this.argsToPush != null) {
			for(HashMap<String, String> arg: this.argsToPush) {
				for(String id: arg.keySet()) {
					this.symbolTable.addDecl(id, arg.get(id), ctx.getStart().getLine());
				}
			}
		}
		this.argsToPush = null;
		symbolTable.printTableState();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBlock(SimpLanPlusParser.BlockContext ctx) {
		symbolTable.printTableState(); symbolTable.exitScope();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStatement(SimpLanPlusParser.StatementContext ctx) {

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStatement(SimpLanPlusParser.StatementContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDeclaration(SimpLanPlusParser.DeclarationContext ctx) {

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclaration(SimpLanPlusParser.DeclarationContext ctx) {  }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDecFun(SimpLanPlusParser.DecFunContext ctx) {
		String id = ctx.ID().getText();
		String type = "";
		if(ctx.type() != null) {
			type = ctx.type().getText();
		} else {
			type = "void";
		}
		String check = symbolTable.lookupDeclaration(id);
		if(check != null) {
			String msg = "line " + ctx.getStart().getLine() + ": Multiple declaration of function " + id;
			errors.add(msg);
		} else {
			ArrayList<HashMap<String, String>> argsList = new ArrayList<>();
			for(SimpLanPlusParser.ArgContext item: ctx.arg()) {
				HashMap<String, String> arg = new HashMap<>();
				arg.put(item.ID().getText(), item.type().getText());
				argsList.add(arg);
			}
			symbolTable.addDecl(id, type, argsList, ctx.getStart().getLine());
			// add to local variable to insert in the scope
			this.argsToPush = argsList;
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDecFun(SimpLanPlusParser.DecFunContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDecVar(SimpLanPlusParser.DecVarContext ctx) {
		String id = ctx.ID().getText();
		String type = ctx.type().getText();
		String check = symbolTable.lookupDeclaration(id);
		if(check != null) {
			String msg = "line " + ctx.getStart().getLine() + ": Multiple declaration of variable " + id;
			errors.add(msg);
		} else {
			symbolTable.addDecl(id, type, ctx.getStart().getLine());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDecVar(SimpLanPlusParser.DecVarContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterType(SimpLanPlusParser.TypeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitType(SimpLanPlusParser.TypeContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterArg(SimpLanPlusParser.ArgContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitArg(SimpLanPlusParser.ArgContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAssignment(SimpLanPlusParser.AssignmentContext ctx) {
		String id = ctx.ID().getText();
		String check = symbolTable.lookup(id);
		if(check == null) {
			String msg = "line " + ctx.getStart().getLine() + ": Variable " + id +" not declared";
			this.errors.add(msg);
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAssignment(SimpLanPlusParser.AssignmentContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPrint(SimpLanPlusParser.PrintContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPrint(SimpLanPlusParser.PrintContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRet(SimpLanPlusParser.RetContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRet(SimpLanPlusParser.RetContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIte(SimpLanPlusParser.IteContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIte(SimpLanPlusParser.IteContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCall(SimpLanPlusParser.CallContext ctx) {
		String id = ctx.ID().getText();
		String check = symbolTable.lookup(id);
		if(check == null) {
			String msg = "line " + ctx.getStart().getLine() + ": Function " + id + " not declared";
			this.errors.add(msg);
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCall(SimpLanPlusParser.CallContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBaseExp(SimpLanPlusParser.BaseExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBaseExp(SimpLanPlusParser.BaseExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBinExp(SimpLanPlusParser.BinExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBinExp(SimpLanPlusParser.BinExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDerExp(SimpLanPlusParser.DerExpContext ctx) {
		String id = ctx.ID().getText();
		String check = symbolTable.lookup(id);
		if(check == null) {
			String msg = "line " + ctx.getStart().getLine() + ": Variable " + id + " not declared";
			this.errors.add(msg);
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDerExp(SimpLanPlusParser.DerExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterValExp(SimpLanPlusParser.ValExpContext ctx) {

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitValExp(SimpLanPlusParser.ValExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNegExp(SimpLanPlusParser.NegExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNegExp(SimpLanPlusParser.NegExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBoolExp(SimpLanPlusParser.BoolExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBoolExp(SimpLanPlusParser.BoolExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterCallExp(SimpLanPlusParser.CallExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitCallExp(SimpLanPlusParser.CallExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNotExp(SimpLanPlusParser.NotExpContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNotExp(SimpLanPlusParser.NotExpContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { }

	public void writeErrors() {
		this.logger.write("=====================SEMANTIC ERRORS==========================");
		this.logger.write("Number of errors: " + this.errors.size());
		for (String error: errors) {
			this.logger.write("\t" + error);
		}
		this.logger.write("=====================SEMANTIC ERRORS==========================");
	}
}