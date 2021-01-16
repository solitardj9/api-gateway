package serviceWorker2.model.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.Data;
import serviceWorker2.model.config.ServiceWorkerConfigComposite;
import serviceWorker2.model.config.ServiceWorkerConfigSingle;
import serviceWorker2.model.utils.JsonUtil;
import serviceWorker2.model.worker.model.SequenceKey;

@Data
public class ServiceWorkerComposite {
	
	private String requestId;		// 관리 및 log 용 ID
	
	private ServiceWorkerConfigComposite config;
	
	private Map<JSONObject, ServiceWorkerSingle> sequence = new HashMap<>();
	
	private String inputPath;
	
	private HttpHeaders inputHttpHeaders;
	
	private Map<String, Object> inputQueryParams;
	
	private String inputRequestBody;
	
	private DocumentContext dcInputRequestBody;
	
	private String outgoingRequestBody;
	
	private HttpStatus outgoingHttpStatus;
	
	public void addServiceWorkerConfigComposite(ServiceWorkerConfigComposite config) {
		//
		requestId = UUID.randomUUID().toString();
		this.config = config;
	}
	
	public void addServiceWorkerConfigComposite(String requestId, ServiceWorkerConfigComposite config) {
		//
		this.requestId = requestId;
		this.config = config;
	}
	
	public void addServiceWorkerConfigSingle(Integer index, ServiceWorkerConfigSingle config) {
		//
		SequenceKey sequenceKey = new SequenceKey(index, config.getName());
		ServiceWorkerSingle serviceWorkerSingle = new ServiceWorkerSingle(requestId, config);
		serviceWorkerSingle.setServiceWorkerComposite(this);
		
		sequence.put(makeJsonKey(sequenceKey), serviceWorkerSingle);
	}
	
