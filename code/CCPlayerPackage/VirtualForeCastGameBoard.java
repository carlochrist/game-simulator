package CCPlayerPackage;

import Visualization.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlo on 02.05.2017.
 */
public class VirtualForeCastGameBoard {

    Manager manager;
    public PlayerEnum [][] virtualForeCastGameBoard = new PlayerEnum[7][7];

    public VirtualForeCastGameBoard(Manager manager){
        this.manager = manager;
    }

    //initialization
    public void initializeBoard(){
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                virtualForeCastGameBoard[i][j] = manager.getPlayerEnumAtPosition(i, j);
            }
        }
    }


    public void printBoard(){
        String line = "";
        System.out.println("         Col 1 | Col 2 | Col 3 | Col 4 | Col 5 | Col 6 | Col 7 |");
        for (int i = 0; i < 7; i++){
            line = "Row " + i + ": | ";
            for (int j = 0; j < 7; j++){
                if(virtualForeCastGameBoard[i][j] == PlayerEnum.OWN){
                    line += virtualForeCastGameBoard[i][j] + "   | ";
                } else{
                    line += virtualForeCastGameBoard[i][j] + " | ";
                }

            }
            //System.out.println(manager.getVisualization().getVisualizationTest());
            //printLineForVisualization(line);
            System.out.println(line);
        }
    }

    public void printLineForVisualization(String line){
        //System.out.println(manager.getVisualization());


//     Visualization visualization = manager.getVisualization();
//     VisualizationController visualizationController = visualization.getVisualizationController();
//     visualizationController.setValue(line);

        manager.getVisualization().getVisualizationController().setValue(line);
    }

    public void addCoinToBoard(PlayerEnum playerEnum, int col){
        for(int i = 6; i >= 0; i--){
                if(virtualForeCastGameBoard[i][col-1]==PlayerEnum.EMPTY){
                    virtualForeCastGameBoard[i][col-1] = playerEnum;
                    break;
                }
            }
        }

    public void addCoinToSpecificPosition(PlayerEnum playerEnum, int row, int col){
        virtualForeCastGameBoard[row][col] = playerEnum;
    }

    public PlayerEnum [][] getvirtualForeCastGameBoard(){
        return virtualForeCastGameBoard;
    }

    public PlayerEnum getPlayerEnumAtPosition(int row, int column){
        return virtualForeCastGameBoard[row][column];
    }

    public List<Integer> getRemainingColumns(){
        List<Integer> columnList = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            if(virtualForeCastGameBoard[0][i] == PlayerEnum.EMPTY){
                columnList.add(i);
            }
        }
        return columnList;
    }

    public int countEnemyCoinsOnBoard(){
        int count = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (manager.getPlayerEnumAtPosition(i, j) == PlayerEnum.RIVAL) {
                    count++;
                }
            }
        }
        return count;
    }




}
