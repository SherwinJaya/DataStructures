package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	String delims = "+-*/()]"; 
    	String temp = ""; 
    	for(int i = 0; i<expr.length();i++) {
    		System.out.println(expr.charAt(i)); 
    		if(Character.isDigit(expr.charAt(i))) {
    			continue; 
    		}
    		if(Character.isWhitespace(expr.charAt(i))) {
    			continue; 
    		}
    		if((delims.contains(Character.toString(expr.charAt(i))) != true)) {
    			temp += expr.charAt(i); 
    		}
    		else if (delims.contains(Character.toString(expr.charAt(i)))) {
    			if(temp != "") {
    				Variable tempp = new Variable(temp); 
    				if(vars.contains(tempp) != true) {
    					vars.add(tempp); 
    				}
    				temp = ""; 
    			}
    		} else if (expr.charAt(i) == '[') {
    			if(!temp.equals("")) {
    				Array tempp = new Array(temp); 
    				arrays.add(tempp); 
    				temp = ""; 
    				
    			}
    		}
    	}
    	Variable tempp = new Variable(temp); 
    	if((vars.contains(tempp) != true) && (delims.contains(temp) != true)) {
    		vars.add(tempp); 
    	}
    		
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	Stack <Integer> num = new Stack<Integer> (); 
    	Stack <Integer> num2 = new Stack <Integer> (); 
    	
    	Stack <Character> op = new Stack<Character> (); 
    	Stack <Character> op2 = new Stack<Character> (); 
    	
    	String opps = "+-/*";  
    	String temp = "";
    	temp += expr.charAt(0);
    	Integer int1 = 0; 
    	Character char1 = ' '; 
    	
    	String answer = variableString(expr, vars, arrays); 
    	
    	for(int i = 0; i < answer.length(); i++) {
    		if(!delims.contains(Character.toString(answer.charAt(i)))) {
    			temp += answer.charAt(i); 
    		} else if(delims.contains((Character.toString(answer.charAt(i))))) {
    			if(isNumber(temp))
    			num.push((int) Double.parseDouble(temp));
    			op.push(answer.charAt(i));
    			temp = ""; 
    		}
    	}
    	if(temp != "" && ! delims.contains(temp)) {
    		num.push((int) Double.parseDouble(temp));
    	}
    	while(!num.isEmpty()) {
    		num2.push(num.pop());
    	}
    	while(!op.isEmpty()) {
    		op2.push(op.pop());
    	}
    	int temp2; 
    	char operation = ' '; 
    	
    	while(num2.size() > 1) {
    		operation = op2.pop(); 
    		
    		if(opps.contains(Character.toString(operation))) {
    			if(op2.isEmpty()) {
    				temp2 = calc(operation, num2.pop(), num2.pop());
    				num2.push(temp2); 
    			}
    			else if((op2.peek() == '*' || op2.peek() == '/') && (operation == '+' || operation == '-')) {
    				int1 = num2.pop(); 
    				char1 = operation; 
    				num2.push(calc(op2.pop(), num2.pop(), num2.pop()));
    				num2.push(int1);
    				op2.push(char1);
    			} else {
    				num2.push(calc(operation, num2.pop(), num2.pop()));
    			}
    		}
    	}
    			return num2.pop();
    }
    
    private static String variableString(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	String otherDelims = "+-*/()]"; 
    	String parenthesis = "["; 
    	String temp = ""; 
    	String answer = ""; 
    	
    	Variable temp2 = null; 
    	
    	for(int i = 0; i < expr.length(); i++) {
    		if(Character.isDigit(expr.charAt(i))) {
    			answer += expr.charAt(i); 
    			continue; 
    		}
    		if(Character.isWhitespace(expr.charAt(i))) {
    			continue; 
    		}
    		else if(parenthesis.contains(Character.toString(expr.charAt(i)))) {
    			answer += temp; 
    			answer += expr.charAt(i); 
    			temp = ""; 
    			continue; 
    		}
    		else if(!otherDelims.contains(Character.toString(expr.charAt(i)))) {
    			temp += expr.charAt(i); 
    		} else if (otherDelims.contains(Character.toString(expr.charAt(i)))) {
    			if(temp != "") {
    				temp2 = vars.get(vars.indexOf(new Variable(temp))); 
    				answer += temp2.value; 
    			}
    			answer += expr.charAt(i); 
    			temp2 = null; 
    			temp = ""; 
    		}
    		else if(expr.charAt(i) == '[') {
    			answer += temp; 
    			answer += expr.charAt(i); 
    			temp = ""; 
    		}
    	}
    	if(temp != "") {
    		temp2 = vars.get(vars.indexOf(new Variable(temp))); 
    		answer += temp2.value; 
    	}
    	return answer; 
    }
    
    private static int calc(char operation, int a, int b) {
    	if(operation == '*') {
    		return a * b; 
    	}
    	else if (operation == '/') {
    		return a / b; 
    	}
    	else if(operation == '-') {
    		return a - b; 
    	}
    	else if(operation == '+') {
    		return a + b; 
    	}
    	return 0; 
    }
    public static boolean isNumber(String s) {
    	if(s.isEmpty())
    		return false; 
    	for(int i= 0; i< s.length(); i++) {
            if(!charIsNumber(s.charAt(i)))
               return false;
        }
       return true;
    }

    public static boolean charIsNumber(char c) {
        char[] list = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'}; 
        for(int i = 0; i < list.length; i++){
            if(list[i] == c)
               return true;

        }
        return false;
    }
}
