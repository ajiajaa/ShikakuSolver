import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Shikaku {

    private int m, n;
    private int[][] v;
    private int[][] numbers;
    private int count;
    private int[] factors;
    private ArrayList<ArrayList<ArrayList<Integer>>> rects;

    public Shikaku() {
        rects = new ArrayList<>();
    }

    public void loadPuzzle() {
        try {
            BufferedReader file = new BufferedReader(new FileReader("1.txt"));

            if (!file.ready()) {
                System.err.println("Unable to open puzzle file.");
                System.exit(1);
            }

            String line = file.readLine();
            StringTokenizer dimensions = new StringTokenizer(line, " ");
            m = Integer.parseInt(dimensions.nextToken());
            n = Integer.parseInt(dimensions.nextToken());

            v = new int[m][n];
            count = 0;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    v[i][j] = -1;
                }
            }

            while ((line = file.readLine()) != null) {
                StringTokenizer data = new StringTokenizer(line, ",");
                int i = Integer.parseInt(data.nextToken());
                int j = Integer.parseInt(data.nextToken());
                int h = Integer.parseInt(data.nextToken());
                v[i][j] = h;
                count++;
            }

            file.close();

            numbers = new int[count + 1][7];
            int k = 1;

            for (int j = 0; j < m; j++) {
                for (int i = 0; i < n; i++) {
                    if (v[j][i] != -1) {
                        numbers[k][0] = v[j][i];
                        numbers[k][1] = j;
                        numbers[k][2] = i;
                        numbers[k][3] = 0;
                        numbers[k][4] = 0;
                        numbers[k][5] = 0;
                        numbers[k][6] = 0;
                        k++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printPuzzle() {
        for (int j = 0; j < m; j++) {
            System.out.print("-");
            for (int i = 0; i < n; i++) {
                System.out.print("---");
            }
            System.out.println();
            System.out.print("|");
            for (int i = 0; i < n; i++) {
                if (v[j][i] == -1) {
                    System.out.print(" |");
                } else if (v[j][i] < 10 && v[j][i] > 0) {
                    System.out.print("0" + v[j][i] + "|");
                } else {
                    System.out.print(v[j][i] + "|");
                }
            }
            System.out.println();
        }
        System.out.print("-");
        for (int j = 0; j < n; j++) {
            System.out.print("---");
        }
        System.out.println();
    }

    public void printUsedRectangles() {
        System.out.println("Rectangles used in the solution:");

        for (int i = 1; i <= count; i++) {
            for (ArrayList<Integer> rectangle : rects.get(i)) {
                int startRow = rectangle.get(0);
                int startCol = rectangle.get(1);
                int endRow = rectangle.get(2);
                int endCol = rectangle.get(3);

                // Check if the rectangle is actually used in the solution
                if (v[startRow][startCol] == i && v[endRow][endCol] == i) {
                    System.out.println("Rectangle: " + i + ", Start Row: " + startRow + ", Start Col: " + startCol
                            + ", End Row: " + endRow + ", End Col: " + endCol);
                }
            }
        }
    }

    public void insertsort() {
        // sort the array from high to low
        int i, j, tmp0, tmp1, tmp2;
        for (i = 2; i < count + 1; i++) {
            j = i;
            while (j > 1 && numbers[j - 1][0] < numbers[j][0]) {
                tmp0 = numbers[j][0];
                tmp1 = numbers[j][1];
                tmp2 = numbers[j][2];

                numbers[j][0] = numbers[j - 1][0];
                numbers[j][1] = numbers[j - 1][1];
                numbers[j][2] = numbers[j - 1][2];

                numbers[j - 1][0] = tmp0;
                numbers[j - 1][1] = tmp1;
                numbers[j - 1][2] = tmp2;
                j--;
            }
        }
    }

    public int factoring(int f) {
        int fsize = 1;
        for (int g = 1; g < f; g++) {
            if ((f % g) == 0)
                fsize++;
        }

        factors = new int[fsize];
        int i = 0;

        for (int g = 1; g < f; g++) {
            if ((f % g) == 0) {
                factors[i] = g;
                i++;
            }
        }

        factors[i] = f;
        return fsize;
    }

    public void undo(int val, int m0, int n0, int m1, int n1, int m2, int n2) {
        for (int j = m1; j <= m2; j++) {
            for (int i = n1; i <= n2; i++) {
                v[j][i] = -1;// set all values within the rectangle to -1
            }
        }
        v[m0][n0] = val;// place the correct value back on the grid
    }

    public void place(int num, int m1, int n1, int m2, int n2) {
        // place a rectangle
        for (int j = m1; j <= m2; j++) {
            for (int i = n1; i <= n2; i++) {
                v[j][i] = num;// set all values within therectangle to num
            }
        }
    }

    public boolean checked(int m0, int n0, int m1, int n1, int m2, int n2) {
        // Check if the rectangle doesnt leave the grid or overlaps another value or
        // rectangle
        if (m1 < 0 || n1 < 0 || m2 > m - 1 || n2 > n - 1)
            return false;
        // check for leaving the grid
        for (int j = m1; j <= m2; j++) {
            for (int i = n1; i <= n2; i++) {
                if (i == n0 && j == m0) {
                    if (i < n2)
                        i++;
                    else if (j < m2) {
                        i = n1;
                        j++;
                    } else
                        return true;
                }
                if (v[j][i] != -1)
                    return false;// check for overlap
            }
        }
        return true;
    }

    public void onesort(int one) {
        // sort a value to the front of the array
        int j = 0;
        int temp0, temp1, temp2;
        while (one - j - 1 > 0) {
            temp0 = numbers[one - j][0];
            temp1 = numbers[one - j][1];
            temp2 = numbers[one - j][2];
            numbers[one - j][0] = numbers[one - j - 1][0];
            numbers[one - j][1] = numbers[one - j - 1][1];
            numbers[one - j][2] = numbers[one - j - 1][2];
            numbers[one - j - 1][0] = temp0;
            numbers[one - j - 1][1] = temp1;
            numbers[one - j - 1][2] = temp2;
            j++;
        }
    }

    public void oneposs() {
        // check if a value has only one placable rectangle if no other rectangles are
        // placed
        int checkcount = 0;
        int i;

        for (i = 1; i <= count; i++) {
            int fsize = factoring(numbers[i][0]);
            int floc = numbers[i][4];
            int dm = factors[floc];
            int dn = numbers[i][0] / factors[floc];
            int m0 = numbers[i][1];
            int n0 = numbers[i][2];
            int offset = 0;
            int temp = numbers[i][3];

            while (temp >= 0) {
                temp -= dn;
                offset++;
            }

            offset--;
            int m1 = numbers[i][1] - offset;
            int n1 = numbers[i][2] - (numbers[i][3] % dn);
            int m2 = m1 + dm - 1;
            int n2 = n1 + dn - 1;

            if (checked(m0, n0, m1, n1, m2, n2)) {
                checkcount++;
            }

            if (checkcount > 1) {
                checkcount = 0;
                numbers[i][3] = 0;
                numbers[i][4] = 0;
            } else if (numbers[i][3] < numbers[i][0] - 1) {
                numbers[i][3]++;
                deleteFactors();
                i--;
            } else {
                numbers[i][3] = 0;

                if (numbers[i][4] < fsize - 1) {
                    numbers[i][4]++;
                    deleteFactors();
                    i--;
                } else {
                    numbers[i][4] = 0;
                    deleteFactors();

                    if (checkcount == 1) {
                        onesort(i);// only one rectangle, thus sort it to the front of the array
                        checkcount = 0;
                    }
                }
            }
        }
    }

    public void backtrack(int rec) {
        int m0 = numbers[rec][1];
        int n0 = numbers[rec][2];
        int rectangle = numbers[rec][5];
        int m1 = rects.get(rec).get(rectangle).get(0);
        int n1 = rects.get(rec).get(rectangle).get(1);
        int m2 = rects.get(rec).get(rectangle).get(2);
        int n2 = rects.get(rec).get(rectangle).get(3);
        boolean legal = checked(m0, n0, m1, n1, m2, n2);

        if (legal) {// rectangle can be placed
            place(rec, m1, n1, m2, n2);// place the rectangle

            if (count == rec) {// if this is the last rectangle, the puzzle is solved
                // System.out.println("Done!");
                printPuzzle();
                printUsedRectangles();
                // System.exit(1);
            } else {
                backtrack(rec + 1);// else next value
            }
        } else {// cant place rectangle
            if (numbers[rec][5] < numbers[rec][6] - 1) {
                // if more possible rectangles for this value..
                numbers[rec][5]++;
                backtrack(rec);// backtrack next rectangle
            } else {// if no more possible rectangles for this value
                numbers[rec][5] = 0;
                int back = 1;

                while (back < rec) {
                    m0 = numbers[rec - back][1];
                    n0 = numbers[rec - back][2];
                    rectangle = numbers[rec - back][5];
                    m1 = rects.get(rec - back).get(rectangle).get(0);
                    n1 = rects.get(rec - back).get(rectangle).get(1);
                    m2 = rects.get(rec - back).get(rectangle).get(2);
                    n2 = rects.get(rec - back).get(rectangle).get(3);
                    undo(numbers[rec - back][0], m0, n0, m1, n1, m2, n2);

                    // remove rectangle from grid
                    if (numbers[rec - back][5] < numbers[rec - back][6] - 1) {
                        numbers[rec - back][5]++;
                        backtrack(rec - back);
                        // try other rectangle from previous value
                    } else {// else keep removing rectangles
                        numbers[rec - back][5] = 0;
                        back++;
                    }
                }
            }
        }
    }

    public void calcrects() {
        // calculate all possible rectangles per gridvalue
        int countrects = 0;
        int countrectangles = 0;
        rects.add(new ArrayList<>());

        for (int i = 1; i <= count; i++) {
            rects.add(new ArrayList<>());
            int fsize = factoring(numbers[i][0]);

            for (int j = 0; j < fsize; j++) {
                int dm = factors[j];
                int dn = numbers[i][0] / factors[j];
                int m0 = numbers[i][1];
                int n0 = numbers[i][2];

                for (numbers[i][3] = 0; numbers[i][3] < numbers[i][0]; numbers[i][3]++) {
                    int offset = 0;
                    int temp = numbers[i][3];

                    while (temp >= 0) {
                        temp -= dn;
                        offset++;
                    }

                    offset--;
                    int m1 = numbers[i][1] - offset;
                    int n1 = numbers[i][2] - (numbers[i][3] % dn);
                    int m2 = m1 + dm - 1;
                    int n2 = n1 + dn - 1;
                    countrectangles++;

                    if (checked(m0, n0, m1, n1, m2, n2)) {
                        // if this rectangle could be placed
                        countrects++;
                        int k = numbers[i][6];
                        rects.get(i).add(new ArrayList<>());
                        // add the rectangle to the array
                        rects.get(i).get(k).add(m1);
                        rects.get(i).get(k).add(n1);
                        rects.get(i).get(k).add(m2);
                        rects.get(i).get(k).add(n2);
                        numbers[i][6]++;
                    }
                }
            }
            deleteFactors();
        }
    }

    public void deleteFactors() {
        factors = null;
    }

    public static void main(String[] args) {
        Shikaku skk = new Shikaku();
        skk.loadPuzzle();
        // skk.printPuzzle();
        skk.insertsort();
        skk.oneposs();
        skk.calcrects();
        skk.backtrack(1);
    }
}
