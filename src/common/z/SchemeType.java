/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common.z;

/**
 * This class represent the four possible types of schema,
 * @author Pablo Rodriguez Monetti
 */
public enum SchemeType {

    /**The term declares a variable contained in its IS. */
    IN,
    /**The term declares a variable contained in its OS. */
    OUT,
    /**The term is an operation. */
    OP,
    /**The term does not declare variables.  */
    WITHOUT_VARS
}
