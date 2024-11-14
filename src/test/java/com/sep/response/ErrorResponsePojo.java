package com.sep.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponsePojo {
    private String message;

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponsePojo{" +
                "message='" + message + '\'' +
                '}';
    }

	public String getDocumentation_url() {
		// TODO Auto-generated method stub
		return null;
	}
}
