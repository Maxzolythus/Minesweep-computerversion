import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

import static javafx.application.Platform.exit;

/**
 * MinesweepGUI.java
 * Created by Maxzolythus Ferguson on 7/8/2016.
 */
public class MinesweepGUI extends Application implements Observer {

    private ArrayList<Rectangle> gameBtns = new ArrayList<>();
    private ArrayList<Text> gameTxt = new ArrayList<>();
    private ArrayList<StackPane> game = new ArrayList<>();

    private boolean isTimerStarted = false;

    private int size = 9;
    //private MinesweepModel model;
    private MinesweepController controller;

    private Text time;
    private Text remainingMines;
    private HBox ctrlPanel;
    private BorderPane main;

    public MinesweepGUI(){
        controller = new MinesweepController(size);
        controller.addObserver(this);
    }

    public void start(Stage stage){
        stage.onCloseRequestProperty().setValue(event -> {Platform.exit(); System.exit(0);});
        main = new BorderPane();
        main.setPrefSize(500, 750);
        main.setStyle("-fx-background-color: #1a1a1a");
        Scene scene = new Scene(main);
        scene.getStylesheets().add("/MinesweepStyles.css");

        // Top of the bp
        
        main.setTop(makeControlPanel());

        // Center of the bp

        main.setCenter(makeGameBoard());

        // Bottom of the bp

        main.setBottom(makeFGPanel());

        stage.setTitle( "Minesweeper" );
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates the board where the game is played
     * @return A GridPane that represents where you make your guesses on the game board.
     */
    private GridPane makeGameBoard(){
        // Makes room for a new game board, or if one has't been generated it does nothing.
        gameBtns.clear();
        gameTxt.clear();
        game.clear();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #262626");
        Rectangle btn;
        Rectangle bot;
        double recSize = 0;
        double arcSize = 0;
        double textSize;

        for(int r = 0;
            r < size;
            ++r) {
            for (int i = 0;
                 i < size;
                 ++i) {

                if(size == 9){ // Determines size of buttons on the game board, based on size of board
                    recSize = 50;
                    arcSize = 20;
                    textSize = 25;

                }
                else{
                    recSize = 50/((double)size/9);
                    arcSize = 20/((double)size/9);
                    textSize = 25/((double)size/9);
                }

                bot = new Rectangle(recSize, recSize, Color.web("#262626"));
                bot.setArcHeight(arcSize);
                bot.setArcWidth(arcSize);

                Text num = new Text(controller.getSpace(r, i).toString());
                num.setFont(new Font(textSize));
                num.setFill(Color.WHITE);
                gameTxt.add(num);

                btn = new Rectangle(recSize, recSize, Color.web("#0039e6"));
                grid.setHgap(recSize/12);
                grid.setVgap(recSize/12);
                btn.setArcHeight(arcSize);
                btn.setArcWidth(arcSize);
                int row = r;
                int col = i;

                btn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                        event -> {
                            controller.guess(row, col);
                            if(!isTimerStarted) {
                                beginTimer();
                                isTimerStarted = true;
                            }
                        }
                    );
                gameBtns.add(btn);
                StackPane stack = new StackPane(bot,num, btn);
                GridPane.setConstraints(stack, r, i);
                game.add(stack);
                grid.getChildren().addAll(stack); // Adding stack instead of btn
            }
        }

        BorderPane.setMargin(grid, new Insets(arcSize, recSize/12, 0, recSize/12));

        return grid;
    }
	
	/**
	* Creates the flag/sweep control panel
	* @return A HBox that controls if your flagging or sweeping when you click
	*/
	private HBox makeFGPanel(){
		HBox botBox = new HBox();
		botBox.setSpacing(50);
		botBox.setAlignment(Pos.CENTER);
		Button sweep = new Button("Sweep");
		//sweep.getStyleClass().add("bottom-buttons-unclicked");
		sweep.getStyleClass().add("bottom-buttons-clicked");

		Button flag = new Button("Flag");
		flag.getStyleClass().add("bottom-buttons-unclicked");
		//flag.getStyleClass().add("bottom-buttons-clicked");

		flag.addEventHandler(MouseEvent.MOUSE_CLICKED,
				event -> {
					if(controller.getSweeping()){
						controller.toggleSweeping();
						flag.setStyle("-fx-background-color: #f2f2f2; -fx-font-size: 20px;");
						sweep.setStyle("-fx-background-color: #8c8c8c; -fx-font-size: 20px; -fx-font-color: #1a1a1a;");
					}
				});
		sweep.addEventHandler(MouseEvent.MOUSE_CLICKED,
				event -> {
					if(!controller.getSweeping()){
						controller.toggleSweeping();
						flag.setStyle("-fx-background-color: #8c8c8c; -fx-font-size: 20px; -fx-font-color: #1a1a1a;");
						sweep.setStyle("-fx-background-color: #f2f2f2; -fx-font-size: 20px;");
					}
				});

		botBox.getChildren().addAll(sweep, flag);
		
		return botBox;
	}
	
