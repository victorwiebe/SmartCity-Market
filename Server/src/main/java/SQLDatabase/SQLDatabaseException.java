package SQLDatabase;

/**
 * This class define all the Exceptions {@link SQLDatabaseConnection} could
 * throw
 * 
 * @author Noam Yefet
 * @since 2016-12-14
 */
public class SQLDatabaseException extends Exception {

	private static final long serialVersionUID = -7130378218981828335L;

	/**
	 * Thrown when Unexpected error occurred
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class CriticalError extends SQLDatabaseException {
		private static final long serialVersionUID = -4621040342204823754L;
	}

	/**
	 * Thrown when requesting product that not exist in catalog
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class ProductNotExistInCatalog extends SQLDatabaseException {
		private static final long serialVersionUID = 3092110065622921929L;
	}
	
	/**
	 * Thrown when product already exists in catalog
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class ProductAlreadyExistInCatalog extends SQLDatabaseException {
		private static final long serialVersionUID = 3092110065622921929L;
	}
	
	/**
	 * Thrown when try to remove product forom catalog and it is still
	 * sold in store or warehouse
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class ProductStillForSale extends SQLDatabaseException {
		private static final long serialVersionUID = 3092110065622921929L;
	}
	
	/**
	 * Thrown when try to do operation on non-existed cart.
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class ProductPackageNotExist extends SQLDatabaseException {
		private static final long serialVersionUID = 3092110065622921929L;
	}
	
	/**
	 * Thrown when try to do operation that changing ProductPackage amount
	 * and there is not enough amount for the operation 
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class ProductPackageAmountNotMatch extends SQLDatabaseException {
		private static final long serialVersionUID = 3092110065622921929L;
	}

	/**
	 * Thrown when worker not exist in database or password don't match
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class AuthenticationError extends SQLDatabaseException {
		private static final long serialVersionUID = -3035025128800993047L;
	}

	/**
	 * Thrown when worker try to do operation before connecting
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class WorkerNotConnected extends SQLDatabaseException {
		private static final long serialVersionUID = -4096975136661332560L;
	}

	/**
	 * Thrown when worker try to connect twice
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class WorkerAlreadyConnected extends SQLDatabaseException {
		private static final long serialVersionUID = 1L;
	}
	
	/**
	 * Thrown when cart try to do operation before connecting
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class CartNotConnected extends SQLDatabaseException {
		private static final long serialVersionUID = -4096975136661332560L;
	}

	/**
	 * Thrown when cart try to connect twice
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class CartAlreadyConnected extends SQLDatabaseException {
		private static final long serialVersionUID = 1L;
	}
	
	/**
	 * Thrown when no connection available
	 * 
	 * @author Noam Yefet
	 *
	 */
	public static class NumberOfConnectionsExceeded extends SQLDatabaseException {
		private static final long serialVersionUID = 1L;
	}
}
