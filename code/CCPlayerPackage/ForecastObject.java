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

    public ForecastObject(Manager managerm) {
        this.manager = manager;
        virtualForeCastGameBoard = new VirtualForeCastGameBoard(manager);
        virtualForeCastGameBoard.initializeBoard();
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

    public void generateForecastObjects() {
        for (int i = 0; i < manager.getRemainingColumnsForecast().size(); i++) {
            ForecastObject forecastObject = new ForecastObject(manager);
            forecastObject.setColumn(manager.getRemainingColumnsForecast().get(i));
            forecastObject.setWin(manager.getMoveGenerator().checkRivalWin());
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
