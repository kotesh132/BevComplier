#include <stdio.h>
#include "headers.hpp"

<#list headers as header>
bool ${header.name}::isValid() {
    return this->valid;
}

void ${header.name}::setValid(bool valid) {
    this->valid = valid;
}

</#list>