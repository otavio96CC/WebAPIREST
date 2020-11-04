package navita.exercicio02.erros;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SemAuth extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SemAuth() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SemAuth(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SemAuth(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SemAuth(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SemAuth(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
