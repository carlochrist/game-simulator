package CCPlayerPackage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlo on 25.08.2017.
 */
public class ForecastObject {
    Manager manager;
    private int column;

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


    public ForecastObject(Manager manager){
        this.manager = manager;
    }

    public void generateForecastObjects(){

    }

}
