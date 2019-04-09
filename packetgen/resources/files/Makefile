CXX = g++
CPPFLAGS = -std=c++0x
VCS_HOME = /auto/edatools/synopsys/vcs/v2017.03mx
CXXFLAGS =  -I$(VCS_HOME)/include -I$(VCS_HOME)/include/systemc23
VCSSOLVER = $(VCS_HOME)/amd64/lib/libvcssolver.so 
SYSTEMCGCC = $(VCS_HOME)/amd64/lib/libsystemc23-gcc452-64.a 
LDFLAGS =  -ldl -lpthread
TARGET_EXE = pgen_test

.PHONY : all
	
all :	
	make $(TARGET_EXE)
	make test

$(TARGET_EXE) : main.cpp
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) $< $(VCSSOLVER) $(SYSTEMCGCC) -o $@ $(LDFLAGS)


test :
	./pgen_test | grep "All" | grep "passed"

help :
	@echo  'make all to run the tests.'
	@echo  'make test to check the binary.'
	@echo  'make clean to remove .o and binary.'

clean :
	rm -f *.o pgen_test