	/**
	* Creates the main control panel, that controls difficulty, and displays certain other aspects of the game
	* @return A HBox that contains the control panel's information  
	*/
	private HBox makeControlPanel(){
		ctrlPanel = new HBox(200);
        VBox txtBox = new VBox();

        BorderPane.setMargin(ctrlPanel, new Insets(10, 5, 0, 0));
        time = new Text("0:00");
        time.setFont(new Font("Arial", 60));
        time.setFill(Color.WHITE);

        remainingMines = new Text("There are " + controller.getRemainingMines() + " mines left.");
        remainingMines.setFont(new Font("Arial", 14));
        remainingMines.setFill(Color.WHITE);

        Button quit = new Button("Quit");
        quit.getStyleClass().add("ctrlpanel-buttons");
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {Platform.exit(); System.exit(0);});

        txtBox.getChildren().addAll(time, remainingMines, quit);

        VBox menu = new VBox();
        menu.setSpacing(10);

        Button easy = new Button("Easy: size 9x9");
        easy.getStyleClass().add("ctrlpanel-buttons");
        easy.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    size = 9;
                    ctrlPanel.setSpacing(200);
                    controller = new MinesweepController(size);
                    controller.addObserver(this);
                    remainingMines.setText("There are " + controller.getRemainingMines() + " mines left.");
                    main.setCenter(makeGameBoard());
                    isTimerStarted = false;
                });

        Button med = new Button("Med: size 16x16");
        med.getStyleClass().add("ctrlpanel-buttons");
        med.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    size = 16;
                    ctrlPanel.setSpacing(200);
                    controller = new MinesweepController(size);
                    controller.addObserver(this);
                    remainingMines.setText("There are " + controller.getRemainingMines() + " mines left.");
                    main.setCenter(makeGameBoard());
                    isTimerStarted = false;
                });
        Button hard = new Button("Hard: size 24x24");
        hard.getStyleClass().add("ctrlpanel-buttons");
        hard.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> {
                    size = 24;
                    ctrlPanel.setSpacing(200);
                    controller = new MinesweepController(size);
                    controller.addObserver(this);
                    remainingMines.setText("There are " + controller.getRemainingMines() + " mines left.");
                    main.setCenter(makeGameBoard());
                    isTimerStarted = false;
                });

        menu.getChildren().addAll(easy,med,hard);

        ctrlPanel.getChildren().addAll(txtBox, menu);
		
		return ctrlPanel;
	}

    /**
     * Starts the timer
     */
    public void beginTimer(){
        controller.start();
    }


    public void update(Observable t, Object o){
        if(!controller.isGameOver()) {
            if (controller.seconds < 10) {
                time.setText(controller.min + ":0" + controller.seconds);
                //System.out.println(model.min + ":0" + model.seconds); debug
            } else {
                time.setText(controller.min + ":" + controller.seconds);
                //System.out.println(model.min + ":" + model.seconds); debug
            }

            Character[][] hidden = controller.getHidden();

            // Updating the game board
            for (int r = 0;
                 r < size;
                 r++) {
                for (int c = 0;
                     c < size;
                     c++) {
                    if(controller.hasFlag(r,c)){
                        gameBtns.get((r * size) + c).setFill(Color.web("#ff1a1a"));
                    }
                    else{
                        gameBtns.get((r * size) + c).setFill(Color.web("#0039e6"));
                    }

                    if (hidden[r][c] == ' ') {
                        //gameBtns.get((r * size) + c).setFill(Color.WHITE);
                        game.get((r * size) + c).getChildren().remove(gameBtns.get((r * size) + c));
                    }
                    else if(Character.isDigit(hidden[r][c])){
                        //gameBtns.get((r * size) + c).setFill(Color.WHITE);
                        game.get((r * size) + c).getChildren().remove(gameBtns.get((r * size) + c));
                    }
                }
            }

            remainingMines.setText("There are " + controller.getRemainingMines() + " mines left.");

            // Checking for a win
            if(controller.hasWon()){
                time.setText("You Win!");
                ctrlPanel.setSpacing(80);
            }

        }
        else{
            time.setText("You Lose!");
            ctrlPanel.setSpacing(80);
        }
    }

    public static void main(String[] args){
        Application.launch();
    }
}
