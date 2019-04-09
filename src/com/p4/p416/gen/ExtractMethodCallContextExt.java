package com.p4.p416.gen;

import java.util.List;

import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.parser.*;
import com.p4.p416.applications.Type;
import com.p4.p416.expressions.ExpressionTreeHelper;
import com.p4.p416.expressions.IExpressionTree;
import com.p4.p416.expressions.ParserALUOp;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.SsaForm;
import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Parser.ArgumentListContext;
import com.p4.p416.gen.P416Parser.ExtractMethodCallContext;
import com.p4.p416.gen.P416Parser.MethodCallStatementContext;

public class ExtractMethodCallContextExt extends MethodCallStatementContextExt {

	private static final Logger L = LoggerFactory.getLogger(ExtractMethodCallContextExt.class);

	public ExtractMethodCallContextExt(ExtractMethodCallContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  MethodCallStatementContext getContext(){
		return (MethodCallStatementContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ExtractMethodCallContext getContext(String str){
		return (ExtractMethodCallContext)new PopulateExtendedContextVisitor().visit(getParser(str).methodCallStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ExtractMethodCallContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ExtractMethodCallContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	@Override
	public void getExtracts(List<P4Extract> extracts, P4Headers hdr) {
		//String extractF = getExtendedContext(getContext().argumentList().argument().get(0)).getFormattedText();
		ArgumentListContext argumentListContext = (ArgumentListContext)getExtendedContext(((ExtractMethodCallContext)getContext()).argumentList()).getContext();
		String extractF = getExtendedContext(argumentListContext.argument(0)).getFormattedText();
		P4Extract ex  = getP4Extract(extractF, this, hdr);
		extracts.add(ex);
	}

	//SSA START
	@Override
	public void setVersions(SsaForm obj){}
	//SSA END

	public static P4Extract getP4Extract(String extractF, AbstractBaseExt ctx, P4Headers hdr){
		Symbol extractSymbol = ctx.resolve(extractF);
		String headerActualName = extractSymbol.getTypeName();
		//int offset = getAlignedByteOffset(extractF);
		int size = extractSymbol.getType().getSizeInBits();
		L.debug(extractF);
		//L.debug("OFFSET = "+offset+", SIZE = "+size);
		//int vloc = getAlignedByteOffset(extractF+".isValid");
		String validString = extractF+".isValid";
		Symbol validSymbol = ctx.resolve(validString);
		int vloc = MemoryManager.getOffset(validString, (Type) validSymbol);
		//TODO remove this and put this interface in the Type
		P4Extract ex  = null;
		if( ctx instanceof P4programContextExt|| ((ArgumentListContext) getExtendedContext(((ExtractMethodCallContext)ctx.getContext()).argumentList()).getContext()).argument().size()<=1) {
			ex = new P4Extract(headerActualName, extractSymbol.getSymbolName(), vloc, extractF + ".isValid", ParserALUOp.defaultALUOP, false);
		}else{
			ExpressionContextExt extractExpression = (ExpressionContextExt) getExtendedContext(((ArgumentListContext) getExtendedContext(((ExtractMethodCallContext)ctx.getContext()).argumentList()).getContext()).argument(1).expression());
			L.info(extractExpression.getFormattedText());
			P4ExtractExpression p4ExtractExpression = new P4ExtractExpression(extractExpression);
			IExpressionTree expressionTree = ExpressionTreeHelper.compactExpression(extractExpression);
			ParserALUOp parserALUOp = ExpressionTreeHelper.getExtractExpression(extractExpression, expressionTree);
			ex = new P4Extract(headerActualName, extractSymbol.getSymbolName(), vloc, extractF + ".isValid",parserALUOp,true);
		}
		P4Header  oldHeader = hdr.resolveHeader(extractF);
		int src = 0;
		boolean matchFound = false;
		if(ex.getParserALUOp().getFieldName() !=null){
			Symbol vbField =  ctx.resolve(ex.getParserALUOp().getFieldName());
			int dest = MemoryManager.getOffset(ex.getParserALUOp().getFieldName(), (Type) vbField)*8;
			ex.getParserALUOp().setDpOffset(new FW(dest+256, ExpressionTreeHelper.PARSEROPSIZE));
			matchFound = true;
		}
		for(BitStringType fields:oldHeader.getFields()){
			boolean emit = true;
			boolean varbit = false;
			if(fields instanceof VLBitStringType){
				emit = false;
				varbit = true;
			}
			String fullName = extractF+"."+fields.getIdentifier();
			Symbol fieldNameSymbol = ctx.resolve(fullName);
			int dest;
			SourceDestinationSize sds=null;
			int s = fields.getLength();
			try{
				dest = MemoryManager.getOffset(fullName, (Type) fieldNameSymbol)*8;
				sds = new SourceDestinationSize(src, dest, s, fields.getIdentifier(), fullName, true, varbit);
				ex.getFields().add(sds);
			}catch (Exception e){
				dest = Integer.MIN_VALUE;
				sds = new SourceDestinationSize(src, dest, s, fields.getIdentifier(), fullName, false, varbit);
				ex.getFields().add(sds);
				src+=s;
				continue;
			}
			if(ex.isALUneeded()){
				int opOffset = ex.getParserALUOp().getRelativeOffset();
				int opSize = ex.getParserALUOp().getSize();
				if(opOffset==src && opSize==s){
					matchFound = true;
					int dpOffset = dest+256;
					ex.getParserALUOp().setDpOffset(new FW(dpOffset,ExpressionTreeHelper.PARSEROPSIZE));
					ex.getParserALUOp().setFieldName(fullName);
				}
			}
			src+=s;
			//}
		}
		if(ex.isALUneeded() && !matchFound){
			throw new IllegalStateException("Can't map length calculation to a single field");
		}
		return ex;
	}
}
