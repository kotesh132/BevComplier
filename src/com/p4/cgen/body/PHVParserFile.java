package com.p4.cgen.body;

import com.p4.cgen.header.PHVParserHFile;
import com.p4.p416.applications.Type;
import com.p4.p416.iface.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

public class PHVParserFile extends PHVParserHFile {
    private static final String phvparser = "phvparser.c";
    private static STGroupFile phvparserStg = new STGroupFile("templates/p416/c/phvparser.stg");
    private static final Logger L = LoggerFactory.getLogger(PHVParserFile.class);

    @Override
    public String getFileName() {
        return phvparser;
    }

    @Override
    public void generateFile(IP4Program p4Program) {

        List<IInstantiation> instantiations = p4Program.getInstantiations();
        String params = getPHVParserMethodParams(instantiations);

        Map<String, List<String>> headerToLinesMap = new LinkedHashMap<>();
        Map<String, String> headerToValidMap = new LinkedHashMap<>();
        Map<String, String> headerToParamsMap = new LinkedHashMap<>();

        Queue<TypeAndInstanceName> typeAndInstanceNameQueue = new ArrayDeque<>();

        Map<String, String> prefixAtInstance = new HashMap<>();

        List<String> lines = new ArrayList<>();
        for (IInstantiation instantiation : instantiations) {
            Type type = instantiation.getType();
            if (type instanceof IStruct || type instanceof IHeader) {
                lines.add(getParentHeaderOrStructExtractLine(instantiation.getNameString()));
                typeAndInstanceNameQueue.offer(new TypeAndInstanceName(type, instantiation.getNameString()));
                prefixAtInstance.put(instantiation.getNameString(), instantiation.getNameString());
            }
        }
        headerToLinesMap.put("parsePacketVector", lines);
        headerToParamsMap.put("parsePacketVector", params);

        Map<String, Integer> map = readPhvFormatFromJson();

        String DOT = ".";
        while (!typeAndInstanceNameQueue.isEmpty()) {
            TypeAndInstanceName typeAndInstanceName = typeAndInstanceNameQueue.poll();
            String instanceName = typeAndInstanceName.getInstanceName();
            Type type = typeAndInstanceName.getType();
            if (type instanceof IStruct) {
                List<IStructField> structFields = ((IStruct) type).getStructFields();
                lines = new ArrayList<>();
                for (IStructField structField : structFields) {
                    Type structFieldType = structField.getType();
                    String flattenedName = prefixAtInstance.get(instanceName) + DOT + structField.getNameString();
                    if (structFieldType instanceof IStruct | structFieldType instanceof IHeader) {
                        ITypeRef typeRef = structField.getTypeRef();
                        if (typeRef.isHeaderStackType()) {
                            int headerStackSize = typeRef.getHeaderStackType().getHeaderStackSize();
                            int i = 0;
                            while (i < headerStackSize) {
                                String headerStackField = structField.getNameString() + "[" + i + "]";
                                String headerStackNormalizedField = structField.getNameString() + "_" + i;
                                lines.add(getHeaderStackExtractLine(instanceName, headerStackField, headerStackNormalizedField));
                                prefixAtInstance.put(headerStackNormalizedField, flattenedName + "_" + i);
                                typeAndInstanceNameQueue.offer(new TypeAndInstanceName(structFieldType, headerStackNormalizedField));
                                i++;
                            }
                        } else {
                            lines.add(getHeaderOrStructExtractLine(instanceName, structField.getNameString()));
                            prefixAtInstance.put(structField.getNameString(), flattenedName);
                            typeAndInstanceNameQueue.offer(new TypeAndInstanceName(structFieldType, structField.getNameString()));
                        }
                    } else if (structFieldType != null && structFieldType.getSizeInBytes() > 0) {
                        Integer phvBitOffset = map.get(flattenedName);
                        int phvByteOffset = phvBitOffset != null ? phvBitOffset / 8 : 0;
                        lines.add(getFieldLine(instanceName, structField.getNameString(), phvByteOffset, structFieldType.getSizeInBytes()));
                    }
                }
                headerToLinesMap.put(instanceName, lines);
                headerToParamsMap.put(instanceName, getParamsForHeader(instanceName, ((IStruct) type).getNameString()));
            }
            if (type instanceof IHeader) {
                List<IStructField> structFields = ((IHeader) type).getStructFields();
                String validFieldFlattenedName = prefixAtInstance.get(instanceName) + DOT + "isValid";
                Integer validBitOffset = map.get(validFieldFlattenedName);
                String validLine = getValidLine(instanceName, validBitOffset != null ? validBitOffset : 0);
                headerToValidMap.put(instanceName, validLine);
                lines = new ArrayList<>();
                for (IStructField structField : structFields) {
                    Type structFieldType = structField.getType();
                    String flattenedName = prefixAtInstance.get(instanceName) + DOT + structField.getNameString();
                    if (structFieldType instanceof IStruct | structFieldType instanceof IHeader) {
                        lines.add(getHeaderOrStructExtractLine(instanceName, structField.getNameString()));
                        typeAndInstanceNameQueue.offer(new TypeAndInstanceName(structFieldType, structField.getNameString()));
                        prefixAtInstance.put(structField.getNameString(), flattenedName);
                    } else if (structFieldType != null && structFieldType.getSizeInBytes() > 0) {
                        Integer phvBitOffset = map.get(flattenedName);
                        int phvByteOffset = phvBitOffset != null ? phvBitOffset / 8 : -100;
                        lines.add(getFieldLine(instanceName, structField.getNameString(), phvByteOffset, structFieldType.getSizeInBytes()));
                    }
                }
                headerToLinesMap.put(instanceName, lines);
                headerToParamsMap.put(instanceName, getParamsForHeader(instanceName, ((IHeader) type).getNameString()));
            }
        }

        headerToLinesMap = reverseMap((LinkedHashMap<String, List<String>>) headerToLinesMap);
        writePHVParser(headerToLinesMap, headerToParamsMap, headerToValidMap);
    }

