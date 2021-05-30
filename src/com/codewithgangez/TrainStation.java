package com.codewithgangez;

import com.mongodb.client.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bson.Document;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class TrainStation extends Application {
    static final int SEATING_CAPACITY = 42; // declaring the seat capacity
    static Passenger[] waitingRoomForPassenger = new Passenger[42];//     waiting room is now a array of objects
    static int randomlyGeneratedNumber;//creating the variable for the randomly generated number
    static ArrayList<Passenger> boardedPassengers = new ArrayList<>();//creating a new arraylist to store the boarded passengers after simulation
    static String stationId;//storing the user input in choicing the station
    static int startIndex =0;//declaring the variable to store the last added index of array


    @Override
    public void start(Stage primaryStage)  {//starting the program
        selectTheRoute();//calling the previously loaded data
        menu();
    }

    private static void loadPreviousColomboData() {//getting the data from the colombo station
        int count = 0;//declaring variable to get count for the passengers in waiting room
        System.out.println("\n*********Loaded  Booked Customer Data from DataBase of Colombo Railway Station*********\n");//loadind the mongodb
        MongoClient mongoClient;
        MongoDatabase mongoDatabase;//connecting to the mongodb
        MongoCollection<org.bson.Document> mongoCollection;
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        mongoDatabase = mongoClient.getDatabase("ColombotoBadulla");
        mongoCollection = mongoDatabase.getCollection("Customer");

        FindIterable<Document> data = mongoCollection.find();
        for (Document temp : data) {
            Passenger passenger = new Passenger(); //----------------------create objects and set data to them
            passenger.setFirstName(temp.getString("name"));//here i am setting the data from passenger objects
            passenger.setSeat(temp.getInteger("seatNumber"));
            waitingRoomForPassenger[count] = passenger;  //here i'm assigning objects 1 by 1 to the waiting room
            count++;
        }
    }

    private static void loadPreviousBadullaData() {//getting the data from the badulla station
        int count = 0;//declaring variable to get count for the passengers in waiting room
        System.out.println("\n*********Loaded  Booked Customer Data from DataBase of Badulla Railway Station*********\n");//loadind the mongodb
        MongoClient mongoClient;
        MongoDatabase mongoDatabase;
        MongoCollection<Document> mongoCollection;
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        mongoDatabase = mongoClient.getDatabase("BadullatoColombo");
        mongoCollection = mongoDatabase.getCollection("Customer");

        FindIterable<Document> data = mongoCollection.find();
        for (Document temp : data) {
            Passenger passenger = new Passenger(); //----------------------create objects and set data to them
            passenger.setFirstName(temp.getString("name"));//here i am setting the data from passenger objects
            passenger.setSeat(temp.getInteger("seatNumber"));
            waitingRoomForPassenger[count] = passenger;  //here i'm assigning objects 1 by 1 to the waiting room
            count++;
        }
    }



    public static void selectTheRoute() {//selecting the route to load the previous data
        System.out.print("\n" +
                "\n****Enter the way you want to 'Load Data' of passengers****\n" +
                "\nColombo to Badulla  --------------     \"1\" \n " +
                "\nBadulla to Colombo  --------------      \"2\" \n" +
                "\n : ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        stationId = input;//creating a variable to store user input
        switch (input) {
            case "1"://if option 1 load the colombo to badulla route data
                loadPreviousColomboData();
                break;
            case "2"://if option 2 load badulla to colombo route data
                loadPreviousBadullaData();
                break;
            default:
                System.out.println("\n****Invalid Input,Enter Valid Input '01' Or '02' From The Menu****");
                selectTheRoute();

        }
    }

    public static void menu() {//creating a menu for the system
        System.out.println("\n");
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("\n");
            System.out.println("Colombo to Bathulla");
            System.out.println("\nPlease select one of choices given below according to your needs");
            System.out.println("_________________________________________________________________");

            System.out.println("\n01. Enter \"A\" to add customer to a seat");
            System.out.println("\n02. Enter \"V\" to view all seats");
            System.out.println("\n03. Enter \"D\" to delete customer from seat");
            System.out.println("\n04. Enter \"S\" to store program data into a file");
            System.out.println("\n05. Enter \"L\" to load program data from file ");
            System.out.println("\n06.Enter \"R\" to");
            System.out.println("\n07. Enter \"Q\" to exit the program");
            System.out.println("\n");

            System.out.print("\n----Select a option----: ");
            String Option = input.next().toLowerCase().trim();//changing the input option to lowercase
            switch (Option) {  //using the switching mechanism to choose the option
                case ("a"):
                    addCustomerToTrainQueue();
                    break;
                case ("v"):
                    viewTrainQueue();
                    break;
                case ("d"):
                    deleteCustomerFromTrainQueue();
                    break;
                case ("s"):
                    storeTrainQueue();
                    break;
                case ("l"):
                    loadTrainQueue();
                    break;
                case ("r"):
                    runSimulation();
                    break;
                case ("b"):
                    boardedPass();
                    break;
                case ("q"):
                    System.exit(0);
                default://setting up an exception to carried out when an invalid option is entered
                    System.out.println("\nEntered option is invalid ");
                    System.out.println("\n-------------Enter the valid option by selecting from the above menu-------------");
                    System.out.println("\n");
            }
        }
    }



    private static void addCustomerToTrainQueue() {

        int count = 0;
        for (Passenger passenger : waitingRoomForPassenger) {//getting passenger objects and storing it to waiting room array
            if (passenger != null) {
                count++;
            }
        }

        if (PassengerQueue.getLength()==SEATING_CAPACITY){//if the passenger queue is full no one will be getting added to queueArray array
            System.out.println("\nThe Passenger queue is full there is no slot for more persons");
            return;
        }

        if (count != 0) {//if there is people in the waiting room then random number generates
            randomlyGeneratedNumber = ThreadLocalRandom.current().nextInt(1, 7);
            for (int i = 0; i < randomlyGeneratedNumber; i++) {
                int index = i+startIndex;//indicating where to start from waiting room
                if (count == 0) {//if there is no people in waiting room then break
                    break;
                }if (waitingRoomForPassenger[index] != null) {//it'll start work if the waiting room is not equals to null
                    PassengerQueue.addPassenger(waitingRoomForPassenger[index].getSeat(), waitingRoomForPassenger[index]);//removing passengers one by one from waiting room
                    waitingRoomForPassenger[index] = null;//removing  passengers from the waiting room and storing them in
                    count--;//passenger removed from waiting room
                }
            }
            startIndex += randomlyGeneratedNumber;//updating the start index

        } else {//if there is no person in the waiting this error message will be thrown
            System.out.println("\n");
            System.out.println("****There is no more people in the 'Waiting Room' to add to the passenger Queue!!!****");
            return;
        }


        //creating a GUI for adding customer
        Stage stage = new Stage();
        Label heading = new Label("The Passengers Queue according to Random Number picking!");
        heading.setLayoutX(80);
        heading.setLayoutY(30);
        heading.setStyle("-fx-font-size:30px;");
        Label ranshow = new Label("The random number is "+randomlyGeneratedNumber);//showing the random nuber
        ranshow.setLayoutX(900);
        ranshow.setLayoutY(675);
        ranshow.setStyle("-fx-font-size:25px;");
        Label waitper = new Label("The remaning people in 'Waiting Room' is "+count);
        waitper.setLayoutX(50);
        waitper.setLayoutY(680);
        waitper.setStyle("-fx-font-size:25px;");
        Button close = new Button("Back To Menu");//creating the done button
        close.setLayoutX(520);// posistionin the button in the gridpane using cartesean
        close.setLayoutY(730);
        close.setStyle("-fx-font-size:15px");

        GridPane gridPane = new GridPane();//posistioning the buttons in the grid
        int x =0;
        int y=0;

        for (int i = 0; i < SEATING_CAPACITY; i++) { //creating the forloop for 42 times to check where the passenger is in the bookcus array
            if (PassengerQueue.queueArray[i] != null) {//setting this to get over with null exception error
                String guiname = PassengerQueue.queueArray[i].getName();//getting customer name from getter of passenger object
                Button abtn = new Button(" " + guiname);
                abtn.setMinWidth(170);
                abtn.setStyle("-fx-font-size: 1em;-fx-text-fill:black;-fx-border-color:black;-fx-text-fill:green;");
                abtn.setId(String.valueOf(i));
                abtn.setDisable(false);
                abtn.setPadding(new Insets(10));
                gridPane.add(abtn,x,y);

                x++;
                if(x==4){
                    y++;
                    x=0;
                }
            }
        }

        close.setOnAction(e ->//back to menu button
                stage.close()
        );

        stage.setOnCloseRequest(e ->//closing the stage using close button
                stage.close()
        );


        gridPane.setLayoutY(100);
        gridPane.setLayoutX(150);
        gridPane.setHgap(5);
        gridPane.setVgap(6);
        AnchorPane anchorPane = new AnchorPane();//creating the anchorpane
        anchorPane.setStyle("-fx-background-color:#66FFFF"); //i am fixing the gridpane inside the anchorpane
        anchorPane.getChildren().addAll(gridPane,close, heading,ranshow,waitper);//calling out all the labels
        Scene scene = new Scene(anchorPane, 1200, 900);//and buttons that we wnat in the GUI
        stage.setTitle(" ---Colombo Station Queue--- ");//giving the title for the stage
        stage.setScene(scene);//creating a scene for GUI
        stage.showAndWait();//calling out the stage

    }

    private static void viewTrainQueue() {
        Stage stage = new Stage();
        Label heading = new Label("Select The Mode Of View To View Passengers");
        heading.setLayoutX(130);
        heading.setLayoutY(25);
        heading.setStyle("-fx-font-size:30px;");
        Button close = new Button("Back To Menu");//creating the done button
        close.setLayoutX(460);// posistionin the button in the gridpane using cartesean
        close.setLayoutY(660);
        close.setStyle("-fx-font-size:15px");
        Button viewQueue = new Button("View The Queue");
        viewQueue.setLayoutX(300);
        viewQueue.setLayoutY(200);
        viewQueue.setStyle("-fx-font-size:30px");
        Button boardedCus = new Button("View The Boarded Passenger");
        boardedCus.setLayoutX(300);
        boardedCus.setLayoutY(400);
        boardedCus.setStyle("-fx-font-size:30px");

        GridPane gridPane = new GridPane();

        stage.setOnCloseRequest(e ->
                stage.close()
        );

        close.setOnAction(e ->
                stage.close()
        );

        viewQueue.setOnAction(e ->{
            passengerQue();
            stage.close();
        });

        boardedCus.setOnAction(e ->{
            boardedPass();
            stage.close();
        });

        gridPane.setLayoutY(100);
        gridPane.setLayoutX(150);
        gridPane.setHgap(40);
        gridPane.setVgap(15);
        AnchorPane anchorPane = new AnchorPane();//creating the anchorpane
        anchorPane.setStyle("-fx-background-color:#66FFFF"); //i am fixing the gridpane inside the anchorpane
        anchorPane.getChildren().addAll(gridPane,close, heading,viewQueue,boardedCus);//calling out all the labels
        Scene scene = new Scene(anchorPane, 1100, 900);//and buttons that we wnat in the GUI
        stage.setTitle(" ---Colombo Station Queue--- ");//giving the title for the stage
        stage.setScene(scene);//creating a scene for GUI
        stage.showAndWait();//calling out the stage

    }

    private static void deleteCustomerFromTrainQueue() {
        System.out.println("\n------------------Enter the customer full name to delete from the Passenger queue & waiting room------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("\n");
        System.out.print("Enter the Full Name : ");
        String names = sc.nextLine();
        int seat = -1;//declaring the variable check the person in the queueArray ,if searching algo if not found it returns -1
        for (int x = 0;x<SEATING_CAPACITY;x++) {//
            if (PassengerQueue.queueArray[x] != null && PassengerQueue.queueArray[x].getFirstName().equalsIgnoreCase(names)) {
                seat=x;//if the seat found in the queueArray array we are using the getter method in the passenger class to get the customer names
                System.out.println("\n");
                System.out.println("  "+names + "   Above  name found in the Passenger's Queue");
            }
        }
        if  (seat == -1) {//if the seat variable gonna print if not found
            System.out.println("\n");
            System.out.println("*********Please enter the valid name from the Passenger Queue*********");
        } else {
            confirmation:
            while (true){//validating the codition to check if you surely want to delete the person
                System.out.print("\nDo you really want to delete the above customer (YES/NO) : ");
                String Option = sc.next().toUpperCase().trim();
                switch (Option){
                    case "YES":
                        PassengerQueue.queueArray[seat] = null;//if you want to delete the passenger surely his/her place in the array will be replaced with a null
                        System.out.println("\n");
                        System.out.println("  "+names + "  this customer have been deleted from the Waiting Room successfully !!!!!");
                        break confirmation;
                    case "NO"://if it's no for deletion from the user it'll break and show you the menu again
                        System.out.println("\n");
                        System.out.println("*********Cancelled by user*********");
                        break confirmation;
                    default:
                        System.out.println("\n");
                        System.out.println("*********Invalid input*********");
                }
            }
        }
        menu();
    }


    private static void storeTrainQueue() {
        File file;
        if (stationId == "1") {//if user input is 1 in select route it'll save here
            file = new File("saveFileCMB.txt");//Creating a file object to manipulate file outside jvm
        }else {//other than that it'll save here
            file = new File("saveFileBDL.txt");//Creating a file object to manipulate file outside jvm
        }
        FileWriter fileWriter;// declaring file writer
        PrintWriter printWriter = null; // declaring print writer
        StringBuilder sb = new StringBuilder();

        for (Passenger passenger : PassengerQueue.queueArray) {//creating a for loop to iterate the passengers and passenger objets
            if (passenger != null) {//it's there to remove null posistions in the array
                sb.append("\n").append(passenger.getName()).append(" ").append(passenger.getSeat());
            }
        }

        try {
            fileWriter = new FileWriter(file); //opens the file to write and creates an environment to write
            printWriter = new PrintWriter(fileWriter, true);//uses the environment created by file writer to write
            printWriter.write(sb.toString());//Writing the string generated in previous for loop
        }
        catch(FileNotFoundException e ){//if the file not found it'll be thrown-parent exception
            System.out.println("File not found");
        } catch (IOException e) {//
            System.out.println("Cannot save - error");//Throws if the file is not accessible
        } finally {
            if (printWriter != null) {
                printWriter.close();//Closing the writer.
            }
        }
        System.out.println("\n*********The data successfully stored to the text file*********\n");
        menu();
    }


    private static void loadTrainQueue() {
        File file;
        if (stationId == "1") {//if user input in select route is 1 it'll load data here
            file = new File("saveFileCMB.txt");//Creating a file object to manipulate file outside jvm
        }else {//otherwise it'll load data from here
            file = new File("saveFileBDL.txt");//Creating a file object to manipulate file outside jvm
        }
        Scanner scanner;

        try {//to avoid run time
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                Passenger passenger = new Passenger();//creating a object to store the data using setter methods
                String names =scanner.next();
                String names2 =scanner.next();
                int seatNum = scanner.nextInt();
                passenger.setFirstName(names+" "+names2);//assigning two strings to the passenger object
                passenger.setSeat(seatNum);
                PassengerQueue.addPassenger(seatNum,passenger);
            }
        } catch (FileNotFoundException e) {//avoiding the run time by try and catch
            System.out.println("File is not found");
        }
        System.out.println("\n*********The data successfully loaded from the text file*********\n");
        menu();
    }

    private static void runSimulation() {

     try {
         int seconds = 0;//total time
         int minTime = 0;//minimum time spent in queue/ smallest time generated
         for (Passenger passenger : PassengerQueue.queueArray) {//checking each Passenger in queueArray array, just like "for i in passenger" in python
             if (passenger != null) {
                 int dice1 = ThreadLocalRandom.current().nextInt(3, 19);//random dice generation
                 int diceTotal = (dice1)*3;//total for a single iteration
                 seconds += diceTotal;//seconds represents the sum of generated diceTotal
                 if (minTime == 0) {//this block runs only in the start
                     minTime = seconds;//this block sets the minimum value
                 }
                 passenger.setSecondsInQueue(seconds);//setting the wait time to each passenger
             }
         }

            //creating the GUI
            Stage stage = new Stage();
            Label heading = new Label("Boarded Passenger Full Name, Seat Number & Waiting Time");
            heading.setLayoutX(80);
            heading.setLayoutY(30);
            heading.setStyle("-fx-font-size:30px;");
            Button close = new Button("Back To Menu");//creating the done button
            close.setLayoutX(520);// posistionin the button in the gridpane using cartesean
            close.setLayoutY(720);
            close.setStyle("-fx-font-size:15px");
            Button rbtn;
            Label queueL = new Label("Maximunm length of the queue is: "+PassengerQueue.getLength()+" People are in the queue");//getting the length of queue
            queueL.setLayoutX(100);
            queueL.setLayoutY(660);
            queueL.setStyle("-fx-font-size:15px;");
            Label averageWaitTime = new Label("The average waiting time of the queue is: "+(minTime+seconds)/PassengerQueue.getLength()+" seconds");//get average time
            averageWaitTime.setLayoutX(100);
            averageWaitTime.setLayoutY(690);
            averageWaitTime.setStyle("-fx-font-size:15px;");
            Label minimumWaitingTime = new Label("The minimum waiting time taken: "+minTime+" seconds");//get minimum time
            minimumWaitingTime.setLayoutX(100);
            minimumWaitingTime.setLayoutY(720);
            minimumWaitingTime.setStyle("-fx-font-size:15px;");
            Label maxmumWaitingTime = new Label("The maximum waiting time taken: "+seconds+" seconds");//get the maximum time
            maxmumWaitingTime.setLayoutX(100);
            maxmumWaitingTime.setLayoutY(750);
            maxmumWaitingTime.setStyle("-fx-font-size:15px;");

            StringBuilder sb = new StringBuilder();//StringBuilder is used to add strings/append strings

            GridPane gridPane = new GridPane();//creating the gridpane
            int x = 0;//Declaring the variables to get the buttons printed
            int y = 0;

            for (int i = 0; i < PassengerQueue.queueArray.length; i++) {

                if (PassengerQueue.queueArray[i] != null) {
                    String guiname = PassengerQueue.queueArray[i].getName();
                    int seatnum = PassengerQueue.queueArray[i].getSeat() + 1;
                    int time = PassengerQueue.queueArray[i].getSecondsInQueue();
                    rbtn = new Button(" " + guiname + " : " + seatnum+" : "+time+" sec");
                    sb.append(rbtn.getText()).append("\n");//append adds strings in argument to a collective string line
                    rbtn.setStyle("-fx-font-size: 1em;-fx-text-fill:green;-fx-border-color:black");
                    rbtn.setMinWidth(130);
                    rbtn.setId(String.valueOf(i));
                    rbtn.setPadding(new Insets(8));
                    gridPane.add(rbtn, x, y);

                    x++;
                    if(x==5){
                        y++;
                        x=0;
                    }

                }
            }

        stage.setOnCloseRequest(e ->
                stage.close()
        );

        close.setOnAction(e ->
                stage.close()
        );

        gridPane.setLayoutY(100);
        gridPane.setLayoutX(150);
        gridPane.setHgap(8);
        gridPane.setVgap(7);
        AnchorPane anchorPane = new AnchorPane();//creating the anchorpane
        anchorPane.setStyle("-fx-background-color:#66FFFF"); //i am fixing the gridpane inside the anchorpane
        anchorPane.getChildren().addAll(gridPane,close, heading,queueL,averageWaitTime,maxmumWaitingTime,minimumWaitingTime);//calling out all the labels
        Scene scene = new Scene(anchorPane, 1200, 900);//and buttons that we wnat in the GUI
        stage.setTitle("----Simulation Report Of Colombo Station----");//giving the title for the stage
        stage.setScene(scene);//creating a scene for GUI
        stage.showAndWait();//calling out the stage
         for(int i=0;i<42;i++){//the array details before i get them deleted i am storing them in the newly created arraylist
           if(PassengerQueue.queueArray[i]!=null) {
               boardedPassengers.add(PassengerQueue.queueArray[i]);
           }
         }
        PassengerQueue.queueArray = new Passenger[42];//a new passenger array is created in the variable of the old array, which gets deleted.
         //storing method is called, sb.toString represents the string built inside the loop above. The string built inside the loop will be saved in a file
         storeSimulationInTextFile(sb.toString());//calling the method

     }catch (ArithmeticException e){
         System.out.println("\n*********There Is No Passengers In The Queue To Board To Denuwara Manike Train*********");
     }
    }

    private static void storeSimulationInTextFile(String string){//storing the simulation data in a text file

        File file;
        if (stationId == "1") {//if user input is 1 in select route it'll save here
            file = new File("saveFileCMB simulation.txt");//Creating a file object to manipulate file outside jvm
        }else {//other than that it'll save here
            file = new File("saveFileBDL simulation.txt");//Creating a file object to manipulate file outside jvm
        }
        FileWriter fileWriter;
        PrintWriter printWriter = null;//declaring the print writer

        try {//using the try and catch to escape the runtime error
            fileWriter = new FileWriter(file);
            printWriter = new PrintWriter(fileWriter,true);
            printWriter.print(string);
        } catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        catch (IOException e) {
            System.out.println("Cannot save report - error");
        }finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    private static void boardedPass(){//getting the details of boarded passengers
        Stage stage = new Stage();
        Label heading = new Label("The Passengers Who Are Boarded To Denuwara Manike Train");
        heading.setLayoutX(130);
        heading.setLayoutY(25);
        heading.setStyle("-fx-font-size:30px;");
        Button close = new Button("Back To Menu");//creating the done button
        close.setLayoutX(520);// posistionin the button in the gridpane using cartesean
        close.setLayoutY(730);
        close.setStyle("-fx-font-size:15px");
        Button nbtn;
        Button returns = new Button("Back View Mode Selection");
        returns.setLayoutX(520);// posistionin the button in the gridpane using cartesean
        returns.setLayoutY(780);
        returns.setStyle("-fx-font-size:15px");

        GridPane gridPane = new GridPane();
        int x = 0;
        int y = 0;

        for (int i = 0; i <42; i++) {//creating the button 42 times
            nbtn = new Button(" Not Boarded - " + (i+1));
            nbtn.setMinWidth(130);
            nbtn.setStyle("-fx-font-size: 1em;-fx-text-fill:red;-fx-border-color:black;");
            nbtn.setId(String.valueOf(i));
            nbtn.setPadding(new Insets(12));

            for (Passenger boardedPassenger : boardedPassengers) {//getting the passenger details who are boarded and not in the queue or waiting room
                if (boardedPassenger != null && boardedPassenger.getSeat() ==i) {//getting the passenger seat no. to validate if he is in the arraylist or not
                    String guiname = boardedPassenger.getName();
                    int seatnum = boardedPassenger.getSeat() + 1;
                    nbtn.setText(" " + guiname + "-" + seatnum);
                    nbtn.setStyle("-fx-font-size: 1em;-fx-text-fill:green;-fx-border-color:black;");
                    nbtn.setMinWidth(170);
                }

            }
            gridPane.add(nbtn, x, y);
            x++;
            if (x == 4) {
                y++;
                x = 0;
            }
        }

        stage.setOnCloseRequest(e ->
                stage.close()
        );

        close.setOnAction(e ->
                stage.close()
        );

        returns.setOnAction(e -> {
            viewTrainQueue();
            stage.close();
        });

        gridPane.setLayoutY(100);
        gridPane.setLayoutX(150);
        gridPane.setHgap(40);
        gridPane.setVgap(15);
        AnchorPane anchorPane = new AnchorPane();//creating the anchorpane
        anchorPane.setStyle("-fx-background-color:#66FFFF"); //i am fixing the gridpane inside the anchorpane
        anchorPane.getChildren().addAll(gridPane,close, heading,returns);//calling out all the labels
        Scene scene = new Scene(anchorPane, 1100, 900);//and buttons that we wnat in the GUI
        stage.setTitle(" ---Colombo Station Queue--- ");//giving the title for the stage
        stage.setScene(scene);//creating a scene for GUI
        stage.showAndWait();//calling out the stage
    }

    private static void passengerQue() {
        Stage stage = new Stage();
        Label heading = new Label("The Passengers In the Passengers queue");
        heading.setLayoutX(130);
        heading.setLayoutY(25);
        heading.setStyle("-fx-font-size:30px;");
        Button close = new Button("Back To Menu");//creating the done button
        close.setLayoutX(520);// posistionin the button in the gridpane using cartesean
        close.setLayoutY(730);
        close.setStyle("-fx-font-size:15px");
        Button nbtn;
        Button returns = new Button("Back View Mode Selection");
        returns.setLayoutX(520);// posistionin the button in the gridpane using cartesean
        returns.setLayoutY(780);
        returns.setStyle("-fx-font-size:15px");

        GridPane gridPane = new GridPane();
        int x = 0;
        int y = 0;

        for (int i = 0; i < SEATING_CAPACITY; i++) {//creating the 42 buttons in the gui
            if (PassengerQueue.queueArray[i] != null) {//giving a validation to not to read the null area in the array
                String guiname = PassengerQueue.queueArray[i].getName();//getting the name & the seat of the passenger in the array
                int seatnum = PassengerQueue.queueArray[i].getSeat() + 1;//and assigning his/her to a specific button
                nbtn = new Button(" " + guiname + "-" + seatnum);
                nbtn.setStyle("-fx-font-size: 1em;-fx-text-fill:green;-fx-border-color:black;");
                nbtn.setMinWidth(170);

            } else {//if there is no passenger found for the seat it'll print empty in the specific button
                nbtn = new Button(" Not Arrived - " + (i + 1));
                nbtn.setMinWidth(130);
                nbtn.setStyle("-fx-font-size: 1em;-fx-text-fill:red;-fx-border-color:black;");

            }
            nbtn.setId(String.valueOf(i));
            nbtn.setPadding(new Insets(12));
            gridPane.add(nbtn, x, y);
            x++;
            if (x == 4) {
                y++;
                x = 0;
            }

        }
        stage.setOnCloseRequest(e ->
                stage.close()
        );

        close.setOnAction(e ->
                stage.close()
        );

        returns.setOnAction(e -> {
            viewTrainQueue();
            stage.close();
        });


        gridPane.setLayoutY(100);
        gridPane.setLayoutX(150);
        gridPane.setHgap(40);
        gridPane.setVgap(15);
        AnchorPane anchorPane = new AnchorPane();//creating the anchorpane
        anchorPane.setStyle("-fx-background-color:#66FFFF"); //i am fixing the gridpane inside the anchorpane
        anchorPane.getChildren().addAll(gridPane,close, heading,returns);//calling out all the labels
        Scene scene = new Scene(anchorPane, 1100, 900);//and buttons that we wnat in the GUI
        stage.setTitle(" ---Colombo Station Queue--- ");//giving the title for the stage
        stage.setScene(scene);//creating a scene for GUI
        stage.showAndWait();//calling out the stage
    }
}




