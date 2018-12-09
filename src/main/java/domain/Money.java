package domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Embeddable
@Access(AccessType.PROPERTY)
public class Money {

	private Double amount;

	private String currency;

	public Money() {
		super();
		setAmount(0.0);
		setCurrency(Constant.currency.EUR.toString());
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Money add(Money money) {
		if (sameCurrency(this, money))
			amount += (money.amount);
		else
			amount += this.convert(money).amount;
		return this;
	}

	public Money subtract(Money money) {
		if (sameCurrency(this, money))
			amount -= (money.amount);
		return this;
	}

	private boolean sameCurrency(Money money, Money money2) {
		return money.currency.equals(money2.currency);
	}

	public Money convert(Money money) {
		Money result = new Money();
		String from = money.getCurrency();
		String to = this.getCurrency();
		String targetURL = "http://free.currencyconverterapi.com/api/v5/convert?q=" + from + "_" + to +"&compact=ultra";
		HttpURLConnection connection = null;
		String inputLine;
		StringBuffer response = new StringBuffer();
		double factorDeConversion = 1.0;
		try {
			URL url = new URL(targetURL);
			// Make connection
			connection = (HttpURLConnection) url.openConnection();
			// Set request type as HTTP GET
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			if (connection.getResponseCode() == 200) {
				// get response stream
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				// feed response into the StringBuilder
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// Start parsing
				JSONObject obj = new JSONObject(response.toString());
				// get Array type
				JSONArray results;
				try {
					factorDeConversion = obj.getDouble(from + "_" + to);
					System.out.println("Un "+ from + " vale  " + factorDeConversion +" " + to);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Can't get data");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		result.setAmount(money.getAmount()*factorDeConversion);
		return result;
	}
}
