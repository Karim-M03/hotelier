package server;

/**
 * Custom exception class for the server application.
 * This class can be used to define various types of exceptions.
 * 
 */
public class Exceptions extends Exception {
    
    public Exceptions() {
        super();
    }

    public Exceptions(String message) {
        super(message);
    }

    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public Exceptions(Throwable cause) {
        super(cause);
    }

    // StorageException class
    public static class StorageException extends Exceptions {
        public StorageException() {
            super();
        }

        public StorageException(String message) {
            super(message);
        }

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }

        public StorageException(Throwable cause) {
            super(cause);
        }
    }

    // ServerException class
    public static class ServerException extends Exceptions {
        public ServerException() {
            super();
        }

        public ServerException(String message) {
            super(message);
        }

        public ServerException(String message, Throwable cause) {
            super(message, cause);
        }

        public ServerException(Throwable cause) {
            super(cause);
        }
    }
    
    
    public static class NoDataFoundException extends Exceptions {
        public NoDataFoundException() {
            super();
        }

        public NoDataFoundException(String message) {
            super(message);
        }

        public NoDataFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public NoDataFoundException(Throwable cause) {
            super(cause);
        }
    }

    // Additional custom exceptions can be added here
}
