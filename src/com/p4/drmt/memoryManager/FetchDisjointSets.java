package com.p4.drmt.memoryManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.p4.cgen1.data.StructType;
import com.p4.utils.Utils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.ds.DisjointSet;
import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.gen.HeaderUnionDeclarationContextExt;
import com.p4.p416.gen.StructTypeDeclarationContextExt;
import com.p4.p416.iface.IHeader;
import com.p4.p416.iface.IHeaderUnion;
import com.p4.p416.iface.IStruct;
import com.p4.p416.iface.IStructField;

public class FetchDisjointSets {

	private static final Logger L = LoggerFactory.getLogger(FetchDisjointSets.class);

	//form all the disjoint sets from the referenced variables taking header union also into consideration
	private List<IDisjointSet<IMemoryInstance>> disjointSets;

	// an intermediate tree structure formed from the referenced variables, which is used to form disjoint sets
	private Map<IMemoryInstance, IMemoryInstanceNode> referencedSymbolTree ;

	// map storing all the referenced varaibles and a boolean(indicating it belongs to header_union) to seggregate all the variables not belonging to union to other set
	private Map<IMemoryInstance, Boolean> memoryInstanceVisitedMap ;

	private Map<IMemoryInstance, Symbol> referencedSymbols;

	private Map<String, IMemoryInstance> isValidInstances ;

	public List<IDisjointSet<IMemoryInstance>> getDisjointSets(Map<IMemoryInstance, Symbol> referencedSymbols) {
		this.referencedSymbols = referencedSymbols;
		disjointSets = new ArrayList<>();
		memoryInstanceVisitedMap = new LinkedHashMap<>();
		referencedSymbolTree = new LinkedHashMap<>();

		buildReferencedSymbolTree();// forms tree

		L.debug("nodes in referencedSymbolTable is "+referencedSymbolTree.size());

		constructDisjointSetsFromTree();

		constructIsValidSet(); //getting a isValid symbol for each header and header_union instance

		L.debug("total number of disjointsets is "+ disjointSets.size());
		return disjointSets;
	}

	private void buildReferencedSymbolTree(){
		L.debug("building referenced symbol tree with all the referenced symbols");
		for(IMemoryInstance iMemoryInstance : referencedSymbols.keySet()){
			memoryInstanceVisitedMap.put(iMemoryInstance, false);// populating memory instance map
			String referencedVaribale = iMemoryInstance.getInstanceName();
			String[] symbolNameParts = referencedVaribale.split("\\.",2);

			Symbol usage = referencedSymbols.get(iMemoryInstance);
			String usageName = symbolNameParts[0];
			usage = usage.resolve(usageName);
			MemoryInstance instance = new MemoryInstance(usageName, usage.getType());
			IMemoryInstanceNode initialNode;
			if(usage.getType() instanceof IHeaderUnion)
				initialNode = referencedSymbolTree.computeIfAbsent(instance, MemoryInstanceHeaderUnionNode::new);
			else
				initialNode = referencedSymbolTree.computeIfAbsent(instance, MemoryInstanceNode::new);

			if(symbolNameParts.length > 1)
				constructTreeOfReferencedFields(symbolNameParts[1], usage.getType(), initialNode, iMemoryInstance, usageName);
		}
	}

	private void constructTreeOfReferencedFields(String referenceVariable, Symbol usage, IMemoryInstanceNode initialNode, IMemoryInstance referenceMemoryInstance, String instancePrefix) {
		String[] symbolNameParts = referenceVariable.split("\\.", 2);
		usage = usage.resolve(symbolNameParts[0]);

		String instanceName = instancePrefix+"."+symbolNameParts[0];
		MemoryInstance memoryInstance = new MemoryInstance(instanceName, usage.getType());
		IMemoryInstanceNode newMemoryInstanceNode;
		if(usage.getType() instanceof IHeaderUnion)
			newMemoryInstanceNode = initialNode.getChildren().computeIfAbsent(memoryInstance, MemoryInstanceHeaderUnionNode::new);
		else
			newMemoryInstanceNode = initialNode.getChildren().computeIfAbsent(memoryInstance, MemoryInstanceNode::new);

		if (symbolNameParts.length > 1) {
			constructTreeOfReferencedFields(symbolNameParts[1], usage.getType(), newMemoryInstanceNode, referenceMemoryInstance, instanceName);
		} else {
			initialNode.getChildren().remove(memoryInstance);
			initialNode.getChildren().put(referenceMemoryInstance, newMemoryInstanceNode);
			newMemoryInstanceNode.setMemoryInstance(referenceMemoryInstance);
		}
	}

	private void constructDisjointSetsFromTree(){
		for(Entry<IMemoryInstance, IMemoryInstanceNode> referencedSymbolInTable : referencedSymbolTree.entrySet()){
			segregateAllDisjointSets(referencedSymbolInTable);
		}

		List<IMemoryInstance> outsideHeaderUnion = new ArrayList<>();
		for(Entry<IMemoryInstance, Boolean> memoryInstanceVisited : memoryInstanceVisitedMap.entrySet()){
			if(!memoryInstanceVisited.getValue()){
				outsideHeaderUnion.add(memoryInstanceVisited.getKey());
			}
		}
		IDisjointSet<IMemoryInstance> outsideHeaderUnionDisjointSet = new DisjointSet<>();
		outsideHeaderUnionDisjointSet.addEquivalenceSet(outsideHeaderUnion);
		disjointSets.add(outsideHeaderUnionDisjointSet);

	}

