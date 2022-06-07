package src.interpreter;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link bytecodeParser}.
 */
public interface bytecodeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link bytecodeParser#assembly}.
	 * @param ctx the parse tree
	 */
	void enterAssembly(bytecodeParser.AssemblyContext ctx);
	/**
	 * Exit a parse tree produced by {@link bytecodeParser#assembly}.
	 * @param ctx the parse tree
	 */
	void exitAssembly(bytecodeParser.AssemblyContext ctx);
	/**
	 * Enter a parse tree produced by {@link bytecodeParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(bytecodeParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link bytecodeParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(bytecodeParser.InstructionContext ctx);
}