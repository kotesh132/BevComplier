#include <stdio.h>
#include "headers.hpp"
#include "header_stack.hpp"

template <class T>
header_stack<T>::header_stack(int size) {
    this->headers.resize(size);
    this->size = size;
    this->nextIndex = 0;
}

template <class T>
void header_stack<T>::push_front(int count){
    for (int i = this->size-1; i >= 0; i -= 1) {
        if (i >= count) {
            this->headers[i] = this[i-count];
        } else {
            this->headers[i].isValid = 0;
        }
    }
    this->nextIndex = this->nextIndex + count;
    if (this->nextIndex > this->size) this->nextIndex = this->size;
}

template <class T>
void header_stack<T>::pop_front(int count){
    for (int i = 0; i < this->size; i++) {
        if (i+count < this->size) {
            this->headers[i] = this[i+count];
        } else {
            this->headers[i].setInvalid();
        }
    }
    if (this->nextIndex >= count) {
        this->nextIndex = this->nextIndex - count;
    } else {
        this->nextIndex = 0;
    }
}

