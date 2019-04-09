package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.applications.Scope;
import com.p4.p416.applications.SsaForm;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.SymbolNotDefinedException;
import com.p4.p416.gen.P416Parser.EnumDeclarationContext;
import com.p4.p416.gen.P416Parser.NonTypeNameContext;
import com.p4.p416.gen.P416Parser.TypeParameterListContext;
import com.p4.utils.Utils.Pair;

public class NonTypeNameContextExt extends AbstractBaseExt {
	
	private static final Logger L = LoggerFactory.getLogger(NonTypeNameContextExt.class);

	public NonTypeNameContextExt(NonTypeNameContext ctx) {
		super(ctx);
	}

	@Override
	public  NonTypeNameContext getContext(){
		return (NonTypeNameContext)contexts.get(contexts.size()-1);
	}

	@Override
	public NonTypeNameContext getContext(String str){
		return (NonTypeNameContext)new PopulateExtendedContextVisitor().visit(getParser(str).nonTypeName());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof NonTypeNameContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ NonTypeNameContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	protected String getName(){
		return getFormattedText();
	}
	//SSA START
	@Override

	public void setVersions(SsaForm obj ){
		if( obj.getParentCtx() instanceof VariableDeclarationContextExt)
		{
			AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(getContext().getText());
			this.ssaVersion = defSymbol.getVersion();
		}
	}

	@Override
	public void markDelete(SsaForm obj){
		if( obj.getParentCtx() instanceof VariableDeclarationContextExt)
		{
			try{
				String objName = getContext().getText();
				AbstractBaseExt defSymbol = (AbstractBaseExt) resolve(objName);
				List<Pair<AbstractBaseExt,AbstractBaseExt> > rhsList= obj.getRhsVer().get(defSymbol);
				if(rhsList==null){
					((VariableDeclarationContextExt) obj.getParentCtx()).setDel(true);
					AbstractBaseExt currentSymbol=(AbstractBaseExt)defSymbol;
					Scope currentScope=currentSymbol.getEnclosingScope();
					if(currentScope!=null) {
						obj.getParentCtx().setContext(null);
					}
				} 
			}catch(SymbolNotDefinedException e) {
				// Ignore if symbol is not found, could be 
				// something in hidden field isValid
			}
		}
	}

	@Override
	public void getSSAFormattedText(Params params){
		if(getContext()==null) return;
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex()-1);
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append(alignmentText);
		String newStr = getExtendedContext(getContext()).getFormattedText();
		params.getStringBuilder().append(newStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END
	
	@Override
	public void getArgumentListExpressionNonTypeName(List<String> argumentListExpressionNonTypeNames)
	{
		argumentListExpressionNonTypeNames.add(this.getFormattedText());
	}
	
	@Override
	public Type getType(){
		Symbol symbol = this.resolve(this.getFormattedText());
		if (!(symbol instanceof NonTypeNameContextExt)){
			return symbol.getType();
		}
		return this;
	}
	
	@Override
	public String getTypeName(){
		if (!(this.getContext().getParent() instanceof TypeParameterListContext)){
		Symbol symbol = this.resolve(this.getFormattedText());
		if (!(symbol instanceof NonTypeNameContextExt)){
			return symbol.getTypeName();
		}
		}
		return this.getFormattedText();
	}
	
	@Override
	public String getSymbolName()
	{
		NonTypeNameContext ctx = getContext();
		NonTypeNameContextExt nameContextExt  = (NonTypeNameContextExt)getExtendedContext(ctx);
		return nameContextExt.getFormattedText();
	}
}
