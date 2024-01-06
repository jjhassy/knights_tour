import java.util.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class KnightFX extends Application
{
    // standard chess board size and the combinations of possible knight moves
    static int size = 8; 
    static int graphics[] = new int[size*size];
    static int xMoves[] = {1, 1,2, 2,-1,-1,-2,-2};
    static int yMoves[] = {2,-2,1,-1, 2,-2, 1,-1};
    static int output[] = new int[size*size];
    // makes sure the square is within the bounds of the chess board
    public boolean valid(int x, int y){
          return ((x >= 0 && y >= 0) && (x < size && y < size));
    }
    
    // makes sure the square is unvisited and valid 
    public boolean empty(int[] board, int x, int y){ 
        return (valid(x,y) && (board[y * size + x] < 0));// i will initialize the array to have -1 in all spots. that'll mean it's not visited.
        // if switching the order of valid and board im gonna be upset
    }
    
    // gets degree of a square 
    public int getDegree(int[] board, int x, int y){
        int degree = 0;
        for (int i = 0; i < size; i++){ 
            if(empty(board, x + xMoves[i], y + yMoves[i])) 
                degree++;
        }
        return degree;
    }    
    
    // finds the next square for a knight Node with the least degree
    public Node warnsdorff(int[] board, Node knight){
    int minDegIDX = -1; 
    // make this more than the possible knight moves so it's guaranteed replaced
    int minDeg = 9; 
    int xCoord, yCoord, degCheck;
    
    // checking all 8 possible moves
    for (int move = 0; move < size; move++){ 
        xCoord = knight.x  + xMoves[move];
        yCoord = knight.y + yMoves[move]; 
        
        if ((empty(board, xCoord, yCoord)) && (degCheck = getDegree(board, xCoord, yCoord)) < minDeg){ // something in this if statement messed up the whole thing
            minDegIDX = move; // save the move for the knight Node
            minDeg = degCheck; // the degree gotten from the coords replaces minDeg
        }
    }
    
    // there was no legal move so return a null knight Node
    if (minDegIDX == -1)
        return null; 
        
    // these coords are the CONFIRMED warnsdorff move    
    xCoord = knight.x + xMoves[minDegIDX]; 
    yCoord = knight.y + yMoves[minDegIDX]; 
    
    // appropriately numbering the square on the board
    board[yCoord * size + xCoord] = board[(knight.y) * size + (knight.x)] + 1; 
    
    // update the knight node
    knight.x = xCoord;
    knight.y = yCoord;
    
    return knight;
    }
    
    // print completed board
    public void print(int[] board){ 
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++){
                // I liked how tabs looked over spaces since it evenly spaces the numbers
                System.out.print("\t" + board[y * size + x]);
            }
            // new line after every row of size
            System.out.println("\n"); 
            output = board;
        }
    }
    
    public boolean neighbor(int x, int y, int xx, int yy){
        for (int i = 0; i < 8; i++){ // check if closed tour
            if (((x + xMoves[i]) == xx) && ((y + yMoves[i]) == yy))
                return true; // this literally doesnt work             
        }
        return false;
    }
    
    // algorithm will try to create a valid board using warnsdorff
    public boolean createBoard(){
    // builds fresh board
    int[] board = new int[size*size];
        for (int i = 0; i < size * size; ++i)
            board[i] = -1; 
        
        Random rand = new Random();
        
        // choose random starting position
        int x = rand.nextInt(size);
        int y = rand.nextInt(size);
        
        // sets first knight position
        Node knight = new Node(x, y);
        board[knight.y * size + knight.x] = 1; 
        
        // initializing next move
        Node nextMove = null; 
        for (int i = 0; i < size * size - 1; i++){ 
            // keep running warnsdorff until we exit the for loop, or we return false
            nextMove = warnsdorff(board, knight);
            if (nextMove == null)
                return false; 
        }
        
        if (!neighbor(nextMove.x,nextMove.y, x,y))
            return false;
        // if we reach here, this means it made it through the trial of getting all n*n squares covered
        print(board);
        return true;  
    }
    
    public void start(Stage stage){
        int[] vertices = new int[size*size];
        System.out.println(vertices.length);
        double[] polygon = new double[vertices.length * 2];
        
        while(!new KnightFX().createBoard()){        
        }
        for (int i = 0; i < output.length; i++){ // is it that the failed arrays mess something up
            System.out.println(output[i]); // numbers it 1-64
            vertices[output[i]-1] = i;
        }
        for (int i = 0; i < vertices.length; i++){
            polygon[i * 2] = (vertices[i]%8) * 80 + 40;
            polygon [i * 2 + 1] = (vertices[i]/8) * 80 + 40; // hopefully this is int div          
        }
        Circle start = new Circle(polygon[0], polygon[1], 3);
        //Circle second = new Circle(polygon[2], polygon[3], 3);        
        Circle end = new Circle(polygon[polygon.length-2], polygon[polygon.length-1], 3);   
        //Circle beforeEnd = new Circle(polygon[polygon.length-4], polygon[polygon.length-3], 3); 
        // these two commented points make it easier to see which direction the tour is going
        start.setFill(Color.RED);

        end.setFill(Color.GREEN);
        Line x1 = new Line(0,0,0,640);
        Line x2 = new Line(80,0,80,640);
        Line x3 = new Line(160,0,160,640);
        Line x4 = new Line(240,0,240,640);
        Line x5 = new Line(320,0,320,640);
        Line x6 = new Line(400,0,400,640);
        Line x7 = new Line(480,0,480,640);
        Line x8 = new Line(560,0,560,640);
        Line x9 = new Line(640,0,640,640);
        
        Line y1 = new Line(0,0,640,0);
        Line y2 = new Line(0,80,640,80);
        Line y3 = new Line(0,160,640,160);
        Line y4 = new Line(0,240,640,240);
        Line y5 = new Line(0,320,640,320); // really wanted to find a way to do this in a for loop
        Line y6 = new Line(0,400,640,400);// had no clue how
        Line y7 = new Line(0,480,640,480);
        Line y8 = new Line(0,560,640,560);
        Line y9 = new Line (0,640,640,640);
        Text title = new Text(50, 700, "CLOSED KNIGHT'S TOUR\nred start\ngreen end");
        
        Polygon path = new Polygon(polygon);
        path.setStroke(Color.RED);
        path.setFill(Color.WHITE);

        Group chessBoard = new Group(start, end, x1,x2,x3,x4,x5,x6,x7,x8, x9, y1,y2,y3,y4,y5,y6,y7,y8, y9);
        Group text = new Group(title);
        Group knight = new Group(path);
        Group board = new Group (knight, chessBoard);
        Group root = new Group(text, board);
        Scene scene = new Scene(root, 800, 800, Color.WHITE);
        stage.setTitle("Knight's Tour jjhassy");
        stage.setScene(scene);
        stage.show();    
    }
    
    class Node // represents a vertex (square) on the chess board
    {
        int x;
        int y;
  
        public Node(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
