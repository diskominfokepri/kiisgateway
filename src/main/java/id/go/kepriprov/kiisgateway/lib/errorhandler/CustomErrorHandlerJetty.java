package id.go.kepriprov.kiisgateway.lib.errorhandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

public class CustomErrorHandlerJetty extends ErrorHandler{

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		response.setContentType("text/json");		
		switch (response.getStatus()) {
			case 404 :
				response.getWriter()
				.append("{\"connection\":\"-1\",")
				.append("\"message\":\"alamat Web Service: ")
				.append("http:"+baseRequest.getHttpURI().toString())
				.append(" tidak ditemukan. silahkan baca kembali manual daftar service yang disediakan oleh KIISGateway\"}");
			break;
			default :
				response.getWriter()
				.append("{\"message\":\"HTTP error ")
				.append(String.valueOf(response.getStatus()))
				.append("\"}");
		} 				
	}
	
}
