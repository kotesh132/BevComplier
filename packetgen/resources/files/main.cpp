#include "Packet.cpp"
                     
int main(){        
	Packet p;
	const int NUM_TESTS=5;
	for(int i=0;i<NUM_TESTS;i++){
		p.randomize();
		std::cout << p.toString() << std::endl;
		// a string of 0's and 1's representing
		// the values in binary.
		const string s=p.header.serialize();

		// construct the PacketHeader back from string
		PacketHeader pkth;
		deserialize(s,pkth);

		// compare the original header and the newly constructed
		// packet header from the serialized string.
		if(CMP(p.header,pkth)==false) {
			std::cout <<"Error: Bad data/(DeSerialize|Serialize)"<<endl;
			abort();
		}
	}
	cout <<"All ("<<NUM_TESTS<<") tests passed."<<endl;
}
