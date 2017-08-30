package CCPlayerPackage;

import basic.GUI;
import basic.Move;
import basic.CCPlayer;
import basic.Position;
import plotter.Sleep;

import java.util.*;

/**
 * Created by Carlo on 07.05.2017.
 */
public class MoveGenerator {

    Position position;
    List<Move> moves;
    List<Integer> blockedColumns = new ArrayList<>();

    Move plannedMove;
    CCPlayer ownPlayer;
    Manager manager;
    WinSituationDetector winSituationDetector;

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
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.VERTICAL) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getStartPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() - 1, winnableChains.get(i).getStartPositionCol()) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() - 1, winnableChains.get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                                return getMoveOfColumn(winnableChains.get(i).getStartPositionCol());
                            }
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
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                if (winnableChains.get(i).getSize() == 2) {
                    //left
                    if (winnableChains.get(i).getStartPositionCol() - 2 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 2) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 2) == PlayerEnum.OWN) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow(), winnableChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                }
                            }
                        }
                    }

                    //right
                    if (winnableChains.get(i).getEndPositionCol() + 2 < 7) {
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
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
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
        for (int i = 0; i < winnableChains.size(); i++) {
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
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (winnableChains.get(i).getEndPositionRow() + 1 == 6) {
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
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (winnableChains.get(i).getEndPositionRow() + 1 == 6) {
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 2, winnableChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
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
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && winnableChains.get(i).getSize() == 2) {
                if (winnableChains.get(i).getEndPositionCol() + 1 < 7 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (winnableChains.get(i).getEndPositionCol() + 2 < 7 && winnableChains.get(i).getEndPositionRow() - 2 >= 0) {
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
        for (int i = 0; i < winnableChains.size(); i++) {
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

        //check special moves
        //checkSpecialMoves(PlayerEnum.OWN);

        //BLOCK COLUMNS
        blockColumns(winnableChains, PlayerEnum.OWN);

        //NO WINMOVE FOUND
        return null;
    }

    public Move preventEnemyWin() {
        List<DetectedChain> harmfulRivalChains = new ArrayList<>();

        //get 2 & 3-coin rival chains
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() >= 2) {
                harmfulRivalChains.add(winSituationDetector.getRivalDetectedChains().get(i));
            }
        }

        //BLOCK COLUMNS
        blockColumns(harmfulRivalChains, PlayerEnum.RIVAL);


        //VERTICAL
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.VERTICAL) {
                //WARNING: 3 coins!
                if (harmfulRivalChains.get(i).getSize() == 3) {
                    if (harmfulRivalChains.get(i).getStartPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() - 1, harmfulRivalChains.get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                            return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol());
                        }
                    }
                }
            }
        }

//        //HORIZONTAL --> prevent completion of chain
//        for(int i = 0; i < harmfulRivalChains.size(); i++) {
//            if (harmfulRivalChains.get(i).getChainType() == ChainType.HORIZONTAL) {
//                if (harmfulRivalChains.get(i).getSize() == 2) {
//                    //left
//                    if(harmfulRivalChains.get(i).getStartPositionCol()-2 >= 0){
//                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 2) != null) {
//                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 2) == PlayerEnum.RIVAL) {
//                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
//                                return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
//                            }
//                        }
//                    }
//                    }
//                    //right
//                    if((harmfulRivalChains.get(i).getEndPositionCol()+2) <7) {
//                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 2) != null) {
//                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
//                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
//                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
//                                }
//                            }
//                        }
//                    }
//                    }
//                }
//            }


        //HORIZONTAL 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.HORIZONTAL && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0 && harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7) {
                    //check if left and right are empty
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    //check falling
                                    //no falling - row = 6
                                    if(harmfulRivalChains.get(i).getStartPositionRow() == 6){
                                        Random random = new Random();
                                        boolean left = random.nextBoolean();
                                        if(left){
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                        } else {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                    //left
                                    if (harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0) {
                                        if (harmfulRivalChains.get(i).getStartPositionRow() + 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                                if(manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                    if(checkNextRivalMoveWin(harmfulRivalChains.get(i).getStartPositionCol() - 1) == false){

                                                    }
                                                    return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                                }
                                            }
                                        }
                                    }
                                    //right
                                    if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                if(manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol()- 1) != PlayerEnum.EMPTY){
                                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                                }

                                            }
                                        }
                                    }

