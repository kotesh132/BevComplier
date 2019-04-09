package com.p4.p416.iface;

import java.util.List;

public interface IP4Program extends IExtendedContext {
	List<IHeader> getHeaders();

	List<IHeaderUnion> getHeaderUnions();

	List<IStruct> getStructs();

	List<IControl> getControls();

	List<IControl> getControlsInMain();

	List<IControl> getDeparser();

	List<IParser> getParsers();

	List<IPackage> getPackages();

	IInstantiation getMainInstantiation();

	List<IInstantiation> getAllInstantiations();

	List<IInstantiation> getInstantiations();

	IMatchKindDeclaration getMatchKindDeclaration();

    public List<String> getUsedStructFields();

	Integer getSram();

	Integer getTcam();

}
