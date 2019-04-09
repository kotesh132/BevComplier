package com.p4.tools.graph.structs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ControlMetaData {
	public final String name;
	public final ControlGraph cg;
	public Boolean applyTraverseStatus;
}
