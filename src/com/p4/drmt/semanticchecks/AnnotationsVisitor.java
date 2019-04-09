package com.p4.drmt.semanticchecks;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Symbol;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser.ComplexAnnotationContext;
import com.p4.p416.gen.P416Parser.NameContext;
import com.p4.p416.gen.P416Parser.NonTypeNameContext;
import com.p4.p416.gen.P416Parser.SimpleAnnotationContext;

public class AnnotationsVisitor extends P416BaseVisitor<ParserRuleContext> {
	
	private static final Logger L = LoggerFactory.getLogger(AnnotationsVisitor.class);	
	
	boolean insideAnnotation = false;

	@Override
	public ParserRuleContext visitSimpleAnnotation(SimpleAnnotationContext ctx) {
		insideAnnotation = true;
		super.visitSimpleAnnotation(ctx);
		insideAnnotation = false;
		return ctx;
	}
	
	@Override
	public ParserRuleContext visitComplexAnnotation(ComplexAnnotationContext ctx) {
		insideAnnotation = true;
		super.visitComplexAnnotation(ctx);
		insideAnnotation = false;
	    return ctx;
	}

	@Override
	public ParserRuleContext visitNonTypeName(NonTypeNameContext ctx) {
		super.visitNonTypeName(ctx);
		try{
	 		if (insideAnnotation && !"verify".equals(ctx.extendedContext.getFormattedText())){
				Symbol s = ctx.extendedContext.resolve(ctx.extendedContext.getFormattedText());
				AbstractBaseExt a = (AbstractBaseExt)s;
				int resolvedLineNum = a.getContext().start.getLine();
				int currentLineNum = ctx.start.getLine();
				if (currentLineNum<resolvedLineNum){
			 		L.error("Line:"+ctx.start.getLine()+": '"+ctx.extendedContext.getFormattedText()+"' can not be resolved. It is declared/defined after usage");
				}
	 		}
		}catch(SymbolNotDefinedException e){
	 		L.error("Line:"+ctx.start.getLine()+": '"+ctx.extendedContext.getFormattedText()+"' can not be resolved::");
		}
		return ctx;
	}

	@Override
	public ParserRuleContext visitName(NameContext ctx) {
		//Intentionally suppressing super.visitName invocation by commenting it, as we don't wont nonTypeNames deduced from name from again getting resolved
		//super.visitName(ctx);
		return ctx;
	}
	
	
	
	
	
}
