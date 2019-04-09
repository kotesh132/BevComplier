package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.FunctionCallContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.ExternFunctionDeclarationContextExt;
import com.p4.p416.gen.ExternObjectDeclarationContextExt;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.HeaderStackTypeContextExt;
import com.p4.p416.gen.HeaderTypeDeclarationContextExt;
import com.p4.p416.gen.MethodPrototypeContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.ActionListContext;
import com.p4.p416.gen.P416Parser.ActionRefContext;
import com.p4.p416.gen.P416Parser.ActionWithArgsContext;
import com.p4.p416.gen.P416Parser.ActionWithoutArgsContext;
import com.p4.p416.gen.P416Parser.ArgumentContext;
import com.p4.p416.gen.P416Parser.ArrayIndexContext;
import com.p4.p416.gen.P416Parser.ArrayIndexLvalueContext;
import com.p4.p416.gen.P416Parser.CastContext;
import com.p4.p416.gen.P416Parser.ConstEntriesContext;
import com.p4.p416.gen.P416Parser.ControlBodyContext;
import com.p4.p416.gen.P416Parser.ControlLocalDeclarationContext;
import com.p4.p416.gen.P416Parser.DeclarationContext;
import com.p4.p416.gen.P416Parser.DirectionContext;
import com.p4.p416.gen.P416Parser.ExternObjectDeclarationContext;
import com.p4.p416.gen.P416Parser.FunctionCallContext;
import com.p4.p416.gen.P416Parser.HeaderTypeDeclarationContext;
import com.p4.p416.gen.P416Parser.KeyContext;
import com.p4.p416.gen.P416Parser.MemberAccessContext;
import com.p4.p416.gen.P416Parser.MethodPrototypeContext;
import com.p4.p416.gen.P416Parser.NonTypeNameContext;
import com.p4.p416.gen.P416Parser.PackageTypeDeclarationContext;
import com.p4.p416.gen.P416Parser.ParameterContext;
import com.p4.p416.gen.P416Parser.ParserDeclarationContext;
import com.p4.p416.gen.P416Parser.ParserTypeDeclarationContext;
import com.p4.p416.gen.P416Parser.PrefixedNameLvalueContext;
import com.p4.p416.gen.P416Parser.PrefixedNonTypeNameLvalueContext;
import com.p4.p416.gen.P416Parser.RangeIndexLvalueContext;
import com.p4.p416.gen.P416Parser.SimpleKeySetExpressionContext;
import com.p4.p416.gen.P416Parser.StructFieldContext;
import com.p4.p416.gen.P416Parser.TempletizedFunctionCallContext;
import com.p4.p416.gen.P416Parser.TypeArgContext;
import com.p4.p416.gen.P416Parser.TypeOrVoidContext;
import com.p4.p416.gen.PackageTypeDeclarationContextExt;
import com.p4.p416.gen.ParserDeclarationContextExt;
import com.p4.p416.gen.ParserTypeDeclarationContextExt;
import com.p4.p416.gen.StructTypeDeclarationContextExt;
import com.p4.p416.gen.TempletizedFunctionCallContextExt;
import com.p4.p416.gen.VarBitSizeTypeContextExt;

public class SymbolChecksVisitor extends P416BaseVisitor<ParserRuleContext> {
	private static final Logger L = LoggerFactory.getLogger(SymbolChecksVisitor.class);	
	
	private boolean insideParserState = false;
	private boolean insideActionDeclaration = false;
	private boolean insideControlDeclaration = false;
	private boolean constEntriesInTableVisited = false;
	private boolean insideControlBody = false;
	private boolean keyInTableVisited = false;
	
