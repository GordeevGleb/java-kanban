package server;

public class KVTaskClient {
    private String apiToken;
    private String clientName;

    public KVTaskClient(String apiToken, String clientName) {
        this.apiToken = apiToken;
        this.clientName = clientName;
    }

    public String getAPI_TOKEN() {
        return apiToken;
    }

    public void setAPI_TOKEN(String API_TOKEN) {
        this.apiToken = API_TOKEN;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
