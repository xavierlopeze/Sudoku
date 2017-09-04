import java.util.Scanner;
public class Sudoku {
	
    private static Scanner in;

	public static void main(String[] args) {
		Board tauler = new Board();
		in = new Scanner(System.in);
    	
    	while(true){
    		
        	//tauler.printPossibleValues();
        	tauler.printBoard();
        	int fila, columna, valor;
    		
        	fila = in.nextInt();
        	columna = in.nextInt();
        	valor = in.nextInt();
        	
        	if(fila==0){
        		tauler.bruteForce();
        		System.out.println("\n Broken Force has been done successfully. \n");
        		break;
        	}
        	else{
            	tauler.cell[(fila-1)*9+(columna-1)].possibleValues.clear();
            	tauler.cell[(fila-1)*9+(columna-1)].possibleValues.add(valor);
            	
            	tauler.fullUpdate();
            	if(tauler.brokenRules()) System.out.println("\n  Rules Are Broken \n");
            	else System.out.println("\n Rules still Hold \n");
            	if(tauler.isSudokuSolved()) break;
        	}
    
    	}
    	//tauler.printPossibleValues();
    	tauler.printBoard();
    }
}
