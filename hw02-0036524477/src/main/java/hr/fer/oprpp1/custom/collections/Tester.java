package hr.fer.oprpp1.custom.collections;

/**
 * A interface that has only 1 method: test. That method checks if the object is
 * acceptable or not
 * 
 * @author Marko Tunjić
 */
public interface Tester {

	/**
	 * A method that test a object and return true if the object passed the test and
	 * false otherwise
	 * 
	 * @param obj the object to be tested
	 * @return true if the object passes the test and false otherwise
	 */
	boolean test(Object obj);
}
