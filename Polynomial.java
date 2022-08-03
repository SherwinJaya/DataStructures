package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	private static Node polyLast;
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		 Node add = null; 
	      Node addptr = null; 

	      for(Node ptr1 = poly1, ptr2 = poly2; ptr1 != null && ptr2 != null; ptr1 = ptr1.next, ptr2 = ptr2.next) {
	          //if(ptr1.term.degree == ptr2.term.degree)
	         if(ptr1.term.degree > ptr2.term.degree) {
	            if(add == null) {
	               add = new Node(ptr2.term.coeff, ptr2.term.degree, null);
	               addptr = add; 
	               addptr.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
	               addptr = addptr.next;
	            } else {
	               addptr.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
	               addptr = addptr.next;
	               addptr.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
	            }
	         } else if(ptr1.term.degree < ptr2.term.degree) {
	            if(add == null) {
	               add = new Node(ptr1.term.coeff, ptr1.term.degree, null);
	               addptr = add; 
	               addptr.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
	               addptr = addptr.next;
	            } else {
	               addptr.next = new Node(ptr1.term.coeff, ptr1.term.degree, null);
	               addptr = addptr.next;
	               addptr.next = new Node(ptr2.term.coeff, ptr2.term.degree, null);
	            }
	         } else {
	            if(add == null) {
	               add = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
	               addptr = add; 
	            } else {
	               addptr.next = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, null);
	               addptr = addptr.next;
	            }
	         }
	      }
	      if(allZero(add)) {
              add = new Node(0,0,null);
              return add;
            }
		return add;
	    	}

	
	/**
	 * Returns the product off two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		polyLast = null; 
		Node temp = null; 
		Node polyMore = poly2; 
		
		while(poly1 != null) {
			temp = new Node(poly1.term.coeff * poly2.term.coeff, poly1.term.degree + poly2.term.degree, temp);
			poly2 = poly2.next; 
			polyLast = temp; 
			if(poly2 == null) {
				
				poly2 = polyMore;
				poly1 = poly1.next; 
				}
		}
		Node pre = null; 
		Node tmp = polyLast; 
		while(tmp != null) {
			Node next = tmp.next; 
			tmp.next = pre; 
			pre = tmp;
			tmp = next; 
		}
		polyLast = pre; 
		return polyLast; 
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		if(poly == null)
			return 0; 
		else if(poly.next == null)
			return poly.term.coeff * (float) Math.pow(x, poly.term.degree);
		return poly.term.coeff * (float) Math.pow(x, poly.term.degree) + evaluate(poly.next, x);
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
	private static boolean allZero(Node poly) {
	    for(Node ptr = poly; ptr != null; ptr = ptr.next) {
	        if(ptr.term.coeff != 0)
	           return false;
	    }

	return true;

	}
}


