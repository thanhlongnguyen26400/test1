package spending_management_project.vo;


import org.springframework.http.HttpStatus;

import java.io.Serializable;


public class Response implements Serializable {
    private Object data;
    private Object error;
    private Object message;
    private HttpStatus status;
    private int code;

    public Response() { }

    public Response(Object data, Object error, HttpStatus status, int code) {
        this.data = data;
        this.error = error;
        this.status = status;
        this.code = code;
        this.message = "success";
    }

    public Response(Object data, Object error, Object message, HttpStatus status, int code) {
        this.data = data;
        this.error = error;
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return status;
    }

    public void setStatusCode(HttpStatus status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                ", error=" + error +
                ", message=" + message +
                ", status=" + status +
                ", code=" + code +
                '}';
    }
}
