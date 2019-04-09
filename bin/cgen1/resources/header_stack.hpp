#ifndef header_stack_hpp
#define header_stack_hpp


template <class T>
class header_stack {
private:
    std::vector<T> headers;
    int size;
    int nextIndex;
    
public:
    header_stack();
    header_stack(int size);
    T& operator[] (int index) const{
        return headers[index];
    }
    
    void push_front(int count);
    void pop_front(int count);
};

#endif /* header_stack_h */
