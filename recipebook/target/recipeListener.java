// Generated from ./recipe.g4 by ANTLR 4.5


import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link recipeParser}.
 */
public interface recipeListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link recipeParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(recipeParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(recipeParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(recipeParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(recipeParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#input_output_declaration}.
	 * @param ctx the parse tree
	 */
	void enterInput_output_declaration(recipeParser.Input_output_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#input_output_declaration}.
	 * @param ctx the parse tree
	 */
	void exitInput_output_declaration(recipeParser.Input_output_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#symbols_declaration}.
	 * @param ctx the parse tree
	 */
	void enterSymbols_declaration(recipeParser.Symbols_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#symbols_declaration}.
	 * @param ctx the parse tree
	 */
	void exitSymbols_declaration(recipeParser.Symbols_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#names_list}.
	 * @param ctx the parse tree
	 */
	void enterNames_list(recipeParser.Names_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#names_list}.
	 * @param ctx the parse tree
	 */
	void exitNames_list(recipeParser.Names_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#table_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTable_declaration(recipeParser.Table_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#table_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTable_declaration(recipeParser.Table_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#table_add}.
	 * @param ctx the parse tree
	 */
	void enterTable_add(recipeParser.Table_addContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#table_add}.
	 * @param ctx the parse tree
	 */
	void exitTable_add(recipeParser.Table_addContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#tablename}.
	 * @param ctx the parse tree
	 */
	void enterTablename(recipeParser.TablenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#tablename}.
	 * @param ctx the parse tree
	 */
	void exitTablename(recipeParser.TablenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#actionname}.
	 * @param ctx the parse tree
	 */
	void enterActionname(recipeParser.ActionnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#actionname}.
	 * @param ctx the parse tree
	 */
	void exitActionname(recipeParser.ActionnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#matchfields}.
	 * @param ctx the parse tree
	 */
	void enterMatchfields(recipeParser.MatchfieldsContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#matchfields}.
	 * @param ctx the parse tree
	 */
	void exitMatchfields(recipeParser.MatchfieldsContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#actionparams}.
	 * @param ctx the parse tree
	 */
	void enterActionparams(recipeParser.ActionparamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#actionparams}.
	 * @param ctx the parse tree
	 */
	void exitActionparams(recipeParser.ActionparamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#ipaddress}.
	 * @param ctx the parse tree
	 */
	void enterIpaddress(recipeParser.IpaddressContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#ipaddress}.
	 * @param ctx the parse tree
	 */
	void exitIpaddress(recipeParser.IpaddressContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#priority}.
	 * @param ctx the parse tree
	 */
	void enterPriority(recipeParser.PriorityContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#priority}.
	 * @param ctx the parse tree
	 */
	void exitPriority(recipeParser.PriorityContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(recipeParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(recipeParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void enterArgument_list(recipeParser.Argument_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#argument_list}.
	 * @param ctx the parse tree
	 */
	void exitArgument_list(recipeParser.Argument_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(recipeParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(recipeParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#key}.
	 * @param ctx the parse tree
	 */
	void enterKey(recipeParser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#key}.
	 * @param ctx the parse tree
	 */
	void exitKey(recipeParser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#key_value_pair}.
	 * @param ctx the parse tree
	 */
	void enterKey_value_pair(recipeParser.Key_value_pairContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#key_value_pair}.
	 * @param ctx the parse tree
	 */
	void exitKey_value_pair(recipeParser.Key_value_pairContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#key_value_pair_list}.
	 * @param ctx the parse tree
	 */
	void enterKey_value_pair_list(recipeParser.Key_value_pair_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#key_value_pair_list}.
	 * @param ctx the parse tree
	 */
	void exitKey_value_pair_list(recipeParser.Key_value_pair_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#protocol_name}.
	 * @param ctx the parse tree
	 */
	void enterProtocol_name(recipeParser.Protocol_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#protocol_name}.
	 * @param ctx the parse tree
	 */
	void exitProtocol_name(recipeParser.Protocol_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#protocol_hdr}.
	 * @param ctx the parse tree
	 */
	void enterProtocol_hdr(recipeParser.Protocol_hdrContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#protocol_hdr}.
	 * @param ctx the parse tree
	 */
	void exitProtocol_hdr(recipeParser.Protocol_hdrContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#protocol_hdr_list}.
	 * @param ctx the parse tree
	 */
	void enterProtocol_hdr_list(recipeParser.Protocol_hdr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#protocol_hdr_list}.
	 * @param ctx the parse tree
	 */
	void exitProtocol_hdr_list(recipeParser.Protocol_hdr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#packet}.
	 * @param ctx the parse tree
	 */
	void enterPacket(recipeParser.PacketContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#packet}.
	 * @param ctx the parse tree
	 */
	void exitPacket(recipeParser.PacketContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#packet_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPacket_declaration(recipeParser.Packet_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#packet_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPacket_declaration(recipeParser.Packet_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(recipeParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(recipeParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#typeRef}.
	 * @param ctx the parse tree
	 */
	void enterTypeRef(recipeParser.TypeRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#typeRef}.
	 * @param ctx the parse tree
	 */
	void exitTypeRef(recipeParser.TypeRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#prefixedType}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedType(recipeParser.PrefixedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#prefixedType}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedType(recipeParser.PrefixedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(recipeParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(recipeParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#tupleType}.
	 * @param ctx the parse tree
	 */
	void enterTupleType(recipeParser.TupleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#tupleType}.
	 * @param ctx the parse tree
	 */
	void exitTupleType(recipeParser.TupleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#headerStackType}.
	 * @param ctx the parse tree
	 */
	void enterHeaderStackType(recipeParser.HeaderStackTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#headerStackType}.
	 * @param ctx the parse tree
	 */
	void exitHeaderStackType(recipeParser.HeaderStackTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#specializedType}.
	 * @param ctx the parse tree
	 */
	void enterSpecializedType(recipeParser.SpecializedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#specializedType}.
	 * @param ctx the parse tree
	 */
	void exitSpecializedType(recipeParser.SpecializedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBaseType(recipeParser.BaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBaseType(recipeParser.BaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#typeArg}.
	 * @param ctx the parse tree
	 */
	void enterTypeArg(recipeParser.TypeArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#typeArg}.
	 * @param ctx the parse tree
	 */
	void exitTypeArg(recipeParser.TypeArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#typeArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentList(recipeParser.TypeArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#typeArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentList(recipeParser.TypeArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#dotPrefix}.
	 * @param ctx the parse tree
	 */
	void enterDotPrefix(recipeParser.DotPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#dotPrefix}.
	 * @param ctx the parse tree
	 */
	void exitDotPrefix(recipeParser.DotPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#nonTypeName}.
	 * @param ctx the parse tree
	 */
	void enterNonTypeName(recipeParser.NonTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#nonTypeName}.
	 * @param ctx the parse tree
	 */
	void exitNonTypeName(recipeParser.NonTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(recipeParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(recipeParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(recipeParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(recipeParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(recipeParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(recipeParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link recipeParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(recipeParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link recipeParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(recipeParser.NumberContext ctx);
}