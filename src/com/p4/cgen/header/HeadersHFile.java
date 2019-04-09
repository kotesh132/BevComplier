package com.p4.cgen.header;

import com.p4.p416.iface.*;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;

public class HeadersHFile extends BaseCFile {

    private static final String headersH = "headers.h";
    private static STGroupFile headersHStg = new STGroupFile("templates/p416/c/headers/headersH.stg");

    @Override
    public String getFileName() {
        return headersH;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        List<IHeader> headers = p4Program.getHeaders();

        //Get headers
        Map<String, Map<String, String>> headersToFieldsMap = new LinkedHashMap<>();
        for (IHeader header : headers) {
            List<IStructField> structFields = header.getStructFields();
            Map<String, String> fieldsMap = getFieldsMap(structFields);
            //Add valid field to header
            fieldsMap.put("isValid" , getCType(1));
            headersToFieldsMap.put(header.getNameString(), fieldsMap);
        }

        //Get structs
        List<IStruct> structs = p4Program.getStructs();
        for (IStruct struct : structs) {
            List<IStructField> structFields = struct.getStructFields();
            Map<String, String> fieldsMap = getFieldsMap(structFields);
            headersToFieldsMap.put(struct.getNameString(), fieldsMap);
        }


        List<IInstantiation> instantiations = p4Program.getInstantiations();

        writeHeadersH(headersToFieldsMap, instantiations);
    }

    private Map<String, String> getFieldsMap(List<IStructField> structFields) {
        Map<String, String> fieldsMap = new LinkedHashMap<>();
        for (IStructField field : structFields) {
            ITypeRef typeRef = field.getTypeRef();
//            if (typeRef.isHeaderStackType()) {
//                IHeaderStackType headerStackType = typeRef.getHeaderStackType();
//                int i = 0;
//                while (i < headerStackType.getHeaderStackSize()) {
//                    fieldsMap.put(field.getNameString() + "_" + i, typeRef.getPrefixedType());
//                    i++;
//                }
//            } else
            if (typeRef.isHeaderStackType()) {
                IHeaderStackType headerStackType = typeRef.getHeaderStackType();
                int headerStackSize = headerStackType.getHeaderStackSize();
                String typeString = typeRef.getPrefixedType();
                fieldsMap.put(field.getNameString() + "[" + headerStackSize + "]", typeString);
            } else if (typeRef.isBaseType()) {
                String sizeString = typeRef.getSize();
                int size = Integer.parseInt(sizeString);
                fieldsMap.put(field.getNameString() + getCVarArraySize(size), getCType(size));
            } else if (typeRef.isTypeName() || typeRef.isHeaderStackType()) {
                String typeString = typeRef.getPrefixedType();
                fieldsMap.put(field.getNameString(), typeString);
            } else {
                throw new RuntimeException("TypeRef for " + typeRef.getFormattedText() + " not handled yet");
            }
        }
        return fieldsMap;
    }

    private void writeHeadersH(Map<String, Map<String, String>> headersToFieldsMap, List<IInstantiation> instantiations) {
        STGroupFile stgGroup = getStgGroupFile();
        ST top = stgGroup.getInstanceOf(TOP);

        ST headersH_ifndef = stgGroup.getInstanceOf("headersH_ifndef");
        ST headersH_include = stgGroup.getInstanceOf("headersH_include");

        List<ST> structs = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> headersMapEntry : headersToFieldsMap.entrySet()) {
            ST headersH_struct = stgGroup.getInstanceOf("headersH_struct");
            headersH_struct.add("structName", headersMapEntry.getKey());
            headersH_struct.add("structFieldsMap", headersMapEntry.getValue());
            structs.add(headersH_struct);
        }

        ST headersH_externs = stgGroup.getInstanceOf("headersH_externs");
        headersH_externs.add("instantiations", instantiations);

        ST headersH_endif = stgGroup.getInstanceOf("headersH_endif");

        List<ST> stmts = new ArrayList<>();
        stmts.add(headersH_ifndef);
        stmts.add(headersH_include);
        stmts.addAll(structs);
        stmts.add(headersH_externs);
        stmts.add(headersH_endif);
        top.add(STMTS, stmts);

        writeTop(getFileName(), top);
    }


    @Override
    public STGroupFile getStgGroupFile() {
        return headersHStg;
    }
}
