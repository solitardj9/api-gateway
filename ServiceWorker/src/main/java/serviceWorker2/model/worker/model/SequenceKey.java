package serviceWorker2.model.worker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceKey {

	private Integer index;
	
	private String api;
}