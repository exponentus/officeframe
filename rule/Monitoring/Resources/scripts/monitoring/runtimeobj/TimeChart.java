package monitoring.runtimeobj;

import java.util.HashMap;
import java.util.Map;

public class TimeChart {
	private String title;
	private String start;
	private String end;
	private String status;
	private Map<String, Long> values = new HashMap<String, Long>();

	public void addValue(String from, long amount) {
		values.put(from, amount);
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, Long> getValues() {
		return values;
	}

	public void setValues(Map<String, Long> values) {
		this.values = values;
	}

	public String getTitle() {
		return title;
	}

	public void setType(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
