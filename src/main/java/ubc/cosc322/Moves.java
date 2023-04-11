package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;

public class Moves {
	List<ArrayList<Integer>> moves;
	List<ArrayList<Integer>> arrowShots;
	
	public Moves() {
		this.moves = new ArrayList<ArrayList<Integer>>();
		this.arrowShots = new ArrayList<ArrayList<Integer>>();
	}

	public void getMoves(Board board, Queen queen) {
		moves = new ArrayList<ArrayList<Integer>>();
		int queenRow = queen.getRow();
		int queenCol = queen.getCol();
		ArrayList<Integer> move;

		// up
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0) {
				break;
			} else if(board.board[queenRow - i][queenCol] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(0);
			moves.add(move);
		}

		// down
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9) {
				break;
			} else if(board.board[queenRow + i][queenCol] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(0);
			moves.add(move);
		}

		// left
		for(int i=1; i<10; ++i) {
			if(queenCol - i < 0) {
				break;
			} else if(board.board[queenRow][queenCol - i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(-i);
			moves.add(move);
		}

		// right
		for(int i=1; i<10; ++i) {
			if(queenCol + i > 9) {
				break;
			} else if(board.board[queenRow][queenCol + i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(i);
			moves.add(move);
		}
		
		// up left
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol - i < 0) {
				break;
			} else if(board.board[queenRow - i][queenCol - i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(-i);
			moves.add(move);
		}

		// up right
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol + i > 9) {
				break;
			} else if(board.board[queenRow - i][queenCol + i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(i);
			moves.add(move);
		}

		// down left
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol - i < 0) {
				break;
			} else if(board.board[queenRow + i][queenCol - i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(-i);
			moves.add(move);
		}

		// down right
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol + i > 9) {
				break;
			} else if(board.board[queenRow + i][queenCol + i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(i);
			moves.add(move);
		}
	}

	public boolean escape(Board board, Queen queen) {
		for(int i=0;i<board.player.length;i++) {
			int numofmove = moves.size();
			if(numofmove <2) {
				return true;
			}
			
		}
		return false;
	}

	public void availableArrows(Board board, Queen queen) {
		arrowShots = new ArrayList<ArrayList<Integer>>();
		int queenRow = queen.getRow();
		int queenCol = queen.getCol();
		ArrayList<Integer> move;

		// up
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0) {
				break;
			} else if(board.board[queenRow - i][queenCol] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(0);
			arrowShots.add(move);
		}

		// down
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9) {
				break;
			}
			else if(board.board[queenRow + i][queenCol] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(0);
			arrowShots.add(move);
		}

		// left
		for(int i=1; i<10; ++i) {
			if(queenCol - i < 0) {
				break;
			} else if(board.board[queenRow][queenCol - i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(-i);
			arrowShots.add(move);
		}

		// right
		for(int i=1; i<10; ++i) {
			if(queenCol + i > 9) {
				break;
			} else if(board.board[queenRow][queenCol + i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(0);
			move.add(i);
			arrowShots.add(move);
		}

		// up left
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol - i < 0) {
				break;
			} else if(board.board[queenRow - i][queenCol - i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(-i);
			arrowShots.add(move);
		}

		// up right
		for(int i=1; i<10; ++i) {
			if(queenRow - i < 0 || queenCol + i > 9) {
				break;
			} else if(board.board[queenRow - i][queenCol + i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(-i);
			move.add(i);
			arrowShots.add(move);
		}
	
		// down left
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol - i < 0) {
				break;
			} else if(board.board[queenRow + i][queenCol - i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(-i);
			arrowShots.add(move);
		}

		// down right
		for(int i=1; i<10; ++i) {
			if(queenRow + i > 9 || queenCol + i > 9) {
				break;
			} else if(board.board[queenRow + i][queenCol + i] != null) {
				break;
			}
			move = new ArrayList<>();
			move.add(i);
			move.add(i);
			arrowShots.add(move);
		}
	}
}