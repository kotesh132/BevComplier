#include <stdio.h>
#include "headers.hpp"

bool ethernet_t::isValid() {
    return this->valid;
}

void ethernet_t::setValid(bool valid) {
    this->valid = valid;
}

bool ipv4_t::isValid() {
    return this->valid;
}

void ipv4_t::setValid(bool valid) {
    this->valid = valid;
}

bool ipv6_t::isValid() {
    return this->valid;
}

void ipv6_t::setValid(bool valid) {
    this->valid = valid;
}

bool tcp_t::isValid() {
    return this->valid;
}

void tcp_t::setValid(bool valid) {
    this->valid = valid;
}

bool udp_t::isValid() {
    return this->valid;
}

void udp_t::setValid(bool valid) {
    this->valid = valid;
}

