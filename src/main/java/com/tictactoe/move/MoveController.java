package com.tictactoe.move;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MoveController {

    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public int checkWin(char[] state,int currentMove){
        
        return 2; // return 2 for win, 1 for draw, 0 for lost
    }

    public int miniMax(char[] currentState,int prevMove,int currentMoveCount,boolean turn,int maxMove){
        //currentState = board state in a string, update everytime for the deeper call
        //prevMove = index of the previousMove
        //currentMoveCount = depth of the tree
        //turn = true if computer's turn, false otherwise. Alternate for every call
        //If the turn is computer's minimize the max
        //maxMove = max depth of the tree, easy=1,medium =3, hard = 9
        int win = checkWin(currentState, prevMove);
        boolean flag = true;
        for (int i=0;i<currentState.length;i++) {
            if (currentState[i] == '0') { // All the cells are filled check if either has won..Base case
                flag = false;
                break;
            }
        }
        if(flag || win!=1){ // no zeros,the board is completely filled or either player has won
            return currentMoveCount * win ;//lesser the move higher should be the value
        }
        int currentTurn;
        if(turn) { //computer's move make the cell 1
            currentTurn = 1;
        }
        else{
            currentTurn = 2;
        }
        List<Integer> list = new ArrayList<>();
        for (int i=0;i<currentState.length;i++){
            if(currentState[i] == '0'){
                currentState[i] = (char) currentTurn;
                list.add(miniMax(currentState,i,currentMoveCount+1,!turn,maxMove));
                currentState[i] = '0';
            }
        }
        if(currentMoveCount == 1){//Now we have to return move instead of returning optimized value
            int minIndex = list.indexOf(Collections.min(list));
            return minIndex;
        }
        if(turn){ // check if it's computer turn , or maxmove reached and minimize the max
            return Collections.min(list);
        }
        else{
           return Collections.max(list);
        }
    }

    @GetMapping("/move")
    public Move move(@RequestParam("board") String state, @RequestParam("maxmove") int maxMove, @RequestParam("prevmove") int prevMove) {
        this.setState(state);
        int cell = this.miniMax(state.toCharArray(),prevMove,0,true,maxMove);
        return new Move(cell);
    }
}
