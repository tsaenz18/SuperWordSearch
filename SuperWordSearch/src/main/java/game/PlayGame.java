package src.main.java.game;


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
            while (j < numWords) {
                possibleWords.add(scanner.next());
                j++;
            }
        }

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
        if(mode.equals("WRAPS")){
            wrapSearch(n, m, boardArr, possibleWords);
        }
    }
        public String noWrapSearch(int n, int m, String[][] board, ArrayList<String> possibleWords){
            Map<String, Coordinate> map = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            for(String word : possibleWords){
                map.clear();
                int len = word.length();
                for(int i = 0; i < n; i++) {
                    for(int j = 0; j < m; j++) {

                            if(board[i][j].equals(String.valueOf(word.charAt(0)))) {
                                Coordinate pt0 = new Coordinate();
                                pt0.setX(i);
                                pt0.setY(j);
                                map.put(String.valueOf(word.charAt(0)), pt0);
                                for (int c = 1; c < len; c++) { //for each letter find the direction
                                //for (int d = 0; d < 8; d++) { //i is possible directions... 8 total
                                    //for (int c = 1; c < len; c++) { //for each letter find the direction
                                    for (int d = 0; d < 8; d++) { //i is possible directions... 8 total
                                        int xd = i + x[d], yd = j + y[d];
                                        System.out.println("xd = " + xd + " | yd = " + yd);
                                        if(xd >= n || xd < 0 || yd >= m || yd < 0){
                                            break;
                                        }else if(board[xd][yd].equals(String.valueOf(word.charAt(c)))){
                                            Coordinate pt = new Coordinate();
                                            pt.setX(x[d]);
                                            pt.setY(y[d]);
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
                    System.out.println(sb.toString());
                } else {
                    sb.append("NOT FOUND");
                    System.out.println(sb.toString());
                }
                return sb.toString();
                }
            return "Done";
            }

        public String wrapSearch(int n, int m, String[][] board, ArrayList<String> possibleWords){
            return "";
        }
}





