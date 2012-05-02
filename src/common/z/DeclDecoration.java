package common.z;


    /**
     * Represents the possible decoration of a schema inclusion.
     * @author Pablo Rodriguez Monetti
     */
	public enum DeclDecoration{
		/**La declaración no esta primada */
		NOT_PRIMED,
		/**La declaración esta primada */
		PRIMED,
		/**La declaración esta primada y no primada: es un delta o un Xi*/
		BOTH
	}