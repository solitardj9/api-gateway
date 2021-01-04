package ServiceWorker.model.seviceWorkerConfig;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonTypeName("single")
public class ServiceWorkerConfigSingle extends ServiceWorkerConfigBase {
	//
	private Config config;
}