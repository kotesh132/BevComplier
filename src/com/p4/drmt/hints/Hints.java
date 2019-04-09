package com.p4.drmt.hints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Hints {
	private CompilerHints compilerHints;
	
	public Hints(Hints.UnNormalized un) {
		compilerHints = new CompilerHints(un.compilerHints);
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public CompilerHints.UnNormalized compilerHints;
	}
	
}
