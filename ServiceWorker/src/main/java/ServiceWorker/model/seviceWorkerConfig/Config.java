package ServiceWorker.model.seviceWorkerConfig;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {
	
	private String scheme;
	
	private String host; 
	
	private String path;
	
	private String method;
	
	private Map<String, List<String>> headers;
	
	private Map<String, String> queryParams;
	
	private Object body;
}