Reciepe Book.


1) Description of the topology of the network.

2) Instructions to configure the network, internode connectivity.

3) instructions to setup/configure individual switches.

4) instructions on the type of the packets to send.

5) instructions on the type of the packets to recieve.

==============================================================================


For now intially we will be working on 3, 4 and 5

Problem Statement:

From the reciepe, we should be able to extract out the variables(symbols) and the associated constraints.

    input port: anything
    Ether() / IP(dst=<hdr.ipv4.dstAddr>, ttl=<ttl>)

and you create the following table entries:

    table_add ipv4_da_lpm set_l2ptr <hdr.ipv4.dstAddr>/32 => <l2ptr>
    table_add mac_da set_bd_dmac_intf <l2ptr> => <out_bd> <dmac> <out_intf>
    table_add send_frame rewrite_mac <out_bd> => <smac>

then the P4 program should produce an output packet like the one
below, matching the input packet in every way except, except for the
fields explicitly mentioned:

    output port: <out_intf>
    Ether(src=<smac>, dst=<dmac>) / IP(dst=<hdr.ipv4.dstAddr>, ttl=<ttl>-1)
    
    
    
    
    


  
List of the commands scapy supports.
arpcachepoison      : Poison target's cache with (your MAC,victim's IP) couple
arping              : Send ARP who-has requests to determine which hosts are up
bind_layers         : Bind 2 layers on some specific fields' values
corrupt_bits        : Flip a given percentage or number of bits from a string
corrupt_bytes       : Corrupt a given percentage or number of bytes from a string
defrag              : defrag(plist) -> ([not fragmented], [defragmented],
defragment          : defrag(plist) -> plist defragmented as much as possible 
dyndns_add          : Send a DNS add message to a nameserver for "name" to have a new "rdata"
dyndns_del          : Send a DNS delete message to a nameserver for "name"
etherleak           : Exploit Etherleak flaw
fragment            : Fragment a big IP datagram
fuzz                : Transform a layer into a fuzzy layer by replacing some default values by random objects
getmacbyip          : Return MAC address corresponding to a given IP address
hexdiff             : Show differences between 2 binary strings
hexdump             : --
hexedit             : --
is_promisc          : Try to guess if target is in Promisc mode. The target is provided by its ip.
linehexdump         : --
ls                  : List  available layers, or infos on a given layer
promiscping         : Send ARP who-has requests to determine which hosts are in promiscuous mode
rdpcap              : Read a pcap file and return a packet list
send                : Send packets at layer 3
sendp               : Send packets at layer 2
sendpfast           : Send packets at layer 2 using tcpreplay for performance
sniff               : Sniff packets
split_layers        : Split 2 layers previously bound
sr                  : Send and receive packets at layer 3
sr1                 : Send packets at layer 3 and return only the first answer
srbt                : send and receive using a bluetooth socket
srbt1               : send and receive 1 packet using a bluetooth socket
srflood             : Flood and receive packets at layer 3
srloop              : Send a packet at layer 3 in loop and print the answer each time
srp                 : Send and receive packets at layer 2
srp1                : Send and receive packets at layer 2 and return only the first answer
srpflood            : Flood and receive packets at layer 2
srploop             : Send a packet at layer 2 in loop and print the answer each time
traceroute          : Instant TCP traceroute
tshark              : Sniff packets and print them calling pkt.show(), a bit like text wireshark
wireshark           : Run wireshark on a list of packets
wrpcap              : Write a list of packets to a pcap file

~            
help(send) -> prints the format of the command. 

send(x, inter=0, loop=0, count=None, verbose=None, realtime=None, *args, **kargs)
    Send packets at layer 3
    send(packets, [inter=0], [loop=0], [verbose=conf.verb]) -> None
    
    
Sample1:


table_configuration:
table_add ipv4_da_lpm set_l2ptr hdr.ipv4.dstAddr/32 => l2ptr
table_add mac_da set_bd_dmac_intf l2ptr => out_bd dmac out_intf
table_add send_frame rewrite_mac out_bd => smac

table_configuration:
table_add ipv4_da_lpm set_l2ptr hdr.ipv4.dstAddr/32 => l2ptr
 
input: ANY

input_packets:
(Ether()/IP(dst=hdr.ipv4.dstAddr, ttl=ttl))

output:
port = out_intf
output_packets:
(Ether(src=smac,dst=dmac)/IP(dst=hdr.ipv4.dstAddr, ttl=ttl-1))


symbols:
hdr.ipv4.dstAddr,
l2ptr,
out_bd,
dmac,
out_intf,
out_bd,
smac,
ttl

Commands to run & compile the grammar

java -cp /Users/sgodugul/gerrit/dev/p4/packetgen/lib/manual/antlr-4.5-complete.jar org.antlr.v4.Tool -o /Users/sgodugul/gerrit/dev/p4/packetgen/recipebook/target/ ./grammar/recipe.g4
javac -classpath /Users/sgodugul/gerrit/dev/p4/packetgen/bin:/Users/sgodugul/gerrit/dev/p4/packetgen/lib/manual/antlr-4.5-complete.jar -d /Users/sgodugul/gerrit/dev/p4/packetgen/bin ./target/*.java
java -classpath /Users/sgodugul/gerrit/dev/p4/packetgen/bin:/Users/sgodugul/gerrit/dev/p4/packetgen/lib/manual/antlr-4.5-complete.jar org.antlr.v4.runtime.misc.TestRig recipe start -gui ./in/recipe1.txt
                                                                    
~                                                                                      
TODO: 
1)make inputport and out put port as part of teh packet declaration.
2) Each input packet  should be associated with one or more output packets.
3) Reference to source p4 file 
4) refer to partial states in the p4 program.

