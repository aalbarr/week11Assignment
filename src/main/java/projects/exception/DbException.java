package projects.exception;

@SuppressWarnings("serial")//DbException extends RuntimeException
public class DbException extends RuntimeException {

	public DbException() {
	}

	//each line that starts w super is overriding the constructors from the superclass
	public DbException(String message) {
		super(message);
	}

	public DbException(Throwable cause) {
		super(cause);
	}

	public DbException(String message, Throwable cause) {
		super(message, cause);
	}



}