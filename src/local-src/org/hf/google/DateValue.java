package org.hf.google;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.joda.time.DateTime;

public class DateValue {

	public  enum Period  {Annual, Quarter}
	public static final DecimalFormat decimalFormat = new DecimalFormat ("####,####.##");

	private DateTime date;
	private Float value;
	private Period period;


	public DateValue(DateTime date, String value, Period period) throws ParseException {
		super();

		this.date = date;
		try {
			this.value = decimalFormat.parse(value).floatValue();
		} catch (ParseException e) {
			this.value = 0.0f;
		}
		this.period = period;
	}


	public DateValue(DateTime date, Float value, Period period) {
		super();
		this.date = date;
		this.value = value;
		this.period = period;
	}


	public final DateTime getDate() {
		return date;
	}
	public final Float getValue() {
		return value;
	}
	public final Period getPeriod() {
		return period;
	}


	@Override
	public String toString() {
		return "DateValue [date=" + FStatement.DATETIME_FORMAT.print(date) + ", value=" + value + ", period="
				+ period + "]";
	}


	


}
