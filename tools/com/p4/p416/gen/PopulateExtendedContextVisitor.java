package com.p4.p416.gen;


import org.antlr.v4.runtime.ParserRuleContext;


public class PopulateExtendedContextVisitor extends P416BaseVisitor<ParserRuleContext>{


		@Override 
		public ParserRuleContext visitP4program(P416Parser.P4programContext ctx){
			super.visitP4program(ctx);
			ctx.extendedContext = new P4programContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDeclaration(P416Parser.DeclarationContext ctx){
			super.visitDeclaration(ctx);
			ctx.extendedContext = new DeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitNonTypeName(P416Parser.NonTypeNameContext ctx){
			super.visitNonTypeName(ctx);
			ctx.extendedContext = new NonTypeNameContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitName(P416Parser.NameContext ctx){
			super.visitName(ctx);
			ctx.extendedContext = new NameContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOptAnnotations(P416Parser.OptAnnotationsContext ctx){
			super.visitOptAnnotations(ctx);
			ctx.extendedContext = new OptAnnotationsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitAnnotations(P416Parser.AnnotationsContext ctx){
			super.visitAnnotations(ctx);
			ctx.extendedContext = new AnnotationsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSimpleAnnotation(P416Parser.SimpleAnnotationContext ctx){
			super.visitSimpleAnnotation(ctx);
			ctx.extendedContext = new SimpleAnnotationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitComplexAnnotation(P416Parser.ComplexAnnotationContext ctx){
			super.visitComplexAnnotation(ctx);
			ctx.extendedContext = new ComplexAnnotationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParameterList(P416Parser.ParameterListContext ctx){
			super.visitParameterList(ctx);
			ctx.extendedContext = new ParameterListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParameter(P416Parser.ParameterContext ctx){
			super.visitParameter(ctx);
			ctx.extendedContext = new ParameterContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitInDirection(P416Parser.InDirectionContext ctx){
			super.visitInDirection(ctx);
			ctx.extendedContext = new DirectionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOutDirection(P416Parser.OutDirectionContext ctx){
			super.visitOutDirection(ctx);
			ctx.extendedContext = new DirectionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitInOutDirection(P416Parser.InOutDirectionContext ctx){
			super.visitInOutDirection(ctx);
			ctx.extendedContext = new DirectionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPackageTypeDeclaration(P416Parser.PackageTypeDeclarationContext ctx){
			super.visitPackageTypeDeclaration(ctx);
			ctx.extendedContext = new PackageTypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitInstantiation(P416Parser.InstantiationContext ctx){
			super.visitInstantiation(ctx);
			ctx.extendedContext = new InstantiationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOptConstructorParameters(P416Parser.OptConstructorParametersContext ctx){
			super.visitOptConstructorParameters(ctx);
			ctx.extendedContext = new OptConstructorParametersContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDotPrefix(P416Parser.DotPrefixContext ctx){
			super.visitDotPrefix(ctx);
			ctx.extendedContext = new DotPrefixContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserDeclaration(P416Parser.ParserDeclarationContext ctx){
			super.visitParserDeclaration(ctx);
			ctx.extendedContext = new ParserDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserLocalElements(P416Parser.ParserLocalElementsContext ctx){
			super.visitParserLocalElements(ctx);
			ctx.extendedContext = new ParserLocalElementsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserLocalElement(P416Parser.ParserLocalElementContext ctx){
			super.visitParserLocalElement(ctx);
			ctx.extendedContext = new ParserLocalElementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserTypeDeclaration(P416Parser.ParserTypeDeclarationContext ctx){
			super.visitParserTypeDeclaration(ctx);
			ctx.extendedContext = new ParserTypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserStates(P416Parser.ParserStatesContext ctx){
			super.visitParserStates(ctx);
			ctx.extendedContext = new ParserStatesContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserState(P416Parser.ParserStateContext ctx){
			super.visitParserState(ctx);
			ctx.extendedContext = new ParserStateContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserStatements(P416Parser.ParserStatementsContext ctx){
			super.visitParserStatements(ctx);
			ctx.extendedContext = new ParserStatementsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserStatement(P416Parser.ParserStatementContext ctx){
			super.visitParserStatement(ctx);
			ctx.extendedContext = new ParserStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitParserBlockStatement(P416Parser.ParserBlockStatementContext ctx){
			super.visitParserBlockStatement(ctx);
			ctx.extendedContext = new ParserBlockStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTransitionStatement(P416Parser.TransitionStatementContext ctx){
			super.visitTransitionStatement(ctx);
			ctx.extendedContext = new TransitionStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStateExpression(P416Parser.StateExpressionContext ctx){
			super.visitStateExpression(ctx);
			ctx.extendedContext = new StateExpressionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSelectExpression(P416Parser.SelectExpressionContext ctx){
			super.visitSelectExpression(ctx);
			ctx.extendedContext = new SelectExpressionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSelectCaseList(P416Parser.SelectCaseListContext ctx){
			super.visitSelectCaseList(ctx);
			ctx.extendedContext = new SelectCaseListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSelectCase(P416Parser.SelectCaseContext ctx){
			super.visitSelectCase(ctx);
			ctx.extendedContext = new SelectCaseContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitKeySetExpression(P416Parser.KeySetExpressionContext ctx){
			super.visitKeySetExpression(ctx);
			ctx.extendedContext = new KeySetExpressionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTupleKeySetExpression(P416Parser.TupleKeySetExpressionContext ctx){
			super.visitTupleKeySetExpression(ctx);
			ctx.extendedContext = new TupleKeySetExpressionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSimpleKeySetExpression(P416Parser.SimpleKeySetExpressionContext ctx){
			super.visitSimpleKeySetExpression(ctx);
			ctx.extendedContext = new SimpleKeySetExpressionContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitControlDeclaration(P416Parser.ControlDeclarationContext ctx){
			super.visitControlDeclaration(ctx);
			ctx.extendedContext = new ControlDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx){
			super.visitControlTypeDeclaration(ctx);
			ctx.extendedContext = new ControlTypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitControlLocalDeclarations(P416Parser.ControlLocalDeclarationsContext ctx){
			super.visitControlLocalDeclarations(ctx);
			ctx.extendedContext = new ControlLocalDeclarationsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitControlLocalDeclaration(P416Parser.ControlLocalDeclarationContext ctx){
			super.visitControlLocalDeclaration(ctx);
			ctx.extendedContext = new ControlLocalDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitControlBody(P416Parser.ControlBodyContext ctx){
			super.visitControlBody(ctx);
			ctx.extendedContext = new ControlBodyContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx){
			super.visitExternObjectDeclaration(ctx);
			ctx.extendedContext = new ExternObjectDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx){
			super.visitExternFunctionDeclaration(ctx);
			ctx.extendedContext = new ExternFunctionDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMethodPrototypes(P416Parser.MethodPrototypesContext ctx){
			super.visitMethodPrototypes(ctx);
			ctx.extendedContext = new MethodPrototypesContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitFunctionPrototype(P416Parser.FunctionPrototypeContext ctx){
			super.visitFunctionPrototype(ctx);
			ctx.extendedContext = new FunctionPrototypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMethodPrototype(P416Parser.MethodPrototypeContext ctx){
			super.visitMethodPrototype(ctx);
			ctx.extendedContext = new MethodPrototypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeRef(P416Parser.TypeRefContext ctx){
			super.visitTypeRef(ctx);
			ctx.extendedContext = new TypeRefContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPrefixedType(P416Parser.PrefixedTypeContext ctx){
			super.visitPrefixedType(ctx);
			ctx.extendedContext = new PrefixedTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeName(P416Parser.TypeNameContext ctx){
			super.visitTypeName(ctx);
			ctx.extendedContext = new TypeNameContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTupleType(P416Parser.TupleTypeContext ctx){
			super.visitTupleType(ctx);
			ctx.extendedContext = new TupleTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitHeaderStackType(P416Parser.HeaderStackTypeContext ctx){
			super.visitHeaderStackType(ctx);
			ctx.extendedContext = new HeaderStackTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSpecializedType(P416Parser.SpecializedTypeContext ctx){
			super.visitSpecializedType(ctx);
			ctx.extendedContext = new SpecializedTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBoolType(P416Parser.BoolTypeContext ctx){
			super.visitBoolType(ctx);
			ctx.extendedContext = new BoolTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitErrorType(P416Parser.ErrorTypeContext ctx){
			super.visitErrorType(ctx);
			ctx.extendedContext = new ErrorTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBitType(P416Parser.BitTypeContext ctx){
			super.visitBitType(ctx);
			ctx.extendedContext = new BitTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBitSizeType(P416Parser.BitSizeTypeContext ctx){
			super.visitBitSizeType(ctx);
			ctx.extendedContext = new BitSizeTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitIntSizeType(P416Parser.IntSizeTypeContext ctx){
			super.visitIntSizeType(ctx);
			ctx.extendedContext = new IntSizeTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitVarBitSizeType(P416Parser.VarBitSizeTypeContext ctx){
			super.visitVarBitSizeType(ctx);
			ctx.extendedContext = new VarBitSizeTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeOrVoid(P416Parser.TypeOrVoidContext ctx){
			super.visitTypeOrVoid(ctx);
			ctx.extendedContext = new TypeOrVoidContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOptTypeParameters(P416Parser.OptTypeParametersContext ctx){
			super.visitOptTypeParameters(ctx);
			ctx.extendedContext = new OptTypeParametersContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeParameterList(P416Parser.TypeParameterListContext ctx){
			super.visitTypeParameterList(ctx);
			ctx.extendedContext = new TypeParameterListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeArg(P416Parser.TypeArgContext ctx){
			super.visitTypeArg(ctx);
			ctx.extendedContext = new TypeArgContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDontcare(P416Parser.DontcareContext ctx){
			super.visitDontcare(ctx);
			ctx.extendedContext = new DontcareContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeArgumentList(P416Parser.TypeArgumentListContext ctx){
			super.visitTypeArgumentList(ctx);
			ctx.extendedContext = new TypeArgumentListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTypeDeclaration(P416Parser.TypeDeclarationContext ctx){
			super.visitTypeDeclaration(ctx);
			ctx.extendedContext = new TypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDerivedTypeDeclaration(P416Parser.DerivedTypeDeclarationContext ctx){
			super.visitDerivedTypeDeclaration(ctx);
			ctx.extendedContext = new DerivedTypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitHeaderTypeDeclaration(P416Parser.HeaderTypeDeclarationContext ctx){
			super.visitHeaderTypeDeclaration(ctx);
			ctx.extendedContext = new HeaderTypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitHeaderUnionDeclaration(P416Parser.HeaderUnionDeclarationContext ctx){
			super.visitHeaderUnionDeclaration(ctx);
			ctx.extendedContext = new HeaderUnionDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStructTypeDeclaration(P416Parser.StructTypeDeclarationContext ctx){
			super.visitStructTypeDeclaration(ctx);
			ctx.extendedContext = new StructTypeDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStructFieldList(P416Parser.StructFieldListContext ctx){
			super.visitStructFieldList(ctx);
			ctx.extendedContext = new StructFieldListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStructField(P416Parser.StructFieldContext ctx){
			super.visitStructField(ctx);
			ctx.extendedContext = new StructFieldContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitEnumDeclaration(P416Parser.EnumDeclarationContext ctx){
			super.visitEnumDeclaration(ctx);
			ctx.extendedContext = new EnumDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitErrorDeclaration(P416Parser.ErrorDeclarationContext ctx){
			super.visitErrorDeclaration(ctx);
			ctx.extendedContext = new ErrorDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMatchKindDeclaration(P416Parser.MatchKindDeclarationContext ctx){
			super.visitMatchKindDeclaration(ctx);
			ctx.extendedContext = new MatchKindDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitIdentifierList(P416Parser.IdentifierListContext ctx){
			super.visitIdentifierList(ctx);
			ctx.extendedContext = new IdentifierListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSimpleTypeDef(P416Parser.SimpleTypeDefContext ctx){
			super.visitSimpleTypeDef(ctx);
			ctx.extendedContext = new TypedefDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDerivedTypeDef(P416Parser.DerivedTypeDefContext ctx){
			super.visitDerivedTypeDef(ctx);
			ctx.extendedContext = new TypedefDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitAssignmentStatement(P416Parser.AssignmentStatementContext ctx){
			super.visitAssignmentStatement(ctx);
			ctx.extendedContext = new AssignmentStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitApplyMethodCall(P416Parser.ApplyMethodCallContext ctx){
			super.visitApplyMethodCall(ctx);
			ctx.extendedContext = new ApplyMethodCallContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitExtractMethodCall(P416Parser.ExtractMethodCallContext ctx){
			super.visitExtractMethodCall(ctx);
			ctx.extendedContext = new ExtractMethodCallContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx){
			super.visitCallWithoutTypeArgs(ctx);
			ctx.extendedContext = new CallWithoutTypeArgsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitCallWithTypeArgs(P416Parser.CallWithTypeArgsContext ctx){
			super.visitCallWithTypeArgs(ctx);
			ctx.extendedContext = new CallWithTypeArgsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitEmptyStatement(P416Parser.EmptyStatementContext ctx){
			super.visitEmptyStatement(ctx);
			ctx.extendedContext = new EmptyStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitReturnStatement(P416Parser.ReturnStatementContext ctx){
			super.visitReturnStatement(ctx);
			ctx.extendedContext = new ReturnStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitExitStatement(P416Parser.ExitStatementContext ctx){
			super.visitExitStatement(ctx);
			ctx.extendedContext = new ExitStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitIfStatement(P416Parser.IfStatementContext ctx){
			super.visitIfStatement(ctx);
			ctx.extendedContext = new IfStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitIfElseStatement(P416Parser.IfElseStatementContext ctx){
			super.visitIfElseStatement(ctx);
			ctx.extendedContext = new IfElseStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDirectApplication(P416Parser.DirectApplicationContext ctx){
			super.visitDirectApplication(ctx);
			ctx.extendedContext = new DirectApplicationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStatement(P416Parser.StatementContext ctx){
			super.visitStatement(ctx);
			ctx.extendedContext = new StatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBlockStatement(P416Parser.BlockStatementContext ctx){
			super.visitBlockStatement(ctx);
			ctx.extendedContext = new BlockStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStatOrDeclList(P416Parser.StatOrDeclListContext ctx){
			super.visitStatOrDeclList(ctx);
			ctx.extendedContext = new StatOrDeclListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSwitchStatement(P416Parser.SwitchStatementContext ctx){
			super.visitSwitchStatement(ctx);
			ctx.extendedContext = new SwitchStatementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSwitchCases(P416Parser.SwitchCasesContext ctx){
			super.visitSwitchCases(ctx);
			ctx.extendedContext = new SwitchCasesContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSwitchCase(P416Parser.SwitchCaseContext ctx){
			super.visitSwitchCase(ctx);
			ctx.extendedContext = new SwitchCaseContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSwitchLabel(P416Parser.SwitchLabelContext ctx){
			super.visitSwitchLabel(ctx);
			ctx.extendedContext = new SwitchLabelContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStatementOrDeclaration(P416Parser.StatementOrDeclarationContext ctx){
			super.visitStatementOrDeclaration(ctx);
			ctx.extendedContext = new StatementOrDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTableDeclaration(P416Parser.TableDeclarationContext ctx){
			super.visitTableDeclaration(ctx);
			ctx.extendedContext = new TableDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTablePropertyList(P416Parser.TablePropertyListContext ctx){
			super.visitTablePropertyList(ctx);
			ctx.extendedContext = new TablePropertyListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitKey(P416Parser.KeyContext ctx){
			super.visitKey(ctx);
			ctx.extendedContext = new KeyContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitActions(P416Parser.ActionsContext ctx){
			super.visitActions(ctx);
			ctx.extendedContext = new ActionsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitConstEntries(P416Parser.ConstEntriesContext ctx){
			super.visitConstEntries(ctx);
			ctx.extendedContext = new ConstEntriesContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitLocalVariable(P416Parser.LocalVariableContext ctx){
			super.visitLocalVariable(ctx);
			ctx.extendedContext = new LocalVariableContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitLocalConstVariable(P416Parser.LocalConstVariableContext ctx){
			super.visitLocalConstVariable(ctx);
			ctx.extendedContext = new LocalConstVariableContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitKeyElementList(P416Parser.KeyElementListContext ctx){
			super.visitKeyElementList(ctx);
			ctx.extendedContext = new KeyElementListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitKeyElement(P416Parser.KeyElementContext ctx){
			super.visitKeyElement(ctx);
			ctx.extendedContext = new KeyElementContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitActionList(P416Parser.ActionListContext ctx){
			super.visitActionList(ctx);
			ctx.extendedContext = new ActionListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitEntriesList(P416Parser.EntriesListContext ctx){
			super.visitEntriesList(ctx);
			ctx.extendedContext = new EntriesListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitEntry(P416Parser.EntryContext ctx){
			super.visitEntry(ctx);
			ctx.extendedContext = new EntryContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitActionWithoutArgs(P416Parser.ActionWithoutArgsContext ctx){
			super.visitActionWithoutArgs(ctx);
			ctx.extendedContext = new ActionWithoutArgsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitActionWithArgs(P416Parser.ActionWithArgsContext ctx){
			super.visitActionWithArgs(ctx);
			ctx.extendedContext = new ActionWithArgsContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitActionDeclaration(P416Parser.ActionDeclarationContext ctx){
			super.visitActionDeclaration(ctx);
			ctx.extendedContext = new ActionDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitVariableDeclaration(P416Parser.VariableDeclarationContext ctx){
			super.visitVariableDeclaration(ctx);
			ctx.extendedContext = new VariableDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitConstantDeclaration(P416Parser.ConstantDeclarationContext ctx){
			super.visitConstantDeclaration(ctx);
			ctx.extendedContext = new ConstantDeclarationContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOptInitializer(P416Parser.OptInitializerContext ctx){
			super.visitOptInitializer(ctx);
			ctx.extendedContext = new OptInitializerContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitInitializer(P416Parser.InitializerContext ctx){
			super.visitInitializer(ctx);
			ctx.extendedContext = new InitializerContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitArgumentList(P416Parser.ArgumentListContext ctx){
			super.visitArgumentList(ctx);
			ctx.extendedContext = new ArgumentListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitArgument(P416Parser.ArgumentContext ctx){
			super.visitArgument(ctx);
			ctx.extendedContext = new ArgumentContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitExpressionList(P416Parser.ExpressionListContext ctx){
			super.visitExpressionList(ctx);
			ctx.extendedContext = new ExpressionListContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMember(P416Parser.MemberContext ctx){
			super.visitMember(ctx);
			ctx.extendedContext = new MemberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPrefixedNonTypeName(P416Parser.PrefixedNonTypeNameContext ctx){
			super.visitPrefixedNonTypeName(ctx);
			ctx.extendedContext = new PrefixedNonTypeNameContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPrefixedNonTypeNameLvalue(P416Parser.PrefixedNonTypeNameLvalueContext ctx){
			super.visitPrefixedNonTypeNameLvalue(ctx);
			ctx.extendedContext = new PrefixedNonTypeNameLvalueContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPrefixedNameLvalue(P416Parser.PrefixedNameLvalueContext ctx){
			super.visitPrefixedNameLvalue(ctx);
			ctx.extendedContext = new PrefixedNameLvalueContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitArrayIndexLvalue(P416Parser.ArrayIndexLvalueContext ctx){
			super.visitArrayIndexLvalue(ctx);
			ctx.extendedContext = new ArrayIndexLvalueContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitRangeIndexLvalue(P416Parser.RangeIndexLvalueContext ctx){
			super.visitRangeIndexLvalue(ctx);
			ctx.extendedContext = new RangeIndexLvalueContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitInteger(P416Parser.IntegerContext ctx){
			super.visitInteger(ctx);
			ctx.extendedContext = new IntegerContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTrue(P416Parser.TrueContext ctx){
			super.visitTrue(ctx);
			ctx.extendedContext = new TrueContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitFalse(P416Parser.FalseContext ctx){
			super.visitFalse(ctx);
			ctx.extendedContext = new FalseContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitString(P416Parser.StringContext ctx){
			super.visitString(ctx);
			ctx.extendedContext = new StringContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitNonType(P416Parser.NonTypeContext ctx){
			super.visitNonType(ctx);
			ctx.extendedContext = new NonTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPrefixedNonType(P416Parser.PrefixedNonTypeContext ctx){
			super.visitPrefixedNonType(ctx);
			ctx.extendedContext = new PrefixedNonTypeContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitArrayIndex(P416Parser.ArrayIndexContext ctx){
			super.visitArrayIndex(ctx);
			ctx.extendedContext = new ArrayIndexContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitRangeIndex(P416Parser.RangeIndexContext ctx){
			super.visitRangeIndex(ctx);
			ctx.extendedContext = new RangeIndexContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSet(P416Parser.SetContext ctx){
			super.visitSet(ctx);
			ctx.extendedContext = new SetContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOf(P416Parser.OfContext ctx){
			super.visitOf(ctx);
			ctx.extendedContext = new OfContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitNot(P416Parser.NotContext ctx){
			super.visitNot(ctx);
			ctx.extendedContext = new NotContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitNegate(P416Parser.NegateContext ctx){
			super.visitNegate(ctx);
			ctx.extendedContext = new NegateContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitUnaryMinus(P416Parser.UnaryMinusContext ctx){
			super.visitUnaryMinus(ctx);
			ctx.extendedContext = new UnaryMinusContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitUnaryPlus(P416Parser.UnaryPlusContext ctx){
			super.visitUnaryPlus(ctx);
			ctx.extendedContext = new UnaryPlusContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMemberAccess(P416Parser.MemberAccessContext ctx){
			super.visitMemberAccess(ctx);
			ctx.extendedContext = new MemberAccessContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitErrorMemberAccess(P416Parser.ErrorMemberAccessContext ctx){
			super.visitErrorMemberAccess(ctx);
			ctx.extendedContext = new ErrorMemberAccessContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx){
			super.visitExprMemberAccess(ctx);
			ctx.extendedContext = new ExprMemberAccessContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitStar(P416Parser.StarContext ctx){
			super.visitStar(ctx);
			ctx.extendedContext = new StarContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitSlah(P416Parser.SlahContext ctx){
			super.visitSlah(ctx);
			ctx.extendedContext = new SlahContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMod(P416Parser.ModContext ctx){
			super.visitMod(ctx);
			ctx.extendedContext = new ModContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPlus(P416Parser.PlusContext ctx){
			super.visitPlus(ctx);
			ctx.extendedContext = new PlusContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitMinus(P416Parser.MinusContext ctx){
			super.visitMinus(ctx);
			ctx.extendedContext = new MinusContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitShiftLeft(P416Parser.ShiftLeftContext ctx){
			super.visitShiftLeft(ctx);
			ctx.extendedContext = new ShiftLeftContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitShifRight(P416Parser.ShifRightContext ctx){
			super.visitShifRight(ctx);
			ctx.extendedContext = new ShifRightContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBitAnd(P416Parser.BitAndContext ctx){
			super.visitBitAnd(ctx);
			ctx.extendedContext = new BitAndContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBitXor(P416Parser.BitXorContext ctx){
			super.visitBitXor(ctx);
			ctx.extendedContext = new BitXorContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBitOr(P416Parser.BitOrContext ctx){
			super.visitBitOr(ctx);
			ctx.extendedContext = new BitOrContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitPlusPlus(P416Parser.PlusPlusContext ctx){
			super.visitPlusPlus(ctx);
			ctx.extendedContext = new PlusPlusContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitLessThanOrEqual(P416Parser.LessThanOrEqualContext ctx){
			super.visitLessThanOrEqual(ctx);
			ctx.extendedContext = new LessThanOrEqualContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitGreaterThanOrEqual(P416Parser.GreaterThanOrEqualContext ctx){
			super.visitGreaterThanOrEqual(ctx);
			ctx.extendedContext = new GreaterThanOrEqualContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitLessThan(P416Parser.LessThanContext ctx){
			super.visitLessThan(ctx);
			ctx.extendedContext = new LessThanContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitGreaterThan(P416Parser.GreaterThanContext ctx){
			super.visitGreaterThan(ctx);
			ctx.extendedContext = new GreaterThanContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitNotEqual(P416Parser.NotEqualContext ctx){
			super.visitNotEqual(ctx);
			ctx.extendedContext = new NotEqualContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitEqual(P416Parser.EqualContext ctx){
			super.visitEqual(ctx);
			ctx.extendedContext = new EqualContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitAnd(P416Parser.AndContext ctx){
			super.visitAnd(ctx);
			ctx.extendedContext = new AndContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOr(P416Parser.OrContext ctx){
			super.visitOr(ctx);
			ctx.extendedContext = new OrContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTernary(P416Parser.TernaryContext ctx){
			super.visitTernary(ctx);
			ctx.extendedContext = new TernaryContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitTempletizedFunctionCall(P416Parser.TempletizedFunctionCallContext ctx){
			super.visitTempletizedFunctionCall(ctx);
			ctx.extendedContext = new TempletizedFunctionCallContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitFunctionCall(P416Parser.FunctionCallContext ctx){
			super.visitFunctionCall(ctx);
			ctx.extendedContext = new FunctionCallContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitConstructor(P416Parser.ConstructorContext ctx){
			super.visitConstructor(ctx);
			ctx.extendedContext = new ConstructorContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitCast(P416Parser.CastContext ctx){
			super.visitCast(ctx);
			ctx.extendedContext = new CastContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitNumber(P416Parser.NumberContext ctx){
			super.visitNumber(ctx);
			ctx.extendedContext = new NumberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitDecimalNumber(P416Parser.DecimalNumberContext ctx){
			super.visitDecimalNumber(ctx);
			ctx.extendedContext = new DecimalNumberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitOctalNumber(P416Parser.OctalNumberContext ctx){
			super.visitOctalNumber(ctx);
			ctx.extendedContext = new OctalNumberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitBinaryNumber(P416Parser.BinaryNumberContext ctx){
			super.visitBinaryNumber(ctx);
			ctx.extendedContext = new BinaryNumberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitHexNumber(P416Parser.HexNumberContext ctx){
			super.visitHexNumber(ctx);
			ctx.extendedContext = new HexNumberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitRealNumber(P416Parser.RealNumberContext ctx){
			super.visitRealNumber(ctx);
			ctx.extendedContext = new RealNumberContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitUnaryExpression_not(P416Parser.UnaryExpression_notContext ctx) {
			super.visitUnaryExpression_not(ctx);
			ctx.extendedContext = new UnaryExpression_notContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitUnaryExpression_tilda(P416Parser.UnaryExpression_tildaContext ctx) {
			super.visitUnaryExpression_tilda(ctx);
			ctx.extendedContext = new UnaryExpression_tildaContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitUnaryExpression_plus(P416Parser.UnaryExpression_plusContext ctx) {
			super.visitUnaryExpression_plus(ctx);
			ctx.extendedContext = new UnaryExpression_plusContextExt(ctx);
			
			return ctx;
		}
		@Override 
		public ParserRuleContext visitUnaryExpression_minus(P416Parser.UnaryExpression_minusContext ctx) {
			super.visitUnaryExpression_minus(ctx);
			ctx.extendedContext = new UnaryExpression_minusContextExt(ctx);
			
			return ctx;
		}
;






}
