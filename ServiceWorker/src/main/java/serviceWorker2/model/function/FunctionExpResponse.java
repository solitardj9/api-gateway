package serviceWorker2.model.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import serviceWorker2.model.function.exp.FunctionExp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionExpResponse {
	
	private FunctionExp functionExp;
	
	private Integer returnedIndex;
}
