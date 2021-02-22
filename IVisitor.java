
// Generated from TypesInterpreter.g4 by ANTLR 4.7.2
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of
 * {@link TypesInterpreterVisitor}, which can be extended to create a visitor
 * which only needs to handle a subset of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public class IVisitor extends TypesInterpreterBaseVisitor<Object> {

	private DataStrt strt;
	static int lineCount = 0;

	public IVisitor(DataStrt d) {
		strt = d;
		lineCount++;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Object visitFile(TypesInterpreterParser.FileContext ctx) {
		return super.visitFile(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Object visitStat(TypesInterpreterParser.StatContext ctx) {
		return super.visitStat(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Object visitParsePrim(TypesInterpreterParser.ParsePrimContext ctx) {
		if (strt.verifyUni(ctx.IDI(0).getText(), ctx.IDI(1).getText())) { // verifica se nome do tipo a criar e a unidade já existem na estrutura de dados
			System.err.println("Linha "+ lineCount +": unidade "+ ctx.IDI(0).getText() +"já existente ou contém um símbolo já existente");
		}

		String erro = strt.inserirUniPrim(ctx.IDI(0).getText(), ctx.IDI(1).getText(), ctx.type().t);

		if(!erro.equals("")) {
			System.err.println("Linha "+ lineCount +": "+ erro);
		}

		return "";
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Object visitParseSec(TypesInterpreterParser.ParseSecContext ctx) {

		HashMap<String, Integer> simbolGrau = (HashMap) visit(ctx.exprSec().comp());

		String erro = strt.inserirUniSec(ctx.exprSec().id.getText(), simbolGrau);

		if(!erro.equals("")) {
			System.err.println("Linha "+ lineCount +": "+ erro);
		}
		
		return "";
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override
	public Object visitConst(TypesInterpreterParser.ConstContext ctx) {

		if (Double.parseDouble(ctx.NUM().getText()) > Double.MAX_VALUE || Double.parseDouble(ctx.NUM().getText()) < Double.MIN_VALUE) {
			System.err.println("Linha " + lineCount + ": valor associado a " + ctx.IDI(0).getText() + " não corresponde a um Double");
		}

		String uni;

		if(ctx.IDI(1) == null)
			uni = "numero";
		else
			uni = ctx.IDI(1).getText();
		
		String erro = strt.inserirConst(uni, ctx.IDI(0).getText(), Double.parseDouble(ctx.NUM().getText())); 

		if(!erro.equals("")) {
			System.err.println("Linha "+ lineCount +": "+ erro);
		}

		return erro;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.
	 * </p>
	 */
	@Override
	public Object visitType(TypesInterpreterParser.TypeContext ctx) {
		return super.visitType(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override
	public Object visitExprSec(TypesInterpreterParser.ExprSecContext ctx) {
		return super.visitExprSec(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override
	public HashMap visitParenthesis(TypesInterpreterParser.ParenthesisContext ctx) {

		HashMap<String, Integer> comp = (HashMap) visit(ctx.comp());

		if (ctx.EXP() != null) {
			String[] unidades = comp.keySet().toArray(new String[comp.keySet().size()]);
			for (int i = 0; i < comp.size(); i++) {
				comp.replace(unidades[i], comp.get(unidades[i]) * Integer.parseInt(ctx.EXP().getText()));
			}
		}
		return comp;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override
	public HashMap visitOp(TypesInterpreterParser.OpContext ctx) {
		HashMap<String, Integer> op = new HashMap<>();

		HashMap<String, Integer> c1 = (HashMap) visit(ctx.c1);
		HashMap<String, Integer> c2 = (HashMap) visit(ctx.c2);

		String[] u1 = c1.keySet().toArray(new String[c1.keySet().size()]);

		for (int i = 0; i < u1.length; i++) {
			op.put(u1[i], c1.get(u1[i]));
		} // adicionar c1 a op

		String[] u2 = c2.keySet().toArray(new String[c2.keySet().size()]);

		if (ctx.OP().getText().equals("/")) {
			for (int i = 0; i < u2.length; i++) { // se um c2 for igual a um c1, subtrair o valor, se for diferente adicionar a op * (-1)
				for (int j = 0; j < u1.length; j++) {
					if (u2[i].equals(u1[j]))
						op.replace(u1[j], op.get(u1[j]) - c2.get(u2[i]));
					if (!op.containsKey(u2[i]))
						op.put(u2[i], c2.get(u2[i]) * (-1));
				}
			}
		} else { // se "*"
			for (int i = 0; i < u2.length; i++) { // se um c2 for igual a um c1, somar o valor, se for diferente adicionar a op
				for (int j = 0; j < u1.length; j++) {
					if (u2[i].equals(u1[j]))
						op.replace(u1[j], op.get(u1[j]) + c2.get(u2[i]));
					if (!op.containsKey(u2[i]))
						op.put(u2[i], c2.get(u2[i]));
				}
			}
		}

		return op;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override
	public HashMap visitExp(TypesInterpreterParser.ExpContext ctx) {

		HashMap<String, Integer> unidade = new HashMap<>();

		if (ctx.EXP() != null)
			unidade.put(ctx.IDI().getText(), Integer.parseInt(ctx.EXP().getText()));
		else
			unidade.put(ctx.IDI().getText(), 1);

		return unidade;
	}

}
