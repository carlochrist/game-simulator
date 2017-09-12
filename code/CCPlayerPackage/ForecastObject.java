package CCPlayerPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlo on 25.08.2017.
 */
public class ForecastObject {
    Manager manager;
    private int column;
    private VirtualForeCastGameBoard virtualForeCastGameBoard;
    List<ForecastObject> forecastObjects = new ArrayList<>();

    public ForecastObject(Manager manager) {
        this.manager = manager;
//        virtualForeCastGameBoard = new VirtualForeCastGameBoard(manager);
//        virtualForeCastGameBoard.initializeBoard();
        try {
            virtualForeCastGameBoard = (VirtualForeCastGameBoard) manager.getVirtualForecastGameBoard().clone();
        } catch (CloneNotSupportedException ex){

        }


    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public List<ForecastObject> getNextForecastObjects() {
        return nextForecastObjects;
    }

    public void setNextForecastObjects(List<ForecastObject> nextForecastObjects) {
        this.nextForecastObjects = nextForecastObjects;
    }

    private boolean isWin;
    private List<ForecastObject> nextForecastObjects = new ArrayList<>();

    public VirtualForeCastGameBoard getVirtualForeCastGameBoard() {
        return virtualForeCastGameBoard;
    }

    public void setVirtualForeCastGameBoard(VirtualForeCastGameBoard virtualForeCastGameBoard) {
        this.virtualForeCastGameBoard = virtualForeCastGameBoard;
    }

    public void initializeVirtualForecastGameBoard() {
        virtualForeCastGameBoard.initializeBoard();
    }

    public List<ForecastObject> getForecastObjects() {
        return forecastObjects;
    }

    public void setForecastObjects(List<ForecastObject> forecastObjects) {
        this.forecastObjects = forecastObjects;
    }

    public void generateForecastObjects(PlayerEnum playerEnum) {
        for (int i = 0; i <= 6; i++) {
            ForecastObject forecastObject = new ForecastObject(manager);
            //initializeVirtualForecastGameBoard
            forecastObject.virtualForeCastGameBoard = this.virtualForeCastGameBoard;
            //set remaining column
            forecastObject.setColumn(i);
            //add column to VirtualForecastGameBoard
            for(int j = 6; j >= 0; j--){
                if(forecastObject.virtualForeCastGameBoard.virtualForeCastGameBoard[j][forecastObject.getColumn()]==PlayerEnum.EMPTY){
                    forecastObject.virtualForeCastGameBoard.virtualForeCastGameBoard[j][forecastObject.getColumn()] = playerEnum;
                    break;
                }
            }
            //check win (4 coins)
            if(playerEnum == PlayerEnum.OWN){
                forecastObject.setWin(manager.getMoveGenerator().checkOwnWin());
            } else {
                forecastObject.setWin(manager.getMoveGenerator().checkRivalWin());
            }
            forecastObjects.add(forecastObject);
        }
    }

    public void addCoinToBoard(PlayerEnum playerEnum, int col) {
        virtualForeCastGameBoard.addCoinToBoard(playerEnum, col);
    }

    public List<Integer> getRemainingColumnsForecast() {
        List<Integer> columnList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (virtualForeCastGameBoard.getvirtualForeCastGameBoard()[0][i] == PlayerEnum.EMPTY) {
                columnList.add(i);
            }
        }
        return columnList;
    }


}
