package src.main.java.game;


import src.main.java.game.Coordinate;
import src.main.java.game.SuperSearchWordPersistenceException;

import java.io.*;
import java.util.*;

public class PlayGame {

    private String INPUT_DATA_FILE = "data.txt";
    private String ERROR_MESSAGE = "*** COULD NOT FIND FILE! Please name input file: 'data.txt' && add it to package: SuperWordSearch/src/ ***";

    //possible directions relative to a point
    int[] x = {-1, -1, -1, 0, 0, 1, 1, 1 },
          y = { -1, 0, 1, -1, 1, -1, 0, 1 };

    Map<String, Coordinate> wrapMap = new HashMap<String, Coordinate>();
    Map<String, String> displayMap = new HashMap<>();

    public void PlayGame(String INPUT_DATA_FILE) throws SuperSearchWordPersistenceException {
        Scanner scanner;
        int n = 0 , m = 0, i = 0, j = 0, numWords;
        String mode = null;
        ArrayList<String> boardInputs = new ArrayList<String>();
        ArrayList<String> possibleWords = new ArrayList<String>();
        ArrayList<ArrayList<String>> board = new ArrayList<ArrayList<String>>();


        try {
            scanner = new Scanner(new BufferedReader(new FileReader(INPUT_DATA_FILE)));
        } catch (FileNotFoundException e) {
            throw new SuperSearchWordPersistenceException(ERROR_MESSAGE, e);
        }
        while (scanner.hasNextLine()) {

            //inputs n and m
            n = Integer.parseInt(scanner.next()); //rows
            m = Integer.parseInt(scanner.next()); //columns

            //inputs for board[n][m] -- O(n^2)
            while (i < n) {
                boardInputs.add(scanner.next());
                i++;
            }
            mode = scanner.next(); //WRAP OR NO_WRAP
            numWords = Integer.parseInt(scanner.next()); //Number of possible valid words (given)

            // inputs for possible words to check
            //while (j < numWords) {
            while (scanner.hasNextLine()) {
                possibleWords.add(scanner.next());
                j++;
            }
        }

        /* //correctly populates ArrayList<String>
        for(String word : possibleWords){
            System.out.println(word);
        }
        */

        //populate points (x , y) of board
        for (String s : boardInputs) {
            String[] sArray = s.split("");
            board.add(new ArrayList(Arrays.asList(sArray)));
        }

        //convert ArrayList Matrix to Array Matrix
        String[][] boardArr = new String[n][m];
        for (int k = 0; k < board.size(); k++) {
            ArrayList<String> row = board.get(k);
            boardArr[k] = row.toArray(new String[row.size()]);
        }

        //DISPLAY BOARD TO CONSOLE
        System.out.println("Board:");
        for(String[] row : boardArr){
            System.out.println(Arrays.toString(row));
        }
        System.out.println("");

        if (mode.equals("NO_WRAP")) {
             for(String word: possibleWords){
                 noWrapSearch(n, m, boardArr, word);
             }
            //noWrapSearch(n, m, boardArr, possibleWords);
        }
        if(mode.equals("WRAP")){
            for(String word: possibleWords){
                displayMap.put(word, "NOT FOUND");
                wrapMap.clear();
                wrapSearch(n, m, boardArr, word);
            }
            for(Map.Entry<String, String> entry : displayMap.entrySet()){
                System.out.println(entry.getKey() + ":");
                System.out.println(entry.getValue());
            }
            //wrapSearch(n, m, boardArr, possibleWords);
        }
    }
        public void noWrapSearch(int n, int m, String[][] board, String word){
            Map<String, Coordinate> map = new HashMap<String, Coordinate>();



            //for(String word : possibleWords){
                StringBuilder sb = new StringBuilder();
                //map.clear();
                int len = word.length();
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < m; j++) {
                            if(board[i][j].equals(String.valueOf(word.charAt(0)))) {
                                Coordinate pt0 = new Coordinate();
                                pt0.setX(i);
                                pt0.setY(j);
                                map.put(String.valueOf(word.charAt(0)), pt0);
                                for (int d = 0; d < 8; d++) { //i is possible directions... 8 total
                                    for (int c = 1; c < len; c++) { //for each letter find the direction
                                        int xd = i + (x[d] * c),
                                            yd = j + (y[d] * c);
                                        if(xd >= n || xd < 0 || yd >= m || yd < 0){
                                            continue;
                                        }
                                        if(board[xd][yd].equals(String.valueOf(word.charAt(c)))){
                                            Coordinate pt = new Coordinate();
                                            pt.setX(xd);
                                            pt.setY(yd);
                                            map.put(String.valueOf(word.charAt(c)), pt);
                                        }
                                    }
                                }
                            }
                        }
                    }
                if(map.size() == len){
                    Coordinate firstPT = new Coordinate();
                    Coordinate lastPT = new Coordinate();
                    firstPT = map.get(String.valueOf(word.charAt(0)));
                    lastPT = map.get(String.valueOf(word.charAt(len - 1)));
                    sb.append("(").append(firstPT.getX()).append(",").append(firstPT.getY()).append(")").append("(").append(lastPT.getX()).append(",").append(lastPT.getY()).append(")");
                    //System.out.println(sb.toString());
                } else {
                    sb.append("NOT FOUND");
                    //System.out.println(sb.toString());
                }
                System.out.println(sb.toString());
                }
            //}

        public void wrapSearch(int n, int m, String[][] board, String word){
            //direction mapping according to index of arrays x & y
            int diagonalLeftUp = 0,
                verticalUp = 1,
                diagonalLeftDown = 2,
                horizontalLeft = 3,
                horizontalRight = 4,
                diagonalRightUp = 5,
                verticalDown = 6,
                diagonalRightDown = 7;

                StringBuilder sb = new StringBuilder();
            int len = word.length();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (board[i][j].equals(String.valueOf(word.charAt(0)))) {
                        Coordinate pt0 = new Coordinate();
                        pt0.setX(i);
                        pt0.setY(j);
                        wrapMap.put(String.valueOf(word.charAt(0)), pt0);
                        for (int d = 0; d < 8; d++) { //i is possible directions... 8 total
                            for (int c = 1; c < len; c++) { //for each letter find the direction
                                int dx = i + (x[d] * c),
                                    dy = j + (y[d] * c);

                                //CHECK HORIZONTAL:

                                //HORIZONTAL RIGHT:
                                if (d == horizontalRight && dx >= m){
                                    String[] temp = leftRotate(board[i], j);
                                    String result = convertStringArrayToString(temp);
                                    if(result.equals(word)){
                                        for(int hr = 0; hr < j; hr++){
                                            Coordinate pt = new Coordinate();
                                            pt.setX(i);
                                            pt.setY(hr);
                                            wrapMap.put(String.valueOf(word.charAt(c + hr)), pt);
                                        }
                                    }
                                    continue;
                                }

                                if (dx < n && dy < m && d == horizontalRight && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(i);
                                    pt.setY(dy);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }
                                //HORIZONTAL LEFT:
                                if (d == horizontalLeft && dx < 0){
                                    int gap = m - (j + 1);
                                    String[] temp = rightRotate(board[i], gap);
                                    String result = convertStringArrayToString(temp);
                                    if(result.equals(word)){
                                        for(int hl = (m - 1); hl > j; hl--){
                                            Coordinate pt = new Coordinate();
                                            pt.setX(i);
                                            pt.setY(hl);
                                            wrapMap.put(String.valueOf(word.charAt(c + (m - hl))), pt);
                                        }
                                    }
                                    continue;
                                }
                                if (dx >= 0 && dy > 0 && d == horizontalLeft && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(i);
                                    pt.setY(dy);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }

                                //CHECK VERTICAL:

                                //VERTICAL DOWN:
                                if(d == verticalDown && dx >= n){
                                    String[] arr = new String[n];
                                    for(int k = 0; k < n; k++){
                                        arr[k] = board[k][j];
                                    }
                                    String[] temp = leftRotate(arr, i);
                                    String result = convertStringArrayToString(temp);
                                    if(result.equals(word)){
                                        for(int vd = 0; vd < i; vd++){
                                            Coordinate pt = new Coordinate();
                                            pt.setX(vd);
                                            pt.setY(j);
                                            wrapMap.put(String.valueOf(word.charAt(c + vd)), pt);
                                        }
                                    }
                                    continue;
                                }
                                if(d == verticalDown && board[dx][dy].equals(String.valueOf(word.charAt(c))) && dx < n){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(dx);
                                    pt.setY(j);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }

                                //VERTICAL UP:
                                if(d == verticalUp && dx < 0){
                                    String[] arr = new String[n];
                                    for(int k = 0; k < n; k++){
                                        arr[k] = board[k][j];
                                    }
                                    int gap = (n - 1) - i;
                                    String[] temp = rightRotate(arr, gap);
                                    String result = convertStringArrayToString(temp);
                                    if(result.equals(word)){
                                        for(int vu = (n - 1); vu > i; vu--){
                                            Coordinate pt = new Coordinate();
                                            pt.setX(vu);
                                            pt.setY(j);
                                            wrapMap.put(String.valueOf(word.charAt(c + vu)), pt);
                                        }
                                    }
                                    continue;
                                }
                                if(dx >= 0 && d == verticalUp && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(dx);
                                    pt.setY(j);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }
                            // ####### CHECK DIAGONALS: #########
                                //DIAGONAL RIGHT DOWN:
                                if(d == diagonalRightDown && (dy >= n || dx >= m)) {
                                    //Check top of board
                                    if (arrayContains(board[0], String.valueOf(word.charAt(c)))) {
                                        //check one below top of board for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                    //Check left side of board
                                    String[] arrayDRD = new String[n];
                                    for (int l = 0; l < n; l++) {
                                        arrayDRD[l] = board[l][0];
                                    }
                                    if (arrayContains(arrayDRD, String.valueOf(word.charAt(c)))) {
                                        //Check column to the right for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                }
                                if(dy < m && dx < n && d == diagonalRightDown && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(dx);
                                    pt.setY(dy);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }
                                //DIAGONAL RIGHT UP:
                                if(d == diagonalRightUp && (dy < 0 || dx <= m)){
                                    //Check bottom of board
                                    if(arrayContains(board[n - 1], String.valueOf(word.charAt(c)))){
                                        //check one above bottom of board for next letter
                                        String substring = word.substring(c + 1);
                                        wrapSearch(n, m, board, substring);
                                    }
                                    //Check left side of board
                                    String[] arrayDRU = new String[n];
                                    for(int l = 0; l < n; l++){
                                        arrayDRU[l] = board[l][0];
                                    }
                                    if(arrayContains(arrayDRU, String.valueOf(word.charAt(c)))) {
                                        //Check column to the right for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                }
                                if(dx < m && dy >= 0 && d == diagonalRightUp && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(dx);
                                    pt.setY(dy);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }
                                //DIAGONAL LEFT UP:
                                if(d == diagonalLeftUp && (dy < 0 || dx < 0)){
                                    //Check bottom of board
                                    if(arrayContains(board[n - 1], String.valueOf(word.charAt(c)))){
                                        //check one above bottom of board for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                    //Check right side of board
                                    String[] arrayDLU = new String[n];
                                    for(int r = 0; r < n; r++){
                                        arrayDLU[r] = board[r][m - 1];
                                    }
                                    if(arrayContains(arrayDLU, String.valueOf(word.charAt(c)))) {
                                        //Check column to the left for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                }
                                if(dy >= 0 && dx >= 0 && d == diagonalLeftUp && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(dx);
                                    pt.setY(dy);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }
                                //DIAGONAL LEFT DOWN:
                                if(d == diagonalLeftDown && (dy >= n || dx < 0)){
                                    //Check top of board
                                    if(arrayContains(board[0], String.valueOf(word.charAt(c)))){
                                        //check one above bottom of board for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                    //Check right side of board
                                    String[] arrayDLU = new String[n];
                                    for(int r = 0; r < n; r++){
                                        arrayDLU[r] = board[r][m - 1];
                                    }
                                    if(arrayContains(arrayDLU, String.valueOf(word.charAt(c)))) {
                                        //Check column to the left for next letter
                                        String substring = word.substring(c);
                                        wrapSearch(n, m, board, substring);
                                    }
                                }
                                if(dy < m && dx >= 0 && d == diagonalLeftDown && board[dx][dy].equals(String.valueOf(word.charAt(c)))){
                                    Coordinate pt = new Coordinate();
                                    pt.setX(dx);
                                    pt.setY(dy);
                                    wrapMap.put(String.valueOf(word.charAt(c)), pt);
                                }
                            }
                        }
                    }
                }
            }
            if(wrapMap.size() == len) {
                Coordinate firstPT = new Coordinate();
                Coordinate lastPT = new Coordinate();
                firstPT = wrapMap.get(String.valueOf(word.charAt(0)));
                lastPT = wrapMap.get(String.valueOf(word.charAt(len - 1)));
                //System.out.println(word + ":");
                sb.append("(").append(firstPT.getX()).append(",").append(firstPT.getY()).append(")").append("(").append(lastPT.getX()).append(",").append(lastPT.getY()).append(")");
                displayMap.put(word, sb.toString());
                //System.out.println(sb.toString());
            }
            //System.out.println(sb.toString());
        }

        public static String[] leftRotate(String[] array, int rotations){
            int n = array.length;
            String[] result = new String[n];
            rotations = rotations % n;
            int i, j, k, l;
            String temp;
            int g_c_d = gcd(rotations, n);

            for(i = 0; i < g_c_d; i++){
                temp = array[i];
                j = i;
                while(true){
                    k = j + rotations;
                    if(k >= n){
                        k = k - n;
                    }
                    if(k == i){
                        break;
                    }
                    array[j] = array[k];
                    j = k;
                }
                array[j] = temp;
            }
            for(l = 0; l < n; l++){
                result[l] = array[l];
            }
            return result;
        }

        public static String[] rightRotate(String[] array, int rotations){
            int n = array.length;
            String[] result = new String[n];
            rotations = n - rotations;
            int i, j, k, l;
            String temp;
            int g_c_d = gcd(rotations, n);

            for(i = 0; i  < g_c_d; i++){
                temp = array[i];
                j = i;

                while(true){
                    k = j + rotations;
                    if(k >= n){
                        k = k - n;
                    }
                    if(k == i){
                        break;
                    }
                    array[j] = array[k];
                    j = k;
                }
                array[j] = temp;
            }
            for(l = 0; l < n; l++){
                result[l] = array[l];
            }
            return result;
        }

        public static int gcd(int a, int b){
            if(b == 0){
                return a;
            } else{
                return gcd(b, a % b);
            }
        }

        public static String convertStringArrayToString(String[] array){
            StringBuilder sb = new StringBuilder();
            for(String str : array){
                sb.append(str);
            }
            return sb.toString();
        }

        public static boolean arrayContains(String[] array, String s){
            List<String> stringList = new ArrayList<>(Arrays.asList(array));
            return stringList.contains(s);
        }
}





