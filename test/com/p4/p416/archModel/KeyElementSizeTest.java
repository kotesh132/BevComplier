package com.p4.p416.archModel;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.p416.iface.IControl;
import com.p4.p416.iface.IKeyElement;
import com.p4.p416.iface.IP4Program;
import com.p4.p416.iface.ITable;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.utils.FileUtils;

public class KeyElementSizeTest {

	private static P4programContext p4programContext = null;

	@BeforeClass
	public static void setUp() {
		File inputFile = new File("test/com/p4/p416/archModel/KeyElementSizeData.p4");
		String text = FileUtils.readFileIntoString(inputFile, "\n");
		P416ParserUtil parserUtil = new P416ParserUtil();
		p4programContext = parserUtil.getP416Context(text, inputFile.getName());
		P4programContextExt.getExtendedContext(p4programContext).defineSymbol(null);
		P4programContextExt.getExtendedContext(p4programContext).getArchitecturalModel().getArchitecturalModelVisitors().get(0).visit(p4programContext);
	}

	@Test
	public void keyElementSizeTest() {
		List<IControl> controls = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getControls();
		for(IControl control : controls){
			List<ITable> tables = control.getTableObjects();
			for(ITable table: tables){
				List<IKeyElement> tableKeys = table.getKeyElements();
				for(IKeyElement keyElement : tableKeys){
					if ( keyElement.getKeyName().equals("hdr.ipv4.srcAddr")){
						if(keyElement.getKeyMatchKind().equals("exact")){
							Assert.assertTrue(keyElement.getSram() == 32);
							Assert.assertTrue(keyElement.getTcam() == null);
						}else if(keyElement.getKeyMatchKind().equals("lpm")){
							Assert.assertTrue(keyElement.getSram() == null);
							Assert.assertTrue(keyElement.getTcam() == 32);
						}
					}else if(keyElement.getKeyName().equals("hdr.ipv4.hdrChecksum")){
						if(keyElement.getKeyMatchKind().equals("exact")){
							Assert.assertTrue(keyElement.getSram() == 16);
							Assert.assertTrue(keyElement.getTcam() == null);
						}else if(keyElement.getKeyMatchKind().equals("lpm")){
							Assert.assertTrue(keyElement.getSram() == null);
							Assert.assertTrue(keyElement.getTcam() == 16);
						}
					}
				}
			}
		}
	}


	@Test
	public void TableRamSizeTest() {
		List<IControl> controls = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getControls();
		for(IControl control : controls){
			List<ITable> tables = control.getTableObjects();
			for(ITable table: tables){
				if(control.getNameString().equals("egress")){
					if(table.getNameString().equals("send_frame")){
						Assert.assertTrue(table.getSram() == 24576);
						Assert.assertTrue(table.getTcam() == 0);
					}else if(table.getNameString().equals("dummy")){
						Assert.assertTrue(table.getSram()  == 8192);
						Assert.assertTrue(table.getTcam() == 0);
					}
				}else if(control.getNameString().equals("egress1")){
					if(table.getNameString().equals("send_frame1")){
						Assert.assertTrue(table.getSram()  == 4096);
						Assert.assertTrue(table.getTcam() == 4096);
					}else if(table.getNameString().equals("dummy1")){
						Assert.assertTrue(table.getSram()  == 10240);
						Assert.assertTrue(table.getTcam() == 2048);
					}
				}
			}
		}
	}

	@Test
	public void ControlRamSizeTest() {
		IP4Program p4Program = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getP4Program();
		List<IControl> controls = p4Program.getControls();
		for(IControl control : controls){
			if(control.getNameString().equals("egress")){
				Assert.assertTrue(control.getSram() == 32768);
				Assert.assertTrue(control.getTcam() == 0);
			}else if(control.getNameString().equals("egress1")){
				Assert.assertTrue(control.getSram() == 14336);
				Assert.assertTrue(control.getTcam() == 6144);
			}
		}
	}

	@Test
	public void p4RamSizeTest() {
		IP4Program p4Program = ((P4programContextExt) P4programContextExt.getExtendedContext(p4programContext)).getP4Program();
		Assert.assertTrue(p4Program.getSram() == 47104);
		Assert.assertTrue(p4Program.getTcam() == 6144);
	}


}
