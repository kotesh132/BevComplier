package com.p4.pktgen.p4blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.p4.drmt.alu.CField;

import lombok.Getter;

import com.p4.drmt.cfg.KeyMeta;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ActionRefContextExt;
import com.p4.p416.gen.ActionsContextExt;
import com.p4.p416.gen.P416Parser.KeyContext;
import com.p4.p416.gen.P416Parser.KeyElementContext;
import com.p4.p416.gen.P416Parser.KeyElementListContext;
import com.p4.p416.gen.P416Parser.TablePropertyContext;
import com.p4.p416.gen.TableDeclarationContextExt;
import com.p4.pktgen.enums.KeyType;

@Getter
public class TableBlock {

	private TableDeclarationContextExt table;
	private Integer tableId;
	private String tableName;
	private List<String> tableActions;
	private List<TableKey> tableKeys;
	
	public TableBlock(TableDeclarationContextExt table, KeyMeta km) {
		this.table = table;
		tableActions = new ArrayList<String>();
		tableKeys = new ArrayList<TableKey>();
		tableId = table.getTableId();
		tableName = table.getTableName();
		
		for(ActionRefContextExt actionRefCtx : ((ActionsContextExt)table.getActionTablePropertyContext()).getActionList()) {
			if(!actionRefCtx.isDefaultAction())
				tableActions.add(actionRefCtx.getActionName());
		}
		
		extractKeyProperties(km);
	}
	
	private void extractKeyProperties(KeyMeta km) {
		TablePropertyContext propertyContext = table.getKeyTablePropertyContext().getContext();
		if(propertyContext instanceof KeyContext) {
			KeyContext keyContext = (KeyContext) propertyContext;
			if(keyContext.keyElementList() != null) {
				Map<String, CField> keyAFMap = new HashMap<String, CField>();
				for(CField af : km.getMap().get(tableId)){
					keyAFMap.put(af.getProgName(), af);
				}
				for(KeyElementContext keyElementContext : ((KeyElementListContext)AbstractBaseExt.getExtendedContext(keyContext.keyElementList()).getContext()).keyElement()) {
					String keyName =  AbstractBaseExt.getExtendedContext(keyElementContext.expression()).getFormattedText();
					keyName = keyName.replaceAll("\\(", "");
					keyName = keyName.replaceAll("\\)", "");
					String type = AbstractBaseExt.getExtendedContext(keyElementContext.name()).getFormattedText();
					CField cf = keyAFMap.get(keyName);
					if(cf.getSize()==1){
						tableKeys.add(new TableKey(keyName, KeyType.valueOf(type),
								keyAFMap.get(keyName).getOffset(), keyAFMap.get(keyName).getSize(), true));
					}else{
						tableKeys.add(new TableKey(keyName, KeyType.valueOf(type),
								keyAFMap.get(keyName).getOffset() * 8, keyAFMap.get(keyName).getSize(), false));
					}
				}
			}
		}
	}
	
	public Integer getTotalWidthOfKeys() {
		int size = 0;
		for(TableKey key : tableKeys) {
			size += key.getSize();
		}
		return size;
	}
}