	private JSONObject makeJsonKey(SequenceKey sequenceKey) {
		//
		JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(sequenceKey));
		return jsonObject;
	}	
	
	private ServiceWorkerSingle getServiceWorkerSingleFromSequenceById(Integer index) {
		// fastjson json-path
		return find("[index = " + index.toString() + "]");
	}
	
	private ServiceWorkerSingle getServiceWorkerSingleFromSequenceByApi(String api) {
		// fastjson json-path
		return find("[api = '" + api + "']");
	}
	
	public ServiceWorkerSingle find(String predicate) {
		//
		Set<JSONObject> keySet = sequence.keySet();
		JSONArray jsonArray = (JSONArray) JSONPath.eval(keySet, predicate);

		ServiceWorkerSingle serviceWorkerSingle = sequence.get(jsonArray.get(0));
		return serviceWorkerSingle;
	}
	
	public void setInputs(HttpHeaders inputHttpHeaders, Map<String, Object> inputQueryParams, String inputRequestBody) {
		//
		if(inputHttpHeaders != null)
			this.inputHttpHeaders = inputHttpHeaders;
		else
			this.inputHttpHeaders = new HttpHeaders();
		
		if(inputQueryParams != null)
			this.inputQueryParams = inputQueryParams;
		else
			this.inputQueryParams = new HashMap<>();
		
		if(inputRequestBody != null)
			this.inputRequestBody = inputRequestBody;
		else
			this.inputRequestBody = "{}";
		dcInputRequestBody = JsonPath.parse(this.inputRequestBody);
	}
	
	public void executeWork() {
		//
		ServiceWorkerSingle prevServiceWorkerSingle = null;
		ServiceWorkerSingle nextServiceWorkerSingle = null;
		
		for (Integer index = 0 ; index < sequence.size() ; index++) {
			//
			nextServiceWorkerSingle = getServiceWorkerSingleFromSequenceById(index);
			
			System.out.println("[ServiceWorkerComposite].executeWork : ServiceWorkerSingle's name = " + nextServiceWorkerSingle.getConfig().getName());
			
			if (prevServiceWorkerSingle == null) {
				nextServiceWorkerSingle.setInputs(inputHttpHeaders, inputQueryParams, inputRequestBody);
				System.out.println("//---------------------------------------------");
				System.out.println("	1-InputHttpHeaders = " + nextServiceWorkerSingle.getInputHttpHeaders());
				System.out.println("	1-InputQueryParams = " + nextServiceWorkerSingle.getInputQueryParams());
				System.out.println("	1-InputRequestBody = " + nextServiceWorkerSingle.getInputRequestBody());
				System.out.println("//---------------------------------------------");
			}
			else {
				System.out.println("//---------------------------------------------");
				System.out.println("	2-InputHttpHeaders = " + prevServiceWorkerSingle.getOutgoingHttpHeaders());
				System.out.println("	2-InputQueryParams = " + prevServiceWorkerSingle.getOutgoingQueryParams());
				System.out.println("	2-InputRequestBody = " + prevServiceWorkerSingle.getOutgoingRequestBody());
				System.out.println("//---------------------------------------------");
				nextServiceWorkerSingle.setInputs(prevServiceWorkerSingle.getOutgoingHttpHeaders(), prevServiceWorkerSingle.getOutgoingQueryParams(), prevServiceWorkerSingle.getOutgoingRequestBody());
				System.out.println("//---------------------------------------------");
				System.out.println("	2-InputHttpHeaders = " + nextServiceWorkerSingle.getInputHttpHeaders());
				System.out.println("	2-InputQueryParams = " + nextServiceWorkerSingle.getInputQueryParams());
				System.out.println("	2-InputRequestBody = " + nextServiceWorkerSingle.getInputRequestBody());
				System.out.println("//---------------------------------------------");
			}
			nextServiceWorkerSingle.executeWork();
			System.out.println("//---------------------------------------------");
			System.out.println("	OutgoingResponseBody = " + nextServiceWorkerSingle.getOutgoingResponseBody());
			System.out.println("//---------------------------------------------");
			prevServiceWorkerSingle = nextServiceWorkerSingle;
			
			if (index.equals(sequence.size() - 1)) {
				outgoingRequestBody = prevServiceWorkerSingle.getOutgoingResponseBody();
				outgoingHttpStatus = prevServiceWorkerSingle.getInputResponseEntity().getStatusCode();
			}
		}
	}
	
	// Inner Interface --------------------------------------------------------------------------------------------------
	// Http Header --------------------------
	public Object getValueOfInputHttpHeader(String key, Integer headerReference) {
		// read value of specific key path in inputHttpHeader
		if (headerReference == null)
			return this.inputHttpHeaders.get(key);
		else
			return this.inputHttpHeaders.get(key).get(headerReference);
	}
	
	// Query Parameters ---------------------
	public Object getValueOfInputQueryParams(String key) {
		// read value of specific key path in inputHttpHeader
		return this.inputQueryParams.get(key);
	}
	
	// Body ---------------------------------
	public Object getValueOfInputRequestBody(String key) {
		// read value of specific key path in inputRequestBody
		return JsonUtil.readValue(dcInputRequestBody, key);
	}
	//-------------------------------------------------------------------------------------------------------------------
	
	// Outer Interface --------------------------------------------------------------------------------------------------
	public Object getValueOfHttpHeaderFromOhterApi(String serviceWorkerName, String key, Integer headerReference) {
		//
		return getServiceWorkerSingleFromSequenceByApi(serviceWorkerName).getValueOfHeaderByComposite(key, headerReference);
	}
	
	public Object getValueOfQueryParamsFromOhterApi(String serviceWorkerName, String key) {
		//
		return getServiceWorkerSingleFromSequenceByApi(serviceWorkerName).getValueOfQueryParamByComposite(key);
	}
	
	public Object getValueOfRequestBodyFromOhterApi(String serviceWorkerName, String key) {
		//
		return getServiceWorkerSingleFromSequenceByApi(serviceWorkerName).getValueOfRequestBodyByComposite(key);
	}
	
	public Object getValueOfResponseBodyFromOhterApi(String serviceWorkerName, String key) {
		//
		return getServiceWorkerSingleFromSequenceByApi(serviceWorkerName).getValueOfResponseBodyByComposite(key);
	}
	
	public Object getValueOfResponseStatusFromOhterApi(String serviceWorkerName, String key) {
		//
		return getServiceWorkerSingleFromSequenceByApi(serviceWorkerName).getValueOfResponseStatusByComposite(key);
	}
	//-------------------------------------------------------------------------------------------------------------------
}