import java.util.TreeSet;

public class Board { 
	Cell[] cell; 
	
	public Board(){
		cell = new Cell[81]; //The Sudoku board is made of 9x9=81 cells.
		for (int i=0; i<cell.length; i++)
			cell[i] = new Cell();
	}
	
	//This method updates the possible values of the input ROW's cells, row takes values 0..8.
	public void updateRowValues(int row){ 

		TreeSet<Integer> usedValues = new TreeSet<Integer>(); // collection that will contain all the determined values in the row
		
		for(int i=0; i<9; i++){// for each cell of the row
			if(cell[9*row+i].possibleValues.size()==1){ // if there is one only possible value (the value of the cell is determined)
				usedValues.addAll(cell[9*row+i].possibleValues); // we add this value to the row's used values
			}
		}
		for(int i=0; i<9; i++){// for each cell of the row
			if(cell[9*row+i].possibleValues.size()>1) // if the value of the cell is not determined
				cell[9*row+i].possibleValues.removeAll(usedValues); // we remove to it's possible values the values that are already determined.
		}
	}
	
	//This method updates the possible Values of the input COLUMN's cells, row takes values 0..8.
	public void updateColumnValues(int column){ 
		
		TreeSet<Integer> usedValues = new TreeSet<Integer>(); // collection that will contain all the determined values in the column
		
		for(int i=0; i<9; i++){ // for each cell of the column
			if(cell[9*i+column].possibleValues.size()==1){ // if there is one only possible value (the value of the cell is determined)
				usedValues.addAll(cell[9*i+column].possibleValues); // we add this value to the column's used values
			}
		}
		for(int i=0; i<9; i++){ // for each cell of the column
			if(cell[9*i+column].possibleValues.size()>1) // if the value of the cell is not determined
				cell[9*i+column].possibleValues.removeAll(usedValues); // we remove to it's possible values the values that are already determined.
		}
	}
	
	//This method updates the possible Values of the input BOXES's cells, row takes values 0..8.
	public void updateBoxValues(int numBox){
		
		/* Order of the boxes  => Therefore boxes start at (rowBox, columnBox):
		 * 0 1 2                	 (0,0) (3,0) (6,0)
		 * 3 4 5               =>    (3,0) (3,3) (6,3)   
		 * 6 7 8                     (6,0) (6,3) (6,6)
		 * */
		int columnBox = (numBox/ 3)*3;
		int rowBox = (numBox % 3)*3;
		
		TreeSet<Integer> usedValues = new TreeSet<Integer>(); // collection that will contain all the determined values in the box
		for(int i=0; i<3; i++){ // for each cell in the box
			for(int j=0; j<3; j++){
				if(cell[9*(i+rowBox)+(j+columnBox)].possibleValues.size()==1){ // if there is one only possible value (the value of the cell is determined)
					usedValues.addAll(cell[9*(i+rowBox)+(j+columnBox)].possibleValues);  // we add this value to the column's used values
				}
			}
		}
		for(int i=0; i<3; i++){ // for each cell in the box
			for(int j=0; j<3; j++){
				if(cell[9*(i+rowBox)+(j+columnBox)].possibleValues.size()>1){ // if the value of the cell is not determined
					cell[9*(i+rowBox)+(j+columnBox)].possibleValues.removeAll(usedValues); // we remove to it's possible values the values that are already determined.
				}
			}
		}
	}
	
	//This method updates the possible Values of each row, each column and each box ONCE.
	public void updatePossibleValues(){
		for(int i =0; i<9; i++){ // there are 9 rows, columns and boxes
    		updateRowValues(i);
    		updateColumnValues(i);
    		updateBoxValues(i);
    	} 						// and we update them all
	}

	//This method returns the sum of the cardinal of possible values for each cell.  
	public int countPossibleValues(){
		int sum = 0;
		for(int i=0; i<81; i++) // for each cell in the board
			sum += cell[i].possibleValues.size(); //We add the cardinal of the collection of possible values for that cell
		return sum;
	}
	
	//This method updates the possible Values of each row, each column and each box until they can no longer be reduced.
	public void fullUpdate(){
		
		int i = countPossibleValues(); //we count the number of possible values of the entire board before the update
		while(true){
			updatePossibleValues();   // we update the possible values
			if(i == countPossibleValues()) // if the number of possible values have not been reduced
				break; // there is no need to update any more
			else // else, we keep updating
				i=countPossibleValues();
		}
	}
	
