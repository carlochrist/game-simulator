package basic;

import java.util.List;

public class DummyPlayer extends Player {
	int counter = 0;
	int[] zugFolge = {4, 3, 3, 2, 5, 5, 4, 4, 3, 1, 1, 2, 2};
	@Override
	Move nextMove(Position p, List<Move> moves) {
		return new Move(zugFolge[counter++]);
	}
}
