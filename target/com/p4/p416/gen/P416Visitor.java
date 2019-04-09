// Generated from P416.g4 by ANTLR 4.5
package com.p4.p416.gen;

import com.p4.p416.gen.*;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link P416Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface P416Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link P416Parser#p4program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitP4program(P416Parser.P4programContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(P416Parser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#nonTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonTypeName(P416Parser.NonTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(P416Parser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#optAnnotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptAnnotations(P416Parser.OptAnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(P416Parser.AnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleAnnotation}
	 * labeled alternative in {@link P416Parser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleAnnotation(P416Parser.SimpleAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code complexAnnotation}
	 * labeled alternative in {@link P416Parser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexAnnotation(P416Parser.ComplexAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(P416Parser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(P416Parser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInDirection(P416Parser.InDirectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code outDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutDirection(P416Parser.OutDirectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inOutDirection}
	 * labeled alternative in {@link P416Parser#direction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInOutDirection(P416Parser.InOutDirectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#packageTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageTypeDeclaration(P416Parser.PackageTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#instantiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstantiation(P416Parser.InstantiationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#optConstructorParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptConstructorParameters(P416Parser.OptConstructorParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#dotPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotPrefix(P416Parser.DotPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserDeclaration(P416Parser.ParserDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserLocalElements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserLocalElements(P416Parser.ParserLocalElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserLocalElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserLocalElement(P416Parser.ParserLocalElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserTypeDeclaration(P416Parser.ParserTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserStates}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserStates(P416Parser.ParserStatesContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserState}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserState(P416Parser.ParserStateContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserStatements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserStatements(P416Parser.ParserStatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserStatement(P416Parser.ParserStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#parserBlockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParserBlockStatement(P416Parser.ParserBlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#transitionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransitionStatement(P416Parser.TransitionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#stateExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStateExpression(P416Parser.StateExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#selectExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpression(P416Parser.SelectExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#selectCaseList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectCaseList(P416Parser.SelectCaseListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#selectCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectCase(P416Parser.SelectCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#keySetExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeySetExpression(P416Parser.KeySetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#tupleKeySetExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleKeySetExpression(P416Parser.TupleKeySetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#simpleKeySetExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleKeySetExpression(P416Parser.SimpleKeySetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#controlDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlDeclaration(P416Parser.ControlDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#controlTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#controlLocalDeclarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlLocalDeclarations(P416Parser.ControlLocalDeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#controlLocalDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlLocalDeclaration(P416Parser.ControlLocalDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#controlBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlBody(P416Parser.ControlBodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code externObjectDeclaration}
	 * labeled alternative in {@link P416Parser#externDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code externFunctionDeclaration}
	 * labeled alternative in {@link P416Parser#externDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#methodPrototypes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodPrototypes(P416Parser.MethodPrototypesContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#functionPrototype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionPrototype(P416Parser.FunctionPrototypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#methodPrototype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodPrototype(P416Parser.MethodPrototypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRef(P416Parser.TypeRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#prefixedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedType(P416Parser.PrefixedTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(P416Parser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#tupleType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(P416Parser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#headerStackType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeaderStackType(P416Parser.HeaderStackTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#specializedType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecializedType(P416Parser.SpecializedTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolType(P416Parser.BoolTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code errorType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorType(P416Parser.ErrorTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitType(P416Parser.BitTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitSizeType(P416Parser.BitSizeTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntSizeType(P416Parser.IntSizeTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varBitSizeType}
	 * labeled alternative in {@link P416Parser#baseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarBitSizeType(P416Parser.VarBitSizeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeOrVoid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeOrVoid(P416Parser.TypeOrVoidContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#optTypeParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptTypeParameters(P416Parser.OptTypeParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameterList(P416Parser.TypeParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArg(P416Parser.TypeArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#dontcare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDontcare(P416Parser.DontcareContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgumentList(P416Parser.TypeArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(P416Parser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#derivedTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerivedTypeDeclaration(P416Parser.DerivedTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#headerTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeaderTypeDeclaration(P416Parser.HeaderTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#headerUnionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHeaderUnionDeclaration(P416Parser.HeaderUnionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#structTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructTypeDeclaration(P416Parser.StructTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#structFieldList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructFieldList(P416Parser.StructFieldListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#structField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructField(P416Parser.StructFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#enumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDeclaration(P416Parser.EnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#errorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorDeclaration(P416Parser.ErrorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#matchKindDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchKindDeclaration(P416Parser.MatchKindDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(P416Parser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleTypeDef}
	 * labeled alternative in {@link P416Parser#typedefDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeDef(P416Parser.SimpleTypeDefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code derivedTypeDef}
	 * labeled alternative in {@link P416Parser#typedefDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDerivedTypeDef(P416Parser.DerivedTypeDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#assignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(P416Parser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code applyMethodCall}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplyMethodCall(P416Parser.ApplyMethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code extractMethodCall}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractMethodCall(P416Parser.ExtractMethodCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callWithoutTypeArgs}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callWithTypeArgs}
	 * labeled alternative in {@link P416Parser#methodCallStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallWithTypeArgs(P416Parser.CallWithTypeArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(P416Parser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(P416Parser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#exitStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExitStatement(P416Parser.ExitStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link P416Parser#conditionalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(P416Parser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifElseStatement}
	 * labeled alternative in {@link P416Parser#conditionalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseStatement(P416Parser.IfElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#directApplication}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirectApplication(P416Parser.DirectApplicationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(P416Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(P416Parser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#statOrDeclList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatOrDeclList(P416Parser.StatOrDeclListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#switchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStatement(P416Parser.SwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#switchCases}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCases(P416Parser.SwitchCasesContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#switchCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchCase(P416Parser.SwitchCaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#switchLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchLabel(P416Parser.SwitchLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#statementOrDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementOrDeclaration(P416Parser.StatementOrDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#tableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableDeclaration(P416Parser.TableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#tablePropertyList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTablePropertyList(P416Parser.TablePropertyListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code key}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKey(P416Parser.KeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code actions}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActions(P416Parser.ActionsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constEntries}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstEntries(P416Parser.ConstEntriesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code localVariable}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariable(P416Parser.LocalVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code localConstVariable}
	 * labeled alternative in {@link P416Parser#tableProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalConstVariable(P416Parser.LocalConstVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#keyElementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyElementList(P416Parser.KeyElementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#keyElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyElement(P416Parser.KeyElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#actionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionList(P416Parser.ActionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#entriesList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntriesList(P416Parser.EntriesListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntry(P416Parser.EntryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code actionWithoutArgs}
	 * labeled alternative in {@link P416Parser#actionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionWithoutArgs(P416Parser.ActionWithoutArgsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code actionWithArgs}
	 * labeled alternative in {@link P416Parser#actionRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionWithArgs(P416Parser.ActionWithArgsContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#actionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActionDeclaration(P416Parser.ActionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(P416Parser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#constantDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclaration(P416Parser.ConstantDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#optInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOptInitializer(P416Parser.OptInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializer(P416Parser.InitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(P416Parser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(P416Parser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(P416Parser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember(P416Parser.MemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#prefixedNonTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedNonTypeName(P416Parser.PrefixedNonTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixedNameLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedNameLvalue(P416Parser.PrefixedNameLvalueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayIndexLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndexLvalue(P416Parser.ArrayIndexLvalueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rangeIndexLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeIndexLvalue(P416Parser.RangeIndexLvalueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixedNonTypeNameLvalue}
	 * labeled alternative in {@link P416Parser#lvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedNonTypeNameLvalue(P416Parser.PrefixedNonTypeNameLvalueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinus(P416Parser.MinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code rangeIndex}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeIndex(P416Parser.RangeIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mod}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMod(P416Parser.ModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(P416Parser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitOr}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOr(P416Parser.BitOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixedNonType}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedNonType(P416Parser.PrefixedNonTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integer}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(P416Parser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cast}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCast(P416Parser.CastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(P416Parser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code shiftLeft}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftLeft(P416Parser.ShiftLeftContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusPlus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusPlus(P416Parser.PlusPlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(P416Parser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code of}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOf(P416Parser.OfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lessThan}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThan(P416Parser.LessThanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code templetizedFunctionCall}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTempletizedFunctionCall(P416Parser.TempletizedFunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code greaterThan}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThan(P416Parser.GreaterThanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccess(P416Parser.MemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprMemberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code set}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet(P416Parser.SetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code shifRight}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShifRight(P416Parser.ShifRightContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(P416Parser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code star}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStar(P416Parser.StarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code false}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(P416Parser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constructor}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructor(P416Parser.ConstructorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqual(P416Parser.NotEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nonType}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonType(P416Parser.NonTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlus(P416Parser.PlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code greaterThanOrEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanOrEqual(P416Parser.GreaterThanOrEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equal}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual(P416Parser.EqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitAnd}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAnd(P416Parser.BitAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryPlus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryPlus(P416Parser.UnaryPlusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code slah}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlah(P416Parser.SlahContext ctx);
	/**
	 * Visit a parse tree produced by the {@code negate}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegate(P416Parser.NegateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bitXor}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitXor(P416Parser.BitXorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lessThanOrEqual}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanOrEqual(P416Parser.LessThanOrEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(P416Parser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code true}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(P416Parser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryMinus}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinus(P416Parser.UnaryMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code errorMemberAccess}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitErrorMemberAccess(P416Parser.ErrorMemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayIndex}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndex(P416Parser.ArrayIndexContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ternary}
	 * labeled alternative in {@link P416Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernary(P416Parser.TernaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#unaryExpression_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression_not(P416Parser.UnaryExpression_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#unaryExpression_tilda}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression_tilda(P416Parser.UnaryExpression_tildaContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#unaryExpression_plus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression_plus(P416Parser.UnaryExpression_plusContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#unaryExpression_minus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression_minus(P416Parser.UnaryExpression_minusContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(P416Parser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#decimalNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalNumber(P416Parser.DecimalNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#octalNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOctalNumber(P416Parser.OctalNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#binaryNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryNumber(P416Parser.BinaryNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#hexNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHexNumber(P416Parser.HexNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link P416Parser#realNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealNumber(P416Parser.RealNumberContext ctx);
}