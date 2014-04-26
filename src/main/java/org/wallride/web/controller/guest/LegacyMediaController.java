package org.wallride.web.controller.guest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@Controller
@RequestMapping("/{language}/media/stories/**")
public class LegacyMediaController {

	@RequestMapping()
	public void media(
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

		HttpClient httpClient = new DefaultHttpClient();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://qoly-jp.s3.amazonaws.com/images/stories/");
		builder.path(org.apache.commons.lang.StringUtils.substringAfter(path, "/media/stories/"));
		URI uri = builder.buildAndExpand().toUri();

		HttpGet httpGet = new HttpGet(uri);
		HttpResponse httpResponse = httpClient.execute(httpGet);

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.OK.value()) {
			response.sendError(statusCode);
			return;
		}

		response.setHeader("Content-Type", httpResponse.getFirstHeader("Content-Type").getValue());
		response.setHeader("Content-Length", httpResponse.getFirstHeader("Content-Length").getValue());
		response.setHeader("Last-Modified", httpResponse.getFirstHeader("Last-Modified").getValue());

		OutputStream out = null;
		InputStream in = null;
		try {
			out = response.getOutputStream();
			in = new BufferedInputStream(httpResponse.getEntity().getContent());
			byte[] buf = new byte[64 * 1024];
			int len = in.read(buf);
			while (len != -1) {
				out.write(buf, 0, len);
				len = in.read(buf);
			}
		}
		finally {
			if (in != null) in.close();
			if (out != null) out.close();
		}
	}
}
