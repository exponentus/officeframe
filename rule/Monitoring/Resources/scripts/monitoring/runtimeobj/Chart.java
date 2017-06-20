package monitoring.runtimeobj;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chart {
	private String title;
	private int maxY;
	private Date maxX;
	private String status;
	private Map<String, Long> values = new HashMap<String, Long>();

	public void addValue(String from, long amount) {
		values.put(from, amount);

	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public Date getMaxX() {
		return maxX;
	}

	public void setMaxX(Date maxX) {
		this.maxX = maxX;
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
