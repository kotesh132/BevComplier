#ifndef headers_hpp
#define headers_hpp


#include "P4BitSet.h"

<#list headers as header>
class ${header.name}{
public:
	<#list header.structFields as structField>
    cppgen::P4BitSet<${structField.size}> ${structField.name};
    </#list>
	bool valid;

    bool isValid();
    void setValid(bool valid);
};

</#list>

<#list structTypes as structType>
class ${structType.name}{
public:
	<#list structType.structFields as structField>
	<#if structField.isBaseType>
	cppgen::P4BitSet<${structField.size}> ${structField.name};
	<#elseif structField.isTypeNameType>
	${structField.prefixedType} ${structField.name};
	</#if>
    </#list>
};

</#list>
#endif