    @Data
    @AllArgsConstructor
    private static class TypeAndInstanceName {
        private Type type;
        private String instanceName;

    }

    private Map<String, Integer> readPhvFormatFromJson(){
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, Integer> map  = new LinkedHashMap<>();
        try {
            if (!phvOffsetsJsonFile.exists()) {
                L.error("Packet vector layout json file not available");
                throw new RuntimeException("Packet vector layout json file not available");
            }
            map = mapper.readValue(phvOffsetsJsonFile, LinkedHashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
            exit(1);
        }
        return map;
    }



    private void writePHVParser(Map<String, List<String>> headerToLinesMap, Map<String, String> headerToParamsMap, Map<String, String> headerToValidMap) {
        STGroupFile stgGroupFile = getStgGroupFile();
        ST top = stgGroupFile.getInstanceOf(TOP);

        ST phvparser_include = stgGroupFile.getInstanceOf("phvparser_include");

        List<ST> phvparser_extractMethods = new ArrayList<>();
        for (String header : headerToLinesMap.keySet()) {
            ST extractMethod = null;
            if ("parsePacketVector".equals(header)) {
                extractMethod = stgGroupFile.getInstanceOf("phvparser_parsePacketVector");
                extractMethod.add("params", headerToParamsMap.get(header));
            } else if (headerToValidMap.containsKey(header)) {
                extractMethod = stgGroupFile.getInstanceOf("phvparser_parseHeader");
                extractMethod.add("validLine", headerToValidMap.get(header));
                extractMethod.add("methodSignature", headerToParamsMap.get(header));
            } else {
                extractMethod = stgGroupFile.getInstanceOf("phvparser_parseStruct");
                extractMethod.add("methodSignature", headerToParamsMap.get(header));
            }

            extractMethod.add("objName", header);
            extractMethod.add("lines", headerToLinesMap.get(header));

            phvparser_extractMethods.add(extractMethod);

        }

        List<ST> stmts = new ArrayList<>();

        top.add(STMTS, stmts);
        stmts.add(phvparser_include);
        stmts.addAll(phvparser_extractMethods);
        writeTop(getFileName(), top);

    }

    private LinkedHashMap<String, List<String>> reverseMap(LinkedHashMap<String, List<String>> linkedHashMap) {
        ArrayList<String> keys = new ArrayList<>(linkedHashMap.keySet());
        Collections.reverse(keys);
        LinkedHashMap<String, List<String>> reversedLinkedHashMap = new LinkedHashMap<>();
        for (String key : keys) {
            reversedLinkedHashMap.put(key, linkedHashMap.get(key));
        }
        return reversedLinkedHashMap;
    }

    private String getValidLine(String header, int bitOffset) {
        return "EXTRACT_BIT(packetBit, (uint8_t *)&(" + header + "->isValid), " + bitOffset + ")";
    }

    private String getFieldLine(String header, String field, int byteOffset, int byteWidth) {
        return "extractAndLoad(packet, (uint8_t *) &(" + header + "->" + field + "), " + byteOffset + ", " + byteWidth + ")";
    }

    private String getHeaderOrStructExtractLine(String parentHeader, String header) {
        return "extract_" + header + "(packet, packetBit, &(" + parentHeader + "->" + header + "))";
    }

    private String getHeaderStackExtractLine(String parentHeader, String header, String headerNormalized) {
        return "extract_" + headerNormalized + "(packet, packetBit, &(" + parentHeader + "->" + header + "))";
    }

    private String getParentHeaderOrStructExtractLine(String header) {
        return "extract_" + header + "(packet, packetBit, " + header + ")";
    }

    private String getParamsForHeader(String header, String headerType) {
        return "void extract_" + header + "(packet_in *packet, packet_in *packetBit, " + headerType + "* " + header + ")";
    }

    @Override
    public STGroupFile getStgGroupFile() {
        return phvparserStg;
    }
}
