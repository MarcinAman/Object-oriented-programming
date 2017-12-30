import org.json.JSONException;


public interface ICurrency {

    void getAllCurrency(String apiResponse) throws JSONException;

    String toString();
}
