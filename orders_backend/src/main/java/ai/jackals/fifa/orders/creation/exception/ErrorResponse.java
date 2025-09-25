package ai.jackals.fifa.orders.creation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Standard error payload returned by REST controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Timestamp when the error occurred */
    private LocalDateTime timestamp;

    /** HTTP status code */
    private int status;

    /** Short reason phrase, e.g. "Bad Request" */
    private String error;

    /** Human-readable error message */
    private String message;

    /** Request path that caused the error */
    private String path;

    /** Field / additional error details */
    private Map<String, Object> details;

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	// Manual builder implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private Map<String, Object> details;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        public Builder status(int status) {
            this.status = status;
            return this;
        }
        public Builder error(String error) {
            this.error = error;
            return this;
        }
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        public Builder path(String path) {
            this.path = path;
            return this;
        }
        public Builder details(Map<String, Object> details) {
            this.details = details;
            return this;
        }
        public ErrorResponse build() {
            ErrorResponse er = new ErrorResponse();
            er.setTimestamp(timestamp);
            er.setStatus(status);
            er.setError(error);
            er.setMessage(message);
            er.setPath(path);
            er.setDetails(details);
            return er;
        }
    }

	@Override
	public int hashCode() {
		return Objects.hash(details, error, message, path, status, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorResponse other = (ErrorResponse) obj;
		return Objects.equals(details, other.details) && Objects.equals(error, other.error)
				&& Objects.equals(message, other.message) && Objects.equals(path, other.path) && status == other.status
				&& Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "ErrorResponse [timestamp=" + timestamp + ", status=" + status + ", error=" + error + ", message="
				+ message + ", path=" + path + ", details=" + details + "]";
	}
    
    
}