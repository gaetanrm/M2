import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SudokuTest {

	@Test
	void testOneSolutionExecutionTime() {
		int n = 4;
		
		SudokuPPC sudokuPPC = new SudokuPPC();
		SudokuBT sudokuBT = new SudokuBT(n);
		long begginingTimeBT, endingTimeBT, executionTimeBT;
		long begginingTimePPC, endingTimePPC, executionTimePPC;
		
		//Save the current time before execution
		begginingTimeBT = System.currentTimeMillis();
	
		sudokuBT.findSolution(0, 0);
		
		//Save the current time after execution
		endingTimeBT = System.currentTimeMillis();
		
		//Save the execution time
		executionTimeBT = endingTimeBT - begginingTimeBT;
		
		//Same with SudokuPPC
		begginingTimePPC = System.currentTimeMillis();
		sudokuPPC.solve(n);	
		endingTimePPC = System.currentTimeMillis();
		executionTimePPC = endingTimePPC - begginingTimePPC;
		
		//Print the executionTime
		System.out.println("\n");
		System.out.println("Pour une grille 4x4 : ");
		System.out.println("Temps d'exécution pour une solution de SudokuBT : " + executionTimeBT + "ms");
		System.out.println("Temps d'exécution pour une solution de SudokuPPC : " + executionTimePPC + "ms");
		System.out.println("\n");
	}
	
	@Test
	void testAllSolutionsExecutionTime() {
		int n = 4;
		
		SudokuPPC sudokuPPC = new SudokuPPC();
		SudokuBT sudokuBT = new SudokuBT(n);
		long begginingTimeBT, endingTimeBT, executionTimeBT;
		long begginingTimePPC, endingTimePPC, executionTimePPC;
		
		//Save the current time before execution
		begginingTimeBT = System.currentTimeMillis();
	
		sudokuBT.findSolutionAll(0, 0);
		
		//Save the current time after execution
		endingTimeBT = System.currentTimeMillis();
		
		//Save the execution time
		executionTimeBT = endingTimeBT - begginingTimeBT;
		
		//Same with SudokuPPC
		begginingTimePPC = System.currentTimeMillis();
		sudokuPPC.solveAll(n);	
		endingTimePPC = System.currentTimeMillis();
		executionTimePPC = endingTimePPC - begginingTimePPC;
		
		//Print the executionTime
		System.out.println("\n");
		System.out.println("Pour une grille 4x4 : ");
		System.out.println("Temps d'exécution pour toutes les solutions de SudokuBT : " + executionTimeBT + "ms");
		System.out.println("Temps d'exécution pour toutes les solutions de SudokuPPC : " + executionTimePPC + "ms");
		System.out.println("\n");
	}

}
