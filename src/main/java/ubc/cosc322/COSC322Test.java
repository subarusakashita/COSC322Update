package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
    private Tree tree;
    private Node node;
    private int toDepth = 2;
    private Stack<Node> path = null;
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	COSC322Test player = new COSC322Test(args[0], args[1]);
    	//HumanPlayer player = new HumanPlayer();
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
     * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    }
 


    @Override
    public void onLogin() {
    	System.out.println("Congratualations!!! "
    			+ "I am called because the server indicated that the login is successfully");
    	System.out.println("The next step is to find a room and join it: "
    			+ "the gameClient instance created in my constructor knows how!");
    	
    	/*
    	List<Room> rooms = gameClient.getRoomList();
    	for(Room room: rooms) {System.out.print(room.getName() + " ");}
    	System.out.println();
    	int roomIndex = (int) Math.random(rooms.size());
    	gameClient.joinRoom(rooms.get(roomIndex).getName());
    	System.out.print("You have successfully joined room " + roomIndex + ".");
    	*/
    	
    	userName = gameClient.getUserName();
    	if(gamegui != null) {
    		gamegui.setRoomInformation(gameClient.getRoomList());
    	}
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document.
    	
    	System.out.println(msgDetails.get(messageType));
    	switch(messageType) {
    	case GameMessage.GAME_STATE_BOARD:
    		gamegui.setGameState((ArrayList<Integer>) msgDetails.get("game-state"));
    		break;
    	case GameMessage.GAME_ACTION_START:
    		if(msgDetails.get(AmazonsGameMessage.PLAYER_BLACK).equals(this.userName)) {
    			System.out.println("White: " + msgDetails.get(AmazonsGameMessage.PLAYER_WHITE));
    			System.out.println("Black: " + this.userName);
    			node = new Node(new Board(true),false);
    			tree = new Tree(node);
    			handleGameStart();
    		}
    		else {
    			System.out.println("White: " + this.userName);
    			System.out.println("Black: " + msgDetails.get(AmazonsGameMessage.PLAYER_BLACK));
    			node = new Node(new Board(false),true);
    			tree = new Tree(node);
    		}
    		break;
    	case GameMessage.GAME_ACTION_MOVE:
    		try {
    			gamegui.updateGameState(msgDetails);
				handleGameMove(msgDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		break;
    	}
    	
    	return true;
    }
    
    public void handleGameStart() {
    	Node child = new Node(new Board(node.getBoard(),false),false);
    	ArrayList<ArrayList<Integer>> makeMove = child.getBoard().randomMove(false);
        tree.addChild(node, child);
        node = child;
        ArrayList<Integer> queenPrevPos = node.getBoard().makeMove.get(0);
        ArrayList<Integer> queenNewPos = node.getBoard().makeMove.get(1);
        ArrayList<Integer> arrPos = node.getBoard().makeMove.get(2);
        gameClient.sendMoveMessage(queenPrevPos,queenNewPos,arrPos);
        gamegui.updateGameState(queenPrevPos, queenNewPos, arrPos);
        System.out.println("Ally: Queen from [" + queenPrevPos.get(0) + ", " + queenPrevPos.get(1) +"]"
        		+ " to ["+ queenNewPos.get(0) + ", " + queenNewPos.get(1) +"]");
    }
    
    public void handleGameMove(Map<String, Object> msgDetails) {
    	// opponent's Move
    	long init_time = System.currentTimeMillis();
    	
    	ArrayList<Integer> queenPrevPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
        ArrayList<Integer> queenNewPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
        ArrayList<Integer> arrPos = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
        Node child = new Node(new Board(node.getBoard(),true),true);
        System.out.println("Enemy: Queen from [" + queenPrevPos.get(0) + ", " + queenPrevPos.get(1) +"]"
        		+ " to ["+ queenNewPos.get(0) + ", " + queenNewPos.get(1) +"]");
        child.getBoard().updateBoard(queenPrevPos, queenNewPos, arrPos);
        //child.getBoard().printBoard();
        tree.addChild(node, child);
    	node=child;
    	if(node.getBoard().gameOverCheck(false) == 0) {
    		System.out.println("Your opponent is out of moves. You win1!");
    		return;
    	}
        if(node.getBoard().gameOverCheck(true) == 1) {
    		System.out.println("You are out of moves. You lose1!");
    		return;
    	}
    	
        // our move
        System.out.println("-----------------------------------------------------------------");
        for(Queen queen: node.getBoard().player) {
        	queen.moves.getMoves(child.getBoard(), queen);
        }
        for(Queen queen: node.getBoard().opponent) {
        	queen.moves.getMoves(child.getBoard(), queen);
        }
        if(node.getChildren().size() == 0) {
        	tree.growTree(node, toDepth);
        }
        path = tree.findPath(node);
    	path.size();
        node = path.pop();
        queenPrevPos = node.getBoard().makeMove.get(0);
        queenNewPos = node.getBoard().makeMove.get(1);
        arrPos = node.getBoard().makeMove.get(2);
        System.out.println("Ally: Queen from [" + queenPrevPos.get(0) + ", " + queenPrevPos.get(1) +"]"
        		+ " to ["+ queenNewPos.get(0) + ", " + queenNewPos.get(1) +"]");
        System.out.println(node.getBoard().selected);
        while (System.currentTimeMillis() < init_time+1500) {
        	//wait
        }
        gameClient.sendMoveMessage(queenPrevPos,queenNewPos,arrPos);
        gamegui.updateGameState(queenPrevPos, queenNewPos, arrPos);
        //node.getBoard().printBoard();
        if(node.getBoard().gameOverCheck(false) == 0) {
    		System.out.println("You are out of moves. You lose2!");
    		return;
    	}
        if(node.getBoard().gameOverCheck(true) == 1) {
    		System.out.println("Your opponent is out of moves. You win2!");
    		return;
    	}
    }
	
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		return  this.gamegui;
	}

	@Override
	public void connect() {
    	gameClient = new GameClient(userName, passwd, this);			
	}

 
}