	//This method returns a boolean indicating if there is any rule broken in the row of input (considering assumed values as well)
	public boolean brokenRow(int row){
		//Please note that the law that we are checking is: "There should be no repetition of values in the row".
		//Understanding that, by the way this is coded, the values can only be integers from 1 to 9.
		
		TreeSet<Integer> allValues = new TreeSet<Integer>(); // collection that will contain all the not (determined or assumed) row values
		for(int i=1; i<=9; i++) allValues.add(i); //we initialize allValues as a collection {1,2,3,4,5,6,7,8,9}
		int valuesRemoved = 0;

		for(int i=0; i<9; i++){ 							// for each element in the row
			if(cell[i+row*9].possibleValues.size()==1){ 		   // if the cell value is determined
				allValues.removeAll(cell[i+row*9].possibleValues); 		// we remove this value from the collection of allValues.
				valuesRemoved++; 								   		// we count this value as a value removed
			}
			else if(cell[i+row*9].assumedValue!=0){				   // else if the cell value is assumed
				allValues.remove(cell[i+row*9].assumedValue);			// we removed the assumed value from the collection of allValues
				valuesRemoved++;										// we count this value as a value removed
			}
		}
		//Please note that by the way this is coded, there is no possibility to have a cell with a determined value and an assumed value at the same time.
		
		// allValues now contains a collection of the values that are not used in this row.
		
		if(9-allValues.size()==valuesRemoved) // if the cardinal of the values that are used in this row is the same than the number of values removed;
			return false; // there is no broken law
		return true; // there is a broken law.
	}
	
	//This method returns a boolean indicating if there is any rule broken in the column of input (considering assumed values as well)
	public boolean brokenColumn(int column){
		//Please note that the law that we are checking is: "There should be no repetition of values in the column".
		//Understanding that, by the way this is coded, the values can only be integers from 1 to 9.
		
		TreeSet<Integer> allValues = new TreeSet<Integer>(); // collection that will contain all the not (determined or assumed) column values
		for(int i=1; i<=9; i++) allValues.add(i); //we initialize allValues as a collection {1,2,3,4,5,6,7,8,9}
		int valuesRemoved = 0;
		
		for(int i=0; i<9; i++){										// for each element in the column
			if(cell[column+i*9].possibleValues.size()==1){	 			// if the cell value is determined
				allValues.removeAll(cell[column+i*9].possibleValues);		// we remove this value from the collection of allValues.
				valuesRemoved++;											// we count this value as a value removed
			}
			else if(cell[column+i*9].assumedValue!=0){				// else if the cell value is assumed
				allValues.remove(cell[column+i*9].assumedValue);		// we removed the assumed value from the collection of allValues
				valuesRemoved++;										// we count this value as a value removed
			}
		}
		//Please note that by the way this is coded, there is no possibility to have a cell with a determined value and an assumed value at the same time.
		
		// allValues now contains a collection of the values that are not used in this column.
		
		if(9-allValues.size()==valuesRemoved)  // if the cardinal of the values that are used in this column is the same than the number of values removed;
			return false; // there is no broken law
		return true; // there is a broken law.
	}
	
	//This method returns a boolean indicating if there is any rule broken in the box of input (considering assumed values as well)
	public boolean brokenBox(int numBox){
		//Please note that the law that we are checking is: "There should be no repetition of values in the box".
		//Understanding that, by the way this is coded, the values can only be integers from 1 to 9.
		
		TreeSet<Integer> allValues = new TreeSet<Integer>(); // collection that will contain all the not (determined or assumed) box values
		for(int i=1; i<=9; i++) allValues.add(i); //we initialize allValues as a collection {1,2,3,4,5,6,7,8,9}
		int valuesRemoved = 0;
		
		/* Order of the boxes  => Therefore boxes start at (rowBox, columnBox):
		 * 0 1 2                	 (0,0) (3,0) (6,0)
		 * 3 4 5               =>    (3,0) (3,3) (6,3)   
		 * 6 7 8                     (6,0) (6,3) (6,6)
		 * */
		int columnBox = (numBox/ 3)*3;
		int rowBox = (numBox % 3)*3;
		
		for(int i=0; i<3; i++){ 													
			for(int j=0; j<3; j++){															// for each element in the box
				if(cell[9*(i+rowBox)+(j+columnBox)].possibleValues.size()==1){					// if the cell value is determined
					allValues.removeAll(cell[9*(i+rowBox)+(j+columnBox)].possibleValues);			// we remove this value from the collection of allValues.
					valuesRemoved++;																// we count this value as a value removed
				}
				else if(cell[9*(i+rowBox)+(j+columnBox)].assumedValue!=0){					// else if the cell value is assumed
					allValues.remove(cell[9*(i+rowBox)+(j+columnBox)].assumedValue);				// we removed the assumed value from the collection of allValues
					valuesRemoved++;																// we count this value as a value removed
				}
			}
		}
		//Please note that by the way this is coded, there is no possibility to have a cell with a determined value and an assumed value at the same time.
		
		// allValues now contains a collection of the values that are not used in this column.
		
		if(9-allValues.size()==valuesRemoved)  // if the cardinal of the values that are used in this column is the same than the number of values removed;
			return false; // there is no broken law
		return true; // there is a broken law.
	}
	
