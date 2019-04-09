// Generated from P416.g4 by ANTLR 4.5
package com.p4.p416.gen;

import com.p4.p416.gen.*;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link P416Parser}.
 */
public interface P416Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link P416Parser#p4program}.
	 * @param ctx the parse tree
	 */
	void enterP4program(P416Parser.P4programContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#p4program}.
	 * @param ctx the parse tree
	 */
	void exitP4program(P416Parser.P4programContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(P416Parser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(P416Parser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#nonTypeName}.
	 * @param ctx the parse tree
	 */
	void enterNonTypeName(P416Parser.NonTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#nonTypeName}.
	 * @param ctx the parse tree
	 */
	void exitNonTypeName(P416Parser.NonTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(P416Parser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(P416Parser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#optAnnotations}.
	 * @param ctx the parse tree
	 */
	void enterOptAnnotations(P416Parser.OptAnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#optAnnotations}.
	 * @param ctx the parse tree
	 */
	void exitOptAnnotations(P416Parser.OptAnnotationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#annotations}.
	 * @param ctx the parse tree
	 */
	void enterAnnotations(P416Parser.AnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#annotations}.
	 * @param ctx the parse tree
	 */
	void exitAnnotations(P416Parser.AnnotationsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleAnnotation}
	 * labeled alternative in {@link P416Parser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterSimpleAnnotation(P416Parser.SimpleAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleAnnotation}
	 * labeled alternative in {@link P416Parser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitSimpleAnnotation(P416Parser.SimpleAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code complexAnnotation}
	 * labeled alternative in {@link P416Parser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterComplexAnnotation(P416Parser.ComplexAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code complexAnnotation}
	 * labeled alternative in {@link P416Parser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitComplexAnnotation(P416Parser.ComplexAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(P416Parser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(P416Parser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(P416Parser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(P416Parser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 */
	void enterInDirection(P416Parser.InDirectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 */
	void exitInDirection(P416Parser.InDirectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code outDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 */
	void enterOutDirection(P416Parser.OutDirectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code outDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 */
	void exitOutDirection(P416Parser.OutDirectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inOutDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 */
	void enterInOutDirection(P416Parser.InOutDirectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inOutDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 */
	void exitInOutDirection(P416Parser.InOutDirectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#packageTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageTypeDeclaration(P416Parser.PackageTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#packageTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageTypeDeclaration(P416Parser.PackageTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#instantiation}.
	 * @param ctx the parse tree
	 */
	void enterInstantiation(P416Parser.InstantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#instantiation}.
	 * @param ctx the parse tree
	 */
	void exitInstantiation(P416Parser.InstantiationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#optConstructorParameters}.
	 * @param ctx the parse tree
	 */
	void enterOptConstructorParameters(P416Parser.OptConstructorParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#optConstructorParameters}.
	 * @param ctx the parse tree
	 */
	void exitOptConstructorParameters(P416Parser.OptConstructorParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#dotPrefix}.
	 * @param ctx the parse tree
	 */
	void enterDotPrefix(P416Parser.DotPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#dotPrefix}.
	 * @param ctx the parse tree
	 */
	void exitDotPrefix(P416Parser.DotPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParserDeclaration(P416Parser.ParserDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParserDeclaration(P416Parser.ParserDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserLocalElements}.
	 * @param ctx the parse tree
	 */
	void enterParserLocalElements(P416Parser.ParserLocalElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserLocalElements}.
	 * @param ctx the parse tree
	 */
	void exitParserLocalElements(P416Parser.ParserLocalElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserLocalElement}.
	 * @param ctx the parse tree
	 */
	void enterParserLocalElement(P416Parser.ParserLocalElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserLocalElement}.
	 * @param ctx the parse tree
	 */
	void exitParserLocalElement(P416Parser.ParserLocalElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParserTypeDeclaration(P416Parser.ParserTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParserTypeDeclaration(P416Parser.ParserTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserStates}.
	 * @param ctx the parse tree
	 */
	void enterParserStates(P416Parser.ParserStatesContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserStates}.
	 * @param ctx the parse tree
	 */
	void exitParserStates(P416Parser.ParserStatesContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserState}.
	 * @param ctx the parse tree
	 */
	void enterParserState(P416Parser.ParserStateContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserState}.
	 * @param ctx the parse tree
	 */
	void exitParserState(P416Parser.ParserStateContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserStatements}.
	 * @param ctx the parse tree
	 */
	void enterParserStatements(P416Parser.ParserStatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserStatements}.
	 * @param ctx the parse tree
	 */
	void exitParserStatements(P416Parser.ParserStatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserStatement}.
	 * @param ctx the parse tree
	 */
	void enterParserStatement(P416Parser.ParserStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserStatement}.
	 * @param ctx the parse tree
	 */
	void exitParserStatement(P416Parser.ParserStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#parserBlockStatement}.
	 * @param ctx the parse tree
	 */
	void enterParserBlockStatement(P416Parser.ParserBlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#parserBlockStatement}.
	 * @param ctx the parse tree
	 */
	void exitParserBlockStatement(P416Parser.ParserBlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#transitionStatement}.
	 * @param ctx the parse tree
	 */
	void enterTransitionStatement(P416Parser.TransitionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#transitionStatement}.
	 * @param ctx the parse tree
	 */
	void exitTransitionStatement(P416Parser.TransitionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#stateExpression}.
	 * @param ctx the parse tree
	 */
	void enterStateExpression(P416Parser.StateExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#stateExpression}.
	 * @param ctx the parse tree
	 */
	void exitStateExpression(P416Parser.StateExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpression(P416Parser.SelectExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#selectExpression}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpression(P416Parser.SelectExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#selectCaseList}.
	 * @param ctx the parse tree
	 */
	void enterSelectCaseList(P416Parser.SelectCaseListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#selectCaseList}.
	 * @param ctx the parse tree
	 */
	void exitSelectCaseList(P416Parser.SelectCaseListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#selectCase}.
	 * @param ctx the parse tree
	 */
	void enterSelectCase(P416Parser.SelectCaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#selectCase}.
	 * @param ctx the parse tree
	 */
	void exitSelectCase(P416Parser.SelectCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#keySetExpression}.
	 * @param ctx the parse tree
	 */
	void enterKeySetExpression(P416Parser.KeySetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#keySetExpression}.
	 * @param ctx the parse tree
	 */
	void exitKeySetExpression(P416Parser.KeySetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#tupleKeySetExpression}.
	 * @param ctx the parse tree
	 */
	void enterTupleKeySetExpression(P416Parser.TupleKeySetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#tupleKeySetExpression}.
	 * @param ctx the parse tree
	 */
	void exitTupleKeySetExpression(P416Parser.TupleKeySetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#simpleKeySetExpression}.
	 * @param ctx the parse tree
	 */
	void enterSimpleKeySetExpression(P416Parser.SimpleKeySetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#simpleKeySetExpression}.
	 * @param ctx the parse tree
	 */
	void exitSimpleKeySetExpression(P416Parser.SimpleKeySetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#controlDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterControlDeclaration(P416Parser.ControlDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#controlDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitControlDeclaration(P416Parser.ControlDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#controlTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#controlTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#controlLocalDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterControlLocalDeclarations(P416Parser.ControlLocalDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#controlLocalDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitControlLocalDeclarations(P416Parser.ControlLocalDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#controlLocalDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterControlLocalDeclaration(P416Parser.ControlLocalDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#controlLocalDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitControlLocalDeclaration(P416Parser.ControlLocalDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#controlBody}.
	 * @param ctx the parse tree
	 */
	void enterControlBody(P416Parser.ControlBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#controlBody}.
	 * @param ctx the parse tree
	 */
	void exitControlBody(P416Parser.ControlBodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code externObjectDeclaration}
	 * labeled alternative in {@link P416Parser#externDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code externObjectDeclaration}
	 * labeled alternative in {@link P416Parser#externDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code externFunctionDeclaration}
	 * labeled alternative in {@link P416Parser#externDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code externFunctionDeclaration}
	 * labeled alternative in {@link P416Parser#externDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#methodPrototypes}.
	 * @param ctx the parse tree
	 */
	void enterMethodPrototypes(P416Parser.MethodPrototypesContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#methodPrototypes}.
	 * @param ctx the parse tree
	 */
	void exitMethodPrototypes(P416Parser.MethodPrototypesContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#functionPrototype}.
	 * @param ctx the parse tree
	 */
	void enterFunctionPrototype(P416Parser.FunctionPrototypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#functionPrototype}.
	 * @param ctx the parse tree
	 */
	void exitFunctionPrototype(P416Parser.FunctionPrototypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#methodPrototype}.
	 * @param ctx the parse tree
	 */
	void enterMethodPrototype(P416Parser.MethodPrototypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#methodPrototype}.
	 * @param ctx the parse tree
	 */
	void exitMethodPrototype(P416Parser.MethodPrototypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeRef}.
	 * @param ctx the parse tree
	 */
	void enterTypeRef(P416Parser.TypeRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeRef}.
	 * @param ctx the parse tree
	 */
	void exitTypeRef(P416Parser.TypeRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#prefixedType}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedType(P416Parser.PrefixedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#prefixedType}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedType(P416Parser.PrefixedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(P416Parser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(P416Parser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#tupleType}.
	 * @param ctx the parse tree
	 */
	void enterTupleType(P416Parser.TupleTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#tupleType}.
	 * @param ctx the parse tree
	 */
	void exitTupleType(P416Parser.TupleTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#headerStackType}.
	 * @param ctx the parse tree
	 */
	void enterHeaderStackType(P416Parser.HeaderStackTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#headerStackType}.
	 * @param ctx the parse tree
	 */
	void exitHeaderStackType(P416Parser.HeaderStackTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#specializedType}.
	 * @param ctx the parse tree
	 */
	void enterSpecializedType(P416Parser.SpecializedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#specializedType}.
	 * @param ctx the parse tree
	 */
	void exitSpecializedType(P416Parser.SpecializedTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBoolType(P416Parser.BoolTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBoolType(P416Parser.BoolTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code errorType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterErrorType(P416Parser.ErrorTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code errorType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitErrorType(P416Parser.ErrorTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBitType(P416Parser.BitTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBitType(P416Parser.BitTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterBitSizeType(P416Parser.BitSizeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitBitSizeType(P416Parser.BitSizeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterIntSizeType(P416Parser.IntSizeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitIntSizeType(P416Parser.IntSizeTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varBitSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void enterVarBitSizeType(P416Parser.VarBitSizeTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varBitSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 */
	void exitVarBitSizeType(P416Parser.VarBitSizeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeOrVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypeOrVoid(P416Parser.TypeOrVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeOrVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypeOrVoid(P416Parser.TypeOrVoidContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#optTypeParameters}.
	 * @param ctx the parse tree
	 */
	void enterOptTypeParameters(P416Parser.OptTypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#optTypeParameters}.
	 * @param ctx the parse tree
	 */
	void exitOptTypeParameters(P416Parser.OptTypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeParameterList}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameterList(P416Parser.TypeParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeParameterList}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameterList(P416Parser.TypeParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeArg}.
	 * @param ctx the parse tree
	 */
	void enterTypeArg(P416Parser.TypeArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeArg}.
	 * @param ctx the parse tree
	 */
	void exitTypeArg(P416Parser.TypeArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#dontcare}.
	 * @param ctx the parse tree
	 */
	void enterDontcare(P416Parser.DontcareContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#dontcare}.
	 * @param ctx the parse tree
	 */
	void exitDontcare(P416Parser.DontcareContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentList(P416Parser.TypeArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentList(P416Parser.TypeArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(P416Parser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(P416Parser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#derivedTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterDerivedTypeDeclaration(P416Parser.DerivedTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#derivedTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitDerivedTypeDeclaration(P416Parser.DerivedTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#headerTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterHeaderTypeDeclaration(P416Parser.HeaderTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#headerTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitHeaderTypeDeclaration(P416Parser.HeaderTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#headerUnionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterHeaderUnionDeclaration(P416Parser.HeaderUnionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#headerUnionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitHeaderUnionDeclaration(P416Parser.HeaderUnionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#structTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStructTypeDeclaration(P416Parser.StructTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#structTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStructTypeDeclaration(P416Parser.StructTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#structFieldList}.
	 * @param ctx the parse tree
	 */
	void enterStructFieldList(P416Parser.StructFieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#structFieldList}.
	 * @param ctx the parse tree
	 */
	void exitStructFieldList(P416Parser.StructFieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#structField}.
	 * @param ctx the parse tree
	 */
	void enterStructField(P416Parser.StructFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#structField}.
	 * @param ctx the parse tree
	 */
	void exitStructField(P416Parser.StructFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(P416Parser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(P416Parser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#errorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterErrorDeclaration(P416Parser.ErrorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#errorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitErrorDeclaration(P416Parser.ErrorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#matchKindDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMatchKindDeclaration(P416Parser.MatchKindDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#matchKindDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMatchKindDeclaration(P416Parser.MatchKindDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(P416Parser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(P416Parser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleTypeDef}
	 * labeled alternative in {@link P416Parser#typedefDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeDef(P416Parser.SimpleTypeDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleTypeDef}
	 * labeled alternative in {@link P416Parser#typedefDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeDef(P416Parser.SimpleTypeDefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code derivedTypeDef}
	 * labeled alternative in {@link P416Parser#typedefDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterDerivedTypeDef(P416Parser.DerivedTypeDefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code derivedTypeDef}
	 * labeled alternative in {@link P416Parser#typedefDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitDerivedTypeDef(P416Parser.DerivedTypeDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(P416Parser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(P416Parser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code applyMethodCall}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void enterApplyMethodCall(P416Parser.ApplyMethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code applyMethodCall}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void exitApplyMethodCall(P416Parser.ApplyMethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code extractMethodCall}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void enterExtractMethodCall(P416Parser.ExtractMethodCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code extractMethodCall}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void exitExtractMethodCall(P416Parser.ExtractMethodCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callWithoutTypeArgs}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callWithoutTypeArgs}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callWithTypeArgs}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void enterCallWithTypeArgs(P416Parser.CallWithTypeArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callWithTypeArgs}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 */
	void exitCallWithTypeArgs(P416Parser.CallWithTypeArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(P416Parser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(P416Parser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(P416Parser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(P416Parser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#exitStatement}.
	 * @param ctx the parse tree
	 */
	void enterExitStatement(P416Parser.ExitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#exitStatement}.
	 * @param ctx the parse tree
	 */
	void exitExitStatement(P416Parser.ExitStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link P416Parser#conditionalStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(P416Parser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link P416Parser#conditionalStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(P416Parser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifElseStatement}
	 * labeled alternative in {@link P416Parser#conditionalStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfElseStatement(P416Parser.IfElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifElseStatement}
	 * labeled alternative in {@link P416Parser#conditionalStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfElseStatement(P416Parser.IfElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#directApplication}.
	 * @param ctx the parse tree
	 */
	void enterDirectApplication(P416Parser.DirectApplicationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#directApplication}.
	 * @param ctx the parse tree
	 */
	void exitDirectApplication(P416Parser.DirectApplicationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(P416Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(P416Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(P416Parser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(P416Parser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#statOrDeclList}.
	 * @param ctx the parse tree
	 */
	void enterStatOrDeclList(P416Parser.StatOrDeclListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#statOrDeclList}.
	 * @param ctx the parse tree
	 */
	void exitStatOrDeclList(P416Parser.StatOrDeclListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(P416Parser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(P416Parser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#switchCases}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCases(P416Parser.SwitchCasesContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#switchCases}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCases(P416Parser.SwitchCasesContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#switchCase}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCase(P416Parser.SwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#switchCase}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCase(P416Parser.SwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(P416Parser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(P416Parser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#statementOrDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStatementOrDeclaration(P416Parser.StatementOrDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#statementOrDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStatementOrDeclaration(P416Parser.StatementOrDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#tableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTableDeclaration(P416Parser.TableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#tableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTableDeclaration(P416Parser.TableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#tablePropertyList}.
	 * @param ctx the parse tree
	 */
	void enterTablePropertyList(P416Parser.TablePropertyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#tablePropertyList}.
	 * @param ctx the parse tree
	 */
	void exitTablePropertyList(P416Parser.TablePropertyListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code key}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void enterKey(P416Parser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code key}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void exitKey(P416Parser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code actions}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void enterActions(P416Parser.ActionsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code actions}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void exitActions(P416Parser.ActionsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constEntries}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void enterConstEntries(P416Parser.ConstEntriesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constEntries}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void exitConstEntries(P416Parser.ConstEntriesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code localVariable}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariable(P416Parser.LocalVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code localVariable}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariable(P416Parser.LocalVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code localConstVariable}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void enterLocalConstVariable(P416Parser.LocalConstVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code localConstVariable}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 */
	void exitLocalConstVariable(P416Parser.LocalConstVariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#keyElementList}.
	 * @param ctx the parse tree
	 */
	void enterKeyElementList(P416Parser.KeyElementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#keyElementList}.
	 * @param ctx the parse tree
	 */
	void exitKeyElementList(P416Parser.KeyElementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#keyElement}.
	 * @param ctx the parse tree
	 */
	void enterKeyElement(P416Parser.KeyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#keyElement}.
	 * @param ctx the parse tree
	 */
	void exitKeyElement(P416Parser.KeyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#actionList}.
	 * @param ctx the parse tree
	 */
	void enterActionList(P416Parser.ActionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#actionList}.
	 * @param ctx the parse tree
	 */
	void exitActionList(P416Parser.ActionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#entriesList}.
	 * @param ctx the parse tree
	 */
	void enterEntriesList(P416Parser.EntriesListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#entriesList}.
	 * @param ctx the parse tree
	 */
	void exitEntriesList(P416Parser.EntriesListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#entry}.
	 * @param ctx the parse tree
	 */
	void enterEntry(P416Parser.EntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#entry}.
	 * @param ctx the parse tree
	 */
	void exitEntry(P416Parser.EntryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code actionWithoutArgs}
	 * labeled alternative in {@link P416Parser#actionRef}.
	 * @param ctx the parse tree
	 */
	void enterActionWithoutArgs(P416Parser.ActionWithoutArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code actionWithoutArgs}
	 * labeled alternative in {@link P416Parser#actionRef}.
	 * @param ctx the parse tree
	 */
	void exitActionWithoutArgs(P416Parser.ActionWithoutArgsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code actionWithArgs}
	 * labeled alternative in {@link P416Parser#actionRef}.
	 * @param ctx the parse tree
	 */
	void enterActionWithArgs(P416Parser.ActionWithArgsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code actionWithArgs}
	 * labeled alternative in {@link P416Parser#actionRef}.
	 * @param ctx the parse tree
	 */
	void exitActionWithArgs(P416Parser.ActionWithArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#actionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterActionDeclaration(P416Parser.ActionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#actionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitActionDeclaration(P416Parser.ActionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(P416Parser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(P416Parser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#constantDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclaration(P416Parser.ConstantDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#constantDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclaration(P416Parser.ConstantDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#optInitializer}.
	 * @param ctx the parse tree
	 */
	void enterOptInitializer(P416Parser.OptInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#optInitializer}.
	 * @param ctx the parse tree
	 */
	void exitOptInitializer(P416Parser.OptInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#initializer}.
	 * @param ctx the parse tree
	 */
	void enterInitializer(P416Parser.InitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#initializer}.
	 * @param ctx the parse tree
	 */
	void exitInitializer(P416Parser.InitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(P416Parser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(P416Parser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(P416Parser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(P416Parser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(P416Parser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(P416Parser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(P416Parser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(P416Parser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#prefixedNonTypeName}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedNonTypeName(P416Parser.PrefixedNonTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#prefixedNonTypeName}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedNonTypeName(P416Parser.PrefixedNonTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixedNameLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedNameLvalue(P416Parser.PrefixedNameLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixedNameLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedNameLvalue(P416Parser.PrefixedNameLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayIndexLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndexLvalue(P416Parser.ArrayIndexLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayIndexLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndexLvalue(P416Parser.ArrayIndexLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rangeIndexLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterRangeIndexLvalue(P416Parser.RangeIndexLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rangeIndexLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitRangeIndexLvalue(P416Parser.RangeIndexLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixedNonTypeNameLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedNonTypeNameLvalue(P416Parser.PrefixedNonTypeNameLvalueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixedNonTypeNameLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedNonTypeNameLvalue(P416Parser.PrefixedNonTypeNameLvalueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinus(P416Parser.MinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinus(P416Parser.MinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rangeIndex}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRangeIndex(P416Parser.RangeIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rangeIndex}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRangeIndex(P416Parser.RangeIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mod}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMod(P416Parser.ModContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mod}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMod(P416Parser.ModContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterString(P416Parser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitString(P416Parser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitOr}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitOr(P416Parser.BitOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitOr}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitOr(P416Parser.BitOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixedNonType}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedNonType(P416Parser.PrefixedNonTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixedNonType}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedNonType(P416Parser.PrefixedNonTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integer}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterInteger(P416Parser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integer}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitInteger(P416Parser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cast}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCast(P416Parser.CastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCast(P416Parser.CastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code not}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNot(P416Parser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code not}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNot(P416Parser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code shiftLeft}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterShiftLeft(P416Parser.ShiftLeftContext ctx);
	/**
	 * Exit a parse tree produced by the {@code shiftLeft}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitShiftLeft(P416Parser.ShiftLeftContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusPlus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlusPlus(P416Parser.PlusPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusPlus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlusPlus(P416Parser.PlusPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code and}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAnd(P416Parser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code and}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAnd(P416Parser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code of}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOf(P416Parser.OfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code of}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOf(P416Parser.OfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lessThan}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThan(P416Parser.LessThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lessThan}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThan(P416Parser.LessThanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code templetizedFunctionCall}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTempletizedFunctionCall(P416Parser.TempletizedFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code templetizedFunctionCall}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTempletizedFunctionCall(P416Parser.TempletizedFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code greaterThan}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThan(P416Parser.GreaterThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code greaterThan}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThan(P416Parser.GreaterThanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberAccess(P416Parser.MemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberAccess(P416Parser.MemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprMemberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExprMemberAccess(P416Parser.ExprMemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprMemberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code set}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSet(P416Parser.SetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code set}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSet(P416Parser.SetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code shifRight}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterShifRight(P416Parser.ShifRightContext ctx);
	/**
	 * Exit a parse tree produced by the {@code shifRight}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitShifRight(P416Parser.ShifRightContext ctx);
	/**
	 * Enter a parse tree produced by the {@code or}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOr(P416Parser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code or}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOr(P416Parser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code star}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterStar(P416Parser.StarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code star}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitStar(P416Parser.StarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code false}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFalse(P416Parser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code false}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFalse(P416Parser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constructor}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(P416Parser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constructor}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(P416Parser.ConstructorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotEqual(P416Parser.NotEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotEqual(P416Parser.NotEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nonType}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNonType(P416Parser.NonTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nonType}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNonType(P416Parser.NonTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlus(P416Parser.PlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlus(P416Parser.PlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code greaterThanOrEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqual(P416Parser.GreaterThanOrEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code greaterThanOrEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqual(P416Parser.GreaterThanOrEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code equal}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqual(P416Parser.EqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code equal}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqual(P416Parser.EqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitAnd}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitAnd(P416Parser.BitAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitAnd}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitAnd(P416Parser.BitAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryPlus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryPlus(P416Parser.UnaryPlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryPlus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryPlus(P416Parser.UnaryPlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code slah}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSlah(P416Parser.SlahContext ctx);
	/**
	 * Exit a parse tree produced by the {@code slah}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSlah(P416Parser.SlahContext ctx);
	/**
	 * Enter a parse tree produced by the {@code negate}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegate(P416Parser.NegateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code negate}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegate(P416Parser.NegateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bitXor}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBitXor(P416Parser.BitXorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bitXor}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBitXor(P416Parser.BitXorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lessThanOrEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqual(P416Parser.LessThanOrEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lessThanOrEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqual(P416Parser.LessThanOrEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(P416Parser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(P416Parser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code true}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTrue(P416Parser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code true}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTrue(P416Parser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryMinus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinus(P416Parser.UnaryMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryMinus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinus(P416Parser.UnaryMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code errorMemberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterErrorMemberAccess(P416Parser.ErrorMemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code errorMemberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitErrorMemberAccess(P416Parser.ErrorMemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayIndex}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndex(P416Parser.ArrayIndexContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayIndex}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndex(P416Parser.ArrayIndexContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ternary}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTernary(P416Parser.TernaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ternary}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTernary(P416Parser.TernaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#unaryExpression_not}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression_not(P416Parser.UnaryExpression_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#unaryExpression_not}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression_not(P416Parser.UnaryExpression_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#unaryExpression_tilda}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression_tilda(P416Parser.UnaryExpression_tildaContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#unaryExpression_tilda}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression_tilda(P416Parser.UnaryExpression_tildaContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#unaryExpression_plus}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression_plus(P416Parser.UnaryExpression_plusContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#unaryExpression_plus}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression_plus(P416Parser.UnaryExpression_plusContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#unaryExpression_minus}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression_minus(P416Parser.UnaryExpression_minusContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#unaryExpression_minus}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression_minus(P416Parser.UnaryExpression_minusContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(P416Parser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(P416Parser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#decimalNumber}.
	 * @param ctx the parse tree
	 */
	void enterDecimalNumber(P416Parser.DecimalNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#decimalNumber}.
	 * @param ctx the parse tree
	 */
	void exitDecimalNumber(P416Parser.DecimalNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#octalNumber}.
	 * @param ctx the parse tree
	 */
	void enterOctalNumber(P416Parser.OctalNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#octalNumber}.
	 * @param ctx the parse tree
	 */
	void exitOctalNumber(P416Parser.OctalNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#binaryNumber}.
	 * @param ctx the parse tree
	 */
	void enterBinaryNumber(P416Parser.BinaryNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#binaryNumber}.
	 * @param ctx the parse tree
	 */
	void exitBinaryNumber(P416Parser.BinaryNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#hexNumber}.
	 * @param ctx the parse tree
	 */
	void enterHexNumber(P416Parser.HexNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#hexNumber}.
	 * @param ctx the parse tree
	 */
	void exitHexNumber(P416Parser.HexNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link P416Parser#realNumber}.
	 * @param ctx the parse tree
	 */
	void enterRealNumber(P416Parser.RealNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link P416Parser#realNumber}.
	 * @param ctx the parse tree
	 */
	void exitRealNumber(P416Parser.RealNumberContext ctx);
}