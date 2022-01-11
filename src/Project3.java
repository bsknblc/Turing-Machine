import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Project3 {

    public static int numberOfVariablesInInput;
    public static int numberOfVariablesInTape;
    public static int numberOfStates;
    public static List<State> states;
    public static StartState startState;
    public static FinalState acceptState;
    public static FinalState rejectState;
    public static String blankSymbol;
    public static List<String> tapeAlphabet;
    public static List<String> inputAlphabet;
    public static List<String> transitionFunction;
    public static Matrix transitionMatrix;
    public static List<String> stringsToBeDetected;

    public static void main(String[] args) {

        //This part takes all the data in the file with the format needed.

        String fileName = "input_file.txt";
        List<String> data = new ArrayList<String>();

        try {
            File file = new File(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                //data contains all characters in the file
                data.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("output_file.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



        numberOfVariablesInInput = Integer.parseInt(data.get(0).substring(0, 1));

        numberOfVariablesInTape = Integer.parseInt(data.get(1).substring(0, 1))+1;

        numberOfStates = Integer.parseInt(data.get(2).substring(0, 1));

        states = new ArrayList<State>();
        for (String state : data.get(3).split("\\s+")) {
            states.add(new State(state, numberOfStates));
        }

        startState = new StartState(data.get(4));

        acceptState = new FinalState(data.get(5), numberOfStates);

        rejectState = new FinalState(data.get(6), numberOfStates);

        blankSymbol = data.get(7);

        tapeAlphabet = new ArrayList<String>();
        for (String symbol : data.get(8).split("\\s+")) {
            tapeAlphabet.add(symbol);
        }

        inputAlphabet = new ArrayList<String>();
        for (String symbol : data.get(9).split("\\s+")) {
            inputAlphabet.add(symbol);
        }

        transitionFunction = new ArrayList<>();
        int index = 10;
        while (data.get(index).contains("q")){
            for (String element : data.get(index).split("\\s+")) {
                transitionFunction.add(element);
            }
            index++;
        }

        transitionMatrix = new Matrix(numberOfStates, numberOfVariablesInTape);

        for(int i = 0; i<index-10; i++){
            State fromState = new State(transitionFunction.get(i*5+0), numberOfStates);
            String readSymbol = transitionFunction.get(i*5+1);
            String writeSymbol = transitionFunction.get(i*5+2);
            String moveHead = transitionFunction.get(i*5+3);
            State toState = new State(transitionFunction.get(i*5+4), numberOfStates);

            System.out.println(fromState);
            System.out.println(readSymbol);
            System.out.println(writeSymbol);
            System.out.println(moveHead);
            System.out.println(toState);

            int readSymbolIndex = 0;
            int k = 0;
            for (String tapeElement : tapeAlphabet) {
                if (readSymbol.equals(tapeElement)) {
                    readSymbolIndex = k;
                }
                k++;
            }

            int column = readSymbolIndex;

            transitionMatrix.setMatrixValues(fromState.getNumber()-1, column, toState, moveHead, writeSymbol);

            System.out.println(transitionMatrix.getMatrixValues(fromState.getNumber()-1, column));
            for (String symbol : transitionMatrix.getMatrixValues(fromState.getNumber()-1, column).split("\\s+")) {
                System.out.println(symbol);
            }

        }

        System.out.println(transitionMatrix);

        stringsToBeDetected = new ArrayList<String>();
        for (int i = index; i < data.size(); i++) {
            stringsToBeDetected.add(data.get(i));
        }
        System.out.println(stringsToBeDetected);

        /*
        System.out.println(numberOfVariablesInInput);
        System.out.println(numberOfVariablesInStack);
        System.out.println(numberOfGoalStates);
        System.out.println(numberOfStates);
        System.out.println(states);
        System.out.println(startState);
        System.out.println(goalStates);
        System.out.println(stackAlphabet);
        System.out.println(initialStackSymbol);
        System.out.println(inputAlphabet);
        System.out.println(transitionFunction);
        System.out.println(stringsToBeDetected);
         */

        //This part simulates the behaviour of PDA.
        List<List<State>> routeTakens = new ArrayList<>();
        for(String stringToBeDetected : stringsToBeDetected){
            Tape tape = new Tape();
            List<String> tapeList = new ArrayList<>();
            for(int i = 0; i<stringToBeDetected.length(); i++){
                String symbol = ""+stringToBeDetected.charAt(i);
                tapeList.add(symbol);
            }
            tapeList.add("b");
            tape.setTape(tapeList);
            List<State> routeTaken = new ArrayList<>();
            routeTaken.add(new State(startState.getName(), numberOfStates));
            routeTakens.add(TM(routeTaken, tapeList));
        }

        String output = "";
        for (List<State> route : routeTakens){
            if(route!=null) {
                String isAccepted = "Rejected!";
                if (!route.get(route.size() - 1).getName().equals("d1")) {
                    if (acceptState.getName().equals(route.get(route.size() - 1).getName())) {
                        isAccepted = "Accepted!";
                    }
                } else {
                    route.remove(route.size() - 1);
                }
                output += route + "\n";
                output += isAccepted + "\n";
            }
        }


        try {
            FileWriter myWriter = new FileWriter("output_file.txt");
            myWriter.write(output);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static List<State> TM(List<State> routeTaken,  List<String> tape){
        int currentIndex = 0;
        boolean stop = true;
        while(stop){
                State currentState = routeTaken.get(routeTaken.size()-1);
                if(currentIndex==tape.size()){
                    tape.add("b");
                }
                String input = tape.get(currentIndex);
                int readSymbolIndex = 0;
                int k = 0;
                for (String inputElement : tapeAlphabet) {
                    if (input.equals(inputElement)) {
                        readSymbolIndex = k;
                    }
                    k++;
                }

                int column = readSymbolIndex;

                if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, column)!=null) {
                    String value = transitionMatrix.getMatrixValues(currentState.getNumber() - 1, column);
                    State toState = new State(value.split("\\s+")[0], numberOfStates);
                    String moveValue = value.split("\\s+")[1];
                    String writeSymbol = value.split("\\s+")[2];

                    tape.set(currentIndex, writeSymbol);
                    if(moveValue.equals("R")){
                    currentIndex += 1;}
                    else{currentIndex -= 1;}
                    routeTaken.add(toState);
                }else{
                    return routeTaken;
                }
        }
        return routeTaken;
    }

}
