package basic;

import CCPlayerPackage.Manager;
import CCPlayerPackage.PlayerEnum;
import Visualization.Visualization;
import Visualization.VisualizationController;

import java.util.List;

/**
 * Created by Carlo on 02.05.2017.
 */
public class CCPlayer extends Player {
    //booblean für erste Runde
    boolean firstRound = true;
    Manager manager = new Manager(this);

    Position p;
    List<Move> moves;


    @Override
    Move nextMove(Position p, List<Move> moves) {
        this.p = p;
        this.moves = moves;

        manager.createMoveGenerator(p, moves);

        boolean meFirst = false;

        //Erster Zug
        if(firstRound == true){
            manager.initializeVirtualGameBoard();
            if(p.getLastMove() == null){
                //System.out.println("Eigener Spieler beginnt!");
                meFirst = true;
                ownMove(manager, getColumnOfMoveAsInt(manager.getMoveGenerator().getMove(meFirst)));
            } else {
                //System.out.println("Gegner hat begonnen!");
                rivalMove(manager, p);
                meFirst = false;
                ownMove(manager, getColumnOfMoveAsInt(manager.getMoveGenerator().getMove(meFirst)));
            }
        } else {
            rivalMove(manager, p);
            manager.getWinSituationDetector().checkAllChains();
            ownMove(manager, getColumnOfMoveAsInt(manager.getMoveGenerator().getMove(meFirst)));
        }

        manager.printVirtualGameBoard();


        //end firstRound
        if(firstRound==true) {
            firstRound = false;
        }

        //safe lastOwnColumn + return move
        manager.setLastOwnColumn(getColumnOfMoveAsInt(manager.getMoveGenerator().getPlannedMove()));
        return manager.getMoveGenerator().getPlannedMove();
    }

    @Override
    public void reset(){
        manager = new Manager(this);
        //manager.createMoveGenerator(p, moves);
        manager.initializeVirtualGameBoard();
    }

    public void ownMove (Manager manager, int col){
        manager.addCoinToBoard(PlayerEnum.OWN, col);
    }

    private void rivalMove(Manager manager, Position p){
        String lastMove = getColumnOfMoveAsString(p.getLastMove());
        //System.out.println("letzter Zug des Gegners in Spalte: " + lastMove);
        manager.addCoinToBoard(PlayerEnum.RIVAL, Integer.parseInt(lastMove));
    }

    public String getColumnOfMoveAsString(Move move)
    {
        if(move != null){
            String moveString = move.toString();
            return Character.toString(moveString.charAt(moveString.length()-2));
        } else{
            return "1";
        }

    }

    public int getColumnOfMoveAsInt(Move move)
    {
        return Integer.parseInt(getColumnOfMoveAsString(move));
    }

}
