package com.p4.p416.gen;


import org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.* ;


public class ExtendedContextGetVisitor extends P416BaseVisitor<AbstractBaseExt>{

	@Override
	public AbstractBaseExt visitP4program(P416Parser.P4programContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitDeclaration(P416Parser.DeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitNonTypeName(P416Parser.NonTypeNameContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitName(P416Parser.NameContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitOptAnnotations(P416Parser.OptAnnotationsContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitAnnotations(P416Parser.AnnotationsContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSimpleAnnotation(P416Parser.SimpleAnnotationContext ctx) {
		return ((SimpleAnnotationContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitComplexAnnotation(P416Parser.ComplexAnnotationContext ctx) {
		return ((ComplexAnnotationContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitParameterList(P416Parser.ParameterListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParameter(P416Parser.ParameterContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitInDirection(P416Parser.InDirectionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitOutDirection(P416Parser.OutDirectionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitInOutDirection(P416Parser.InOutDirectionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitPackageTypeDeclaration(P416Parser.PackageTypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitInstantiation(P416Parser.InstantiationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitOptConstructorParameters(P416Parser.OptConstructorParametersContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitDotPrefix(P416Parser.DotPrefixContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserDeclaration(P416Parser.ParserDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserLocalElements(P416Parser.ParserLocalElementsContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserLocalElement(P416Parser.ParserLocalElementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserTypeDeclaration(P416Parser.ParserTypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserStates(P416Parser.ParserStatesContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserState(P416Parser.ParserStateContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserStatements(P416Parser.ParserStatementsContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserStatement(P416Parser.ParserStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitParserBlockStatement(P416Parser.ParserBlockStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTransitionStatement(P416Parser.TransitionStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStateExpression(P416Parser.StateExpressionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSelectExpression(P416Parser.SelectExpressionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSelectCaseList(P416Parser.SelectCaseListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSelectCase(P416Parser.SelectCaseContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitKeySetExpression(P416Parser.KeySetExpressionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTupleKeySetExpression(P416Parser.TupleKeySetExpressionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSimpleKeySetExpression(P416Parser.SimpleKeySetExpressionContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitControlDeclaration(P416Parser.ControlDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitControlLocalDeclarations(P416Parser.ControlLocalDeclarationsContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitControlLocalDeclaration(P416Parser.ControlLocalDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitControlBody(P416Parser.ControlBodyContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx) {
		return ((ExternObjectDeclarationContextExt) ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx) {
		return ((ExternFunctionDeclarationContextExt) ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitMethodPrototypes(P416Parser.MethodPrototypesContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitFunctionPrototype(P416Parser.FunctionPrototypeContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitMethodPrototype(P416Parser.MethodPrototypeContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTypeRef(P416Parser.TypeRefContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitPrefixedType(P416Parser.PrefixedTypeContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTypeName(P416Parser.TypeNameContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTupleType(P416Parser.TupleTypeContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitHeaderStackType(P416Parser.HeaderStackTypeContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSpecializedType(P416Parser.SpecializedTypeContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitBoolType(P416Parser.BoolTypeContext ctx) {
		return ((BoolTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitErrorType(P416Parser.ErrorTypeContext ctx) {
		return ((ErrorTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitBitType(P416Parser.BitTypeContext ctx) {
		return ((BitTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitBitSizeType(P416Parser.BitSizeTypeContext ctx) {
		return ((BitSizeTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitIntSizeType(P416Parser.IntSizeTypeContext ctx) {
		return ((IntSizeTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitVarBitSizeType(P416Parser.VarBitSizeTypeContext ctx) {
		return ((VarBitSizeTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitTypeOrVoid(P416Parser.TypeOrVoidContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitOptTypeParameters(P416Parser.OptTypeParametersContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTypeParameterList(P416Parser.TypeParameterListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTypeArg(P416Parser.TypeArgContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitDontcare(P416Parser.DontcareContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTypeArgumentList(P416Parser.TypeArgumentListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTypeDeclaration(P416Parser.TypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitDerivedTypeDeclaration(P416Parser.DerivedTypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitHeaderTypeDeclaration(P416Parser.HeaderTypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitHeaderUnionDeclaration(P416Parser.HeaderUnionDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStructTypeDeclaration(P416Parser.StructTypeDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStructFieldList(P416Parser.StructFieldListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStructField(P416Parser.StructFieldContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitEnumDeclaration(P416Parser.EnumDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitErrorDeclaration(P416Parser.ErrorDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitMatchKindDeclaration(P416Parser.MatchKindDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitIdentifierList(P416Parser.IdentifierListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSimpleTypeDef(P416Parser.SimpleTypeDefContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitDerivedTypeDef(P416Parser.DerivedTypeDefContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitAssignmentStatement(P416Parser.AssignmentStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitApplyMethodCall(P416Parser.ApplyMethodCallContext ctx) {
		return ((ApplyMethodCallContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitExtractMethodCall(P416Parser.ExtractMethodCallContext ctx) {
		return ((ExtractMethodCallContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx) {
		return ((CallWithoutTypeArgsContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitCallWithTypeArgs(P416Parser.CallWithTypeArgsContext ctx) {
		return ((CallWithTypeArgsContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitEmptyStatement(P416Parser.EmptyStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitReturnStatement(P416Parser.ReturnStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitExitStatement(P416Parser.ExitStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitIfStatement(P416Parser.IfStatementContext ctx) {
		return ((IfStatementContextExt) ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitIfElseStatement(P416Parser.IfElseStatementContext ctx) {
		return ((IfElseStatementContextExt) ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitDirectApplication(P416Parser.DirectApplicationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStatement(P416Parser.StatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitBlockStatement(P416Parser.BlockStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStatOrDeclList(P416Parser.StatOrDeclListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSwitchStatement(P416Parser.SwitchStatementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSwitchCases(P416Parser.SwitchCasesContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSwitchCase(P416Parser.SwitchCaseContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitSwitchLabel(P416Parser.SwitchLabelContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitStatementOrDeclaration(P416Parser.StatementOrDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTableDeclaration(P416Parser.TableDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitTablePropertyList(P416Parser.TablePropertyListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitKey(P416Parser.KeyContext ctx) {
		return ((KeyContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitActions(P416Parser.ActionsContext ctx) {
		return ((ActionsContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitConstEntries(P416Parser.ConstEntriesContext ctx) {
		return ((ConstEntriesContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitLocalVariable(P416Parser.LocalVariableContext ctx) {
		return ((LocalVariableContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitLocalConstVariable(P416Parser.LocalConstVariableContext ctx) {
		return ((LocalConstVariableContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitKeyElementList(P416Parser.KeyElementListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitKeyElement(P416Parser.KeyElementContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitActionList(P416Parser.ActionListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitEntriesList(P416Parser.EntriesListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitEntry(P416Parser.EntryContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitActionWithoutArgs(P416Parser.ActionWithoutArgsContext ctx) {
		return ((ActionWithoutArgsContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitActionWithArgs(P416Parser.ActionWithArgsContext ctx) {
		return ((ActionWithArgsContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitActionDeclaration(P416Parser.ActionDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitVariableDeclaration(P416Parser.VariableDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitConstantDeclaration(P416Parser.ConstantDeclarationContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitOptInitializer(P416Parser.OptInitializerContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitInitializer(P416Parser.InitializerContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitArgumentList(P416Parser.ArgumentListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitArgument(P416Parser.ArgumentContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitExpressionList(P416Parser.ExpressionListContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitMember(P416Parser.MemberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitPrefixedNonTypeName(P416Parser.PrefixedNonTypeNameContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitPrefixedNonTypeNameLvalue(P416Parser.PrefixedNonTypeNameLvalueContext ctx) {
		return ((PrefixedNonTypeNameLvalueContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitPrefixedNameLvalue(P416Parser.PrefixedNameLvalueContext ctx) {
		return ((PrefixedNameLvalueContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitArrayIndexLvalue(P416Parser.ArrayIndexLvalueContext ctx) {
		return ((ArrayIndexLvalueContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitRangeIndexLvalue(P416Parser.RangeIndexLvalueContext ctx) {
		return ((RangeIndexLvalueContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitInteger(P416Parser.IntegerContext ctx) {
		return ((IntegerContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitTrue(P416Parser.TrueContext ctx) {
		return ((TrueContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitFalse(P416Parser.FalseContext ctx) {
		return ((FalseContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitString(P416Parser.StringContext ctx) {
		return ((StringContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitNonType(P416Parser.NonTypeContext ctx) {
		return ((NonTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitPrefixedNonType(P416Parser.PrefixedNonTypeContext ctx) {
		return ((PrefixedNonTypeContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitArrayIndex(P416Parser.ArrayIndexContext ctx) {
		return ((ArrayIndexContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitRangeIndex(P416Parser.RangeIndexContext ctx) {
		return ((RangeIndexContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitSet(P416Parser.SetContext ctx) {
		return ((SetContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitOf(P416Parser.OfContext ctx) {
		return ((OfContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitNot(P416Parser.NotContext ctx) {
		return ((NotContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitNegate(P416Parser.NegateContext ctx) {
		return ((NegateContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitUnaryMinus(P416Parser.UnaryMinusContext ctx) {
		return ((UnaryMinusContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitUnaryPlus(P416Parser.UnaryPlusContext ctx) {
		return ((UnaryPlusContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitMemberAccess(P416Parser.MemberAccessContext ctx) {
		return ((MemberAccessContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitErrorMemberAccess(P416Parser.ErrorMemberAccessContext ctx) {
		return ((ErrorMemberAccessContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx) {
		return ((ExprMemberAccessContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitStar(P416Parser.StarContext ctx) {
		return ((StarContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitSlah(P416Parser.SlahContext ctx) {
		return ((SlahContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitMod(P416Parser.ModContext ctx) {
		return ((ModContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitPlus(P416Parser.PlusContext ctx) {
		return ((PlusContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitMinus(P416Parser.MinusContext ctx) {
		return ((MinusContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitShiftLeft(P416Parser.ShiftLeftContext ctx) {
		return ((ShiftLeftContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitShifRight(P416Parser.ShifRightContext ctx) {
		return ((ShifRightContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitBitAnd(P416Parser.BitAndContext ctx) {
		return ((BitAndContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitBitXor(P416Parser.BitXorContext ctx) {
		return ((BitXorContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitBitOr(P416Parser.BitOrContext ctx) {
		return ((BitOrContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitPlusPlus(P416Parser.PlusPlusContext ctx) {
		return ((PlusPlusContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitLessThanOrEqual(P416Parser.LessThanOrEqualContext ctx) {
		return ((LessThanOrEqualContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitGreaterThanOrEqual(P416Parser.GreaterThanOrEqualContext ctx) {
		return ((GreaterThanOrEqualContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitLessThan(P416Parser.LessThanContext ctx) {
		return ((LessThanContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitGreaterThan(P416Parser.GreaterThanContext ctx) {
		return ((GreaterThanContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitNotEqual(P416Parser.NotEqualContext ctx) {
		return ((NotEqualContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitEqual(P416Parser.EqualContext ctx) {
		return ((EqualContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitAnd(P416Parser.AndContext ctx) {
		return ((AndContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitOr(P416Parser.OrContext ctx) {
		return ((OrContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitTernary(P416Parser.TernaryContext ctx) {
		return ((TernaryContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitTempletizedFunctionCall(P416Parser.TempletizedFunctionCallContext ctx) {
		return ((TempletizedFunctionCallContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitFunctionCall(P416Parser.FunctionCallContext ctx) {
		return ((FunctionCallContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitConstructor(P416Parser.ConstructorContext ctx) {
		return ((ConstructorContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitCast(P416Parser.CastContext ctx) {
		return ((CastContextExt)ctx.extendedContext).getContext().extendedContext;
	}
	@Override
	public AbstractBaseExt visitNumber(P416Parser.NumberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitDecimalNumber(P416Parser.DecimalNumberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitOctalNumber(P416Parser.OctalNumberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitBinaryNumber(P416Parser.BinaryNumberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitHexNumber(P416Parser.HexNumberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitRealNumber(P416Parser.RealNumberContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}

	@Override
	public AbstractBaseExt visitUnaryExpression_not(P416Parser.UnaryExpression_notContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitUnaryExpression_tilda(P416Parser.UnaryExpression_tildaContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitUnaryExpression_plus(P416Parser.UnaryExpression_plusContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}
	@Override
	public AbstractBaseExt visitUnaryExpression_minus(P416Parser.UnaryExpression_minusContext ctx) {
		if ( ctx.extendedContext.getContext() != null)
			return ctx.extendedContext.getContext().extendedContext;
		else
			return null;
	}

}
