package serviceWorker2.model.config.singleConfig;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRule {

	private List<String> condition;
	
	private Action action;
}