package com.p4.cgen.interfaces;

import com.p4.p416.gen.P416Parser;
import com.p4.p416.iface.IP4Program;
import org.stringtemplate.v4.STGroupFile;

public interface ICFile {

    String getFileName();

    void generateFile(IP4Program p4Program);

    STGroupFile getStgGroupFile();
}
