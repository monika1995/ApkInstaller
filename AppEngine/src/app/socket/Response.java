package app.socket;

public interface Response {
	void onResponseObtained(Object response, int responseType,
                            boolean isCachedData);

	void onErrorObtained(String errormsg, int responseType);
}
