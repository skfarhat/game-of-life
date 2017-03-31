import javax.swing.text.Position;
import java.lang.reflect.Array;

/**
 * Created by Sami on 31/03/2017.
 */
public class SquareGrid2D<T extends Positionable> {

    private Positionable[][] grid;

    public SquareGrid2D(Class<T> c, int n) {

        // creating an 2d array of generics is fiddly
        grid = (T[][]) Array.newInstance(c, n, 2);

        // set all the correct positions
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grid[i][j].setPos(new Point2D(i, j));
    }

    public Positionable get(int x, int y) {
        return grid[x][y];
    }

}
