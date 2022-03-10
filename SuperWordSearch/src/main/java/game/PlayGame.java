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


        if (mode.equals("NO_WRAP")) {
             noWrapSearch(n, m, boardArr, possibleWords);
        }
        if(mode.equals("WRAP")){
            wrapSearch(n, m, boardArr, possibleWords);
        }
    }
        public void noWrapSearch(int n, int m, String[][] board, ArrayList<String> possibleWords){
            Map<String, Coordinate> map = new HashMap<String, Coordinate>();

            //display board to console
            for(String[] row : board){
                System.out.println(Arrays.toString(row));
            }

            for(String word : possibleWords){
                StringBuilder sb = new StringBuilder();
                map.clear();
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
            }

        public void wrapSearch(int n, int m, String[][] board, ArrayList<String> possibleWords){
            //direction mapping according to index of arrays x & y
            int diagonalLeftUp = 0,
                verticalUp = 1,
                diagonalLeftDown = 2,
                horizontalLeft = 3,
                horizontalRight = 4,
                diagonalRightUp = 5,
                verticalDown = 6,
                diagonalRightDown = 7;

            Map<String, Coordinate> map = new HashMap<String, Coordinate>();
            //display board to console
            for(String[] row : board){
                System.out.println(Arrays.toString(row));
            }

            for(String word : possibleWords) {
                StringBuilder sb = new StringBuilder();
                map.clear();
                int len = word.length();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        if (board[i][j].equals(String.valueOf(word.charAt(0)))) {
                            Coordinate pt0 = new Coordinate();
                            pt0.setX(i);
                            pt0.setY(j);
                            map.put(String.valueOf(word.charAt(0)), pt0);
                            for (int d = 0; d < 8; d++) { //i is possible directions... 8 total
                                for (int c = 1; c < len; c++) { //for each letter find the direction
                                    int dx = i + (x[d] * c),
                                        dy = j + (y[d] * c);

                                    //CHECK HORIZONTAL:

                                    //HORIZONTAL RIGHT:
                                    if (d == horizontalRight && board[dx][dy].equals(String.valueOf(word.charAt(c))) && dx < m){
                                        Coordinate pt = new Coordinate();
                                        pt.setX(i);
                                        pt.setY(dy);
                                        map.put(String.valueOf(word.charAt(c)), pt);
                                    }
                                    if (d == horizontalRight && dx >= m){
                                        String[] temp = leftRotate(board[i], j);
                                        String result = convertStringArrayToString(temp);
                                        if(result.equals(word)){
                                            for(int hr = 0; hr < j; hr++){
                                                Coordinate pt = new Coordinate();
                                                pt.setX(i);
                                                pt.setY(hr);
                                                map.put(String.valueOf(word.charAt(c + hr)), pt);
                                            }
                                        }
                                        continue;
                                    }
                                    //HORIZONTAL LEFT:
                                    if (d == horizontalLeft && board[dx][dy].equals(String.valueOf(word.charAt(c))) && dx >= 0){
                                        Coordinate pt = new Coordinate();
                                        pt.setX(i);
                                        pt.setY(dy);
                                        map.put(String.valueOf(word.charAt(c)), pt);
                                    }
                                    if (d == horizontalLeft && dx < 0){
                                        int gap = m - (j + 1);
                                        String[] temp = rightRotate(board[i], gap);
                                        String result = convertStringArrayToString(temp);
                                        if(result.equals(word)){
                                            for(int hl = m; hl > j; hl--){
                                                Coordinate pt = new Coordinate();
                                                pt.setX(i);
                                                pt.setY(hl);
                                                map.put(String.valueOf(word.charAt(c + (m - hl))), pt);
                                            }
                                        }
                                        continue;
                                    }

                                    //CHECK VERTICAL:

                                    //VERTICAL DOWN:
                                    if(d == verticalDown && board[dx][dy].equals(String.valueOf(word.charAt(c))) && dy < n){
                                        Coordinate pt = new Coordinate();
                                        pt.setX(dx);
                                        pt.setY(j);
                                        map.put(String.valueOf(word.charAt(c)), pt);
                                    }
                                    if(d == verticalDown && dy >= n){
                                        String[] arr = new String[n];
                                        for(int k = 0; k < n; k++){
                                            arr[k] = board[k][j];
                                        }
                                        String[] temp = rightRotate(arr, i);
                                        String result = convertStringArrayToString(temp);
                                        if(result.equals(word)){
                                            for(int vd = 0; vd < i; vd++){
                                                Coordinate pt = new Coordinate();
                                                pt.setX(vd);
                                                pt.setY(j);
                                                map.put(String.valueOf(word.charAt(c + vd)), pt);
                                            }
                                        }
                                        continue;
                                    }

                                    //VERTICAL UP:
                                    if(d == verticalUp && board[dx][dy].equals(String.valueOf(word.charAt(c))) && dy >= 0){
                                        Coordinate pt = new Coordinate();
                                        pt.setX(dx);
                                        pt.setY(j);
                                        map.put(String.valueOf(word.charAt(c)), pt);
                                    }
                                    if(d == verticalUp && dy < 0){

                                    }

                                }
                            }



                            //move in a direction
                            //if direction leads to next letter keep moving
                            //if boundary is reached (row = 0, row = n, column = 0, column = n)

                            //if moving diagonal right down -- then check board[0] && check board[0][0] to board[n][0]

                            //if moving diagonal left down -- then check board[0] && check board[0][m] to board[n][m]

                            //if moving diagonal right up -- check board[n] && check board[0][0] to board[n][0]

                            //if moving diagonal left up -- check board[n] && check board[0][m] to board[n][m]

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
}





