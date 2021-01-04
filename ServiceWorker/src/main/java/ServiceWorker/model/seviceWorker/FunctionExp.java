package ServiceWorker.model.seviceWorker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionExp {
	
	public enum FunctionExpKey {
		
		API(0, "api") {
			@Override
			public void setValue(FunctionExp functionExp, String value) {
				functionExp.api = value;
			}
		},
		RESOURCE(1, "resource") {
			@Override
			public void setValue(FunctionExp functionExp, String value) {
				functionExp.resource = value;
			}
		},
		KEY(2, "key") {
			@Override
			public void setValue(FunctionExp functionExp, String value) {
				functionExp.key = value;
			}
		}
		;
		
		private Integer index;
		private String key;
		
		FunctionExpKey(Integer index, String key) {
			this.index = index;
			this.key = key;
		}
		
		public Integer getIndex() {
			return index;
		}
		
		@Override
		public String toString() {
			return key;
		}
		
		public abstract void setValue(FunctionExp functionExp, String value);
	}
	
	private String api;
	
	private String resource;
	
	private String key;
	
	public void setValue(Integer index, String value) {
		//
		for (FunctionExpKey iter : FunctionExpKey.values()) {
			if (iter.getIndex().equals(index)) {
				iter.setValue(this, value);
			}
		}
	}
}