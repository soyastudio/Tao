package soya.framework.pipeline;

public class PipelineCreateException extends Exception {
    public PipelineCreateException() {
    }

    public PipelineCreateException(String message) {
        super(message);
    }

    public PipelineCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineCreateException(Throwable cause) {
        super(cause);
    }
}