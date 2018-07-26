import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @authors S.Iryupin, A.Usmanov
 * @version dated Jul,25 2018
 *
 * В исходном коде проведены следующие изменения:
 * Добавлен общий массив ячеек (каждая ячейка это массив из трех значений - маркер, адрес,приоритет)
 * Добавлена регистрация ходов в массиве свободных ячеек (в нем ссылки на свободные ячейки)
 * Добавлены массивы выигрышных вариантов для игрока и AI, при регитрации хода удаляются варианты противника
 * Добавлена опция для начала игры компьютером (константа FIRST_MOVE)
 *
 * Алгоритм работы AI:
 * 1) ищем свой выигрышный ход, если есть делаем его
 * 2) ищем выигрышной ход противника, если есть  - закрываем
 * 3) ищем свободные ячейки из играющих вариантов противника и своих с наибольшим количеством значений, делаем пересечение и случайно выбираем ячейку, если диапазоны не пересекаются, то пытаемся выбрать из играющего варианта противника, если у него ничего не играет, то выбираем свое (играем от обороны)
 * 4) если по каким то причинам логика выше не сработала (например начало игры), то берем случайную свободную ячейку
 * Алгоритм так себе, но видимость борьбы создает, на польших полях, например по правилам рэндзю (поле 15x15  - пять в ряд)  даже выигрывает. На поле 3x3 весьма предсказуем и победить его труда не составляет
  */
class Java1Homework4 {

    final int SIZE = 15;
    final int WIN_SIZE = 5;
    static final int IDX_VALUE = 0;
    static final int IDX_ADDRESS = 1;
    static final int IDX_PRIORITY = 2;

    final char DOT_X = 'x';
    final char DOT_O = 'o';
    final char DOT_EMPTY = '.';
    static final int AI_VALUE = 2;
    static final int PLAYER_VALUE = 1;
    static final int EMPTY_VALUE = 0;
    final int FIRST_MOVE = PLAYER_VALUE;
    char[][] map = new char[SIZE][SIZE];
    int[][] cells = new int[][]{};
    int[][] freeCells;
    int[][][] playerVariants = new int[][][]{}; //первое измерение - вариант, //второе измерение массив массивов из двух значений (адрес и значение маркера 0 - ничье,1 - игрока, 2 - компьютера)
    int[][][] aiVariants = new int[][][]{}; //первое измерение - вариант, //второе измерение массив массивов из двух значений (адрес и значение маркера 0 - ничье,1 - игрока, 2 - компьютера)
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    public static void main(String[] args) {
        new Java1Homework4();
    }

