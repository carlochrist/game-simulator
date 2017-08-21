package CCPlayerPackage;

import basic.Move;
import basic.CCPlayer;
import basic.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Carlo on 07.05.2017.
 */
public class MoveGenerator {

    Position position;
    List<Move> moves;
    List<Integer> detectedMoves = new ArrayList<>();
    List<Integer> blockedColumns = new ArrayList<>();

    Move plannedMove;
    CCPlayer ownPlayer;
    Manager manager;
    WinSituationDetector winSituationDetector;
    int move = 0;

    public MoveGenerator(Position p, List<Move> moves, Manager manager, WinSituationDetector winSituationDetector, CCPlayer ownPlayer) {
        position = p;
        this.moves = moves;
        this.manager = manager;
        this.winSituationDetector = winSituationDetector;
        this.ownPlayer = ownPlayer;
    }

    public Move getOwnWinMove(List<DetectedChain> ownDetectedChains) {

        List<DetectedChain> winnableChains = new ArrayList<>();
        //Eigene 2er/3er Reihe zum Gewinn führen
        //get >= 2-coin chains
        for (int i = 0; i < ownDetectedChains.size(); i++) {
            if (ownDetectedChains.get(i).getSize() >= 2) {
                winnableChains.add(ownDetectedChains.get(i));
            }
        }

        //VERTICAL
        for(int i = 0; i < winnableChains.size(); i++){
            if(winnableChains.get(i).getChainType()==ChainType.VERTICAL){
                if(winnableChains.get(i).getSize()==3) {
                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getStartPositionCol()) != null) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                            return getMoveOfColumn(winnableChains.get(i).getStartPositionCol());
                        }
                    }
                }
            }
        }

        //HORIZONTAL --> win 3 coin chain
        for (int i = 0; i < winnableChains.size(); i++) {
            //SET ON LEFT
            if (winnableChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                if (winnableChains.get(i).getSize() == 3) {
                    //place left
                    if (winnableChains.get(i).getStartPositionCol() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (winnableChains.get(i).getStartPositionRow() == 6) {
                                    //WIN IT!
                                    return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                } else {
                                    //check falling
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() + 1, winnableChains.get(i).getStartPositionCol() - 1) != null) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() + 1, winnableChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //SET ON RIGHT
                    if (winnableChains.get(i).getEndPositionCol() + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (winnableChains.get(i).getEndPositionRow() == 6) {
                                    //WIN IT!
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    //check falling
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


            //HORIZONTAL --> complete 2coin-chain with missing coin
            for(int i = 0; i < winnableChains.size(); i++) {
                if (winnableChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                    if (winnableChains.get(i).getSize() == 2) {
                        //left
                        if(winnableChains.get(i).getStartPositionCol()-2 >= 0) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 2) != null) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 2) == PlayerEnum.OWN) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }

                            //right
                            if(winnableChains.get(i).getEndPositionCol()+2 < 7) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 2) == PlayerEnum.OWN) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            //DIAGONAL_TOP_LEFT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() -1 >=0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()-1, winnableChains.get(i).getEndPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()-1, winnableChains.get(i).getEndPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                }
                            }
                            }
                        }
                    }
                }
            }


            //DIAGONAL_TOP_RIGHT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
            //DIAGONAL_BOTTOM_RIGHT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() +1 <7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+1, winnableChains.get(i).getEndPositionCol() +1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+1, winnableChains.get(i).getEndPositionCol() +1) == PlayerEnum.EMPTY) {


                                //check falling
                                if(winnableChains.get(i).getEndPositionRow()+1==6) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    if (winnableChains.get(i).getEndPositionRow() + 2 < 7 && winnableChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 2, winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            //DIAGONAL_BOTTOM_LEFT - complete 3 coins
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT){
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() +1 < 7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+1, winnableChains.get(i).getEndPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+1, winnableChains.get(i).getEndPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if(winnableChains.get(i).getEndPositionRow()+1==6) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow()+2, winnableChains.get(i).getEndPositionCol() -1) != PlayerEnum.EMPTY){
                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP RIGHT - 2 coins with empty fields on left & right
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && winnableChains.get(i).getSize() == 2) {
                if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (winnableChains.get(i).getEndPositionCol() + 2 < 7&& winnableChains.get(i).getEndPositionRow() - 2 >= 0) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() + 2) == PlayerEnum.OWN) {
                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP LEFT - 2 coins with empty fields on left & right
        for(int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && winnableChains.get(i).getSize() == 2) {
                if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (winnableChains.get(i).getEndPositionCol() - 2 >= 0 && winnableChains.get(i).getEndPositionRow() - 2 >= 0) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) == PlayerEnum.OWN) {
                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //NO WINMOVE FOUND
        return null;
    }

    public Move preventEnemyWin(){
        List<DetectedChain> harmfulEnemyChains = new ArrayList<>();

        //get 2 & 3-coin rival chains
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() >= 2) {
                harmfulEnemyChains.add(winSituationDetector.getRivalDetectedChains().get(i));
            }
        }

        //BLOCK COLUMNS
        for(int i = 0; i < harmfulEnemyChains.size(); i++){
                if(harmfulEnemyChains.get(i).getSize()==3){
                    //HORIZONTAL LEFT
                    if(harmfulEnemyChains.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyChains.get(i).getStartPositionCol()-1 >= 0) {
                        if (harmfulEnemyChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (harmfulEnemyChains.get(i).getStartPositionRow() - 1 >= 0) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow() - 1, harmfulEnemyChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                        
                    //HORIZONTAL RIGHT
                    if(harmfulEnemyChains.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyChains.get(i).getStartPositionCol()-1 >= 0) {
                        if (harmfulEnemyChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (harmfulEnemyChains.get(i).getEndPositionRow() + 1 < 7) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow() + 1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }


                    //DIAGONAL TOP RIGHT
                    if(harmfulEnemyChains.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow()-1 >= 0) {
                        if(harmfulEnemyChains.get(i).getChainType()==ChainType.DIAGONAL_TOP_RIGHT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }

                    //DIAGONAL TOP LEFT
                    if(harmfulEnemyChains.get(i).getEndPositionCol()-1 >= 0 && harmfulEnemyChains.get(i).getEndPositionRow()-1 >= 0) {
                        if(harmfulEnemyChains.get(i).getChainType()==ChainType.DIAGONAL_TOP_LEFT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(harmfulEnemyChains.get(i).getEndPositionCol() -1);
                                }
                            }
                        }
                    }

                    //DIAGONAL BOTTOM RIGHT
                    if(harmfulEnemyChains.get(i).getEndPositionCol()+1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow()+1 < 7) {
                        if(harmfulEnemyChains.get(i).getChainType()==ChainType.DIAGONAL_BOTTOM_RIGHT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()+1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(harmfulEnemyChains.get(i).getEndPositionCol() +1);
                                }
                            }
                        }
                    }

                    //DIAGONAL BOTTOM LEFT
                    if(harmfulEnemyChains.get(i).getEndPositionCol()-1 >= 0 && harmfulEnemyChains.get(i).getEndPositionRow()+1 < 7) {
                        if(harmfulEnemyChains.get(i).getChainType()==ChainType.DIAGONAL_BOTTOM_LEFT) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()+1, harmfulEnemyChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(harmfulEnemyChains.get(i).getEndPositionCol() -1);
                                }
                            }
                        }
                    }
                }
            }
        

        //VERTICAL
        for(int i = 0; i < harmfulEnemyChains.size(); i++){
            if(harmfulEnemyChains.get(i).getChainType()==ChainType.VERTICAL){
                //WARNING: 3 coins!
                if(harmfulEnemyChains.get(i).getSize()==3){
                    if(harmfulEnemyChains.get(i).getStartPositionRow()-1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow() - 1, harmfulEnemyChains.get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                            return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol());
                        }
                    }
                }
            }
        }

