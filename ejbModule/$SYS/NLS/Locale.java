package $SYS.NLS;

public class Locale {

	public static $SYS.NLS.Locale $New() {
		return new $SYS.NLS.Locale();
	}
	public String CharacterSet;
	public String Country;
	public String CountryAbbr;
	public String Currency;
	public String Description;
	public String Language;
	public String LanguageAbbr;

	public String Name;

	public Locale() {
	}

	public Locale(String Name) {
	}

}