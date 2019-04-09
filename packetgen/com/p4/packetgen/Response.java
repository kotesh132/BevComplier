package com.p4.packetgen;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Response {
	private String pkt;
	private boolean success;
	private String exception;
	
	public Response(Response.UnNormalized r) {
		this.pkt = r.pkt;
		this.success = r.success;
		this.exception = r.exception;
	}
	
	@NoArgsConstructor
    public static class UnNormalized{
		public String pkt;
		public boolean success;
		public String exception;
    }
}
