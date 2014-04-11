package org.wallride.web.support;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AmazonItemClient {

	private static final Charset REQUEST_CHARSET = Charset.forName("SJIS");
	private static final Logger logger = LoggerFactory.getLogger(AmazonItemClient.class);

	private String accessKey;
	private String secretAccessKey;
	private String url;

	public AmazonItemClient(String accessKey, String secretAccessKey, String url) {
		this.accessKey = accessKey;
		this.secretAccessKey = secretAccessKey;
		this.url = url;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public String getUrl() {
		return url;
	}

	public List<AmazonItemResult> executeItemSearch(String keyword) throws AmazonItemException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Service", "AWSECommerceService"));
		params.add(new BasicNameValuePair("AWSAccessKeyId", getAccessKey()));
		params.add(new BasicNameValuePair("Keywords", keyword));
		params.add(new BasicNameValuePair("ResponseGroup", "Small,Images,ItemAttributes,OfferSummary"));
		return execute(params);
	}

	private List<AmazonItemResult> execute(List<NameValuePair> params) throws AmazonItemException {
		try {
			HttpPost httppost = new HttpPost(getUrl());
			httppost.setEntity(new UrlEncodedFormEntity(params, REQUEST_CHARSET.name()));

			long startTime = System.nanoTime();
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new AmazonItemException();
			}

			long estimatedTime = System.nanoTime() - startTime;
			NumberFormat nanoformat = NumberFormat.getNumberInstance();
			nanoformat.setMaximumFractionDigits(3);
			nanoformat.setMinimumFractionDigits(3);
			logger.info("amazonへ通信が完了しました。処理時間 -> {}ms", nanoformat.format((double) estimatedTime / 1000 / 1000));

			Unmarshaller unmarshaller = JAXBContext.newInstance(AmazonItemResult.class).createUnmarshaller();
			List<AmazonItemResult> result = (List<AmazonItemResult>) unmarshaller.unmarshal(entity.getContent());
			return result;
		}
		catch (Exception e) {
			throw new AmazonItemException(e);
		}

	}
}
