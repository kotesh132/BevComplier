package com.p4.packetgen.utils;


import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
/*
 *  Customised error messaging for ANTLR 
 */
public class DescriptiveErrorListener extends BaseErrorListener {
	private static final Logger L = LoggerFactory.getLogger(DescriptiveErrorListener.class);

	public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();
	private static final boolean REPORT_SYNTAX_ERRORS = true;
	private String errorMsg = "";
	@Getter
	public boolean hasError = false;

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		if (!DescriptiveErrorListener.REPORT_SYNTAX_ERRORS) {
			return;
		} else {
			hasError = true;
		}

		String sourceName = recognizer.getInputStream().getSourceName();
		if (!sourceName.isEmpty() && !sourceName.equals("<unknown>")) {
			sourceName = String.format("%s : line %d: col %d: ", sourceName, line, charPositionInLine);
		} else {
			sourceName = String.format("line %d: col %d: ", line, charPositionInLine);
		}

		errorMsg = sourceName + " " + msg;
		DescriptiveErrorListener.L.error(errorMsg);
		System.err.println(errorMsg);
	}

	@Override
	public String toString() {
		return errorMsg;
	}

}

