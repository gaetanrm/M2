
public class SudokuElement {
	
	int element, positionX, positionY;
	String operationConstraint;

	public SudokuElement(int element, int positionX, int positionY) {
		super();
		this.element = element;
		this.positionX = positionX;
		this.positionY = positionY;
		this.operationConstraint = null;
	}
	
	public SudokuElement(int element, int positionX, int positionY, String operationConstraint) {
		super();
		this.element = element;
		this.positionX = positionX;
		this.positionY = positionY;
		this.operationConstraint = operationConstraint;
	}

	public int getElement() {
		return element;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}
	
	
}
