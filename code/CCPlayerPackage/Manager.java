package CCPlayerPackage;

import Visualization.Visualization;
import basic.*;

import java.util.List;

/**
 * Created by Carlo on 02.05.2017.
 */
public class Manager {

    CCPlayer ownPlayer;

    VirtualGameBoard virtualGameBoard = new VirtualGameBoard(this);
    VirtualForeCastGameBoard virtualForecastGameBoard = new VirtualForeCastGameBoard(this);
    MoveGenerator moveGenerator;
    WinSituationDetector winSituationDetector = new WinSituationDetector(this);
    public int lastOwnColumn = 8;

    //Visualization
    //GameSimulator gameSimulator = new GameSimulator();
    Visualization visualization = Main.gs.getVisualization();


    //constructor
    public Manager(CCPlayer ownPlayer){
        this.ownPlayer = ownPlayer;
    }

    //general methods
    public void createMoveGenerator(Position p, List<Move> moves){
        moveGenerator = new MoveGenerator(p, moves, this, this.winSituationDetector, ownPlayer);
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }

    public WinSituationDetector getWinSituationDetector() {
        return winSituationDetector;
    }

    public Visualization getVisualization(){
        return visualization;
    }

    public int getLastOwnColumn() {
        return lastOwnColumn;
    }

    public void setLastOwnColumn(int lastOwnColumn) {
        this.lastOwnColumn = lastOwnColumn;
    }


    //virtualGameBoard
    public void initializeVirtualGameBoard(){
        virtualGameBoard.initializeBoard();
    }
    public void printVirtualGameBoard(){
        virtualGameBoard.printBoard();
    }
    public PlayerEnum getPlayerEnumAtPosition(int row, int column){
        PlayerEnum playerEnum = virtualGameBoard.getPlayerEnumAtPosition(row, column);
        return playerEnum;
    }
    public void addCoinToVirtualGameBoard(PlayerEnum playerEnum, int col){
        virtualGameBoard.addCoinToBoard(playerEnum, col);
    }
    public List<Integer> getRemainingColumns(){
        return virtualGameBoard.getRemainingColumns();
    }
    public VirtualGameBoard getVirtualGameBoard() {
        return virtualGameBoard;
    }
    public void setVirtualGameBoard(VirtualGameBoard virtualGameBoard) {
        this.virtualGameBoard = virtualGameBoard;
    }



    //virtualForecastGameBoard
    public void initializeForecastVirtualGameBoard(){
        virtualForecastGameBoard.initializeBoard();
    }
    public void printForecastVirtualGameBoard(){
        virtualForecastGameBoard.printBoard();
    }

    public void addCoinToVirtualForeCastGameBoard(PlayerEnum playerEnum, int col){
        virtualGameBoard.addCoinToBoard(playerEnum, col);
    }
    public PlayerEnum getPlayerEnumAtPositionForecast(int row, int column){
        PlayerEnum playerEnum = virtualForecastGameBoard.getPlayerEnumAtPosition(row, column);
        return playerEnum;
    }
    public void addCoinToForecastBoard(PlayerEnum playerEnum, int col){
        virtualForecastGameBoard.addCoinToBoard(playerEnum, col);
    }
    public void addCoinToForecastBoardAtPosition(PlayerEnum playerEnum, int row, int col){
        virtualForecastGameBoard.addCoinToSpecificPosition(playerEnum, row, col);
    }
    public List<Integer> getRemainingColumnsForecast(){
        return virtualForecastGameBoard.getRemainingColumns();
    }








    public void swapPlayerCoinsForecastBoard(){
        for(int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                if(getPlayerEnumAtPosition(i, j) == PlayerEnum.RIVAL) {
                    addCoinToForecastBoardAtPosition(PlayerEnum.OWN, i, j);
                } else {
                    if(getPlayerEnumAtPosition(i, j) == PlayerEnum.OWN) {
                        addCoinToForecastBoardAtPosition(PlayerEnum.RIVAL, i, j);
                    }
                }
            }
        }
    }

}
