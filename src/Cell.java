import java.util.TreeSet;

public class Cell { 
	TreeSet<Integer> possibleValues;
	int assumedValue;
	
	public Cell(){
		assumedValue = 0; 
		
		possibleValues = new TreeSet<Integer>();
		for(int i=1; i<=9; i++) 
			possibleValues.add(i);	
		/*
		possibleValues is initialized as a collection {1,2,3,4,5,6,7,8,9} containing all the possible values of the cell.
		Ideally we will reduce the cardinal of this collection to 1, for example {5},  this would mean that the cell value is determined and is 5.
		Usually in the Sudokus of easy difficulty there is no need to assume values for the cells, i.e. you can determine all the values just by applying Sudoku rules.
		
		If the number of possible values can not be reduced by applying the Sudoku rules without making any assumption, we are forced to make assumptions.
		This is more frequent in Sudokus of harder difficulty.
		
		The assumption of values for the cells is made in bruteForce(). 
		The variable assumedValue of each cell indicates if there was made a value assumption and its value (if assumedValue=0 no assumption was made).
		This is why assumedValue is initialized as 0.
		
		If possibleValues.size()=1 the value of the cell is determined and therefore there will be no need to make assumptions for that cell.
		*/
	}
}