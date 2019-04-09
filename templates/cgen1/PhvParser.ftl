#ifndef phvparser_hpp
#define phvparser_hpp

#include <stdio.h>
#include "P4BitSet.h"
#include "headers.hpp"
#include "primitives.hpp"

template <int M, int N>
void parsePacketVector(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit,<#list parameters as parameter>${parameter.type}* ${parameter.name}<#sep>, </#list>){
<#list parameters as parameter>
	extract_${parameter.name}(packet, packetBit, ${parameter.name});
</#list>
}

<#if (structTypeMap?size > 0) >
<#list structTypeMap?keys as key> 
template <int M, int N>
void extract_${key}(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ${structTypeMap[key].name}* ${key}){
<#list structTypeMap[key].structFields as structField>
	<#if structField.isBaseType>
	${key}->${structField.name} = (*packet)["${structField.endSize}:${structField.startSize}"];
	<#else>
	extract_${structField.name}(packet, packetBit, &(${key}->${structField.name}));
	</#if>
</#list>
}
</#list> 
</#if>

<#if (headersMap?size > 0) >
<#list headersMap?keys as key> 
template <int M, int N>
void extract_${key}(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ${headersMap[key].name}* ${key}){
	${key}->setValid((*packetBit)[${headersMap[key].isValid}] == 1);
    
    if (${key}->isValid()) {
<#list headersMap[key].structFields as structField>
	<#if structField.isBaseType>
	${key}->${structField.name} = (*packet)["${structField.endSize}:${structField.startSize}"];
	<#else>
	extract_${structField.name}(packet, packetBit, &(${key}->${structField.name}));
	</#if>
</#list>
	}
}
</#list> 
</#if>

#endif /* phvparser_hpp */