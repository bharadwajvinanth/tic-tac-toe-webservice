package com.tictactoe.move;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {
    private int cell;
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }
    public int checkWin(char[] state,int currentMove){
        //Check for current move's row column and diagonal
        return 2; // return 2 for win, 1 for draw, 0 for lost
    }
    public int miniMax(char[] currentState,int prevMove,int currentMoveCount,boolean turn,int maxMove){
        // currentState = board state in a string, update everytime for the deeper call
        //prevMove = index of the previousMove
        //currentMoveCount = depth of the tree
        //turn = true if computer's turn, false otherwise. Alternate for every call
        //If the turn is computer's minimize the max
        //maxMove = max depth of the tree, easy=1,medium =3, hard = 9
        boolean flag = true;
        for (int i=0;i<currentState.length;i++) {
            if (currentState[i] == '0') { // All the cells are filled check if either has won..Base case
                flag = false;
                break;
            }
        }
        if(flag){ // no zeros the board is completely filled
            int win = checkWin(currentState, prevMove);
            return currentMoveCount * win * -1;//lesser the move higher should be the value
        }
        int currentTurn;
        if(turn) {//computer's move make the cell 1
            currentTurn = 1;
        }
        else{
            currentTurn = 2;
        }
        for (int i=0;i<currentState.length;i++){
            if(currentState[i] == '0'){
                currentState[i] = (char) currentTurn;
                miniMax(currentState,i,currentMoveCount+1,!turn,maxMove);
                currentState[i] = '0';
            }
        }
        if(turn){ // check if it's computer turn , or maxmove reached and minimize the max

        }
        return 1;//Written simply change
    }

    @GetMapping("/move")
    public Move move(@RequestParam("board") String state, @RequestParam("maxmove") int maxMove, @RequestParam("prevmove") int prevMove) {
        this.setState(state);
        this.miniMax(state.toCharArray(),prevMove,0,true,maxMove);
        return new Move(cell);
    }
}
