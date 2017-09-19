package CCPlayerPackage;

import basic.GUI;
import basic.Move;
import basic.CCPlayer;
import basic.Position;
import com.sun.xml.internal.fastinfoset.tools.FI_SAX_Or_XML_SAX_SAXEvent;
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
                                System.out.println("WINMOVE VERTIKAL!!!");
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
                                    System.out.println("WINMOVE HORI LEFT!!!!");
                                    return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                } else {
                                    //check falling
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() + 1, winnableChains.get(i).getStartPositionCol() - 1) != null) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() + 1, winnableChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            System.out.println("WINMOVE HORI LEFT!!!!");
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
                                    System.out.println("WINMOVE HORI RIGHT!!!");
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    //check falling
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) != null) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            System.out.println("WINMOVE HORI RIGHT!!!");
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
                                    //check falling
                                    if (winnableChains.get(i).getStartPositionRow() == 6) {
                                        return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                    }
                                    if (winnableChains.get(i).getStartPositionRow() + 1 < 7 && winnableChains.get(i).getStartPositionCol() - 1 >= 0) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getStartPositionRow() + 1, winnableChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            System.out.println("START-ROW: " + winnableChains.get(i).getStartPositionRow());
                                            System.out.println("START-COL: " + winnableChains.get(i).getStartPositionCol());
                                            System.out.println("END-ROW: " + winnableChains.get(i).getEndPositionRow());
                                            System.out.println("END-COL: " + winnableChains.get(i).getEndPositionCol());
                                            System.out.println("SIZE" + winnableChains.get(i).getSize());
                                            System.out.println("CHAINTYPE: " + winnableChains.get(i).getChainType());


                                            System.out.println("EIGENER WINMOVE HORIZONTAL LEFT");
                                            return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //right
                    if (winnableChains.get(i).getEndPositionCol() + 2 < 7) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 2) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 2) == PlayerEnum.OWN) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    //check falling
                                    if (winnableChains.get(i).getEndPositionRow() == 6) {
                                        return getMoveOfColumn(winnableChains.get(i).getStartPositionCol() + 1);
                                    }
                                    if (winnableChains.get(i).getEndPositionRow() + 1 < 7 && winnableChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 1, winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            System.out.println("START-ROW: " + winnableChains.get(i).getStartPositionRow());
                                            System.out.println("START-COL: " + winnableChains.get(i).getStartPositionCol());
                                            System.out.println("END-ROW: " + winnableChains.get(i).getEndPositionRow());
                                            System.out.println("END-COL: " + winnableChains.get(i).getEndPositionCol());
                                            System.out.println("SIZE" + winnableChains.get(i).getSize());
                                            System.out.println("CHAINTYPE: " + winnableChains.get(i).getChainType());

                                            System.out.println("EIGENER WINMOVE HORIZONTAL RIGHT");
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

        //DIAGONAL_TOP_LEFT - complete 3 coins
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (winnableChains.get(i).getSize() == 3) {
                    if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    System.out.println("WINMOVE MIT DIA 3 TOP LEFT!!!!");
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
                                    System.out.println("WINMOVE MIT DIA 3 TOP RIGHT!!!!");
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
                                    System.out.println("WINMOVE DIA 3 BOTTOM RIGHT!!!");
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() + 1);
                                } else {
                                    if (winnableChains.get(i).getEndPositionRow() + 2 < 7 && winnableChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 2, winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            System.out.println("WINMOVE DIA 3 BOTTOM RIGHT!!!");
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
                                    System.out.println("WINMOVE DIA 3 BOTTOM LEFT!!!");
                                    return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() + 2, winnableChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        System.out.println("WINMOVE DIA  3 BOTTOM LEFT!!!");
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
                                        //check falling
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() + 1) != PlayerEnum.OWN) {
                                            System.out.println("WINMOVE DIA TOP RIGHT 2!!!");
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

        //DIAGONAL TOP LEFT - 2 coins with empty fields on left & right
        for (int i = 0; i < winnableChains.size(); i++) {
            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && winnableChains.get(i).getSize() == 2) {
                if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (winnableChains.get(i).getEndPositionCol() - 2 >= 0 && winnableChains.get(i).getEndPositionRow() - 2 >= 0) {
                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) == PlayerEnum.OWN) {
                                        //check falling
                                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow(), winnableChains.get(i).getEndPositionCol() - 1) != PlayerEnum.OWN) {
                                            System.out.println("WINMOVE DIA TOP LEFT 2!!!");
                                            return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

//        //DIAGONAL BOTTOM RIGHT - 2 coins with empty fields on left & right
//        for (int i = 0; i < winnableChains.size(); i++) {
//            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT && winnableChains.get(i).getSize() == 2) {
//                if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
//                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
//                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
//                            if (winnableChains.get(i).getEndPositionCol() - 2 >= 0 && winnableChains.get(i).getEndPositionRow() - 2 >= 0) {
//                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) != null) {
//                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) == PlayerEnum.OWN) {
//                                        System.out.println("WINMOVE DIA TOP LEFT 2!!!");
//                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        //DIAGONAL BOTTOM LEFT - 2 coins with empty fields on left & right
//        for (int i = 0; i < winnableChains.size(); i++) {
//            if (winnableChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT && winnableChains.get(i).getSize() == 2) {
//                if (winnableChains.get(i).getEndPositionCol() - 1 >= 0 && winnableChains.get(i).getEndPositionRow() - 1 >= 0) {
//                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) != null) {
//                        if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 1, winnableChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
//                            if (winnableChains.get(i).getEndPositionCol() - 2 >= 0 && winnableChains.get(i).getEndPositionRow() - 2 >= 0) {
//                                if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) != null) {
//                                    if (manager.getPlayerEnumAtPosition(winnableChains.get(i).getEndPositionRow() - 2, winnableChains.get(i).getEndPositionCol() - 2) == PlayerEnum.OWN) {
//                                        System.out.println("WINMOVE DIA TOP LEFT 2!!!");
//                                        return getMoveOfColumn(winnableChains.get(i).getEndPositionCol() - 1);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }

        //check special moves
        checkSpecialMoves(PlayerEnum.OWN);

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
                                    if (harmfulRivalChains.get(i).getStartPositionRow() == 6) {
                                        Random random = new Random();
                                        boolean left = random.nextBoolean();
                                        if (left) {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                        } else {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
                                    //left
                                    if (harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0) {
                                        if (harmfulRivalChains.get(i).getStartPositionRow() + 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                    //if rival doesn't win with next move
                                                    if (getNextRivalWinColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1) == 8) {
                                                        return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                                    }
                                                    //if rival wins with next move --> set to his col
                                                    return getMoveOfColumn(getNextRivalWinColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1));
                                                }
                                            }
                                        }
                                    }
                                    //right
                                    if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7) {
                                        if (harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
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
            }
        }

        //HORIZONTAL prevent completion of 2 coins with empty fields on left & right + 1 rival coin
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                if (harmfulRivalChains.get(i).getSize() == 2) {
                    //left
                    if (harmfulRivalChains.get(i).getStartPositionCol() - 2 >= 0) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 2) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 2) == PlayerEnum.RIVAL) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow(), harmfulRivalChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    //check falling
                                    if (harmfulRivalChains.get(i).getStartPositionRow() == 6) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                    }
                                    if (harmfulRivalChains.get(i).getStartPositionRow() + 1 < 7 && harmfulRivalChains.get(i).getStartPositionCol() - 1 >= 0) {
                                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getStartPositionRow() + 1, harmfulRivalChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getStartPositionCol() - 1);
                                        }
                                    }
                                }
                            }
                        }

                        //right
                        if (harmfulRivalChains.get(i).getEndPositionCol() + 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 2) != null) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                        //check falling
                                        if (harmfulRivalChains.get(i).getEndPositionRow() == 6) {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() + 1);
                                        }
                                        if ((harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7)) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
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
                                        System.out.println("START-ROW: " + harmfulRivalChains.get(i).getStartPositionRow());
                                        System.out.println("START-COL: " + harmfulRivalChains.get(i).getStartPositionCol());
                                        System.out.println("END-ROW: " + harmfulRivalChains.get(i).getEndPositionRow());
                                        System.out.println("END-COL: " + harmfulRivalChains.get(i).getEndPositionCol());
                                        System.out.println("SIZE" + harmfulRivalChains.get(i).getSize());
                                        System.out.println("CHAINTYPE: " + harmfulRivalChains.get(i).getChainType());
                                        //check falling
                                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                System.out.println("IM RETURN GEGNER HINDERN TOP LEFT");
                                                System.out.println(manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY);
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
                                        System.out.println("START-ROW: " + harmfulRivalChains.get(i).getStartPositionRow());
                                        System.out.println("START-COL: " + harmfulRivalChains.get(i).getStartPositionCol());
                                        System.out.println("END-ROW: " + harmfulRivalChains.get(i).getEndPositionRow());
                                        System.out.println("END-COL: " + harmfulRivalChains.get(i).getEndPositionCol());
                                        System.out.println("SIZE" + harmfulRivalChains.get(i).getSize());
                                        System.out.println("CHAINTYPE: " + harmfulRivalChains.get(i).getChainType());
                                        //check falling
                                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() - 1) != null) {
                                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow(), harmfulRivalChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                                System.out.println("IM RETURN GEGNER HINDERN TOP LEFT");
                                                return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() - 1);
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

        //DIAGONAL BOTTOM RIGHT - 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getEndPositionCol() + 1 < 7 && harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                            if (harmfulRivalChains.get(i).getEndPositionRow() + 2 < 7 && harmfulRivalChains.get(i).getEndPositionCol() + 2 < 7) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() + 2) == PlayerEnum.RIVAL) {
                                        System.out.println("START-ROW: " + harmfulRivalChains.get(i).getStartPositionRow());
                                        System.out.println("START-COL: " + harmfulRivalChains.get(i).getStartPositionCol());
                                        System.out.println("END-ROW: " + harmfulRivalChains.get(i).getEndPositionRow());
                                        System.out.println("END-COL: " + harmfulRivalChains.get(i).getEndPositionCol());
                                        System.out.println("SIZE" + harmfulRivalChains.get(i).getSize());
                                        System.out.println("CHAINTYPE: " + harmfulRivalChains.get(i).getChainType());
                                        //check falling
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

        //DIAGONAL BOTTOM LEFT - 2 coins with empty fields on left & right
        for (int i = 0; i < harmfulRivalChains.size(); i++) {
            if (harmfulRivalChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT && harmfulRivalChains.get(i).getSize() == 2) {
                if (harmfulRivalChains.get(i).getEndPositionCol() - 1 >= 0 && harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) != null) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                            if (harmfulRivalChains.get(i).getEndPositionRow() + 2 < 7 && harmfulRivalChains.get(i).getEndPositionCol() - 2 >= 0) {
                                if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() - 2) != null) {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() - 2) == PlayerEnum.RIVAL) {
                                        System.out.println("START-ROW: " + harmfulRivalChains.get(i).getStartPositionRow());
                                        System.out.println("START-COL: " + harmfulRivalChains.get(i).getStartPositionCol());
                                        System.out.println("END-ROW: " + harmfulRivalChains.get(i).getEndPositionRow());
                                        System.out.println("END-COL: " + harmfulRivalChains.get(i).getEndPositionCol());
                                        System.out.println("SIZE" + harmfulRivalChains.get(i).getSize());
                                        System.out.println("CHAINTYPE: " + harmfulRivalChains.get(i).getChainType());
                                        //check falling
                                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() - 1);
                                        }
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
                    if (harmfulRivalChains.get(i).getEndPositionCol() - 1 >= 0 && harmfulRivalChains.get(i).getEndPositionRow() + 1 < 7) {
                        if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) != null) {
                            if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 1, harmfulRivalChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (harmfulRivalChains.get(i).getEndPositionRow() + 1 == 6) {
                                    return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() - 1);
                                } else {
                                    if (manager.getPlayerEnumAtPosition(harmfulRivalChains.get(i).getEndPositionRow() + 2, harmfulRivalChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                        return getMoveOfColumn(harmfulRivalChains.get(i).getEndPositionCol() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //check special moves
        if (checkSpecialMoves(PlayerEnum.RIVAL) != null) {
            return checkSpecialMoves(PlayerEnum.RIVAL);
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
                                        System.out.println("BLOCK TOP RIGHT!");
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
                                        System.out.println("test");
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
            }

            //2 COINS HORIZONTAL
            if (foundChains.get(i).getChainType() == ChainType.HORIZONTAL) {
                if (foundChains.get(i).getSize() == 2) {
                    //left
                    if (foundChains.get(i).getStartPositionCol() - 2 >= 0) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getStartPositionRow(), foundChains.get(i).getStartPositionCol() - 2) != null) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getStartPositionRow(), foundChains.get(i).getStartPositionCol() - 2) == PlayerEnum.OWN) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getStartPositionRow(), foundChains.get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    //check falling
                                    if (foundChains.get(i).getStartPositionRow() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getStartPositionRow() + 1, foundChains.get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            System.out.println("BLOCK!");
                                            blockedColumns.add(foundChains.get(i).getStartPositionCol() - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //right
                    if (foundChains.get(i).getEndPositionCol() + 2 < 7) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 2) != null) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 2) == PlayerEnum.OWN) {
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    //check falling
                                    if (foundChains.get(i).getEndPositionRow() + 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                            System.out.println("BLOCK!");
                                            blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                        }
                                    }
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
                            //check falling
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                System.out.println("DIA BLOCK!");
                                blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                            }
                        }
                    }
                }


                //DIAGONAL TOP LEFT
                if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                    if (foundChains.get(i).getEndPositionRow() - 2 >= 0 && foundChains.get(i).getEndPositionCol() - 2 >= 0) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 2, foundChains.get(i).getEndPositionCol() - 2) == playerEnum) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() - 1, foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow(), foundChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    blockedColumns.add(foundChains.get(i).getEndPositionCol() - 1);
                                }
                            }
                        }
                    }
                }

                //DIAGONAL BOTTOM RIGHT
                if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_RIGHT) {
                    if (foundChains.get(i).getEndPositionRow() + 2 < 7 && foundChains.get(i).getEndPositionCol() + 2 < 7) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 2, foundChains.get(i).getEndPositionCol() + 2) == playerEnum) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 2, foundChains.get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                    blockedColumns.add(foundChains.get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }

                //DIAGONAL BOTTOM LEFT
                if (foundChains.get(i).getChainType() == ChainType.DIAGONAL_BOTTOM_LEFT) {
                    if (foundChains.get(i).getEndPositionRow() + 2 < 7 && foundChains.get(i).getEndPositionCol() - 2 >= 0) {
                        if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 2, foundChains.get(i).getEndPositionCol() - 2) == playerEnum) {
                            if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 1, foundChains.get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check falling
                                if (manager.getPlayerEnumAtPosition(foundChains.get(i).getEndPositionRow() + 2, foundChains.get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                    blockedColumns.add(foundChains.get(i).getEndPositionCol() - 1);
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
                                                    return getMoveOfColumn(j + 1);
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

    public Move getNotBlockedpreventRivalChainMove(List<Integer> notBlockedMoves) {
        //TODO: improve this!


        //HORIZONTAL
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() == 2 &&
                    winSituationDetector.getRivalDetectedChains().get(i).getChainType() == ChainType.HORIZONTAL) {
                if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1 >= 0 &&
                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow(),
                            winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY &&
                            manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow(),
                                    winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                        Random random = new Random();
                        boolean isLeft = random.nextBoolean();
                        //LEFT
                        if (isLeft) {
                            if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1 >= 0) {
                                if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow(),
                                        winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                    //check if blocked
                                    boolean blocked = true;
                                    for (int j = 0; j < notBlockedMoves.size(); j++) {
                                        if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) {
                                            blocked = false;
                                        }
                                    }
                                    if (!blocked) {
                                        //check falling
                                        if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() == 6) {
                                            return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1);
                                        }
                                        if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() + 1 < 7 &&
                                                winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() + 1,
                                                    winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                                return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1);
                                            }
                                        }
                                    }
                                }
                            }
                        } else
                        //RIGHT
                        {
                            if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7) {
                                if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow(),
                                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    //check if blocked
                                    boolean blocked = true;
                                    for (int j = 0; j < notBlockedMoves.size(); j++) {
                                        if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) {
                                            blocked = false;
                                        }
                                    }
                                    if (!blocked) {
                                        //check falling
                                        if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() == 6) {
                                            return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1);
                                        }
                                        if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1 < 7 &&
                                                winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1,
                                                    winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        //LEFT
                        if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1 >= 0) {
                            if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow(),
                                    winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) == PlayerEnum.EMPTY) {
                                //check if blocked
                                boolean blocked = true;
                                for (int j = 0; j < notBlockedMoves.size(); j++) {
                                    if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) {
                                        blocked = false;
                                    }
                                }
                                if (!blocked) {
                                    //check falling
                                    if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() == 6) {
                                        return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1);
                                    }
                                    if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() + 1 < 7 &&
                                            winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1 < 7) {
                                        if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() + 1,
                                                winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1) != PlayerEnum.EMPTY) {
                                            return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionCol() - 1);
                                        }
                                    }
                                }
                            }
                        }
                        //RIGHT
                        {
                            if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7) {
                                if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow(),
                                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                                    //check if blocked
                                    boolean blocked = true;
                                    for (int j = 0; j < notBlockedMoves.size(); j++) {
                                        if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) {
                                            blocked = false;
                                        }
                                    }
                                    if (!blocked) {
                                        //check falling
                                        if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() == 6) {
                                            return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1);
                                        }
                                        if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1 < 7 &&
                                                winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7) {
                                            if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1,
                                                    winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                                return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1);
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


        //VERTICAL
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() == 2 &&
                    winSituationDetector.getRivalDetectedChains().get(i).getChainType() == ChainType.VERTICAL) {
                if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() - 1 >= 0 &&
                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() - 1,
                            winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol()) == PlayerEnum.EMPTY) {
                        if (winSituationDetector.getRivalDetectedChains().get(i).getStartPositionRow() - 2 >= 0) {
                            //check if blocked
                            for (int j = 0; j < notBlockedMoves.size(); j++) {
                                if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol()) {
                                    return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol());
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP RIGHT
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() == 2 &&
                    winSituationDetector.getRivalDetectedChains().get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7 &&
                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() - 1,
                            winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                        //check falling
                        if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow(),
                                winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                            //check if blocked
                            for (int j = 0; j < notBlockedMoves.size(); j++) {
                                if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) {
                                    return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL TOP LEFT
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() == 2 &&
                    winSituationDetector.getRivalDetectedChains().get(i).getChainType() == ChainType.DIAGONAL_TOP_LEFT) {
                if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1 >= 0 &&
                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() - 1 >= 0) {
                    if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() - 1,
                            winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                        //check falling
                        if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow(),
                                winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                            //check if blocked
                            for (int j = 0; j < notBlockedMoves.size(); j++) {
                                if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1) {
                                    return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        //DIAGONAL BOTTOM RIGHT
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() == 2 &&
                    winSituationDetector.getRivalDetectedChains().get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1 < 7 &&
                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1,
                            winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) == PlayerEnum.EMPTY) {
                        //check falling
                        if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 2,
                                    winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) != PlayerEnum.EMPTY) {
                                //check if blocked
                                for (int j = 0; j < notBlockedMoves.size(); j++) {
                                    if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1) {
                                        return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() + 1);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        //DIAGONAL BOTTOM LEFT
        for (int i = 0; i < winSituationDetector.getRivalDetectedChains().size(); i++) {
            if (winSituationDetector.getRivalDetectedChains().get(i).getSize() == 2 &&
                    winSituationDetector.getRivalDetectedChains().get(i).getChainType() == ChainType.DIAGONAL_TOP_RIGHT) {
                if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1 >= 0 &&
                        winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1 < 7) {
                    if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 1,
                            winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1) == PlayerEnum.EMPTY) {
                        //check falling
                        if (winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 2 < 7) {
                            if (manager.getPlayerEnumAtPosition(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionRow() + 2,
                                    winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1) != PlayerEnum.EMPTY) {
                                //check if blocked
                                for (int j = 0; j < notBlockedMoves.size(); j++) {
                                    if (notBlockedMoves.get(j) == winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1) {
                                        return getMoveOfColumn(winSituationDetector.getRivalDetectedChains().get(i).getEndPositionCol() - 1);
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

    public Move getNotBlockedRandomMove(List<Integer> notBlockedMoves) {
        //place in middle if possible - else random
        for (int i = 0; i < manager.getRemainingColumns().size(); i++) {
            if (manager.getRemainingColumns().get(i) == 3) {
                return getMoveOfColumn(manager.getRemainingColumns().get(i));
            }
        }

        Random random = new Random();
        if (notBlockedMoves.size() != 0) {
            return getMoveOfColumn(notBlockedMoves.get(random.nextInt(notBlockedMoves.size())));
        } else {
//            if(blockedColumns.get(0)+1 < 7){
//                return getMoveOfColumn((blockedColumns.get(0)+1));
//            }
//            if(blockedColumns.get(0)-1 >= 0){
//                return getMoveOfColumn((blockedColumns.get(0)-1));
//            }
            return getMoveOfColumn(manager.getRemainingColumns().size() / 2);
        }
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
        //first moves
        if (meFirst == true) {
            return plannedMove = getMoveOfColumn(3);
        } else {
            //generate first move as 2nd player
            if (manager.getVirtualGameBoard().countEnemyCoinsOnBoard() == 1
                    && manager.getPlayerEnumAtPosition(6, 3) == PlayerEnum.EMPTY) {
                return plannedMove = getMoveOfColumn(3);
            }
            if (manager.getVirtualGameBoard().countEnemyCoinsOnBoard() == 1) {
                return plannedMove = getMoveOfColumn(3);
            }
        }

        if (manager.ownPlayer.getLastRivalCol() == 2) {
            System.out.println("2 gelegt!");
        }

        //win it!
        if (getOwnWinMove(winSituationDetector.getOwnDetectedChains()) != null) {
            plannedMove = getOwnWinMove(winSituationDetector.getOwnDetectedChains());
            System.out.println("EIGNER WINMOVE!");
            return plannedMove;
        } else {
            //prevent enemy
            if (preventEnemyWin() != null) {
                //return move
                System.out.println("GEGNER HINDERN!");
                plannedMove = preventEnemyWin();
            }
        }

        //set not blocked moves
        List<Integer> notBlockedMoves = getNotBlockedMovesOfRemainingColumns();

        //get basic, not blocked move
        if (plannedMove == null) {
            //improve own move
            //plannedMove = notBlockedOwnChainImproveMove(notBlockedMoves);

            //prevent rival
            plannedMove = getNotBlockedpreventRivalChainMove(notBlockedMoves);
            if (plannedMove != null) {
                System.out.println("getNotBlockedpreventRivalChainMove!");
            }


            //if no prevention move -> random!
            if (plannedMove == null) {
                plannedMove = getNotBlockedRandomMove(notBlockedMoves);
                System.out.println("RANDOM MOVE!");
            }
        }

        //restore virtualGameBoard
        manager.setVirtualGameBoard(tempVirtualGameBoard);

        System.out.println("BLOCKED COLUMNS!");
        for (int i = 0; i < blockedColumns.size(); i++) {
            System.out.println(blockedColumns.get(i));
        }


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

    public int getNextRivalWinColumn(int ownCol) {
        int winColumn = 8;

        //add own move to gameboard
        manager.getVirtualForecastGameBoard().addCoinToBoard(PlayerEnum.OWN, ownCol);

        for (int i = 0; i <= 6; i++) {
            manager.getVirtualForecastGameBoard().addCoinToBoard(PlayerEnum.RIVAL, i);
            if (checkRivalWin()) {
                winColumn = i;
            }
            manager.getVirtualForecastGameBoard().removeLastCoinFromColumn(i);
        }

        return winColumn;
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

        manager.winSituationDetector.checkAllChainsForecast();

        //check win own chains
        for (int i = 0; i < manager.winSituationDetector.getRivalDetectedForecastChains().size(); i++) {
            if (manager.winSituationDetector.getRivalDetectedForecastChains().get(i).getSize() == 4) {
                win = true;
            }
        }
        return win;
    }


}

