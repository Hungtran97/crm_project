package crm06.customenum;

public enum UrlEnum {
	LOGIN("login");

	private final String url;

	private UrlEnum(String url) {
		this.url = url;
	}

	public String url() {
		return this.url;
	}

}