//        //HORIZONTAL --> prevent completion of chain
//        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
//            if (harmfulEnemyChains.get(i).getChainType() == ChainType.HORIZONTAL) {
//                if (harmfulEnemyChains.get(i).getSize() == 2) {
//                    //left
//                    if(harmfulEnemyChains.get(i).getStartPositionCol()-2 >= 0){
//                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 2) != null) {
//                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 2) == PlayerEnum.RIVAL) {
//                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
//                                return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
//                            }
//                        }
//                    }
//                    }
//                    //right
//                    if((harmfulEnemyChains.get(i).getEndPositionCol()+2) <7) {
//                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 2) != null) {
//                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
//                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
//                                    return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
//                                }
//                            }
//                        }
//                    }
//                    }
//                }
//            }


        //HORIZONTAL 2 coins with empty fields on left & right
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.HORIZONTAL && harmfulEnemyChains.get(i).getSize() == 2) {
                if (harmfulEnemyChains.get(i).getStartPositionCol() - 1 >= 0 && harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7){
                    //check if left and right are empty
                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) != null) {
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                //left
                                if (harmfulEnemyChains.get(i).getStartPositionCol() - 1 >= 0) {
                                    if (harmfulEnemyChains.get(i).getStartPositionRow() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow() + 1, harmfulEnemyChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                                        }
                                    }
                                }
                                //right
                                if (harmfulEnemyChains.get(i).getEndPositionCol() + 1  < 7) {
                                    if (harmfulEnemyChains.get(i).getEndPositionRow() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow() + 1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                }

//                                    Random random = new Random();
//                                    int setLeft = random.nextInt(2);
//                                    if (setLeft == 1) {
//                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol()-1);
//                                    } else {
//                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol()+1);
//                                    }

                            }
                        }
                    }
                }
            }
            }
        }

        //DIAGONAL TOP RIGHT - 2 coins with empty fields on left & right
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && harmfulEnemyChains.get(i).getSize() == 2) {
                if(harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow() - 1 >= 0){
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if(harmfulEnemyChains.get(i).getEndPositionRow()-2 >= 0 && harmfulEnemyChains.get(i).getEndPositionCol()+2 < 7){
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol()+1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP LEFT - 2 coins with empty fields on left & right
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT && harmfulEnemyChains.get(i).getSize() == 2) {
                if(harmfulEnemyChains.get(i).getEndPositionCol() -1 >= 0 && harmfulEnemyChains.get(i).getEndPositionRow() - 1 >= 0){
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if(harmfulEnemyChains.get(i).getEndPositionRow()-2 >= 0 && harmfulEnemyChains.get(i).getEndPositionCol()-2 >= 0){
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() - 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() - 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol()-1);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        //DIAGONAL BOTTOM RIGHT - 2 coins with empty fields on left & right
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT && harmfulEnemyChains.get(i).getSize() == 2) {
                if(harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow() - 1 >= 0){
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                           if(harmfulEnemyChains.get(i).getEndPositionRow()-2 >= 0 && harmfulEnemyChains.get(i).getEndPositionCol()+2 <7){
                               if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() + 2) != null) {
                                   if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                       return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol()+1);
                                   }
                               }
                           }
                        }
                    }
                }
            }
        }

        //DIAGONAL BOTTOM LEFT - 2 coins with empty fields on left & right
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT && harmfulEnemyChains.get(i).getSize() == 2) {
                if(harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow() - 1 >= 0){
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if(harmfulEnemyChains.get(i).getEndPositionRow()-2 >= 0 && harmfulEnemyChains.get(i).getEndPositionRow()-2 >= 0){
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()-2, harmfulEnemyChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol()+1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        
        
        
        
        //HORIZONTAL: block size: 3
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            //chainSize: 3
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.HORIZONTAL && harmfulEnemyChains.get(i).getSize() >= 3) {
                //SET ON LEFT
                if (harmfulEnemyChains.get(i).getStartPositionCol() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                            //check falling
                            if (harmfulEnemyChains.get(i).getEndPositionRow() == 6) {
                                return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                            } else {
                                if(harmfulEnemyChains.get(i).getStartPositionRow() + 1 < 7 && harmfulEnemyChains.get(i).getStartPositionCol() - 1 >= 0)
                                {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow() + 1, harmfulEnemyChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
                //SET ON RIGHT
                if (harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            //check falling
                            if (harmfulEnemyChains.get(i).getEndPositionRow() == 6) {
                                return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                            } else {
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow() + 1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL_TOP_LEFT - complete 3 coins
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (harmfulEnemyChains.get(i).getSize() == 3) {
                    if (harmfulEnemyChains.get(i).getStartPositionCol() - 1 >= 0 && harmfulEnemyChains.get(i).getStartPositionRow() -1 >=0) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow()-1, harmfulEnemyChains.get(i).getStartPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow()-1, harmfulEnemyChains.get(i).getStartPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow(), harmfulEnemyChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }


        //DIAGONAL_TOP_RIGHT - complete 3 coins
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (harmfulEnemyChains.get(i).getSize() == 3) {
                    if (harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow() - 1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow() - 1, harmfulEnemyChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow(), harmfulEnemyChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL_BOTTOM_RIGHT - complete 3 coins
            for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                if (harmfulEnemyChains.get(i).getSize() == 3) {
                    if (harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7 && harmfulEnemyChains.get(i).getEndPositionRow() +1 <7) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()+1, harmfulEnemyChains.get(i).getEndPositionCol() +1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow()+1, harmfulEnemyChains.get(i).getEndPositionCol() +1) == PlayerEnum.EMPTY) {
                                //check falling
                                if(harmfulEnemyChains.get(i).getEndPositionRow()+1==6) {
                                    return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    if(harmfulEnemyChains.get(i).getEndPositionRow() + 2 < 7 && harmfulEnemyChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getEndPositionRow() + 2, harmfulEnemyChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(harmfulEnemyChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                }
                                }
                            }
                        }
                    }
                }
            }

        //DIAGONAL_BOTTOM_LEFT - complete 3 coins
        for(int i = 0; i < harmfulEnemyChains.size(); i++) {
            if (harmfulEnemyChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT){
                if (harmfulEnemyChains.get(i).getSize() == 3) {
                    if (harmfulEnemyChains.get(i).getStartPositionCol() - 1 >= 0 && harmfulEnemyChains.get(i).getStartPositionRow() +1 < 7) {
                        if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow()+1, harmfulEnemyChains.get(i).getStartPositionCol() -1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow()+1, harmfulEnemyChains.get(i).getStartPositionCol() -1) == PlayerEnum.EMPTY) {
                                //check falling
                                if(harmfulEnemyChains.get(i).getStartPositionRow()+1==6) {
                                    return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(harmfulEnemyChains.get(i).getStartPositionRow()+2, harmfulEnemyChains.get(i).getStartPositionCol() -1) != PlayerEnum.EMPTY){
                                        return getMoveOfColumn(harmfulEnemyChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //no prevention needed
        return null;
    }

    public Move getBasicMove(int lastOwnColumn) {

        List<Integer> remainingColumns = manager.getRemainingColumns();
        Random random = new Random();
        int rnd = random.nextInt(2);

        //first Move
        if(lastOwnColumn == 8){
            return moves.get(3);
        } else {
            //other moves
            for(int i = 0; i < remainingColumns.size(); i++){
                //place left (rnd = 0)
                if(rnd == 0){
                    if(remainingColumns.get(i)==lastOwnColumn-2) {
                        return moves.get(i);
                    }
                    else{
                            if(remainingColumns.get(i)==lastOwnColumn+2) {
                                return moves.get(i);
                            }
                        }
                } else {
                    //place right (rnd = 1)
                        if(remainingColumns.get(i)==lastOwnColumn+2){
                            return moves.get(i);
                        }
                        else {
                            if(remainingColumns.get(i)==lastOwnColumn-2){
                                return moves.get(i);
                            }
                        }
                }
            }

        }

        //if nothing works..
        return moves.get(moves.size()/2);
    }

//    public Move getMove_simple() {
//        if (getOwnWinMove(winSituationDetector.getOwnDetectedChains()) != null) {
//            plannedMove = getOwnWinMove(winSituationDetector.getOwnDetectedChains());
//            return plannedMove;
//        } else {
//            //prevent enemy
//            if (preventEnemyWin() != null) {
//
//                //return move
//                plannedMove = preventEnemyWin();
//            } else {
//                //basic move
//                plannedMove = getBasicMove(manager.getLastOwnColumn());
//            }
//        }
//
//        //return move
//        return plannedMove;
//    }


    public Move getMove(boolean meFirst) {

        //safe real virtualGameBoard
        VirtualGameBoard tempVirtualGameBoard = manager.getVirtualGameBoard();

        //logic sequence
        //win it!
        if(getOwnWinMove(winSituationDetector.getOwnDetectedChains()) != null){
            plannedMove = getOwnWinMove(winSituationDetector.getOwnDetectedChains());
            return plannedMove;
        } else {
            //prevent enemy
            if(preventEnemyWin()!=null){
//
//FORECAST
////                //prevention found --> now forecast!
////                //initialize forecastBoard
////                manager.initializeForecastVirtualGameBoard();
////                //add own coin to forecastBoard
////                manager.addCoinToForecastBoard(PlayerEnum.OWN, ownPlayer.getColumnOfMoveAsInt(preventEnemyWin()));
////
////
////                //RIVAL MOVE
////                //swap coins
////                manager.swapPlayerCoinsForecastBoard();
////
////
////                //check all possible rival moves to new constellation
////                for(int i = 0; i < manager.getRemainingColumns().size(); i++){
////
////                }

                //return move
                plannedMove = preventEnemyWin();
            }
        }

        //check if move is on blocked list

        List<Integer> notBlockedMoves = manager.getVirtualGameBoard().getRemainingColumns();
        for(int i = 0; i < manager.getVirtualGameBoard().getRemainingColumns().size(); i++){
            for(int j = 0; j < blockedColumns.size(); j++){
                if (manager.getVirtualGameBoard().getRemainingColumns().get(i) == blockedColumns.get(j)) {
                    //only remove from blocked moves if there are at least 2 possible ones
                    if(notBlockedMoves.size()>=2){
                        notBlockedMoves.remove(manager.getVirtualGameBoard().getRemainingColumns().get(i));
                    }
                }
            }
        }

        //get not blocked move, if no own or enemy-prevention-move
        if(plannedMove == null) {
            if (meFirst == true) {
                return plannedMove = getMoveOfColumn(4);
            } else {
                Random random = new Random();
                //moves.get(random.nextInt(moves.size()))
                //int randomValue = random.nextInt((notBlockedMoves.size())+1)-1;
                System.out.println("NOTBLOCKED MOVES");
                System.out.println(notBlockedMoves.size());
                if(notBlockedMoves.size()!=0){
                    plannedMove = getMoveOfColumn(notBlockedMoves.get(random.nextInt(notBlockedMoves.size())));
                } else {
                    return plannedMove = getMoveOfColumn(4);
                }

                //plannedMove = getMoveOfColumn(randomValue);
                //or basic move
                //TODO: combine this! plannedMove = getBasicMove(manager.getLastOwnColumn());
            }
        }

        //restore virtualGameBoard
        manager.setVirtualGameBoard(tempVirtualGameBoard);

        //return move
        return plannedMove;
    }

    public Move getPlannedMove() {
        return plannedMove;
    }

    public Move getMoveOfColumn(int column){
        Move returnMove = null;
        for(int i = 0; i < moves.size(); i++){
            String moveString = moves.get(i).toString();
            if(Integer.parseInt(Character.toString(moveString.charAt(moveString.length()-2)))==column+1){
                returnMove = moves.get(i);
            }
        }
        return returnMove;
    }

    public String getColumnOfMoveAsString(Move move)
    {
        String moveString = move.toString();
        return Character.toString(moveString.charAt(moveString.length()-2));
    }
}

