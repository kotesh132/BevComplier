#ifndef phvdeparser_hpp
#define phvdeparser_hpp

#include <stdio.h>
#include "headers.hpp"
#include "primitives.hpp"

template <int M, int N>
void deParsePacketVector(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit,<#list parameters as parameter>${parameter.type}* ${parameter.name}<#sep>, </#list>){
<#list parameters as parameter>
	emit_${parameter.name}(packet, packetBit, ${parameter.name});
</#list>
}

<#if (structTypeMap?size > 0) >
<#list structTypeMap?keys as key> 
template <int M, int N>
void emit_${key}(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ${structTypeMap[key].name}* ${key}){
<#list structTypeMap[key].structFields as structField>
	<#if structField.isBaseType>
	(*packet)["${structField.endSize}:${structField.startSize}"] = ${key}->${structField.name};
	<#else>
	emit_${structField.name}(packet, packetBit, &(${key}->${structField.name}));
	</#if>
</#list>
}
</#list> 
</#if>

<#if (headersMap?size > 0) >
<#list headersMap?keys as key> 
template <int M, int N>
void emit_${key}(cppgen::P4BitSet<M> *packet, cppgen::P4BitSet<N> *packetBit, ${headersMap[key].name}* ${key}){
	(*packetBit)[${headersMap[key].isValid}] = ${key}->isValid() ? 1 : 0;
    
    if (${key}->isValid()) {
<#list headersMap[key].structFields as structField>
	<#if structField.isBaseType>
	(*packet)["${structField.endSize}:${structField.startSize}"] = ${key}->${structField.name};
	<#else>
	emit_${structField.name}(packet, packetBit, &(${key}->${structField.name}));
	</#if>
</#list>
	}
}
</#list> 
</#if>

#endif /* phvdeparser_hpp */