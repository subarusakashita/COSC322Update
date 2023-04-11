package ubc.cosc322;

import java.util.ArrayList;

public class Queen extends Tile{
	private int prevRow, prevCol, arrRow, arrCol;
	private boolean opponent;
	public Moves moves;
	
	public Queen(int row, int col, boolean opponent) {
		super(row,col);
		this.prevRow = row;
		this.prevCol = col;
		this.opponent = opponent;
		this.moves = new Moves();
	}

	public int getPrevRow() {
        return prevRow;
    }
    
	public void setPrevRow(int prevRow) {
        this.prevRow = prevRow;
    }
	
    public int getPrevCol() {
        return prevCol;
    }

	public void setPrevCol(int prevCol) {
        this.prevCol = prevCol;
    }

	public int getArrRow() {
        return this.arrRow;
    }

	public void setArrRow(int arrRow) {
        this.arrRow = arrRow;
    }

	public int getArrCol() {
        return this.arrCol;
    }
    
	public void setArrCol(int arrCol) {
        this.arrCol = arrCol;
    }

	public boolean getOpponent() {
        return opponent;
    }

	public ArrayList<Integer> bestArrowShot(Board board) {
        int[][] scoredBoard = new int[10][10];

        for(int i = 0; i < board.player.length; i++) {
            int row = board.player[i].getRow();
            int col = board.player[i].getCol();
            scoredBoard[row][col] = -3;
        }

        for(int i = 0; i < board.opponent.length; i++) {
            int row = board.opponent[i].getRow();
            int col = board.opponent[i].getCol();
           scoredBoard[row][col] = 3;
        }

        for(int i = 0; i < 3; i++) {
            int score;
            for(int row = 0; row < 10; row++) {
                for(int col = 0; col < 10; col++) {
                    if(scoredBoard[row][col] == 0) {
                    	score = 0;
                        if(row > 0) {
                        	if(scoredBoard[row-1][col] == 3) {
                        		score += scoredBoard[row-1][col];
                        	} 
                        }
                        if(row < 9) {
                        	if(scoredBoard[row+1][col] == 3) {
                        		score += scoredBoard[row+1][col];
                        	} 
                        }
                        if(col > 0) {
                        	if(scoredBoard[row][col-1] == 3) {
                        		score += scoredBoard[row][col-1];
                        	} 
                        }
                        if(col < 9) {
                        	if(scoredBoard[row][col+1] == 3) {
                        		score += scoredBoard[row][col+1];
                        	} 
                        }
                        if(score != 0) {
                            score -= score / Math.abs(score);
                        }
                        scoredBoard[row][col] = score;
                    }
                }
            }
        }

        this.moves.availableArrows(board, this);
        ArrayList<Integer> bestMove = new ArrayList<Integer>();
        int bestScore = -99;

        for(int i = 0; i < this.moves.arrowShots.size(); i++) {
            int moveScore = scoredBoard[this.moves.arrowShots.get(i).get(0) + this.getRow()][this.moves.arrowShots.get(i).get(1) + this.getCol()];
            if(moveScore > bestScore) {
                bestMove = this.moves.arrowShots.get(i);
                bestScore = moveScore;
            }
        }
        return bestMove;
    }
}