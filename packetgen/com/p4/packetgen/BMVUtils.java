package com.p4.packetgen;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.DRMTRunnerSession;

public class BMVUtils {
	private static final Logger L = LoggerFactory.getLogger(BMVUtils.class);
	public static Response sendToServer(String requestType, String requestJson, String bmvServerIp, Integer bmvServerPort) {
		
		URL url = null;
		HttpURLConnection conn = null;
		Response resp = null;
		try {
			url = new URL("http://" + bmvServerIp + ":" + bmvServerPort + "/" + requestType);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			OutputStream os = conn.getOutputStream();
			os.write(requestJson.getBytes());
			os.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder sb = new StringBuilder();
			while ((output = br.readLine()) != null) {
				sb.append(output + "\n");
			}
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			resp = new Response(mapper.readValue(sb.toString(), Response.UnNormalized.class));
		} catch (Exception e) {
			L.error(e.getMessage());
		} finally {
			if(conn != null)
				conn.disconnect();
		}
		return resp;
	}
	
	public static Response programTable(String cmd, String bmvServerIp, Integer bmvServerPort) {
		return sendToServer("program_table", "{\"cmd\":\"" + cmd +"\"}", bmvServerIp, bmvServerPort);
	}
	
	public static Response getExpectedPacket(String pkt, String bmvServerIp, Integer bmvServerPort) {
		return sendToServer("send_packet", "{\"pkt\":\"" + pkt +"\"}", bmvServerIp, bmvServerPort);
	}

}