    Java1Homework4() {
        initMap();
        initVariants();
        int turn = 0;
        while (true) {
            if (FIRST_MOVE==PLAYER_VALUE || turn++>0) {
                humanTurn();
                if (checkWin(DOT_X)) {
                    System.out.println("YOU WON!");
                    break;
                }
            }
            if (isMapFull()) {
                System.out.println("Sorry, DRAW!");
                break;
            }
            aiTurn();
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println("AI WON!");
                break;
            }
            if (isMapFull()) {
                System.out.println("Sorry, DRAW!");
                break;
            }
        }
        System.out.println("GAME OVER.");
        printMap();
    }

    void initMap() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                map[i][j] = DOT_EMPTY;
    }

    void initVariants() {
        //заполняем массив ячеек поля
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                cells = Arrays.copyOf(cells, cells.length + 1);
                cells[cells.length - 1] = new int[]{0, cells.length - 1, 0}; //0 - значение, 1 - адрес,  2 - приоритет
            }
        for (int i = 0; i < cells.length; i++) {
            freeCells = Arrays.copyOf(cells, cells.length + 1);
            freeCells[i] = cells[i];
        }
        //варианты по горизонтали
        for (int i = 0; i < cells.length; i++) {
            //начало варианта i, определяем конец варианта
            int j = i + WIN_SIZE;
            if (i / SIZE == (j - 1) / SIZE) { //определяем в той же строке конец или нет
                int variantNo = playerVariants.length;
                playerVariants = Arrays.copyOf(playerVariants, playerVariants.length + 1);
                playerVariants[variantNo] = new int[WIN_SIZE][];
                aiVariants = Arrays.copyOf(aiVariants, aiVariants.length + 1);
                aiVariants[variantNo] = new int[WIN_SIZE][];
                for (int k = i; k < j; k++) {
                    playerVariants[variantNo][k - i] = cells[k];
                    aiVariants[variantNo][k - i] = cells[k];
                }
            }
        }
        //варианты по вертикали
        for (int i = 0; i < cells.length; i++) {
            //начало варианта i, определяем конец варианта
            int j = i + SIZE * (WIN_SIZE - 1);
            if (j < cells.length) { //определяем в той же колонке
                int variantNo = playerVariants.length;
                playerVariants = Arrays.copyOf(playerVariants, playerVariants.length + 1);
                playerVariants[variantNo] = new int[WIN_SIZE][];
                aiVariants = Arrays.copyOf(aiVariants, aiVariants.length + 1);
                aiVariants[variantNo] = new int[WIN_SIZE][];
                for (int k = 0; k < WIN_SIZE; k++) {
                    playerVariants[variantNo][k] = cells[i + SIZE * k];
                    aiVariants[variantNo][k] = cells[i + SIZE * k];
                }
            }
        }
        //варианты по диагоналям слева
        for (int i = 0; i < cells.length; i++) {
            //начало варианта i, определяем конец варианта
            int j = i + (SIZE + 1) * (WIN_SIZE - 1);
            if (j < cells.length && j % SIZE > i % SIZE) { //определяем в той же колонке (конец в пределах массива и колонка правее)
                int variantNo = playerVariants.length;
                playerVariants = Arrays.copyOf(playerVariants, playerVariants.length + 1);
                playerVariants[variantNo] = new int[WIN_SIZE][];
                aiVariants = Arrays.copyOf(aiVariants, aiVariants.length + 1);
                aiVariants[variantNo] = new int[WIN_SIZE][];
                for (int k = 0; k < WIN_SIZE; k++) {
                    playerVariants[variantNo][k] = cells[i + (SIZE + 1) * k];
                    aiVariants[variantNo][k] = cells[i + (SIZE + 1) * k];
                }
            }
        }
        //варианты по диагоналям справа
        for (int i = 0; i < cells.length; i++) {
            //начало варианта i, определяем конец варианта
            int j = i + (SIZE - 1) * (WIN_SIZE - 1);
            if (j < cells.length && j % SIZE < i % SIZE) { //определяем в той же колонке (конец в пределах массива и колонка левее)
                int variantNo = playerVariants.length;
                playerVariants = Arrays.copyOf(playerVariants, playerVariants.length + 1);
                playerVariants[variantNo] = new int[WIN_SIZE][];
                aiVariants = Arrays.copyOf(aiVariants, aiVariants.length + 1);
                aiVariants[variantNo] = new int[WIN_SIZE][];
                for (int k = 0; k < WIN_SIZE; k++) {
                    playerVariants[variantNo][k] = cells[i + (SIZE - 1) * k];
                    aiVariants[variantNo][k] = cells[i + (SIZE - 1) * k];
                }
            }
        }


    }

    void printMap() {
        System.out.print(String.format("%2s", " ") + " ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(String.format("%2d", i + 1) + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(String.format("%2d", i + 1) + " ");
            for (int j = 0; j < SIZE; j++)
                System.out.print(String.format("%2s", map[i][j]) + " ");
            System.out.println();
        }
        System.out.println();
    }

    void humanTurn() {
        int x, y;
        do {
            System.out.println("Enter X and Y (1.." + SIZE + "):");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(x, y));
        registerMove(y, x, DOT_X);

    }

    void deleteVariants(int[][][] array, int byValue) {
        for (int i = array.length - 1; i > -1; i--) {
            boolean needDelete = false;
            for (int j = 0; j < WIN_SIZE && !needDelete; j++) {
                if (array[i] != null)
                    needDelete = array[i][j][0] == byValue;
            }
            if (needDelete) {
                for (int j = i; i < array.length - 1; i++) array[i] = array[i + 1];
                array[array.length - 1] = null;
            }
        }
    }

    private void registerMove(int y, int x, char dot) {
        int cellIdx = SIZE * y + x;
        int cellValue = (dot == DOT_X) ? 1 : 2;
        int[] cell = cells[cellIdx];
        cell[0] = cellValue;
        int pos = -1;
        for (int i = 0; i < freeCells.length - 1; i++) {
            if (freeCells[i] == cell) {
                pos = i;
                break;
            }
        }
        if (pos > -1) {
            for (int i = pos; i < freeCells.length - 1; i++) freeCells[i] = freeCells[i + 1];
            freeCells = Arrays.copyOf(freeCells, freeCells.length - 1);
        }
        if (dot == DOT_X) deleteVariants(aiVariants, cellValue);
        else deleteVariants(playerVariants, cellValue);
        map[y][x] = dot;
    }

    void aiTurn() {
        int pos = getWinPosition(AI_VALUE);
        if (pos == -1) pos = getWinPosition(PLAYER_VALUE);
        if (pos == -1) pos = getBestPosition();
        if (pos == -1) pos = getRandomPosition();

        registerMove(pos / SIZE, pos % SIZE, DOT_O);

    }

    private int getRandomPosition() {
        return freeCells[rand.nextInt(freeCells.length)][IDX_ADDRESS];
    }
    private int getWinPosition(int value) {
        int pos = -1;
        int[][][] variants = (value==PLAYER_VALUE)?playerVariants:aiVariants;
        for (int[][] variant:variants) {
            if (variant==null) continue;

            int toWin = WIN_SIZE;
            int emptyPos = -1;
            for (int[] cell:variant) {
                if (cell[IDX_VALUE]==value) {
                    toWin--;
                }
                else if (cell[IDX_VALUE]==EMPTY_VALUE) {
                    emptyPos = cell[IDX_ADDRESS];
                }
            }
            if (toWin<2 && emptyPos>-1) { //вторая проверка излишняя, но пусть будет
                pos = emptyPos;
            }
        }
        return pos;
    }
    int[] mergeCells(int[] cells1,int[] cells2) {
        int[] cells = new int[]{};
        for (int cellId:cells1) {
            int pos = Arrays.binarySearch(cells2,cellId);
            if (pos>-1) {
                cells = Arrays.copyOf(cells, cells.length + 1);
                cells[cells.length - 1] = cellId;
            }
        }
        return  cells;
    }

    private int getBestPosition() {
        resetCellPriority();
        int[] playerCells = getBestCells(PLAYER_VALUE);
        int[] aiCells = getBestCells(AI_VALUE);
        int[] aiAndPlayerCells = mergeCells(playerCells,aiCells);

        int choosed = chooseFromCellArray(aiAndPlayerCells);
        if (choosed>-1) return choosed;
        choosed = chooseFromCellArray(playerCells);
        if (choosed>-1) return choosed;
        return chooseFromCellArray(aiCells);

    }
    int chooseFromCellArray(int[] array) {
        if (array==null || array.length==0) return -1;
        return array[rand.nextInt(array.length)];

    }
    private void resetCellPriority() {
        for (int[] freecell : freeCells) {
            if (freecell == null) continue;
            freecell[IDX_PRIORITY] = 0;
        }
    }
    private int[] getBestCells(int value) {
        int[][][] variants = (value==PLAYER_VALUE)?playerVariants:aiVariants;
        //считаем приоритет
        //ищем самые опасные вражьи варианты
        int maxPriority = -1;
        int[][][] bestVariants = new int[][][]{};
        for (int[][] variant:variants) {
            if (variant==null) continue;
            int priority = 0;

            for (int[] cell:variant) {
                if (cell[IDX_VALUE]==value) {
                    priority++;
                }

            }

            if (priority>maxPriority ) {
                bestVariants = new int[][][]{};
                maxPriority = priority;
            }
            if (priority>-1 && maxPriority==priority) {
                bestVariants = Arrays.copyOf(bestVariants, bestVariants.length + 1);
                bestVariants[bestVariants.length-1] = variant;
            }
        }
        maxPriority = 0;
        for (int[][] variant:bestVariants) {
            if (variant==null) continue;
            for (int[] cell:variant) {
                if (cell[IDX_VALUE]==EMPTY_VALUE) {
                    cell[IDX_PRIORITY]++;
                    if (cell[IDX_PRIORITY] > maxPriority) maxPriority = cell[IDX_PRIORITY];
                }
            }
        }
        int[] choice = new int[]{};
        for (int[][] variant:bestVariants) {
            if (variant==null) continue;
            for (int[] cell:variant) {
                if (cell[IDX_VALUE]==EMPTY_VALUE && cell[IDX_PRIORITY]==maxPriority) {
                    if (Arrays.binarySearch(choice,cell[IDX_ADDRESS])<0) {
                        choice = Arrays.copyOf(choice, choice.length + 1);
                        choice[choice.length - 1] = cell[IDX_ADDRESS];
                    }
                }
            }
        }


        return choice;
    }

    private int[] getBestEnemyCells() {
        //считаем приоритет
        //ищем самые опасные вражьи варианты
        int maxPriority = -1;
        int[] variants = new int[]{};
        for (int i = 0;i<playerVariants.length;i++) {
            int[][] variant = playerVariants[i];
            if (variant==null) continue;
            int priority = 0;
            int toWin = WIN_SIZE;
            for (int[] cell:variant) {
                if (maxPriority == Integer.MAX_VALUE) break;
                if (cell[IDX_VALUE]==1) {
                    toWin--;
                    priority++;
                }
                priority++;
            }
            if (toWin<WIN_SIZE/2 + 1) priority = Integer.MAX_VALUE;
            if (priority>maxPriority ) {
                variants = new int[]{};
                maxPriority = priority;
            }
            if (priority>0 && maxPriority==priority) {
                variants = Arrays.copyOf(variants, variants.length + 1);
                variants[variants.length-1] = i;
            }
        }
        for (int i = 0; i < variants.length; i++) {
            int[][] variant = aiVariants[variants[i]];
            if (variant==null) continue;
            for (int[] cell:variant) {
                cell[IDX_PRIORITY]++;
            }
        }

        int[] choice = new int[]{};
        for (int[] freecell:freeCells) {
            if (freecell == null) continue;
            int priority = 0;
            for (int i = 0; i < variants.length; i++) {
                int[][] variant = playerVariants[variants[i]];
                for (int[] cell : variant) {
                    if (cell == freecell) {
                        choice = Arrays.copyOf(choice, choice.length + 1);
                        choice[choice.length - 1] = cell[IDX_ADDRESS];
                    }
                }

            }
        }
        return choice;
    }
    private int[] getBestAiCells() {
        //считаем приоритет
        //ищем самые опасные вражьи варианты
        int maxPriority = -1;
        int[] variants = new int[]{};
        for (int i = 0;i<aiVariants.length;i++) {
            if (maxPriority == Integer.MAX_VALUE) break;
            int[][] variant = aiVariants[i];
            if (variant==null) continue;
            int priority = 0;
            int toWin = WIN_SIZE;
            for (int[] cell:variant) {
                if (cell[IDX_VALUE]==2) {
                    toWin--;
                    priority++;
                }
                priority++;
            }
            if (toWin<WIN_SIZE/2 + 1) priority = Integer.MAX_VALUE/2;
            if (toWin<2) priority = Integer.MAX_VALUE;

            if (priority>maxPriority ) {
                variants = new int[]{};
                maxPriority = priority;
            }
            if (priority>0 && maxPriority==priority) {
                variants = Arrays.copyOf(variants, variants.length + 1);
                variants[variants.length-1] = i;
            }
        }
        for (int i = 0; i < variants.length; i++) {
            int[][] variant = aiVariants[variants[i]];
            if (variant==null) continue;
            for (int[] cell:variant) {
                cell[IDX_PRIORITY]++;
            }
            System.out.println();
        }
        int[] choice = new int[]{};
        for (int[] freecell:freeCells) {
            if (freecell == null) continue;
            int priority = 0;
            for (int i = 0; i < variants.length; i++) {
                int[][] variant = aiVariants[variants[i]];
                if (variant==null) continue;
                for (int[] cell : variant) {
                    if (cell == freecell) {
                        choice = Arrays.copyOf(choice, choice.length + 1);
                        choice[choice.length - 1] = cell[IDX_ADDRESS];
                    }
                }

            }
        }
        return choice;
    }
    boolean checkWin(char dot) {
        int[][][] array = (dot==DOT_X)? playerVariants:aiVariants;
        for (int[][] variant:array) {
            if (variant==null) break;
            boolean win = true;
            for (int[] cell : variant) {
                if (cell[0]==0) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        return false;
    }


   boolean isMapFull() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (map[i][j] == DOT_EMPTY)
                    return false;
        return true;
    }

    boolean isCellValid(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE)
            return false;
        return map[y][x] == DOT_EMPTY; // by DSerov
    }
}