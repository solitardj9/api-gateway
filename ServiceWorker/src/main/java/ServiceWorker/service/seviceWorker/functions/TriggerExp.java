package ServiceWorker.service.seviceWorker.functions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerExp {
	
	public enum TriggerExpExpKey {
		
		RESOURCE(0, "resource") {
			@Override
			public void setValue(TriggerExp functionExp, String value) {
				functionExp.resource = value;
			}
		},
		KEY(1, "key") {
			@Override
			public void setValue(TriggerExp functionExp, String value) {
				functionExp.key = value;
			}
		},
		VALUE(2, "value") {
			@Override
			public void setValue(TriggerExp functionExp, String value) {
				functionExp.value = value;
			}
		}
		;
		
		private Integer index;
		private String key;
		
		TriggerExpExpKey(Integer index, String key) {
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
		
		public abstract void setValue(TriggerExp functionExp, String value);
	}
	
	private String resource;
	
	private String key;
	
	private String value;
	
	public void setValue(Integer index, String value) {
		//
		for (TriggerExpExpKey iter : TriggerExpExpKey.values()) {
			if (iter.getIndex().equals(index)) {
				iter.setValue(this, value);
			}
		}
	}
}