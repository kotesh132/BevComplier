package com.p4.drmt.hints;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CompilerHints {
	
		private List<String> inlining;
		private List<String> parser_extra_fields;
		private List<String> outer_layer_states;
		
		public CompilerHints(CompilerHints.UnNormalized un) {
			inlining = un.inlining;
			parser_extra_fields = un.parser_extra_fields;
			outer_layer_states = un.outer_layer_states;
		}
		
		@NoArgsConstructor
		public static class UnNormalized {
			public List<String> inlining;
			public List<String> parser_extra_fields;
			public List<String> outer_layer_states;
		}

}
