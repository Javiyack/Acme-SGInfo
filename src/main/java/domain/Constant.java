
package domain;

public class Constant {

	public enum currency {
		EUR,
		USD
}
	public enum requestStatus {
		PENDING,
		REJECTED,
		ACCEPTED,
		CANCELLED
}

	// Monedas
	public static final String	CURRENCY_EURO						= "EUR";
	public static final String	CURRENCY_DOLLAR						= "USD";
	
	



	
	// File parent types
	public static final String	FILE_MESSAGE						= "message";
	public static final String	FILE_INCIDENCE						= "incidence";
	public static final String	FILE_TENDER							= "tender";
	public static final String	FILE_TENDER_RESULT					= "tenderResult";
}
