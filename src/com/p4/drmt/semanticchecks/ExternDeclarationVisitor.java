package com.p4.drmt.semanticchecks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.ExternObjectDeclarationContextExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.P416Parser.ExternObjectDeclarationContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IParameter;

public class ExternDeclarationVisitor  extends P416BaseVisitor<ParserRuleContext>{

	private static final Logger L = LoggerFactory.getLogger(ExternDeclarationVisitor.class);

	String externDeclName = null;
	Map<String, ExternObjectDeclarationContextExt> externObjects = new HashMap<String, ExternObjectDeclarationContextExt>() ;
	Map<String, ControlDeclarationContextExt> controlBlocks = new HashMap<String, ControlDeclarationContextExt> ();
	List<Map<String, Integer>> functionPrototypes = new ArrayList<>();
	boolean extern = false;

	@Override
	public ParserRuleContext visitP4program(P416Parser.P4programContext ctx) {
		(P4programContextExt.getExtendedContext(ctx)).getControlBlocks(controlBlocks);
		(P4programContextExt.getExtendedContext(ctx)).getExternObjects(externObjects);
		super.visitP4program(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitControlTypeDeclaration(P416Parser.ControlTypeDeclarationContext ctx) {
		for(String name: controlBlocks.keySet()) {
			if(controlBlocks.get(name) != null) {
				List<IParameter> paramList = controlBlocks.get(name).getParameters();
				for(IParameter param :paramList) {
					if(externObjects.get(param.getTypeRef().getPrefixedType()) != null) {
						if(param.getDirection() != null) {
							L.error("Line:"+ctx.start.getLine()+": "+"Cannot have externs with direction.");
						}
					}
				}
			}
		}
		super.visitControlTypeDeclaration(ctx);
		return ctx;
	}


	@Override
	public ParserRuleContext visitExternObjectDeclaration(P416Parser.ExternObjectDeclarationContext ctx) {
		extern = true;
		functionPrototypes = new ArrayList<>();
		externDeclName = ((ExternObjectDeclarationContext) ctx).nonTypeName().getText();
		super.visitExternObjectDeclaration(ctx);
		extern = false;
		return ctx;
	}

	@Override
	public ParserRuleContext visitFunctionPrototype(P416Parser.FunctionPrototypeContext ctx) {
		if(extern) {
			int paramSize = 0;
			String name = ctx.name().getText();
			Map<String, Integer> func = new HashMap<>();
			if(ctx.parameterList() != null) {
				paramSize = ctx.parameterList().parameter().size();
			}
			func.put(name, paramSize);
			if(duplicates(name, paramSize)) {
				L.error("Found duplicate method.");
			} else {
				functionPrototypes.add(func);
			}
		}

		return ctx;
	}

	boolean duplicates(String funName, Integer funParamCount)
	{
		for(int i=0 ; i< functionPrototypes.size();i++) {
			String name = functionPrototypes.get(i).keySet().toArray()[0].toString();
			Integer paramCount = functionPrototypes.get(i).get(name);

			if(funName.equals(name) && paramCount.equals(funParamCount)) {
				return true;
			}
		}
		return false;
	}


	@Override
	public ParserRuleContext visitMethodPrototype(P416Parser.MethodPrototypeContext ctx) {
		//		IDENTIFIER LPARAN parameterList? RPARAN SEMI;
		if(ctx.IDENTIFIER() != null && externDeclName != null &&
				!externDeclName.equals(ctx.IDENTIFIER().getText())) {
			L.error("Line:"+ctx.start.getLine()+": "+"No return type specified.");
		}
		super.visitMethodPrototype(ctx);
		return ctx;
	}

}