	//This method returns a boolean indicating if there is any broken rule in the sudoku board.
	public boolean brokenRules(){
		for(int i=0; i<9; i++){ // for each row, column and box, check if there is any broken rule
			if(   brokenRow(i) 
			   || brokenColumn(i)
			   || brokenBox(i))
				return true;
		}
		return false;
	}
	
	//This method returns a boolean indicating if the Sudoku board problem is solved.
	public boolean isSudokuSolved(){
		for(int i=0; i<81; i++)
			if(cell[i].possibleValues.size()>=1 && cell[i].assumedValue==0)
				return false;
				
		return !brokenRules(); // not broken rules => is solved
	}
	
	/*
	 * This is the most interesting function in this code, consists of the brute force algorithm to solve sudoku boards.
	 * 
	 * Essentially we guess values until we find a solution.
	 * If the sodoku board has a solution, it will be found (regardless of the execution time).
	 * */
	public void bruteForce(){
		int num; //number of the cell we are working with
		num = 0;
		while(cell[num].possibleValues.size()==1){//if the cell is value is determined
			num +=1; 							  //we go to the next cell
		}
		//int countIterats = 0; 
		while(!isSudokuSolved()){ //while the sudoku is not solved

				if(cell[num].possibleValues.higher(cell[num].assumedValue)!=null){ //if we can assume another value in the cell that we are in
					
					cell[num].assumedValue = cell[num].possibleValues.higher(cell[num].assumedValue); // we assume the next value
					
					if(!brokenRules()){ // if there are no broken rules
						if(num==80) break; // if we are in the last cell, the sudoku is solved
						else{ //if we are not in the last cell, we go to the next cell that is not determined
							do num +=1; while(cell[num].possibleValues.size()==1);
						}
					}
				}
				else{ // if we CANT assume another value in the cell that we are in
					cell[num].assumedValue=0; // we reset the assumed value to zero
					do num -=1; while(cell[num].possibleValues.size()==1); // and go back to the undetermined cell.
					
				}
				
				//System.out.println("Iterat " + countIterats);
				//countIterats +=1;
				//printBoard();
		}		
	}
	

	public void printPossibleValues(){
		System.out.print("  _______________________\n");
		System.out.print("  ____POSSIBLE VALUES____\n");
		System.out.print("  _______________________\n");
		for(int i=0; i<9; i++){
			if(i==3 || i == 6) System.out.print(" |-----------------------|\n");
			for(int j=0; j<9; j++){
				if(j==0 || j==3 || j==6) System.out.print(" | ");
				else System.out.print(" ");
				System.out.print(cell[j+9*i].possibleValues.size());
		
				if(j==8) System.out.print(" |");
			}
		System.out.print("\n");
		}
		System.out.print("  ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯\n");
	}
	public void printBoard(){
	System.out.print("  _______________________\n");
	System.out.print("  ______SUDOKU BOARD_____\n");
	System.out.print("  _______________________\n");
	for(int i=0; i<9; i++){
		if(i==3 || i == 6) System.out.print(" |-----------------------|\n");
		for(int j=0; j<9; j++){
			if(j==0 || j==3 || j==6) System.out.print(" | ");
			else System.out.print(" ");
			if(cell[j+9*i].possibleValues.size()!=1){
				System.out.print(cell[j+9*i].assumedValue);
			}
			else System.out.print(cell[j+9*i].possibleValues.stream().findFirst().get());
			
	
			if(j==8) System.out.print(" |");
		}
	System.out.print("\n");
	}
	System.out.print("  ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯\n");
}
}
