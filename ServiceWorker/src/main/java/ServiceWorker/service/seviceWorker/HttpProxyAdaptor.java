package ServiceWorker.service.seviceWorker;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class HttpProxyAdaptor {
	
	RestTemplate restTemplate;
	
	public HttpProxyAdaptor() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		//
		try {
			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLContext sslContext;
			sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry =
					RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", sslsf)
					.register("http", new PlainConnectionSocketFactory())
					.build();
			
			BasicHttpClientConnectionManager connectionManager = 
					new BasicHttpClientConnectionManager(socketFactoryRegistry);
			
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
					.setConnectionManager(connectionManager).build();
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			requestFactory.setConnectionRequestTimeout(10000);
			requestFactory.setConnectTimeout(10000);
			requestFactory.setReadTimeout(300000);
			
			restTemplate = new RestTemplate(requestFactory);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<String> executeHttpProxy(String scheme, String url, String path, Map<String, Object> queryParams, HttpMethod method, HttpHeaders headers, String body) {
		//
		if (url != null) {
			url = scheme + "://" + url;
			UriComponentsBuilder uriComponentsBuilder = makeUriComponentsBuilder(scheme, url, path);
			
			if (queryParams != null) {
				for (Entry<String, Object> entry : queryParams.entrySet()) {
					uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
				}
			}
			
			URI uri = uriComponentsBuilder.scheme(scheme).build().encode().toUri();
			
			HttpEntity<String> requestEntity = new HttpEntity(body, headers);
			
			ResponseEntity<String> responseEntity = null;
			
			try {
				responseEntity = restTemplate.exchange(uri, method, requestEntity, String.class);
				return responseEntity;
			}
			catch (HttpClientErrorException e) {
	    		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	    	}
			catch (HttpServerErrorException e) {
	    		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	    	}
	    	catch (Exception e) {
	    		return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
	    	}
		}
		return new ResponseEntity("url is null", HttpStatus.BAD_REQUEST);
	}
	
	private UriComponentsBuilder makeUriComponentsBuilder(String scheme, String url, String path) {
		//
		return UrlHostUtil.makeUriComponentsBuilder(scheme, url, path);
	}
}