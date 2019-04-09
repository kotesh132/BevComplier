// Generated from recipe.g4 by ANTLR 4.5


import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link recipeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface recipeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link recipeParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(recipeParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(recipeParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#input_output_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput_output_declaration(recipeParser.Input_output_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#symbols_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbols_declaration(recipeParser.Symbols_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#names_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNames_list(recipeParser.Names_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#table_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_declaration(recipeParser.Table_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#table_add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_add(recipeParser.Table_addContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#tablename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablename(recipeParser.TablenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#actionname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionname(recipeParser.ActionnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#matchfields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchfields(recipeParser.MatchfieldsContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#actionparams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionparams(recipeParser.ActionparamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#ipaddress}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIpaddress(recipeParser.IpaddressContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#priority}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPriority(recipeParser.PriorityContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(recipeParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#argument_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument_list(recipeParser.Argument_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg(recipeParser.ArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey(recipeParser.KeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#key_value_pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_value_pair(recipeParser.Key_value_pairContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#key_value_pair_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey_value_pair_list(recipeParser.Key_value_pair_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#protocol_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtocol_name(recipeParser.Protocol_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#protocol_hdr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtocol_hdr(recipeParser.Protocol_hdrContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#protocol_hdr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtocol_hdr_list(recipeParser.Protocol_hdr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#packet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPacket(recipeParser.PacketContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#packet_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPacket_declaration(recipeParser.Packet_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(recipeParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#typeRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRef(recipeParser.TypeRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#prefixedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedType(recipeParser.PrefixedTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(recipeParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#tupleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(recipeParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#headerStackType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeaderStackType(recipeParser.HeaderStackTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#specializedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecializedType(recipeParser.SpecializedTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseType(recipeParser.BaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#typeArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArg(recipeParser.TypeArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#typeArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgumentList(recipeParser.TypeArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#dotPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotPrefix(recipeParser.DotPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#nonTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonTypeName(recipeParser.NonTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(recipeParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(recipeParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(recipeParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link recipeParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(recipeParser.NumberContext ctx);
}