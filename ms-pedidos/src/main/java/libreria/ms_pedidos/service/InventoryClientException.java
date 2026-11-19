package libreria.ms_pedidos.service;

import org.springframework.http.HttpStatusCode;

public class InventoryClientException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public InventoryClientException(HttpStatusCode statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
