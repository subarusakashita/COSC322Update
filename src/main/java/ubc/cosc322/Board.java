package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class Board {
	Tile[][] board;
	Queen[] player;
	Queen[] opponent;
	Queen selected;
	ArrayList<ArrayList<Integer>> makeMove;
	
	public Board(boolean black) {
		if(!black) {
			board = new Tile[][] 
			{
				{null,null,null,new Queen(0,3,false),null,null,new Queen(0,6,false),null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{new Queen(3,0,false),null,null,null,null,null,null,null,null,new Queen(3,9,false)},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{new Queen(6,0,true),null,null,null,null,null,null,null,null,new Queen(6,9,true)},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,new Queen(9,3,true),null,null,new Queen(9,6,true),null,null,null}
			};
			player = new Queen[] {(Queen) board[0][3],(Queen) board[0][6],(Queen) board[3][0],(Queen) board[3][9]};
			opponent = new Queen[] {(Queen) board[6][0],(Queen) board[6][9],(Queen) board[9][3],(Queen) board[9][6]};
		}
		else {
			board = new Tile[][] 
			{
				{null,null,null,new Queen(0,3,true),null,null,new Queen(0,6,true),null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{new Queen(3,0,true),null,null,null,null,null,null,null,null,new Queen(3,9,true)},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{new Queen(6,0,false),null,null,null,null,null,null,null,null,new Queen(6,9,false)},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,null,null,null,null,null,null,null},
				{null,null,null,new Queen(9,3,false),null,null,new Queen(9,6,false),null,null,null}
			};
			player = new Queen[] {(Queen) board[6][0],(Queen) board[6][9],(Queen) board[9][3],(Queen) board[9][6]};
			opponent = new Queen[] {(Queen) board[0][3],(Queen) board[0][6],(Queen) board[3][0],(Queen) board[3][9]};
		}
	}
	public Board(Board oldBoard, boolean enemy) {
        this.board = new Tile[10][10];
        this.opponent = new Queen[4];
        this.player = new Queen[4];
        for(int row = 0; row < 10; row++) {
            for(int col = 0; col < 10; col++) {
                if(oldBoard.board[row][col] instanceof Queen) {
                    Queen oldQueen = (Queen) oldBoard.board[row][col];
                    Queen newQueen = new Queen(row, col, oldQueen.getOpponent());
                    this.board[row][col] = newQueen;
                    if(oldQueen.getOpponent()) {
                        for(int i = 0; i < opponent.length; i++) {
                            if(opponent[i] == null) {
                                opponent[i] = newQueen;
                                break;
                            }
                        }
                    } else {
                        for(int i = 0; i < player.length; i++) {
                            if(player[i] == null) {
                                player[i] = newQueen;
                                break;
                            }
                        }
                    }
                } else if(oldBoard.board[row][col] instanceof Arrow) {
                    this.board[row][col] = new Arrow(row, col);
                }
            }
        }
        if(enemy) {
        	Queen[] temp = player;
        	player = opponent;
        	opponent = temp;
        }
    }
	
	public void updateBoard(ArrayList<Integer> queenPrevPos, ArrayList<Integer> queenNewPos, ArrayList<Integer> arrowPos) {
		updateBoard(queenPrevPos, queenNewPos);
		updateBoard(arrowPos);
	}

	public void updateBoard(ArrayList<Integer> queenPrevPos, ArrayList<Integer> queenNewPos) {
		int prevRow = queenPrevPos.get(0)-1, prevCol = queenPrevPos.get(1)-1, 
			newRow = queenNewPos.get(0)-1, newCol = queenNewPos.get(1)-1;
		setSelected((Queen) board[prevRow][prevCol]);
		getSelected().setPrevRow(prevRow);
		getSelected().setPrevCol(prevCol);
		getSelected().setRow(newRow);
		getSelected().setCol(newCol);
		board[newRow][newCol] = getSelected();
		board[prevRow][prevCol] = null;
	}

	public void updateBoard(ArrayList<Integer> arrowPos) {
		int arrRow = arrowPos.get(0)-1, arrCol = arrowPos.get(1)-1;
		getSelected().setArrRow(arrRow);
		getSelected().setArrCol(arrCol);
		board[arrRow][arrCol] = new Arrow(arrRow,arrCol);
	}

	public ArrayList<ArrayList<Integer>> randomMove(boolean enemy) {
		ArrayList<Integer> queenPrevPos;
        ArrayList<Integer> queenNewPos;
        ArrayList<Integer> arrowPos;
		if(enemy) {
			if(this.gameOverCheck(true) != 1) {
				for(Queen queen: this.opponent) {
	            	queen.moves.getMoves(this,queen);
	            }
				selected = this.opponent[(int) (Math.random()*4)];
				while(selected.moves.moves.size()==0) {
					selected = this.opponent[(int) (Math.random()*4)];
				}
		        ArrayList<Integer> action = selected.moves.moves.get((int) (Math.random()*selected.moves.moves.size()));
		        queenPrevPos = new ArrayList<Integer>();
		        queenPrevPos.add(selected.getRow()+1); queenPrevPos.add(selected.getCol()+1);
		        queenNewPos = new ArrayList<Integer>();
		        queenNewPos.add(selected.getRow()+action.get(0)+1); queenNewPos.add(selected.getCol()+action.get(1)+1);
		        updateBoard(queenPrevPos, queenNewPos);
		        selected.moves.availableArrows(this, selected);
		        ArrayList<Integer> arrowShot = selected.moves.arrowShots.get((int) (Math.random()*selected.moves.arrowShots.size()));
		        arrowPos = new ArrayList<Integer>();
		        arrowPos.add(selected.getRow()+arrowShot.get(0)+1); arrowPos.add(selected.getCol()+arrowShot.get(1)+1);
		        updateBoard(arrowPos);
		        makeMove = new ArrayList<>();
				makeMove.add(queenPrevPos); 
				makeMove.add(queenNewPos); 
				makeMove.add(arrowPos);
				return makeMove;
			}
		}
		else {
			if(this.gameOverCheck(false) != 0) {
				for(Queen queen: this.player) {
	            	queen.moves.getMoves(this,queen);
	            }
				selected = this.player[(int) (Math.random()*4)];
				while(selected.moves.moves.size()==0) {
					selected = this.player[(int) (Math.random()*4)];
				}
		        ArrayList<Integer> action = selected.moves.moves.get((int) (Math.random()*selected.moves.moves.size()));
		        queenPrevPos = new ArrayList<Integer>();
		        queenPrevPos.add(selected.getRow()+1); queenPrevPos.add(selected.getCol()+1);
		        queenNewPos = new ArrayList<Integer>();
		        queenNewPos.add(selected.getRow()+action.get(0)+1); queenNewPos.add(selected.getCol()+action.get(1)+1);
		        updateBoard(queenPrevPos, queenNewPos);
		        selected.moves.availableArrows(this, selected);
		        ArrayList<Integer> arrowShot = selected.bestArrowShot(this);
		        arrowPos = new ArrayList<Integer>();
		        arrowPos.add(selected.getRow()+arrowShot.get(0)+1); arrowPos.add(selected.getCol()+arrowShot.get(1)+1);
		        updateBoard(arrowPos);
		        makeMove = new ArrayList<>();
				makeMove.add(queenPrevPos); makeMove.add(queenNewPos); makeMove.add(arrowPos);
				return makeMove;
			}
		}
		makeMove = new ArrayList<>();
		makeMove.add(null); 
		makeMove.add(null); 
		makeMove.add(null);
		return makeMove;
	}
	
	public int gameOverCheck(boolean enemy) {
        if(enemy) {
        	for(Queen queen: this.opponent) {
            	queen.moves.getMoves(this,queen);
            }
            if(this.opponent[0].moves.moves.size() == 0 && this.opponent[1].moves.moves.size() == 0 
            && this.opponent[2].moves.moves.size() == 0 && this.opponent[3].moves.moves.size() == 0) {
            	return 1;
            }
        }
        else {
        	for(Queen queen: this.player) {
            	queen.moves.getMoves(this,queen);
            }
            if(this.player[0].moves.moves.size() == 0 && this.player[1].moves.moves.size() == 0 
            && this.player[2].moves.moves.size() == 0 && this.player[3].moves.moves.size() == 0) {
            	return 0;
            }
        }
        return -1;
	}

	public int evaluateBoard() {
        int score = 0;
        for(int i = 0; i < player.length; i ++) {
            player[i].moves.getMoves(this, player[i]);
            score += player[i].moves.moves.size();
        }
        for(int i = 0; i < opponent.length; i ++) {
            opponent[i].moves.getMoves(this, opponent[i]);
            score -= opponent[i].moves.moves.size();
        }
        return score;
    }
	
	public boolean inDanger() {
		for(int i=0; i<player.length; i++) {
			//see cells around player[i] to check if it is in danger. 
			int queenRow = player[i].getRow();
			int queenCol = player[i].getCol();
			
			//make a list of emptyPostions
			ArrayList<Tile> emptyPositions = new ArrayList<>();
			//checks bottom 3 tiles
			for(int j=-1; j<1; j++) {
				if(queenRow+1 < 10 && queenCol+j >= 0 && queenCol+j < 10 &&
					board[queenRow+1][queenCol+j] == null) {
					emptyPositions.add(new Tile(queenRow+1,queenCol+j));
				}
			}
			//checks top 3 tiles
			for(int j=-1; j<1; j++) {
				if(queenRow-1 >= 0 && queenCol+j >= 0 && queenCol+j < 10 &&
					board[queenRow-1][queenCol+j] == null) {
					emptyPositions.add(new Tile(queenRow-1,queenCol+j));
				}
			}
			//checks right tile
			if(queenCol+1 < 10 && board[queenRow][queenCol+1] == null) {
				emptyPositions.add(new Tile(queenRow,queenCol+1));
			}
			//checks left tile
			if(queenCol-1 >= 0 && board[queenRow][queenCol-1] == null) {
				emptyPositions.add(new Tile(queenRow,queenCol-1));
			}
			
			//check if its below two
			if (emptyPositions.size() == 1) {
				return true;
			}else if (emptyPositions.size() == 2) {
				if(emptyPositions.get(0).getRow() == emptyPositions.get(1).getRow() &&
						Math.abs(emptyPositions.get(0).getCol()-emptyPositions.get(1).getCol()) == 1) {
					return true;
				} else if(emptyPositions.get(0).getCol() == emptyPositions.get(1).getCol() &&
						Math.abs(emptyPositions.get(0).getRow()-emptyPositions.get(1).getRow()) == 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void printMove() {
		System.out.println("QCurr: "+ makeMove.get(0).toString());
		System.out.println("QNew: "+ makeMove.get(1).toString());
		System.out.println("Arrow: "+ makeMove.get(2).toString());
	}
	public void printBoard() {
		for(int r=9; r>=0; --r) {
			for(int c=0;c<10;++c) {
				if(board[r][c] instanceof Queen)System.out.print(" Q");
				else if(board[r][c] instanceof Arrow)System.out.print(" A");
				else System.out.print(" N");
			}
			System.out.println();
		}
	}

	public Queen getSelected() {
		return this.selected;
	}

	public void setSelected(Queen selected) {
		this.selected = selected;
	}
	
}