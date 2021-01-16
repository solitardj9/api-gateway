package serviceWorker2.model.config;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import serviceWorker2.model.config.singleConfig.SingleConfig;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonTypeName("single")
public class ServiceWorkerConfigSingle extends ServiceWorkerConfigBase {
	//
	private SingleConfig config;
}