//                                    Random random = new Random();
//                                    int setLeft = random.nextInt(2);
//                                    if (setLeft == 1) {
//                                        return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol()-1);
//                                    } else {
//                                        return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol()+1);
//                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP RIGHT - 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (harmfulRivalChains.get(i).getEndPositionRow() - 2 >= 0 && harmfulRivalChains.get(i).getEndPositionCol() + 2 < 7) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP LEFT - 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getEndPositionCol() - 1 >= 0 && harmfulRivalChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (harmfulRivalChains.get(i).getEndPositionRow() - 2 >= 0 && harmfulRivalChains.get(i).getEndPositionCol() - 2 >= 0) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() - 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() - 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        //DIAGONAL BOTTOM RIGHT - 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (harmfulRivalChains.get(i).getEndPositionRow() - 2 >= 0 && harmfulRivalChains.get(i).getEndPositionCol() + 2 < 7) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL BOTTOM LEFT - 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (harmfulRivalChains.get(i).getEndPositionRow() - 2 >= 0 && harmfulRivalChains.get(i).getEndPositionRow() - 2 >= 0) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        //HORIZONTAL: block size: 3
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            //chainSize: 3
            if (harmfulRivalChains.get(i).getChainType() == ChainType.HORIZONTAL && harmfulRivalChains.get(i).getSize() >= 3) {
                //SET ON LEFT
                if (harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                            //check falling
                            if (harmfulRivalChains.get(i).getEndPositionRow() == 6) {
                                return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                            } else {
                                if (harmfulRivalChains.get(i).getStartPositionRow() + 1 < 7 && harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
                //SET ON RIGHT
                if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            //check falling
                            if (harmfulRivalChains.get(i).getEndPositionRow() == 6) {
                                return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                            } else {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL_TOP_LEFT - complete 3 coins
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (harmfulRivalChains.get(i).getSize() == 3) {
                    if (harmfulRivalChains.get(i).getEndPositionCol() - 1 >= 0 && harmfulRivalChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }


        //DIAGONAL_TOP_RIGHT - complete 3 coins
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (harmfulRivalChains.get(i).getSize() == 3) {
                    if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() - 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL_BOTTOM_RIGHT - complete 3 coins
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                if (harmfulRivalChains.get(i).getSize() == 3) {
                    if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (harmfulRivalChains.get(i).getEndPositionRow() + 1 == 6) {
                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    if (harmfulRivalChains.get(i).getEndPositionRow() + 2 < 7 && harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
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
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT) {
                if (harmfulRivalChains.get(i).getSize() == 3) {
                    if (harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0 && harmfulRivalChains.get(i).getStartPositionRow() + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (harmfulRivalChains.get(i).getStartPositionRow() + 1 == 6) {
                                    return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 2, harmfulRivalChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
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


    public void blockColumns(List<DetectedChain> foundChains, PlayerEnum playerEnum) {
        //BLOCK COLUMNS
        for (int i = 0; i < foundChains.size(); i++) {

            //3 COINS
            if (foundChains.get(i).getSize() == 3) {
                //HORIZONTAL LEFT
                if (foundChains.get(i).getEndPositionCol() + 1 < 7 && foundChains.get(i).getStartPositionCol() - 1 >= 0) {
                    if (foundChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getStartPositionRow(), foundChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (foundChains.get(i).getStartPositionRow() - 1 >= 0) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getStartPositionRow() - 1, foundChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(foundChains.get(i).getStartPositionCol() - 1);
                                }
                            }
                        }
                    }
                }

                //HORIZONTAL RIGHT
                if (foundChains.get(i).getEndPositionCol() + 1 < 7 && foundChains.get(i).getStartPositionCol() - 1 >= 0) {
                    if (foundChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (foundChains.get(i).getEndPositionRow() + 1 < 7) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }


                //DIAGONAL TOP RIGHT
                if (foundChains.get(i).getEndPositionCol() + 1 < 7 && foundChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 1, foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (foundChains.get(i).getEndPositionCol() + 1 >= 0 && foundChains.get(i).getEndPositionRow() + 1 >= 0) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }

                //DIAGONAL TOP LEFT
                if (foundChains.get(i).getEndPositionCol() - 1 >= 0 && foundChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 1, foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (foundChains.get(i).getEndPositionCol() - 1 >= 0 && foundChains.get(i).getEndPositionRow() + 1 < 7) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }

                //DIAGONAL BOTTOM RIGHT
                if (foundChains.get(i).getEndPositionCol() + 1 < 7 && foundChains.get(i).getEndPositionRow() + 1 < 7) {
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                if (foundChains.get(i).getEndPositionCol() + 1 >= 0 && foundChains.get(i).getEndPositionRow() + 1 >= 0) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }
                }

                //DIAGONAL BOTTOM LEFT
                if (foundChains.get(i).getEndPositionCol() - 1 >= 0 && foundChains.get(i).getEndPositionRow() + 1 < 7) {
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                if (foundChains.get(i).getEndPositionCol() - 1 >= 0 && foundChains.get(i).getEndPositionRow() + 1 >= 0) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }


                //2 COINS DIAGONAL
                if (foundChains.get(i).getSize() == 2) {
                    //DIAGONAL TOP RIGHT
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                        if (foundChains.get(i).getEndPositionRow() - 2 >= 0 && foundChains.get(i).getEndPositionCol() + 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 2, foundChains.get(i).getEndPositionCol() + 2) == playerEnum) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 1, foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }


                    //DIAGONAL TOP LEFT
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                        if (foundChains.get(i).getEndPositionRow() - 2 >= 0 && foundChains.get(i).getEndPositionCol() - 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 2, foundChains.get(i).getEndPositionCol() - 2) == playerEnum) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 1, foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }

                    //DIAGONAL BOTTOM RIGHT
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                        if (foundChains.get(i).getEndPositionRow() + 2 >= 0 && foundChains.get(i).getEndPositionCol() - 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 2, foundChains.get(i).getEndPositionCol() + 2) == playerEnum) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }
                    }

                    //DIAGONAL BOTTOM LEFT
                    if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT) {
                        if (foundChains.get(i).getEndPositionRow() + 2 >= 0 && foundChains.get(i).getEndPositionCol() - 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 2, foundChains.get(i).getEndPositionCol() - 2) == playerEnum) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                        blockedColumns.add(foundChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public Move getBasicMove(int lastOwnColumn) {

        List<Integer> remainingColumns = manager.getRemainingColumns();
        Random random = new Random();
        int rnd = random.nextInt(2);

        //first Move
        if (lastOwnColumn == 8) {
            return moves.get(3);
        } else {
            //other moves
            for (int i = 0; i < remainingColumns.size(); i++) {
                //place left (rnd = 0)
                if (rnd == 0) {
                    if (remainingColumns.get(i) == lastOwnColumn - 2) {
                        return moves.get(i);
                    } else {
                        if (remainingColumns.get(i) == lastOwnColumn + 2) {
                            return moves.get(i);
                        }
                    }
                } else {
                    //place right (rnd = 1)
                    if (remainingColumns.get(i) == lastOwnColumn + 2) {
                        return moves.get(i);
                    } else {
                        if (remainingColumns.get(i) == lastOwnColumn - 2) {
                            return moves.get(i);
                        }
                    }
                }
            }

        }

        //if nothing works..
        return moves.get(moves.size() / 2);
    }

    //specialWinMoves
    //TODO: two own coins with empty coins to the left and right and one empty coin in the middle them

    public Move checkSpecialMoves(PlayerEnum playerEnum) {
        for (int i = 6; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (manager.getPlayerEnumAtPosition(i, j) == playerEnum) {
                    if (j + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(i, j + 1) == PlayerEnum.EMPTY) {
                            if (j + 2 < 7) {
                                if (manager.getPlayerEnumAtPosition(i, j + 2) == playerEnum) {
                                    if (j + 3 < 7) {
                                        if (manager.getPlayerEnumAtPosition(i, j + 3) == PlayerEnum.EMPTY) {
                                            if (j - 1 >= 0) {
                                                if (manager.getPlayerEnumAtPosition(i, j - 1) == PlayerEnum.EMPTY) {

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    //TODO two own coins left and right empty AND left or right +2 1 empty --> set in middle!


    public Move notBlockedOwnChainImproveMove(List<Integer> notBlockedMoves) {
        Random random = new Random();
        if (notBlockedMoves.size() != 0) {
            //improve own coins
            int trys = 0;
            while (trys < 50) {
                int randomColumn = notBlockedMoves.get(random.nextInt(notBlockedMoves.size()));
                for (int i = 6; i >= 0; i--) {
                    if (manager.getPlayerEnumAtPosition(i, randomColumn) == PlayerEnum.OWN) {
                        boolean leftOrRight = random.nextBoolean();
                        //left
                        if (leftOrRight == true) {
                            if (randomColumn - 1 >= 0) {
                                if (manager.getPlayerEnumAtPosition(i, randomColumn - 1) == PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(randomColumn - 1);
                                }
                            }
                        } else {
                            if (randomColumn + 1 < 7) {
                                if (manager.getPlayerEnumAtPosition(i, randomColumn + 1) == PlayerEnum.EMPTY) {
                                    return getMoveOfColumn(randomColumn + 1);
                                }
                            }
                        }
                    }
                }
                trys++;
            }
        }
        return getMoveOfColumn(random.nextInt(notBlockedMoves.size()));
    }

    public Move improveOwn2CoinChains(List<Integer> notBlockedMoves) {
        //vertical
        for (int i = 0; i < winSituationDetector.getOwnDetectedChains().size(); i++) {
            if (winSituationDetector.getOwnDetectedChains().get(i).getChainType() == ChainType.VERTICAL &&
                    winSituationDetector.getOwnDetectedChains().get(i).getSize() == 2) {
                if (winSituationDetector.getOwnDetectedChains().get(i).getStartPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(
                            winSituationDetector.getOwnDetectedChains().get(i).getStartPositionRow() - 1,
                            winSituationDetector.getOwnDetectedChains().get(i).getStartPositionCol()) == PlayerEnum.EMPTY) {
                        return getMoveOfColumn(winSituationDetector.getOwnDetectedChains().get(i).getStartPositionCol());
                    }
                }
            }
        }

        //TODO: other chains


        return null;
    }


    public List<Integer> getNotBlockedMovesOfRemainingColumns() {
        List<Integer> notBlockedMoves = manager.getVirtualGameBoard().getRemainingColumns();
        for (int i = 0; i < manager.getVirtualGameBoard().getRemainingColumns().size(); i++) {
            for (int j = 0; j < blockedColumns.size(); j++) {
                if (manager.getVirtualGameBoard().getRemainingColumns().get(i) == blockedColumns.get(j)) {
                    //only remove from blocked moves if there are at least 2 possible ones
                    if (notBlockedMoves.size() >= 2) {
                        notBlockedMoves.remove(manager.getVirtualGameBoard().getRemainingColumns().get(i));
                    }
                }
            }
        }
        return notBlockedMoves;
    }


    public Move getMove(boolean meFirst) {

        //safe real virtualGameBoard
        VirtualGameBoard tempVirtualGameBoard = manager.getVirtualGameBoard();

        //initialize forecastBoard
        manager.initializeForecastVirtualGameBoard();

        //logic sequence
        //win it!
        if (getOwnWinMove(winSituationDetector.getOwnDetectedChains()) != null) {
            plannedMove = getOwnWinMove(winSituationDetector.getOwnDetectedChains());
            return plannedMove;
        } else {
            //prevent enemy
            if (preventEnemyWin() != null) {

////FORECAST
//                //prevention found --> now forecast!
//                //initialize forecastBoard
//                manager.initializeForecastVirtualGameBoard();
//                //add own coin to forecastBoard
//                manager.addCoinToForecastBoard(PlayerEnum.OWN, ownPlayer.getColumnOfMoveAsInt(preventEnemyWin()));
//
//
//                //RIVAL MOVE
//                //swap coins
//                manager.swapPlayerCoinsForecastBoard();
//
//
//                //check all possible rival moves to new constellation


                //return move
                plannedMove = preventEnemyWin();

            }
        }

        //check if move is on blocked list

        List<Integer> notBlockedMoves = getNotBlockedMovesOfRemainingColumns();

        //get not blocked move, if no own or enemy-prevention-move
        if (plannedMove == null) {
            if (meFirst == true) {
                return plannedMove = getMoveOfColumn(3);
            } else {
                //generate first move as 2nd player
                if (manager.getVirtualGameBoard().countEnemyCoinsOnBoard() == 1
                        && manager.getPlayerEnumAtPosition(6, 3) == PlayerEnum.EMPTY) {
                    return plannedMove = getMoveOfColumn(3);
                }
                if (manager.getVirtualGameBoard().countEnemyCoinsOnBoard() == 1) {
                    Random random = new Random();
                    int column = random.nextInt(5 - 1 + 1) + 1;
                    plannedMove = getMoveOfColumn(column);
                }
            }

            //improve own chains


            //get basic, not blocked move
            if (plannedMove == null) {
                plannedMove = notBlockedOwnChainImproveMove(notBlockedMoves);
            }


            //prevent rival

            // random variant
            // plannedMove = getMoveOfColumn(notBlockedMoves.get(random.nextInt(notBlockedMoves.size())));


            //plannedMove = getMoveOfColumn(randomValue);
            //or basic move
            //TODO: combine this! plannedMove = getBasicMove(manager.getLastOwnColumn());
        }

//        //FORECAST NEXT TWO RIVAL MOVES
//
//        //ADD OWN MOVE
//        List<ForecastObject> forecastObjects = new ArrayList<>();
//        for (int i = 0; i <= 6; i++) {
//            //create new forecast-object
//            ForecastObject forecastObject = new ForecastObject(manager);
//
//            //initializeVirtualForecastGameBoard
//            forecastObject.initializeVirtualForecastGameBoard();
//            //set remaining column
//            forecastObject.setColumn(i);
//            //add column to VirtualForecastGameBoard
//            forecastObject.addCoinToBoard(PlayerEnum.OWN, forecastObject.getColumn());
//            //check win (4 coins)
//            forecastObject.setWin(checkOwnWin());
//
//            //generate new forecastObjects RIVAL MOVE
//            forecastObject.generateForecastObjects(PlayerEnum.RIVAL);
//
////            //ADD RIVAL MOVE
////            for (int j = 0; j <= 6; j++) {
////                forecastObject.getForecastObjects().get(j).generateForecastObjects(PlayerEnum.RIVAL);
////            }
////
////            //ADD OWN MOVE
////            for (int k = 0; k <= 6; k++) {
////                for (int l = 0; l <= 6; l++) {
////                    forecastObject.getForecastObjects().get(k).getForecastObjects().get(l).generateForecastObjects(PlayerEnum.OWN);
////                }
////            }
////            //ADD RIVAL MOVE
////            for (int m = 0; m <= 6; m++) {
////                for (int n = 0; n <= 6; n++) {
////                    for (int o = 0; o <= 6; o++) {
////                        forecastObject.getForecastObjects().get(o).getForecastObjects().get(n).getForecastObjects().get(o).generateForecastObjects(PlayerEnum.RIVAL);
////                    }
////                }
////            }
//
//            forecastObjects.add(forecastObject);
//        }

        //restore virtualGameBoard
        manager.setVirtualGameBoard(tempVirtualGameBoard);

        //return move
        return plannedMove;
    }


    public Move getPlannedMove() {
        return plannedMove;
    }

    public Move getMoveOfColumn(int column) {
        Move returnMove = null;
        for (int i = 0; i < moves.size(); i++) {
            String moveString = moves.get(i).toString();
            if (Integer.parseInt(Character.toString(moveString.charAt(moveString.length() - 2))) == column + 1) {
                returnMove = moves.get(i);
            }
        }
        return returnMove;
    }

    public String getColumnOfMoveAsString(Move move) {
        String moveString = move.toString();
        return Character.toString(moveString.charAt(moveString.length() - 2));
    }

    public boolean checkNextRivalMoveWin(int ownCol) {
        boolean win = false;

        //FORECAST
        manager.getVirtualForecastGameBoard().addCoinToBoard(PlayerEnum.OWN, ownCol);


        //ADD OWN MOVE
        List<ForecastObject> forecastObjects = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            //create new forecast-object
            ForecastObject forecastObject = new ForecastObject(manager);

            //initializeVirtualForecastGameBoard
            forecastObject.initializeVirtualForecastGameBoard();
            //set remaining column
            forecastObject.setColumn(i);
            //add column to VirtualForecastGameBoard
            forecastObject.addCoinToBoard(PlayerEnum.OWN, forecastObject.getColumn());
            //check win (4 coins)
            forecastObject.setWin(checkOwnWin());

            //generate new forecastObjects RIVAL MOVE
            forecastObject.generateForecastObjects(PlayerEnum.RIVAL);


        }
        return win;
    }


    public boolean checkOwnWin() {
        boolean win = false;

        manager.winSituationDetector.checkAllChainsForecast();

        //check win own chains
        for (int i = 0; i < manager.winSituationDetector.getOwnDetectedForecastChains().size(); i++) {
            if (manager.winSituationDetector.getOwnDetectedForecastChains().get(i).getSize() >= 4) {
                win = true;
            }
        }
        return win;
    }

    public boolean checkRivalWin() {
        boolean win = false;

        manager.winSituationDetector.checkAllChains();

        //check win own chains
        for (int i = 0; i < manager.winSituationDetector.getRivalDetectedForecastChains().size(); i++) {
            if (manager.winSituationDetector.getRivalDetectedForecastChains().get(i).getSize() == 4) {
                win = true;
            }
        }
        return win;
    }


}