	List<String> controlParamNamesList = new ArrayList<String>();
	String currentControlBlockName;

	
	@Override
	public ParserRuleContext visitAssignmentStatement(P416Parser.AssignmentStatementContext ctx){
        try{
        	super.visitAssignmentStatement(ctx);
        	String lValueString = AbstractBaseExt.getExtendedContext(ctx.lvalue()).getFormattedText();
            if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER) || lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
            	lValueString = lValueString.substring(0, lValueString.lastIndexOf("."));
            }
            else if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_INDEX) || lValueString.endsWith("."+HeaderStackTypeContextExt.HEADER_SIZE)){
            	lValueString = lValueString.substring(0, lValueString.lastIndexOf("."));
            	Symbol lSymbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueString);
        		//Double resolve is needed to determine the type of header in some cases
            	if (lSymbol!=null && "header".equals(lSymbol.getType().getTypeName()) || "header".equals(AbstractBaseExt.getExtendedContext(ctx).resolve(lSymbol.getType().getTypeName()).getTypeName()))
        			L.error("Line:"+ctx.start.getLine()+": cannot be the target of an assignment: "+AbstractBaseExt.getExtendedContext(ctx.lvalue()).getFormattedText());
            	
            }
            Symbol lValueSymbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueString);
            Type lValueType = lValueSymbol.getType();
            Type rValueType = AbstractBaseExt.getExtendedContext(ctx.expression()).getType();
            
            if ("extern".equals(lValueType.getTypeName())){ //Either check for more specific types or generalize going forward
            	L.error("Line:"+ctx.start.getLine()+": "+" LHS of Assignment Statement: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+" can't be extern");
            	return ctx;
            }
            if (rValueType == null){
	            L.error("Line:"+ctx.start.getLine()+": "+" Indeterminate Assignment Statement: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
            	return ctx;
            }	
            if (!AbstractBaseExt.getExtendedContext(ctx).isTypeCompatible(lValueType,rValueType)){
		            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx.lvalue()).getFormattedText()+ "' is incompatible with '"+AbstractBaseExt.getExtendedContext(ctx.expression()).getFormattedText()+"'");
            }
        }catch(SymbolNotDefinedException e){
            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx.lvalue()).getFormattedText()+"' can not be resolved");
        }
        return ctx;
	}
	
	@Override
	public ParserRuleContext visitExprMemberAccess(P416Parser.ExprMemberAccessContext ctx){
//		try{
//			String text = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
//			if (text.endsWith("."+HeaderTypeDeclarationContextExt.IS_VALID) || text.endsWith("."+HeaderTypeDeclarationContextExt.SET_VALID) || text.endsWith("."+HeaderTypeDeclarationContextExt.SET_INVALID)){
//				text = text.substring(0, text.lastIndexOf("."));
//				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(text);
//				if ("header".equals(symbol.getType().getTypeName()))
//					return ctx;
//			}
//			if ((text.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER) || text.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER))){
//				text = text.substring(0, text.lastIndexOf("."));
//				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(text);
//				if ("header".equals(symbol.getType().getTypeName())){
//				if (insideParserDeclaration){
//					return ctx;
//				}
//				else{
//					L.error("Line:"+ctx.start.getLine()+": "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+": Operations like next and last should be used only in parser declaration");
//				}
//				}
//			}
//			if (text.endsWith("."+HeaderStackTypeContextExt.HEADER_SIZE) || text.endsWith("."+HeaderStackTypeContextExt.LAST_INDEX) || text.endsWith("."+HeaderStackTypeContextExt.PUSH_FRONT) || text.endsWith("."+HeaderStackTypeContextExt.POP_FRONT)){
//				text = text.substring(0, text.lastIndexOf("."));
//				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(text);
//				if ("header".equals(symbol.getType().getTypeName())){
//					return ctx;
//				}
//			}
////			AbstractBaseExt.getExtendedContext(ctx).resolve(text);
//		}
		try{
			if (!(ctx.expression() instanceof CastContext)){
				AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}
			else{
			super.visitExprMemberAccess(ctx);
			}
		}
		catch(SymbolNotDefinedException e){
			L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' can not be resolved");
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitVariableDeclaration(P416Parser.VariableDeclarationContext ctx){
        try{
        	super.visitVariableDeclaration(ctx);
        	
        	Symbol lValueSymbol = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx.name()).getFormattedText());
        	Type lValueType = lValueSymbol.getType();
        	
        	if ("BitSizeType".equals(lValueType.getTypeName()) || "IntSizeType".equals(lValueType.getTypeName())){
        		int size = lValueType.getTypeSize();
        		//This width should be declarable
        		if (size>1024){
        			L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' width too large");
        		}
        	}
        	
        	if (ctx.optInitializer()!=null && ctx.optInitializer().initializer()!=null){
        		Type rValueType = AbstractBaseExt.getExtendedContext(ctx.optInitializer().initializer().expression()).getType();

        		if (rValueType == null){
        			L.error("Line:"+ctx.start.getLine()+": "+" Indeterminate Assignment Statement: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
        			return ctx;
        		}	
            
        		if (!lValueType.isEquivalenceCompatible((rValueType))){
        			L.error("Line:"+ctx.start.getLine()+": '"+ AbstractBaseExt.getExtendedContext(ctx.typeRef()).getFormattedText()+ "' is incompatible with '"+ctx.optInitializer().initializer().expression().extendedContext.getFormattedText()+"'");
        		}
        	}
        	
        	if (insideControlLocalDeclaration){
        		if (!isBaseType(AbstractBaseExt.getExtendedContext(ctx.typeRef()).getType().getTypeName()) && !((AbstractBaseExt.getExtendedContext(ctx.typeRef()).getFormattedText())).startsWith("tuple") ){
        			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx.typeRef()).getFormattedText());
        			if (symbol!=null && "extern".equals(symbol.getType().getTypeName())){
        				L.error("Line:"+ctx.start.getLine()+": Cannot declare variables of type: "+AbstractBaseExt.getExtendedContext(ctx.typeRef()).getType().getTypeName()+" (Consider using an instantiation)");
        			}
        		}
        	}
        	
        	//header type based declaration can't be typed
        	String typeName = AbstractBaseExt.getExtendedContext(ctx.typeRef()).getType().getTypeName();
        	if (!isBaseType(typeName)){
        		Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(typeName);
        		if (s instanceof HeaderTypeDeclarationContextExt){
        			if (ctx.typeRef().extendedContext.isSpecializedType()){
        				L.error("Line:"+ctx.start.getLine()+": Header type based declaration can't be typed: "+ctx.typeRef().extendedContext.getFormattedText());
        			}
        		}
        		else if (s instanceof StructTypeDeclarationContextExt && lValueType instanceof HeaderStackTypeContextExt){
    				L.error("Line:"+ctx.start.getLine()+": Struct illegal in header stack: "+ctx.typeRef().extendedContext.getFormattedText());
        		}
        	}
        	
        	
        }catch(SymbolNotDefinedException e){
            L.error("Line:"+ctx.start.getLine()+": '"+ctx.typeRef().extendedContext.getFormattedText()+"' can not be resolved");
        }
        return ctx;
	}
	
	@Override
	public ParserRuleContext visitStatementOrDeclaration(P416Parser.StatementOrDeclarationContext ctx){
		super.visitStatementOrDeclaration(ctx);
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitConstantDeclaration(P416Parser.ConstantDeclarationContext ctx){
        try{
        	super.visitConstantDeclaration(ctx);

        	Type lValueType = ctx.typeRef().extendedContext;
        	
        	if ("BitSizeType".equals(lValueType.getTypeName()) || "IntSizeType".equals(lValueType.getTypeName())){
        		try{
        		int size = lValueType.getTypeSize();
        		}
        		catch(Throwable e){
        			L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' width too large");
        		}
        	}
        	if (ctx.initializer()!=null){
            Type rValueType = ctx.initializer().expression().extendedContext.getType();

            if (rValueType == null){
	            L.error("Line:"+ctx.start.getLine()+": "+" Indeterminate Assignment Statement: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
            	return ctx;
            }	
            
            //Illegal to declare extern as constants
            String typeName = lValueType.getTypeName();
            if (!(("BitSizeType".equals(typeName)) || ("IntSizeType".equals(typeName)) || ("BoolType".equals(typeName)) ||  ("BitType".equals(typeName)))){
            	Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(typeName);
            	if (symbol!=null && "extern".equals(symbol.getTypeName())){
            		L.error("Line:"+ctx.start.getLine()+": "+" Illegal constants of extern types: "+typeName);
            		return ctx;
            	}
            }
            
            Symbol lSymbol = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.name().extendedContext.getFormattedText());
            if (lSymbol!=null){
            Type lSymbolType = lSymbol.getType();

            if (!AbstractBaseExt.getExtendedContext(ctx).isTypeCompatible(lSymbolType,rValueType)){
		            L.error("Line:"+ctx.start.getLine()+": '"+ctx.typeRef().extendedContext.getFormattedText()+ "' is incompatible with '"+ctx.initializer().expression().extendedContext.getFormattedText()+"'");
            }
            }
        	}
        }catch(SymbolNotDefinedException e){
            L.error("Line:"+ctx.start.getLine()+": '"+ctx.typeRef().extendedContext.getFormattedText()+"' can not be resolved");
        }
        return ctx;
	}

	@Override
	public ParserRuleContext visitCallWithoutTypeArgs(P416Parser.CallWithoutTypeArgsContext ctx){
		super.visitCallWithoutTypeArgs(ctx);		
		try{
			String lValueString = ctx.lvalue().extendedContext.getFormattedText();
			if ((lValueString.endsWith("."+HeaderTypeDeclarationContextExt.IS_VALID)) || (lValueString.endsWith("."+HeaderTypeDeclarationContextExt.SET_INVALID))
			 || (lValueString.endsWith("."+HeaderTypeDeclarationContextExt.SET_VALID))){
	        	String lValueStringPre = lValueString.substring(0, lValueString.lastIndexOf("."));
	        	Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringPre);
	        	if (symbol!=null && "header".equals(symbol.getType().getTypeName()))
	        		return ctx;
	        	else{
	        		Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(symbol.getType().getTypeName());
	        		//Double resolve is needed to determine the type of header in some cases
	        		if (s!=null &&  "header".equals(s.getTypeName()))
	        			L.error("Line:"+ctx.start.getLine()+": isValid/setValid/setInvalid is defined only on header type "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					return ctx;
	        	}
			}
			else if ((lValueString.endsWith("."+HeaderStackTypeContextExt.PUSH_FRONT)) || (lValueString.endsWith("."+HeaderStackTypeContextExt.POP_FRONT))){
				String lValueStringPre = lValueString.substring(0, lValueString.lastIndexOf("."));
				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringPre);
				//Double resolve is needed to determine the type of header in some cases
				if (symbol!=null && ("header".equals(symbol.getType().getTypeName()) || "header".equals(AbstractBaseExt.getExtendedContext(ctx).resolve(symbol.getType().getTypeName()).getTypeName()))){
					for (ArgumentContext arg:ctx.argumentList().argument()){
						Type type = arg.expression().extendedContext.getType();
						if (!type.getIsConstant()){
							L.error("Line:"+ctx.start.getLine()+": '"+arg.expression().extendedContext.getFormattedText()+"': argument must be a constant in push_front");
						}
					}
	        		return ctx;
				}
	        	else{
					L.error("Line:"+ctx.start.getLine()+": isValid/setValid/setInvalid is defined only on header type "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					return ctx;
	        	}
			}
			else if (lValueString.endsWith(".emit")){
				List<ArgumentContext> argList = ctx.argumentList().argument();
				for (ArgumentContext arg:argList){
					String emitFrom = arg.expression().extendedContext.getFormattedText();
					try{
					Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(emitFrom);
					Symbol s2 = AbstractBaseExt.getExtendedContext(ctx).resolve(s.getTypeName());
					if ("header".equals(s2.getTypeName())){
						if (emitFrom.contains(".")){
							String prefix = emitFrom.substring(0,emitFrom.lastIndexOf("."));
							Symbol s3 = AbstractBaseExt.getExtendedContext(ctx).resolve(prefix);
							if("struct".equals(s3.getTypeName())){
								L.error("Line:"+ctx.start.getLine()+": Cannot extract field: "+emitFrom+" from struct type: "+prefix);
							}
						}
					}
					}catch(SymbolNotDefinedException e){
						L.error("Line:"+ctx.start.getLine()+": '"+emitFrom+"' can not be resolved");
					}
				}
				
			}
			if (!"verify".equals(lValueString)){
				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.lvalue().extendedContext.getFormattedText());
				Type type = symbol.getType();
				if (type instanceof ExternFunctionDeclarationContextExt){
					ExternFunctionDeclarationContextExt externFuncDeclType = (ExternFunctionDeclarationContextExt)type;
					
					//No Arguments in both callee and caller hence safe
					if (ctx.argumentList()==null && externFuncDeclType.getContext().functionPrototype().parameterList()==null){
						return ctx;
					}
					else if (ctx.argumentList()==null){
						//caller has no arguments but callee has
						L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						//return ctx;
					}
					else if (externFuncDeclType.getContext().functionPrototype().parameterList()==null){
						//callee has no arguments but caller has
						L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						//return ctx;
					}
					
					if (!(ctx.argumentList()==null || externFuncDeclType.getContext().functionPrototype().parameterList()==null)){
					List<ArgumentContext> argumentListOfCaller = ctx.argumentList().argument();
					List<ParameterContext> listOfParamsOfCallee = externFuncDeclType.getContext().functionPrototype().parameterList().parameter();

					TypeOrVoidContext typeOrVoidOfCallee = externFuncDeclType.getContext().functionPrototype().typeOrVoid();
					
					int callerArgSize = argumentListOfCaller.size();
					int calleeParamSize = listOfParamsOfCallee.size();
					
					
					//both types of listOfParamsOfCallee and argumentListOfCaller should be same
					//TODO: are we supporting function overloading?
					
					if (calleeParamSize!=callerArgSize){
						L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						//return ctx;
					}
					else {
						//check if there are generics in the actual function definition
						List<NonTypeNameContext> listOfGenericTypes = null;
						List<String> genericTypeStrings = null;
						if (externFuncDeclType.getContext().functionPrototype().optTypeParameters()!=null){
							listOfGenericTypes = externFuncDeclType.getContext().functionPrototype().optTypeParameters().typeParameterList().nonTypeName();

						}
						for (int i=0;i<callerArgSize && i<calleeParamSize;i++){
						Type type1 = argumentListOfCaller.get(i).extendedContext.getType();
						Type type2 = listOfParamsOfCallee.get(i).extendedContext.getType();
						
						if (!isBaseType(type1.getTypeName())){
						Symbol type1Symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(argumentListOfCaller.get(i).extendedContext.getFormattedText());
						String type1TypeName = type1Symbol.getTypeName();
						if (!isBaseType(type1TypeName)){
							Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(type1TypeName);
							//Generic resolves to the same name. TODO: May be we can redesign to accommodate is a symbol is a generic
							//Checking if it the callee argument is a generic type and the called param is actually base type
							if (s.getTypeName().equals(type1TypeName) && isBaseType(type2.getTypeName())){
								L.error("Line:"+ctx.start.getLine()+": Can't unify "+s.getTypeName()+" to "+type2.getTypeName());

							}
						}
						}
						
						
						
						//Check if it is actually a generic function called -start
						if (listOfGenericTypes!=null){
							if ("IntegerLiteral".equals(type1.getTypeName())){
								for (NonTypeNameContext genericType:listOfGenericTypes){
									if (genericType.extendedContext.getFormattedText().equals(type2.getTypeName())){
										L.error("Line:"+ctx.start.getLine()+": Cannot infer bitwidth for integer-valued type parameter : "+genericType.getText());
									}
								}
							}
							else{
								genericTypeStrings = new ArrayList<String>();
								for (NonTypeNameContext genericType:listOfGenericTypes){
									genericTypeStrings.add(genericType.getText());
								}
								if (!genericTypeStrings.contains(type2.getTypeName()) && !this.isBaseType(type2.getTypeName())){
									try{
										AbstractBaseExt.getExtendedContext(ctx).resolve(type2.getTypeName());
									}
									catch(SymbolNotDefinedException e){
										L.error("Line:"+ctx.start.getLine()+": Unknown param type of the function : "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+" : "+type2.getTypeName());
									}
								}
							}
						}
						//Check if it is actually a generic function called -end						
						if (genericTypeStrings!=null && !genericTypeStrings.contains(type2.getTypeName()) && !type2.isEquivalenceCompatible(type1)){
							L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
							break;
						}
						
						ArgumentContext rawArgType = argumentListOfCaller.get(i);
						ParameterContext rawParamType = listOfParamsOfCallee.get(i);
						if (rawParamType.direction()!=null && "out".equals(rawParamType.direction().extendedContext.getFormattedText())){
							try{
							Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(rawArgType.extendedContext.getFormattedText());
							}catch(SymbolNotDefinedException e){
								L.error("Line:"+ctx.start.getLine()+": Either symbol is not defined or non lvalue is passed to an 'out' param of the function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
								break;
							}
						}
					}
					}
					}
					return ctx;
				}
				else if (type instanceof MethodPrototypeContextExt){
					//TODO: currently just checking the number of arguments in caller and the definition. In future, we have to check the types of arguments too
					MethodPrototypeContextExt method = (MethodPrototypeContextExt) type;
					int numOfParamsInMethodDefinition = 0;
					int numOfParamsInMethodCall = 0;
					if (method.getContext().functionPrototype()!=null){
						if (method.getContext().functionPrototype().parameterList()==null){
							numOfParamsInMethodDefinition = 0;
						}else{
							List<ParameterContext> paramList = method.getContext().functionPrototype().parameterList().parameter();
							numOfParamsInMethodDefinition = paramList.size();
						}
					}else{
						if (method.getContext().parameterList()==null){
							numOfParamsInMethodDefinition = 0;
						}else{
							List<ParameterContext> paramList = method.getContext().parameterList().parameter();
							numOfParamsInMethodDefinition = paramList.size();
						}
					}
					
					if (ctx.argumentList()==null){
						numOfParamsInMethodCall = 0;
					}else{
						numOfParamsInMethodCall = ctx.argumentList().argument().size();
					}
					
					if (numOfParamsInMethodDefinition!=numOfParamsInMethodCall){
						L.error("Line:"+ctx.start.getLine()+": There is no method with same number of arguments: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					}
				}
			}
			else{
				if (!insideParserState && "verify".equals(lValueString)){
					L.error("Line:"+ctx.start.getLine()+": verify: may only be invoked in parsers");
				}
			}
		}
		catch(SymbolNotDefinedException e){
			L.error("Line:"+ctx.start.getLine()+": '"+ctx.lvalue().extendedContext.getFormattedText()+"' can not be resolved");
		}
		
		String lValue = ctx.lvalue().extendedContext.getFormattedText();
		
		if (lValue.endsWith("."+HeaderStackTypeContextExt.PUSH_FRONT)){
			for (ArgumentContext arg:ctx.argumentList().argument()){
				Type type = arg.expression().extendedContext.getType();
				if (!type.getIsConstant()){
					L.error("Line:"+ctx.start.getLine()+": '"+arg.expression().extendedContext.getFormattedText()+"': argument must be a constant in push_front");
				}
			}
		}
		return ctx;
	}

@Override
	public ParserRuleContext visitCallWithTypeArgs(P416Parser.CallWithTypeArgsContext ctx){
		super.visitCallWithTypeArgs(ctx);
		try{
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.lvalue().extendedContext.getFormattedText());
			Type type = symbol.getType();
			if (type instanceof ExternFunctionDeclarationContextExt){
				ExternFunctionDeclarationContextExt externFuncDeclType = (ExternFunctionDeclarationContextExt)type;
				
				//No Arguments in both callee and caller hence safe
				if (ctx.argumentList()==null && externFuncDeclType.getContext().functionPrototype().parameterList()==null){
					if (ctx.typeArgumentList() == null && externFuncDeclType.getContext().functionPrototype().optTypeParameters().typeParameterList()==null){
						return ctx;
					}
				}
				else if (ctx.argumentList()==null){
					//caller has no arguments but callee has
					L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					//return ctx;
				}
				else if (externFuncDeclType.getContext().functionPrototype().parameterList()==null){
					//callee has no arguments but caller has
					L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					//return ctx;
				}
				
				if (!(ctx.argumentList()==null || externFuncDeclType.getContext().functionPrototype().parameterList()==null)){
				List<ArgumentContext> argumentListOfCaller = ctx.argumentList().argument();
				List<ParameterContext> listOfParamsOfCallee = externFuncDeclType.getContext().functionPrototype().parameterList().parameter();

				TypeOrVoidContext typeOrVoidOfCallee = externFuncDeclType.getContext().functionPrototype().typeOrVoid();
				
				int callerArgSize = argumentListOfCaller.size();
				int calleeParamSize = listOfParamsOfCallee.size();
				
				
				//both types of listOfParamsOfCallee and argumentListOfCaller should be same
				//TODO: are we supporting function overloading?
				
				if (calleeParamSize!=callerArgSize){
					L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					//return ctx;
				}
				else {
					for (int i=0;i<callerArgSize && i<calleeParamSize;i++){
					//TODO: Do we need resolve here instead of getting type.
					Type type1 = argumentListOfCaller.get(i).extendedContext.getType();
					Type type2 = listOfParamsOfCallee.get(i).extendedContext.getType();
					if (!type2.isEquivalenceCompatible(type1)){
						L.error("Line:"+ctx.start.getLine()+": Arguments mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						break;
					}
				}
				}
				}
				
				
				
				//Now check the typed params
				
				if (ctx.typeArgumentList() == null && externFuncDeclType.getContext().functionPrototype().optTypeParameters().typeParameterList()==null){
					return ctx;
				}
				else if (ctx.typeArgumentList()==null){
					//caller has no arguments but callee has
					L.error("Line:"+ctx.start.getLine()+": Type Parameters mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					//return ctx;
				}
				else if (externFuncDeclType.getContext().functionPrototype().optTypeParameters()==null){
					//callee has no arguments but caller has
					L.error("Line:"+ctx.start.getLine()+": Type Parameters mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					//return ctx;
				}
				
				if (!(ctx.typeArgumentList()==null || externFuncDeclType.getContext().functionPrototype().optTypeParameters()==null)){
				List<TypeArgContext> typeArgListOfCaller = ctx.typeArgumentList().typeArg();
				List<NonTypeNameContext> typeParamListOfCallee = externFuncDeclType.getContext().functionPrototype().optTypeParameters().typeParameterList().nonTypeName();
				
				int callerTypeArgSize = typeArgListOfCaller.size();
				int calleeTypeParamSize = typeParamListOfCallee.size();
				
				if (callerTypeArgSize!=calleeTypeParamSize){
					L.error("Line:"+ctx.start.getLine()+": Type Parameters mismatch in caller and callee function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					//return ctx;
				}else{
					for (int i=0;i<callerTypeArgSize && i<calleeTypeParamSize;i++){
					Type type1 = typeArgListOfCaller.get(i).extendedContext.getType();
					Type type2 = typeParamListOfCallee.get(i).extendedContext.getType();
					if (!type1.isEquivalenceCompatible(type2)){
						L.error("Line:"+ctx.start.getLine()+": Type Parameters mismatch in callee and caller function: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						break;
					}
				}
				}
				}
				
				//Check if any of the typed params are called with Integer based value
				if (!(ctx.argumentList()==null || externFuncDeclType.getContext().functionPrototype().parameterList()==null)){
					List<ArgumentContext> argumentListOfCaller = ctx.argumentList().argument();
					List<ParameterContext> listOfParamsOfCallee = externFuncDeclType.getContext().functionPrototype().parameterList().parameter();
					if (!(ctx.typeArgumentList()==null || externFuncDeclType.getContext().functionPrototype().optTypeParameters()==null)){
						List<TypeArgContext> typeArgListOfCaller = ctx.typeArgumentList().typeArg();
						List<NonTypeNameContext> typeParamListOfCallee = externFuncDeclType.getContext().functionPrototype().optTypeParameters().typeParameterList().nonTypeName();
						int i=0;
						for (ParameterContext param:listOfParamsOfCallee){
							for(NonTypeNameContext typeParam:typeParamListOfCallee){
								if (typeParam.extendedContext.getFormattedText().equals(param.typeRef().extendedContext.getFormattedText())){
									if ("IntegerLiteral".equals(argumentListOfCaller.get(i).extendedContext.getType().getType())){
										L.debug("Typed Params called with Integer based value!");//Revisit here
									}
								}
							}
							i++;
						}
					}
				}
				
				
				return ctx;
			}
			else if (type instanceof MethodPrototypeContextExt){
				//TODO: currently just checking the number of arguments in caller and the definition. In future, we have to check the types of arguments too
				//Also, checking of type params conditions have to be checked here too (like the ones present in "if case":if (type instanceof ExternFunctionDeclarationContextExt). In next checkin, will integrate it
				MethodPrototypeContextExt method = (MethodPrototypeContextExt) type;
				int numOfParamsInMethodDefinition = 0;
				int numOfParamsInMethodCall = 0;
				if (method.getContext().functionPrototype()!=null){
					if (method.getContext().functionPrototype().parameterList()==null){
						numOfParamsInMethodDefinition = 0;
					}else{
						List<ParameterContext> paramList = method.getContext().functionPrototype().parameterList().parameter();
						numOfParamsInMethodDefinition = paramList.size();
					}
				}else{
					if (method.getContext().parameterList()==null){
						numOfParamsInMethodDefinition = 0;
					}else{
						List<ParameterContext> paramList = method.getContext().parameterList().parameter();
						numOfParamsInMethodDefinition = paramList.size();
					}
				}
				
				if (ctx.argumentList()==null){
					numOfParamsInMethodCall = 0;
				}else{
					numOfParamsInMethodCall = ctx.argumentList().argument().size();
				}
				
				if (numOfParamsInMethodDefinition!=numOfParamsInMethodCall){
					L.error("Line:"+ctx.start.getLine()+": There is no method with same number of arguments: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
				}
			}
		}
		catch(SymbolNotDefinedException e){
			L.error("Line:"+ctx.start.getLine()+": '"+ctx.lvalue().extendedContext.getFormattedText()+"' can not be resolved");
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitExtractMethodCall(P416Parser.ExtractMethodCallContext ctx){
		super.visitExtractMethodCall(ctx);
		try{	
			AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.lvalue().extendedContext.getFormattedText());
			if (ctx.argumentList()!=null){
				List<ArgumentContext> argList = ctx.argumentList().argument();		
				if (argList.size()==1){
					ArgumentContext arg=argList.get(0);
					if (!(arg.expression().extendedContext.getType() instanceof HeaderTypeDeclarationContextExt)){
						L.error("Line:"+ctx.start.getLine()+": extract method invoked with non header type argument: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					}else{
						HeaderTypeDeclarationContextExt h = (HeaderTypeDeclarationContextExt)(arg.expression().extendedContext.getType());
						if (h.getTypeSize()==VarBitSizeTypeContextExt.SIZE_INDETERMINATE){
							L.error("Line:"+ctx.start.getLine()+": extract method can't be invoked with header having varbit type: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						}
					}
				}
			}
		}
		catch(SymbolNotDefinedException e){
			L.error("Line:"+ctx.start.getLine()+": '"+ctx.lvalue().extendedContext.getFormattedText()+"' can not be resolved");
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitApplyMethodCall(P416Parser.ApplyMethodCallContext ctx){
		if (insideActionDeclaration){
			L.error("Line:"+ctx.start.getLine()+": apply statement can not be inside action declaration: '"+ctx.lvalue().extendedContext.getFormattedText()+".apply()'");
		}
		super.visitApplyMethodCall(ctx);
		try{
			AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.lvalue().extendedContext.getFormattedText());
		}
		catch(SymbolNotDefinedException e){
			L.error("Line:"+ctx.start.getLine()+": '"+ctx.lvalue().extendedContext.getFormattedText()+"' can not be resolved");
		}
		return ctx;
	}
	
	@Override 
	public ParserRuleContext visitNonTypeName(P416Parser.NonTypeNameContext ctx){
	 	try{
	 		if (!"verify".equals(AbstractBaseExt.getExtendedContext(ctx).getFormattedText()))
	 		AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
	 	}catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' can not be resolved");		
	 	}
	 	return ctx;
	 }
	
	@Override
	public ParserRuleContext visitName(P416Parser.NameContext ctx){
		String name = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
		if (AbstractBaseExt.getExtendedContext(ctx).getSemanticChecksPass().reservedKeywords.contains(name)){
			L.error("Line:"+ctx.start.getLine()+": '"+name+ " is a reserved p4 keyword");
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitStateExpression(P416Parser.StateExpressionContext ctx){
		try{
		super.visitStateExpression(ctx);
			if (ctx.name()!=null){
				if (!"accept".equals(ctx.name().extendedContext.getFormattedText()) && !"reject".equals(ctx.name().extendedContext.getFormattedText())){//since accept is valid state to be transitioned to
					AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.name().extendedContext.getFormattedText());
				}
			}
		}
		catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' cans not be resolved");		
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitSelectCase(P416Parser.SelectCaseContext ctx){
		try{
			super.visitSelectCase(ctx);
			if (!"accept".equals(ctx.name().extendedContext.getFormattedText()) && !"reject".equals(ctx.name().extendedContext.getFormattedText())){//since accept is valid state to be transitioned to
				AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.name().extendedContext.getFormattedText());
			}
		}
		catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+ctx.name().extendedContext.getFormattedText()+"' can not be resolved");		
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitTypeName(P416Parser.TypeNameContext ctx){
		try{
			super.visitTypeName(ctx);
			AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
		}
		catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' can not be resolved");		
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitInstantiation(P416Parser.InstantiationContext ctx){
		super.visitName(ctx.name());
		if (ctx.argumentList()!=null){
			super.visitArgumentList(ctx.argumentList());
		}
			
		//Self Control Block references not allowed
		if (insideControlDeclaration && currentControlBlockName.equals(ctx.typeRef().extendedContext.getFormattedText())){
			L.error("Line:"+ctx.start.getLine()+": Self Reference of control blocks not allowed");
		}
		
		//extern can't be instantiated in control blocks
		

		//This ensures that the typeRef name (e.g., V1Switch) given by p4 developer isn't wrongly flagged as unresolved if the instance name specified in the declaration is main
		if (!("main".equalsIgnoreCase(ctx.name().extendedContext.getFormattedText()))){
			super.visitTypeRef(ctx.typeRef());
			//Struct instantiations not allowed
			try{
			Symbol sym = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.typeRef().extendedContext.getTypeName());
			if ("struct".equals(sym.getTypeName())){
				L.error("Line:"+ctx.start.getLine()+": structs have no constructors. struct:"+ctx.typeRef().extendedContext.getFormattedText());
			} else if ("extern".equals(sym.getTypeName())){
				ExternObjectDeclarationContext externObjectInstance = ((ExternObjectDeclarationContextExt)(sym.getType())).getContext();
				List<NonTypeNameContext> generictypeParams = null;
				List<String> generictypeParamStrings = new ArrayList<String>();
				if (externObjectInstance.optTypeParameters()!=null){
					generictypeParams = externObjectInstance.optTypeParameters().typeParameterList().nonTypeName();
					for (NonTypeNameContext genericNonTypeName:generictypeParams){
						generictypeParamStrings.add(genericNonTypeName.extendedContext.getFormattedText());
					}
				}
				if (externObjectInstance.methodPrototypes()!=null){
				for (MethodPrototypeContext mProtoType:externObjectInstance.methodPrototypes().methodPrototype()){
					if (mProtoType.functionPrototype()==null){
						if (ctx.typeRef().extendedContext.getTypeName().equals(mProtoType.IDENTIFIER().getText())){
							//Here we are in one of the constructor declarations of extern
							if (mProtoType.parameterList() == null && ctx.argumentList()==null){
								//No Arguments. Safe to return
								return ctx;
							}
							int numberOfConstructorParams = mProtoType.parameterList().parameter().size();
							int numberOfConstructorInvocationParams = ctx.argumentList().argument().size();
							//Assumption here is that we are not supporting constructor overloading
							if (numberOfConstructorParams!=numberOfConstructorInvocationParams){
								L.error("Line:"+ctx.start.getLine()+": Incorrect number of arguments to the extern constructor: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
								return ctx;
							}
							else{
								int currentNumOfTypedArg=0;
								for (int i=0;i<numberOfConstructorParams;i++){
									Type type1 = mProtoType.parameterList().parameter(i).extendedContext.getType();
									Type type2 = ctx.argumentList().argument(i).extendedContext.getType();
									if (type1.getTypeName()!=type2.getTypeName() || type1.getTypeSize()!=type2.getTypeSize()){
										if (!generictypeParamStrings.contains(type1.getTypeName())){
											L.error("Line:"+ctx.start.getLine()+": Incorrect type of arguments to the extern constructor: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
										} else{
											//It is necessary to match the sizes even for generic types based on the callee function arguments and the generic type passed through the calling function
											int sizeOfGenericTypePassed = ctx.typeRef().specializedType().typeArgumentList().typeArg(currentNumOfTypedArg).extendedContext.getType().getTypeSize();
											String actualTypeOfGenericTypePassed = ctx.typeRef().specializedType().typeArgumentList().typeArg(currentNumOfTypedArg).extendedContext.getType().getTypeName();
											int sizeOfParameterPassed = type2.getTypeSize();
											if (isBaseType(type2.getTypeName()) && (sizeOfGenericTypePassed!=sizeOfParameterPassed || actualTypeOfGenericTypePassed!=type2.getTypeName())){
												L.error("Line:"+ctx.start.getLine()+": Cannot unify "+type2.getTypeName()+":"+sizeOfParameterPassed+" to "+actualTypeOfGenericTypePassed+":"+sizeOfGenericTypePassed+" in the call: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()); 
											}
										}
									}
									if (generictypeParamStrings.contains(type1.getTypeName())){
										currentNumOfTypedArg++;
									}
								}
							}
						}
					}
				}
				}
					
					//Generic declaration check for extern
					//TODO: Need to do it for other types too
					if (externObjectInstance.optTypeParameters()!=null){
						List<NonTypeNameContext> typeParamListOfExtern = externObjectInstance.optTypeParameters().typeParameterList().nonTypeName();
						int sizeOfExternGenericsDeclaration = typeParamListOfExtern.size();
						int sizeOfInstantiationGenericTypes = 0;
						if (ctx.typeRef().specializedType()!=null){
							sizeOfInstantiationGenericTypes = ctx.typeRef().specializedType().typeArgumentList().typeArg().size();
						}
						if (sizeOfInstantiationGenericTypes!=sizeOfExternGenericsDeclaration){
							L.error("Line:"+ctx.start.getLine()+": Incorrect number of generic type arguments in the extern instantiation: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
						}
					}
					
					
				}
			}
			catch(SymbolNotDefinedException e){
				L.error("Line:"+ctx.start.getLine()+": Unable to resolve:"+ctx.typeRef().extendedContext.getFormattedText());
			}
		}
		
		//making sure directions are same in case of passing parser types are arguments -start
		List<String> directionListOfCallee= new ArrayList<String>();
		List<String> directionListOfCalled= new ArrayList<String>();
		
		if (ctx.argumentList()!=null){
		List<ArgumentContext> callArgList = ctx.argumentList().argument();
		for (ArgumentContext arg:callArgList){
			if ("parser".equals(arg.extendedContext.getType().getTypeName())){
				if (arg.expression().extendedContext.getType() instanceof FunctionCallContextExt){
					String name = ((FunctionCallContext)(((FunctionCallContextExt)(arg.expression().extendedContext.getType())).getContext())).expression().extendedContext.getFormattedText();
					Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(name);
					if (s instanceof ParserDeclarationContextExt){
						ParserDeclarationContextExt parser = (ParserDeclarationContextExt)s;
						List<ParameterContext> paramList = new ArrayList<ParameterContext>();
						//Doing this because params can be in parserTypeDeclaration child or direct optConstructorParameters child or both
						if (parser.getContext().parserTypeDeclaration()!=null && parser.getContext().parserTypeDeclaration().parameterList()!=null){
							paramList.addAll(parser.getContext().parserTypeDeclaration().parameterList().parameter());
						}
						if (parser.getContext().optConstructorParameters()!=null && parser.getContext().optConstructorParameters().parameterList()!=null){
							paramList.addAll(parser.getContext().optConstructorParameters().parameterList().parameter());
						}
						for (ParameterContext param:paramList){
							if (param.direction()==null){
								directionListOfCallee.add("NoDirection");
							}else{
								directionListOfCallee.add(param.direction().extendedContext.getFormattedText());
							}
						}
					}
				}
			}
		}
		
		
		String typeName = ctx.typeRef().extendedContext.getType().getTypeName();
		if (!isBaseType(typeName)){
			try{
			Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(typeName);
			if (s instanceof PackageTypeDeclarationContextExt){
				PackageTypeDeclarationContextExt packageContext = (PackageTypeDeclarationContextExt)s;
				List<ParameterContext> packageParamList = packageContext.getContext().parameterList().parameter();
				for (ParameterContext param:packageParamList){
					List<ParameterContext> paramList2 = new ArrayList<ParameterContext>();
					String argType = param.typeRef().extendedContext.getType().getTypeName();
					if (!isBaseType(argType)){
						Symbol s2 = AbstractBaseExt.getExtendedContext(ctx).resolve(argType);
						if (s2 instanceof ParserTypeDeclarationContextExt){
							ParserTypeDeclarationContextExt parser = (ParserTypeDeclarationContextExt)s2;
							
							if (parser.getContext()!=null && parser.getContext().parameterList()!=null){
								paramList2.addAll(parser.getContext().parameterList().parameter());
							}
							
							for (ParameterContext paramctx:paramList2){
								if (paramctx.direction()==null){
									directionListOfCalled.add("NoDirection");
								}else{
									directionListOfCalled.add(paramctx.direction().extendedContext.getFormattedText());
								}
							}
						}
					}
				}
			}
			}catch(SymbolNotDefinedException e){
				L.debug("Line:"+ctx.start.getLine()+": Unable to resolve:"+typeName);
			}
		}
		}
		
		if (directionListOfCalled.size()==directionListOfCallee.size()){
			for (int i=0;i<directionListOfCalled.size();i++){
				if (directionListOfCalled.get(i).equals(directionListOfCallee.get(i))){
					continue;
				} else{
					L.error("Line:"+ctx.start.getLine()+": Cannot unify because of direction mismatch: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
					break;
				}
			}
		}
		
		//making sure directions are same in case of passing parser types are arguments -end
		
		//Control Block instantiation can't be at global level
		if (!isBaseType(ctx.typeRef().extendedContext.getType().getTypeName()) && (ctx.getParent() instanceof DeclarationContext)){
			try{
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.typeRef().extendedContext.getFormattedText());
			if (symbol!=null){
				String typeName = symbol.getType().getTypeName();
				if ("control".equals(typeName)){
					L.error("Line:"+ctx.start.getLine()+": '"+ctx.name().extendedContext.getFormattedText()+"' : cannot instantiate at top-level");
				}
			}
			}catch(SymbolNotDefinedException e){
				L.debug("Line:"+ctx.start.getLine()+": Unable to resolve:"+ctx.typeRef().extendedContext.getFormattedText());
			}
		}
		
		//Control block objects can't be used as type parameters
		if (!isBaseType(ctx.typeRef().extendedContext.getType().getTypeName())){
			String typeArg = ctx.typeRef().extendedContext.getFormattedText();
			String typeArgStripped = typeArg;
			try{
			if (typeArg.contains("<"))
				typeArgStripped = typeArg.substring(0,typeArg.indexOf("<"));
				Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(typeArgStripped);
				//TODO: There are some other places where this has to be checked
				if (ctx.typeRef().extendedContext.isSpecializedType()){
				List<TypeArgContext> typeArgList = ctx.typeRef().specializedType().typeArgumentList().typeArg();
				for (TypeArgContext typeArgV:typeArgList){
					if (!isBaseType(typeArgV.extendedContext.getType().getTypeName())){
						Symbol s3 = AbstractBaseExt.getExtendedContext(ctx).resolve(typeArgV.extendedContext.getFormattedText());
						if ("control".equals(s3.getTypeName()))
							L.error("Line:"+ctx.start.getLine()+": Cannot use control control block as a type parameter: "+typeArgStripped);
					}
				}
			}
			}catch(SymbolNotDefinedException e){
				L.debug("Line:"+ctx.start.getLine()+": Unable to resolve:"+typeArgStripped);
			}
		}
		
		//Control block's type params need to be declared when instantiated say as an argument to a package instantiation
		if (ctx.argumentList()!=null){
			List<ArgumentContext> argList = ctx.argumentList().argument();
			for (ArgumentContext arg:argList){
				String typeName = arg.extendedContext.getType().getTypeName();
				if (typeName!=null && !isBaseType(typeName)){
					Type argExpressionType = arg.expression().extendedContext.getType();
					String symbolName = argExpressionType.getSymbolName();
					Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(symbolName);
					if (symbol instanceof ControlDeclarationContextExt){
						ControlDeclarationContextExt controlDecl = (ControlDeclarationContextExt)symbol;
						if (controlDecl.getContext().controlTypeDeclaration().optTypeParameters()!=null){
							int size = controlDecl.getContext().controlTypeDeclaration().optTypeParameters().typeParameterList().nonTypeName().size();
							if (argExpressionType instanceof FunctionCallContextExt && size>0){
								L.error("Line:"+ctx.start.getLine()+": Number of type parameters for control block vary: "+size+" vs "+0);
							} else if (argExpressionType instanceof TempletizedFunctionCallContextExt && size>0){
								TempletizedFunctionCallContextExt templFunctionCall = (TempletizedFunctionCallContextExt) argExpressionType;
								int calledFunctionTemplSize = templFunctionCall.getContext().typeArgumentList().typeArg().size();
								if (calledFunctionTemplSize!=size){
									L.error("Line:"+ctx.start.getLine()+": Number of type parameters for control block vary: "+size+" vs "+calledFunctionTemplSize);
								}
							}
						}
					}
				}
			}
		}
		return ctx;
	}
		
	
	@Override
	public ParserRuleContext visitIfStatement(P416Parser.IfStatementContext ctx){
		try{
			super.visitIfStatement(ctx);
			if ((ctx.expression().extendedContext.getType() == null) || !("BoolType".equals(ctx.expression().extendedContext.getType().getTypeName()) || "BooleanLiteralTrue".equals(ctx.expression().extendedContext.getType().getTypeName()) || "BooleanLiteralFalse".equals(ctx.expression().extendedContext.getType().getTypeName()))){
				L.error("Line:"+ctx.start.getLine()+": '"+ctx.expression().extendedContext.getFormattedText()+"' expression in if statement is either not bool or isn't properly formed.");
			}
		}
		catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' : Issue in if Statement");		
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitIfElseStatement(P416Parser.IfElseStatementContext ctx){
		try{
			super.visitIfElseStatement(ctx);
			if ((ctx.expression().extendedContext.getType() == null) || !("BoolType".equals(ctx.expression().extendedContext.getType().getTypeName()) || "BooleanLiteralTrue".equals(ctx.expression().extendedContext.getType().getTypeName()) || "BooleanLiteralFalse".equals(ctx.expression().extendedContext.getType().getTypeName()))){
				L.error("Line:"+ctx.start.getLine()+": '"+ctx.expression().extendedContext.getFormattedText()+"' expression in if statement is either not bool or isn't properly formed.");
			}
		}
		catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+"' : Issue in if/else Statement");		
		}
		return ctx;
	}
	
	@Override 
	public ParserRuleContext visitParserState(P416Parser.ParserStateContext ctx){
		insideParserState = true;
		super.visitParserState(ctx);
		insideParserState = false;
		return ctx;
	}

	
	@Override
	public ParserRuleContext visitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx){
		String externName = ctx.nonTypeName().extendedContext.getFormattedText();
		if (ctx.methodPrototypes()!=null){
		List<MethodPrototypeContext> methodPrototypesList = ctx.methodPrototypes().methodPrototype();
		for (MethodPrototypeContext methodPrototype: methodPrototypesList){
			
			//Making sure that the parameters of a function prototype declaration is not of type control	
			if (methodPrototype.parameterList()!=null){
			List<ParameterContext> pctxList = methodPrototype.parameterList().parameter();
			for (ParameterContext param:pctxList){
				if (!isBaseType(param.typeRef().extendedContext.getType().getTypeName())){
				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(param.typeRef().getText());
				if (symbol!=null && "control".equals(symbol.getTypeName())){
					L.error("Line:"+ctx.start.getLine()+": '"+param.name().getText()+"': parameter cannot have type control: "+param.typeRef().getText());
				}
				}
			}
			}
			//Making sure that the parameters of a function prototype declaration is not of type control - end
			
			if (methodPrototype.functionPrototype()!=null){
			String methodPrototypeName = methodPrototype.functionPrototype().name().extendedContext.getFormattedText();
			String returnTypeName = methodPrototype.functionPrototype().typeOrVoid().extendedContext.getFormattedText();
			try{
			Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(methodPrototypeName);
//			if ("extern".equals(s.getTypeName())){
			if (externName.equals(methodPrototypeName) && !(returnTypeName==null || "".equals(returnTypeName))){
				L.error("Line:"+ctx.start.getLine()+": '"+methodPrototypeName+"': Constructor cannot have a return type");
			}
//			}
			}catch(SymbolNotDefinedException e){
			}
			}
		}
		}
		return ctx;
	}
	
	private boolean isBaseType(String typeName) {
		String basicTypeNames[] = {"BitSizeType","IntSizeType","IntegerLiteral","BoolType","BooleanLiteralTrue","BooleanLiteralFalse","BitType","VarBitSizeType","enum"};		
		for (int i=0;i<basicTypeNames.length;i++){
			if (basicTypeNames[i].equals(typeName)){
				return true;
			}
		}
		
		//check if it is of kind tuple which has type of format [TypeName1, TypeName2, ..]
		if (typeName.startsWith("[") && typeName.endsWith("]")){
			typeName = typeName.substring(1, typeName.length()-1);
			StringTokenizer tokenizer = new StringTokenizer(typeName,",");
			while (tokenizer.hasMoreTokens()){
				String typeN = tokenizer.nextToken().trim();
				boolean typeMatched = false;
				for (int i=0;i<basicTypeNames.length;i++){
					if (basicTypeNames[i].equals(typeN)){
						typeMatched = true;
					}
				}
				if (!typeMatched){
					return false;
				}
			}
			return true;
		}
		
		return false;
	}

	@Override
	public ParserRuleContext visitExternFunctionDeclaration(P416Parser.ExternFunctionDeclarationContext ctx){
		//super.visitExternFunctionDeclaration(ctx);
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitTypeDeclaration(P416Parser.TypeDeclarationContext ctx){
		super.visitTypeDeclaration(ctx);
		//added for optimisation so as to not visit children of a declaration for resolving symbols
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitIdentifierList(P416Parser.IdentifierListContext ctx){
		//added for optimisation so as to not visit children of identifier list for resolving symbols
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitOptAnnotations(P416Parser.OptAnnotationsContext ctx){
		//added for optimisation so as to not visit children of annotations for resolving symbols
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitFunctionCall(P416Parser.FunctionCallContext ctx){
		super.visitFunctionCall(ctx);
		try{
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.expression().extendedContext.getFormattedText());
			if (symbol!=null){
				String returnTypeName = symbol.getType().getTypeName();//TEMP AK 13 JUN
				if (!isBaseType(returnTypeName) && !"action".equals(returnTypeName) && !"parser".equals(returnTypeName) && !"control".equals(returnTypeName) && !"extern".equals(returnTypeName)){
					try{
						symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(returnTypeName);
						if (symbol!=null && "struct".equals(symbol.getTypeName())){
							L.error("Line:"+ctx.start.getLine()+": "+ctx.expression().extendedContext.getFormattedText()+": functions or methods returning structures are not supported");
						}
						else if (symbol!=null && "control".equals(symbol.getTypeName())){
							L.error("Line:"+ctx.start.getLine()+": Illegal return type control: "+returnTypeName);
						}
					}catch(SymbolNotDefinedException e){
						L.error("Line:"+ctx.start.getLine()+": '"+returnTypeName+"' can not be resolved");
					}
				}
			}
		}catch(SymbolNotDefinedException e){
            L.debug("Line:"+ctx.start.getLine()+": '"+ctx.expression().extendedContext.getFormattedText()+"' can not be resolved");
		}
		return ctx;
	}
	
	
	List<String> actionStringsList = new ArrayList<String>();

	private boolean insideParserDeclaration;

	private boolean insideParserTypeDeclaration;

	private boolean insideControlLocalDeclaration;
	
	@Override
	public ParserRuleContext visitActionList(P416Parser.ActionListContext ctx){
		ActionListContext actionList = ctx;
		if (actionList!=null)
		for (ActionRefContext action:actionList.actionRef()){
			if (action instanceof ActionWithoutArgsContext)
				actionStringsList.add(((ActionWithoutArgsContext)action).name().extendedContext.getFormattedText());
			else if (action instanceof ActionWithArgsContext)
				actionStringsList.add(((ActionWithArgsContext)action).name().extendedContext.getFormattedText());
			else{
				//TODO This case will never arise. Will delete
				actionStringsList.add(action.extendedContext.getFormattedText());
			}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitTableDeclaration(P416Parser.TableDeclarationContext ctx){
		actionStringsList = new ArrayList<String>();
		super.visitTableDeclaration(ctx);
		keyInTableVisited = false;
		constEntriesInTableVisited=false;
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitLocalConstVariable(P416Parser.LocalConstVariableContext ctx){
		if ("default_action".equals(ctx.IDENTIFIER().getText())){
			//Strip off Parenthesis if present on the initializer
			String initializer = ctx.initializer().extendedContext.getFormattedText();
			if (ctx.initializer().extendedContext.getFormattedText().endsWith("()")){
				initializer = initializer.substring(0,initializer.length()-2);
			}
			else if (ctx.initializer().expression() instanceof FunctionCallContext){
				FunctionCallContext fCall = (FunctionCallContext)(ctx.initializer().expression());
				initializer = fCall.expression().extendedContext.getFormattedText();
			}
			if (!actionStringsList.contains(initializer)){
				L.error("Line:"+ctx.start.getLine()+": '"+ctx.initializer().extendedContext.getFormattedText()+"' is not in the list of actions");
			}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitActionDeclaration(P416Parser.ActionDeclarationContext ctx){
		insideActionDeclaration = true;
		super.visitActionDeclaration(ctx);
		if (ctx.parameterList()!=null){
		List<ParameterContext> parameterList = ctx.parameterList().parameter();
		for (ParameterContext param:parameterList){
			String typeRef = param.typeRef().extendedContext.getFormattedText();
			if (!this.isBaseType(param.typeRef().extendedContext.getType().getTypeName())){
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(typeRef);
			if (symbol!=null){
				String typeName = symbol.getTypeName();
				if ("extern".equals(typeName)){
					L.error("Line:"+ctx.start.getLine()+": '"+param.typeRef().extendedContext.getFormattedText()+"': Action parameters cannot have extern types");
				}
			}
			}
		}
		}
		insideActionDeclaration = false;
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitControlDeclaration(P416Parser.ControlDeclarationContext ctx){
		insideControlDeclaration = true;
		controlParamNamesList = new ArrayList<String>();
		currentControlBlockName = ctx.controlTypeDeclaration().name().extendedContext.getFormattedText();
		if (ctx.controlTypeDeclaration().optTypeParameters()!=null){
		List<NonTypeNameContext> listOfTypeParams = ctx.controlTypeDeclaration().optTypeParameters().typeParameterList().nonTypeName();
		for (NonTypeNameContext nonTypeNameTypeParam: listOfTypeParams){
			controlParamNamesList.add(nonTypeNameTypeParam.extendedContext.getFormattedText());
		}
		}
		super.visitControlDeclaration(ctx);
		insideControlDeclaration = false;
		controlParamNamesList.clear();
		currentControlBlockName = null;
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx){
		super.visitControlTypeDeclaration(ctx);
		if ( ctx.parameterList()!=null){
		List<ParameterContext> paramList = ctx.parameterList().parameter();
		for (ParameterContext param:paramList){
			if (!this.isBaseType(param.typeRef().extendedContext.getType().getTypeName())){
				String paramRaw = param.typeRef().extendedContext.getFormattedText();
				String paramStripped = paramRaw;
				if (paramRaw.contains("<"))
					paramStripped = paramRaw.substring(0,paramRaw.indexOf("<"));
				Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(paramStripped);
				if (s instanceof ExternObjectDeclarationContextExt){
					ExternObjectDeclarationContextExt e = (ExternObjectDeclarationContextExt)s;
					if (e.getContext().optTypeParameters()!=null){
						if (!param.typeRef().extendedContext.isSpecializedType()){
							//Extern definition expects typed params but actual params are not typed
							//Currently not matching the types of generic argument type to definition type though. Not needed in such detail currently
							L.error("Line:"+ctx.start.getLine()+": Extern definition expects typed params. But the control type arguments aren't typed: "+ctx.parameterList().extendedContext.getFormattedText());
						}
						else{
							int typeArgSize = e.getContext().optTypeParameters().typeParameterList().nonTypeName().size();
							int controlParamTypeArgSize = param.typeRef().specializedType().typeArgumentList().typeArg().size();
							if (typeArgSize!=controlParamTypeArgSize){
								L.error("Line:"+ctx.start.getLine()+": Extern definition and control type arguments argument count mismatch: "+ctx.parameterList().extendedContext.getFormattedText());
							}
						}
					}
				}
			}
		}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitParameterList(P416Parser.ParameterListContext ctx){
		super.visitParameterList(ctx);
		
		//Directionless paramaters should be in the end
		boolean directionLess = false;
		List<ParameterContext> parameterContextsList = ctx.parameter();
		if (insideActionDeclaration){
			for (ParameterContext parameterContext:parameterContextsList){
				DirectionContext direction = parameterContext.direction();
				if (direction==null){
					directionLess = true;
				}
				if (directionLess && direction!=null){//Means there is one directionLess before direction = true. It should fail as Directionless paramaters should be in the end
					L.error("Line:"+ctx.start.getLine()+": Directionless Parameters should be in the end");
				}
				String typeName = parameterContext.typeRef().extendedContext.getType().getTypeName();
				if (!("BitSizeType".equals(typeName) || "IntSizeType".equals(typeName) || "BoolType".equals(typeName) || "BitType".equals(typeName))){
				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(parameterContext.typeRef().extendedContext.getFormattedText());
				if ("control".equals(symbol.getTypeName())){
					L.error("Line:"+ctx.start.getLine()+": Action Parameters can't be of control type: "+parameterContext.name().getText());
				}
				}
			}
		}
		
		//Control Declaration Params
		if (insideControlDeclaration){
			for (ParameterContext parameterContext:parameterContextsList){
				String param = parameterContext.name().extendedContext.getFormattedText();
				controlParamNamesList.add(param);
			}
		}
		
		return ctx;
	}

	@Override
	public ParserRuleContext visitKey(KeyContext ctx) {
		if (constEntriesInTableVisited){
			L.error("Line:"+ctx.start.getLine()+": Entries list must be after table key");
			return ctx;
		}
		keyInTableVisited = true;
		super.visitKey(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitConstEntries(ConstEntriesContext ctx) {
		constEntriesInTableVisited = true;
		super.visitConstEntries(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitMemberAccess(MemberAccessContext ctx) {
		//super.visitErrorMemberAccess(ctx);
		if (ctx.typeName().prefixedType().ERROR()!=null){
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			if (symbol==null){
				L.info("Member Access can't be resolved");
			}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitControlBody(ControlBodyContext ctx) {
		insideControlBody = true;
		super.visitControlBody(ctx);
		insideControlBody = false;
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitPackageTypeDeclaration(PackageTypeDeclarationContext ctx){
		//Package Params should be directionless: should not be in/out/inout: P4 SPEC 7.2.12. Package types 
		super.visitPackageTypeDeclaration(ctx);
		List<ParameterContext> params = ctx.parameterList().parameter();
		for (ParameterContext param:params){
			if (param.direction()!=null && ("in".equals(param.direction().getText()) || "out".equals(param.direction().getText()) || "inout".equals(param.direction().getText()))){
				L.error("Line:"+ctx.start.getLine()+": Package should not have in/out/inout parameters");
			}
		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitParserDeclaration(ParserDeclarationContext ctx){
	//Parameters can't be of parser type
	List<ParameterContext> listOfParams = new ArrayList<ParameterContext>();
    if (ctx.optConstructorParameters()!=null && ctx.optConstructorParameters().parameterList()!=null)
	listOfParams.addAll(ctx.optConstructorParameters().parameterList().parameter());
	for (ParameterContext param:listOfParams){
		String typeRef = param.typeRef().extendedContext.getFormattedText();
		if (!this.isBaseType(param.typeRef().extendedContext.getType().getTypeName())){
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(typeRef);
			if (symbol!=null && !"main".equalsIgnoreCase(param.name().extendedContext.getFormattedText()) && "parser".equals(symbol.getTypeName())){
				L.error("Line:"+ctx.start.getLine()+": Parameters can't be of parser type: "+param.name().getText());
			}
		}
	}
    this.insideParserDeclaration = true;
	super.visitParserDeclaration(ctx);
    this.insideParserDeclaration = false;
	return ctx;
	}
	
	
	@Override
	public ParserRuleContext visitParserTypeDeclaration(ParserTypeDeclarationContext ctx){
	//Parameters can't be of parser type
	List<ParameterContext> listOfParams = new ArrayList<ParameterContext>();
	if (ctx.parameterList()!=null)
    listOfParams.addAll(ctx.parameterList().parameter());
	for (ParameterContext param:listOfParams){
		String typeRef = param.typeRef().extendedContext.getFormattedText();
		if (!this.isBaseType(param.typeRef().extendedContext.getType().getTypeName())){
		//if (!this.isBaseType(typeRef)){
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(typeRef);
			
			if (symbol!=null && !"main".equalsIgnoreCase(param.name().extendedContext.getFormattedText()) && "parser".equals(symbol.getTypeName())){
				L.error("Line:"+ctx.start.getLine()+": Parameters can't be of parser type: "+param.name().getText());
			}
		}
	}
		this.insideParserTypeDeclaration = true;
		super.visitParserTypeDeclaration(ctx);
		this.insideParserTypeDeclaration = false;
	return ctx;
	}
	
	@Override
	public ParserRuleContext visitSimpleKeySetExpression(SimpleKeySetExpressionContext ctx){
		super.visitSimpleKeySetExpression(ctx);
		
		//Type check for Range operation e.g., 4w7 .. 4w9
		if (ctx.RANGE()!=null){
		Type type1 = ctx.expression(0).extendedContext.getType();
		Type type2 = ctx.expression(1).extendedContext.getType();
		if (!((type1.getTypeName() == type2.getTypeName()) && (type1.getTypeSize() == type2.getTypeSize()))){
			L.error("Line:"+ctx.start.getLine()+": Range: Cannot operate on values with different types");
		}
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitTempletizedFunctionCall(TempletizedFunctionCallContext ctx) {
		super.visitTempletizedFunctionCall(ctx);
		List<TypeArgContext> typeArgList = ctx.typeArgumentList().typeArg();
		for (TypeArgContext typeArg:typeArgList){
			Type type = typeArg.extendedContext.getType();
			if (!isBaseType(type.getTypeName()) && ctx.expression().extendedContext.getFormattedText().endsWith(".lookahead")){
				Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(typeArg.extendedContext.getFormattedText());
				if (symbol!=null){
					//Means atleast one of the fields is of type VarBit. It is not allowed in case of lookahead
					if (symbol.getType().getTypeSize() == VarBitSizeTypeContextExt.SIZE_INDETERMINATE){
						L.error("Line:"+ctx.start.getLine()+": type argument must be a fixed-width type: "+type.getTypeName());
					}
				}
			}
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitControlLocalDeclaration(ControlLocalDeclarationContext ctx) {
		insideControlLocalDeclaration = true;
		super.visitControlLocalDeclaration(ctx);
		insideControlLocalDeclaration = false;
		return ctx;
	}	
	
	@Override
	public ParserRuleContext visitHeaderTypeDeclaration(HeaderTypeDeclarationContext ctx) {
		super.visitHeaderTypeDeclaration(ctx);
		if (ctx.structFieldList()!=null){
		List<StructFieldContext> fieldList = ctx.structFieldList().structField();
		for (StructFieldContext field:fieldList){
			String typeName = field.extendedContext.getType().getTypeName();
			if (!("BitSizeType".equals(typeName) || "IntSizeType".equals(typeName) || "VarBitSizeType".equals(typeName))){
				L.error("Line:"+field.start.getLine()+": Field "+field.name().getText()+" of header "+ctx.name().getText()+" can't be of type: "+typeName);
			}
		}
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitPrefixedNonTypeNameLvalue(PrefixedNonTypeNameLvalueContext ctx) {
		super.visitPrefixedNonTypeNameLvalue(ctx);
		String lValueString = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
		String lValueStringMinusLast = "";
		String lValueStringMinusNext="";
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER)){
        	lValueStringMinusLast = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusLast);
			if ("header".equals(symbol.getType().getTypeName()))
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			else{
				//Double resolve is needed to determine the type of header in some cases
				Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(symbol.getType().getTypeName());
				if ("header".equals(s.getTypeName()))
		            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			}
			if (!this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last and next on header should be present only inside parser");
			}
        }
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
        	lValueStringMinusNext = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusNext);
			if ("header".equals(symbol.getType().getTypeName()) && !this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': next on header should be present only inside parser");
			}
        }
		return ctx;
	}	
	
	@Override
	public ParserRuleContext visitPrefixedNameLvalue(PrefixedNameLvalueContext ctx) {
		super.visitPrefixedNameLvalue(ctx);
		String lValueString = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
		String lValueStringMinusLast = "";
		String lValueStringMinusNext="";
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER)){
        	lValueStringMinusLast = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusLast);
			if ("header".equals(symbol.getType().getTypeName()))
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			else{
				//Double resolve is needed to determine the type of header in some cases
				Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(symbol.getType().getTypeName());
				if ("header".equals(s.getTypeName()))
		            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			}
			if (!this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last and next on header should be present only inside parser");
			}
        }
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
        	lValueStringMinusNext = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusNext);
			if ("header".equals(symbol.getType().getTypeName()) && !this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': next on header should be present only inside parser");
			}
        }
		return ctx;
	}	
	
	@Override
	public ParserRuleContext visitArrayIndexLvalue(ArrayIndexLvalueContext ctx) {
		super.visitArrayIndexLvalue(ctx);
		String lValueString = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
		String lValueStringMinusLast = "";
		String lValueStringMinusNext="";
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER)){
        	lValueStringMinusLast = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusLast);
			if ("header".equals(symbol.getType().getTypeName()))
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			else{
				//Double resolve is needed to determine the type of header in some cases
				Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(symbol.getType().getTypeName());
				if ("header".equals(s.getTypeName()))
		            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			}
			if (!this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last and next on header should be present only inside parser");
			}
        }
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
        	lValueStringMinusNext = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusNext);
			if ("header".equals(symbol.getType().getTypeName()) && !this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': next on header should be present only inside parser");
			}
        }
		return ctx;
	}	
	
	@Override
	public ParserRuleContext visitRangeIndexLvalue(RangeIndexLvalueContext ctx) {
		super.visitRangeIndexLvalue(ctx);
		String lValueString = AbstractBaseExt.getExtendedContext(ctx).getFormattedText();
		String lValueStringMinusLast = "";
		String lValueStringMinusNext="";
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.LAST_HEADER)){
        	lValueStringMinusLast = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusLast);
			if ("header".equals(symbol.getType().getTypeName()))
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			else{
				//Double resolve is needed to determine the type of header in some cases
				Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(symbol.getType().getTypeName());
				if ("header".equals(s.getTypeName()))
		            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last on header type can't be lValue");
			}
			if (!this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': last and next on header should be present only inside parser");
			}
        }
        if (lValueString.endsWith("."+HeaderStackTypeContextExt.NEXT_HEADER)){
        	lValueStringMinusNext = lValueString.substring(0, lValueString.lastIndexOf("."));
			Symbol symbol = AbstractBaseExt.getExtendedContext(ctx).resolve(lValueStringMinusNext);
			if ("header".equals(symbol.getType().getTypeName()) && !this.insideParserDeclaration){
	            L.error("Line:"+ctx.start.getLine()+": '"+AbstractBaseExt.getExtendedContext(ctx).getFormattedText()+ "': next on header should be present only inside parser");
			}
        }
		return ctx;
	}	
	
	@Override
	public ParserRuleContext visitSlah(P416Parser.SlahContext ctx) {
		super.visitSlah(ctx);
		//It's handled in expression evaluator
//		Type type1 = ctx.expression(0).extendedContext.getType();
//		Type type2 = ctx.expression(0).extendedContext.getType();
//		if ( (type1!=null && "IntSizeType".equals(type1.getTypeName())) || (type2!=null && "IntSizeType".equals(type2.getTypeName()))){
//			L.error("Line:"+ctx.start.getLine()+": Division not defined on signed types: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
//		}
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitMod(P416Parser.ModContext ctx) {
		super.visitMod(ctx);
		//It's handled in expression evaluator
//		Type type1 = ctx.expression(0).extendedContext.getType();
//		Type type2 = ctx.expression(0).extendedContext.getType();
//		if ( (type1!=null && "IntSizeType".equals(type1.getTypeName())) || (type2!=null && "IntSizeType".equals(type2.getTypeName()))){
//			L.error("Line:"+ctx.start.getLine()+": Modulo not defined on signed types: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
//		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitArrayIndex(ArrayIndexContext ctx) {
		super.visitArrayIndex(ctx);
		
		String typeName = ctx.expression(0).extendedContext.getTypeName();
		if (this.isBaseType(typeName)){
			L.error("Line:"+ctx.start.getLine()+": Array index access not permitted on base type: "+typeName);
			return ctx;
		}
		
		Symbol s = AbstractBaseExt.getExtendedContext(ctx).resolve(ctx.expression(0).extendedContext.getFormattedText());
		int actualArraySize = s.getType().getTypeSize();
	
		
		Integer arrayIndexAccessed=null;
		
		try{
			arrayIndexAccessed = Integer.parseInt(ctx.expression(1).extendedContext.getFormattedText());
		}catch(NumberFormatException e){
			L.debug("Line:"+ctx.start.getLine()+": Number improper: "+ctx.expression(1).extendedContext.getFormattedText());
		}
		
		//Do the below after foolproof expression evaluation instead of above:
		//Integer arrayIndexAccessed = ctx.expression(1).extendedContext.getNewValue();

		
		if (arrayIndexAccessed!=null){
		
			if (arrayIndexAccessed<0){
				//IndexOutOfBounds
				L.error("Line:"+ctx.start.getLine()+": Accessed array index can't be negative: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}
		
			if (actualArraySize<=arrayIndexAccessed && actualArraySize>=0){
				//IndexOutOfBounds
				L.error("Line:"+ctx.start.getLine()+": Accessed array index size greater that actual array size: "+AbstractBaseExt.getExtendedContext(ctx).getFormattedText());
			}
		}
		
		return ctx;
	}
	
	

}
