package id.go.kepriprov.kiisgateway.lib.loganderror;

import java.io.IOException;

import org.eclipse.jetty.server.AbstractNCSARequestLog;

public class AccessLogHandler extends AbstractNCSARequestLog {
	
	@Override
	protected boolean isEnabled() {		
		return true;
	}

	@Override
	public void write(String requestEntry) throws IOException {
		System.out.println(requestEntry);
	}
	
}
