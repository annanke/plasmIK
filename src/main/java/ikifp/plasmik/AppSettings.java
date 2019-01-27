package ikifp.plasmik;

public class AppSettings {
	private static String defaultEmail = "a@a";
	public static String getDefaultUserEmail() {
		return defaultEmail;
	}
	public static void setDefaultUserEmail(String email) {
		defaultEmail=email;
	}
}
