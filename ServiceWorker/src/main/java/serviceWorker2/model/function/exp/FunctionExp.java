package serviceWorker2.model.function.exp;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionExp {

	private String function;
	
	private Map<Integer, Object> params = new HashMap<>();
	
	public void setParam(Integer index, Object param) {
		params.put(index, param);
	}
}