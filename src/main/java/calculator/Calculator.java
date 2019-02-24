package com.calculator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class Calculator {

	//following attributes can be externalize by JNDI/Spring configs
	private static String FIRST_LEVEL_OPERATION_KEY = "PREF_OP_1";
	private static String SECOND_LEVEL_OPERATION_KEY = "PREF_OP_2";
	private static String THIRD_LEVEL_OPERATION_KEY = "PREF_OP_3";
	private static String OPERATION_SPOLIT_PATTERN = ",";
	//following attribute can also be externalize by JNDI/Spring configs or can be kept under /resources 
	private static String OPERATION_CONFIG_FILE_PATH = "com/calculator/supoperations.properties";
	private static String INPUT_FILE_NAME = "\\src\\com\\calculator\\input.txt";
	private static int ZERO = 0; //place of number of test cases in input
	private static String INVALID = "INVALID";
	private static String INVALID_EXPRESSION = "INVALID EXPRESSION";
	static Properties configProperties = new Properties();
	
	public enum OPERCOMBEnum {
	    OPERCOMB( new String[] {"++", "+-", "-+", "+*", "*+", "--", "*-", "-*", "/*", "*/", "**", "//", "/-", "-/", "+/", "/+", "^+", "^-", "^*", "^/", "+^", "-^", "*^", "/^"});

	    private String[] arr;

	    OPERCOMBEnum(String[] arr) {
	        this.arr = arr;
	    }

	    public String[] arr() {
	        return arr;
	    }
	}
	
	static {
		// Load supported externalize operations - we can create a framework of externalized things to have necessory Operation function as well as addition to OperationsFactory map 
		try {
			InputStream inputStream = Calculator.class.getClassLoader().getResourceAsStream(OPERATION_CONFIG_FILE_PATH);
			configProperties.load(inputStream);
		} catch (IOException ioe) {
			//Issue with loading operations config file
			ioe.getMessage();
		}
	}
	
	public static void main(String[] args) throws IOException {
		// Fetch data 
		// Scanner scanner = new Scanner(System.in);
		// System.out.println("Enter a string of arithematic expression : ");
		// String operation = scanner.nextLine();
		
		// Assumption 1: input will be getting read from input.txt file in given format as per question
		// Assumption 2: there will be no spaces or special chars in the line expressions

		List<String> inputLines = Files.readAllLines(Paths.get(Paths.get("").toAbsolutePath().normalize().toString()+INPUT_FILE_NAME));
		if(inputLines != null & inputLines.size()>0) {
			System.out.println("Total number of Test Case - " + inputLines.get(ZERO));
			System.out.println(Integer.valueOf(inputLines.get(ZERO))==(inputLines.size() -1) ? "":"Total number of Test-Case & number of Lines does not match : still displaying results.");

		}
		List<String> inputDataList = null;
		for (int index=0; index < inputLines.size(); index++) {
			if(index==0) {
				continue;
			}
			
			try {
				validate(inputLines.get(index));
			} catch(IllegalArgumentException e) {
				if(e.getMessage().equals(INVALID))
				System.out.println("Case #" + index + ":" + INVALID_EXPRESSION);
				
				continue;
			}

			char[] chars = inputLines.get(index).toCharArray();
			inputDataList = new ArrayList<String>(chars.length);
			for(char ch : chars){
				inputDataList.add("" + ch + "");
			}
			getResult(inputDataList, Arrays.asList(String.valueOf(configProperties.get(FIRST_LEVEL_OPERATION_KEY)).trim().split(OPERATION_SPOLIT_PATTERN))); //First level operations
			getResult(inputDataList, Arrays.asList(String.valueOf(configProperties.get(SECOND_LEVEL_OPERATION_KEY)).trim().split(OPERATION_SPOLIT_PATTERN))); //Second level operations
			getResult(inputDataList, Arrays.asList(String.valueOf(configProperties.get(THIRD_LEVEL_OPERATION_KEY)).trim().split(OPERATION_SPOLIT_PATTERN))); //Third level operations
			System.out.println("Case #" + index + ":" + inputDataList.get(0));
		}
		

	}

	/**
	 * Get result method processes on input data based on supported operations and return result in same list by removing other contents 
	 * 
	 * @param inputDataList contains each scanned input char as String in the form of List<String> 
	 * @param supportedOperList contains supported operations for properties files
	 * @return resultList which could contain only single entry at last of processing
	 */
	public static List<String> getResult(List<String> inputDataList, List<String> supportedOperList) {
		
		int result = 0;
		
		for(int index=0; index< inputDataList.size()-1; index++) {
			if(supportedOperList.contains(inputDataList.get(index))) {
				//Open close principal handling by removal of extensive if else checks with Java 8 feature BiFunction 
				try {
					BiFunction<Integer, Integer, Integer> operation = (BiFunction<Integer, Integer, Integer>) OperationsFactory.getOperation(inputDataList.get(index));
					result = operation.apply(Integer.valueOf(inputDataList.get(index-1)).intValue(), Integer.valueOf(inputDataList.get(index+1)).intValue());
				} catch (Exception ignore) {
					//exception to ignore
				}
                try {
                	// simple result placement to ease readability
                	inputDataList.set(index, String.valueOf(result));
                	inputDataList.remove(index+1);
                	inputDataList.remove(index-1);
                }catch(Exception ignore){
                	//exception to ignore
                }
			} else {
				continue;
			}
			index = 0; 
		}
		
		return inputDataList;
	}
	
	public static void validate(String lineString) throws IllegalArgumentException {
		validateBrackets(lineString);
		validateOperaterPlacement(lineString);
	}
	
	
	/**
	 * validateBrackets to handle validations of braces
	 * 
	 * @param lineString input line string to validate for braces
	 * @throws IllegalArgumentException managing
	 */
    public static void validateBrackets(String lineString) throws IllegalArgumentException {
    	//validates if line starts with ) or )
    	if(lineString.startsWith(Character.toString(')')) || lineString.startsWith("))")) {
    		throw new IllegalArgumentException(INVALID); 
    	}

    	//validates if line contains an integer and immediate to it braces ) (
/*    	while(lineString.contains(Character.toString('('))||lineString.contains(Character.toString(')'))){
            for(int o=0; o<lineString.length();o++){
                try{
                    if((lineString.charAt(o)==')' || Character.isDigit(lineString.charAt(o))) 
                            && (lineString.charAt(o+1)=='(')){                         
                        throw new IllegalArgumentException(INVALID);        
                    }                                                      
                }catch (Exception ignored){
                }
                o=0;
            }
        }
*/    }
    
    public static void validateOperaterPlacement(String lineString) throws IllegalArgumentException {
    	String[] arr = OPERCOMBEnum.OPERCOMB.arr();
    	for (int index = 0; index < arr.length; index++) {
    		if(lineString.contains(arr[index])) {
    			throw new IllegalArgumentException(INVALID);     
    		}
		}
    }


}
