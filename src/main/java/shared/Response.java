package shared;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Response<T> {
    private boolean success;
    private String responseMessage;
    private int responseCode;
    private T data;

    // Constructor for responses without data
    public Response(boolean success, int responseCode, String responseMessage) {
        this.success = success;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    // Constructor for responses with data
    public Response(boolean success, int responseCode, String responseMessage, T data) {
        this.success = success;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public T getData() {
        return data;
    }

    // Generic method to create a Response object from JSON
    public static <T> Response<T> fromJson(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(Response.class, clazz).getType();
        return new Gson().fromJson(json, type);
    }

    // Generic method to convert a Response object to JSON
    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", data=" + (data != null ? data.toString() : "null") +
                '}';
    }
}
