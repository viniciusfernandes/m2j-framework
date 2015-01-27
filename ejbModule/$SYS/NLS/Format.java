package $SYS.NLS;

public class Format {

	public static $SYS.NLS.Format $New() {
		return new $SYS.NLS.Format();
	}
	public String AM;
	public Integer DateFormat;
	public Integer DateMaximum;
	public Integer DateMinimum;
	public String DateSeparator;
	public String Locale; 
	public String Midnight;
	public String MinusSign;
	public String MonthAbbr;
	public String MonthName;
	public String Noon;
	public String NumericGroupSeparator;
	public Integer NumericGroupSize;
	public String PlusSign;
	public String PM; 
	public Integer TimeFormat; 
	public Integer TimePrecision; 
	public String TimeSeparator; 
	public String WeekdayAbbr; 
	public String WeekdayName; 
	
	public Integer YearOption;
	
	public Format() {
	}
	
	public Format(String Locale) {
	}
}