	private void segregateAllDisjointSets(Entry<IMemoryInstance, IMemoryInstanceNode> referencedSymbolInTable){
		if( referencedSymbolInTable.getValue() instanceof MemoryInstanceHeaderUnionNode){
			IDisjointSet<IMemoryInstance> headerUnionDisjointSet = ((MemoryInstanceHeaderUnionNode)referencedSymbolInTable.getValue()).getHeaderFieldsAsDisjointSet(memoryInstanceVisitedMap);
			disjointSets.add(headerUnionDisjointSet);
		}else{
			for(Entry<IMemoryInstance, IMemoryInstanceNode>  childSymbol : referencedSymbolInTable.getValue().getChildren().entrySet()){
				segregateAllDisjointSets(childSymbol);
			}
		}
	}


	private void constructIsValidSet() {

		IDisjointSet<IMemoryInstance> isValidDisjointSet = new DisjointSet<>();
		isValidInstances = new LinkedHashMap<String, IMemoryInstance>() ;
		for(Entry<IMemoryInstance, IMemoryInstanceNode> referencedSymbolInTable : referencedSymbolTree.entrySet()){
			putValidSymbolForHeaderOrHeaderUnion(isValidInstances, referencedSymbolInTable.getKey().getType(), referencedSymbolInTable.getKey().getInstanceName(), referencedSymbolInTable.getValue().getChildren());
		}

		List<IMemoryInstance> isValidInstancesSet = new ArrayList<>(isValidInstances.values());

		isValidInstancesSet.forEach(s -> L.debug("isValid instance added is "+s.getInstanceName()));

		isValidDisjointSet.addEquivalenceSet(isValidInstancesSet);
		this.disjointSets.add(isValidDisjointSet);
		L.info("total number of isValid symbols is "+ isValidInstancesSet.size());
		if(isValidInstancesSet.size()>0) {
			L.info("First isValid member = " + isValidInstancesSet.get(0).getInstanceName());
			L.info("Last isValid member = " + isValidInstancesSet.get(isValidInstancesSet.size() - 1).getInstanceName());
		}
	}

	private void putValidSymbolForHeaderOrHeaderUnion(Map<String, IMemoryInstance> isValidSet, Type instanceType, String instanceName, 
			Map<IMemoryInstance, IMemoryInstanceNode> children){
		if( instanceType instanceof IHeaderUnion){
			IMemoryInstance isValidInstance = formIsValidInstance(instanceName, instanceType);
			isValidSet.putIfAbsent(isValidInstance.getInstanceName(), isValidInstance);

			HeaderUnionDeclarationContextExt headerUnionDeclarationContextExt = (HeaderUnionDeclarationContextExt)instanceType;
			List<IStructField> headersInHeaderUnion = headerUnionDeclarationContextExt.getStructFields();
			for(IStructField headerInHeaderUnion: headersInHeaderUnion){
				isValidInstance = formIsValidInstance(instanceName+"."+headerInHeaderUnion.getNameString(), instanceType);
				isValidSet.putIfAbsent(isValidInstance.getInstanceName(), isValidInstance);
			}

		}else if(instanceType instanceof IHeader){
			String validInstanceName = instanceName;
			IMemoryInstance isValidInstance  = formIsValidInstance(validInstanceName, instanceType);
			isValidSet.putIfAbsent(isValidInstance.getInstanceName(), isValidInstance);

		}else if(instanceType instanceof IStruct){
			StructTypeDeclarationContextExt structTypeDeclarationContextExt = (StructTypeDeclarationContextExt)instanceType;
			List<IStructField> headersInStructType = structTypeDeclarationContextExt.getStructFields();
			for(IStructField headerInStructType: headersInStructType){
				String headerInStructName = instanceName+"."+headerInStructType.getNameString();
				putValidSymbolForHeaderOrHeaderUnion(isValidSet, headerInStructType.getType(), headerInStructName, null);
			}
		}
		if(children != null){
			for( Entry<IMemoryInstance, IMemoryInstanceNode> memoryInstance : children.entrySet()){
				putValidSymbolForHeaderOrHeaderUnion(isValidSet, memoryInstance.getKey().getType(), memoryInstance.getKey().getInstanceName(),
						memoryInstance.getValue().getChildren());
			}
		}
	}

	private IMemoryInstance formIsValidInstance(String isValidPrefix, Symbol resolveScope){
		String validInstanceName = isValidPrefix+".isValid";
		Symbol isValidType = resolveScope.resolve(validInstanceName);
		IMemoryInstance isValidInstance = new MemoryInstance(validInstanceName, (Type) isValidType);
		return isValidInstance;
	}

	Map<String, IMemoryInstance> getIsValidInstances(){
		return isValidInstances;
	}

	public Utils.Pair<String, String> getFirstLastisValidInstances(){
		String first, last;
		first = isValidInstances.keySet().stream().reduce((f, s) -> f).get().toString();
		last = isValidInstances.keySet().stream().reduce((f, s) -> s).get().toString();
		return Utils.Pair.of(first, last);
	}

	public List<String> allIsValidInstances(){
		return new ArrayList<>(isValidInstances.keySet());
	}

}
