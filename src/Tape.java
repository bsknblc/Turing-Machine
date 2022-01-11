import java.util.ArrayList;
import java.util.List;

public class Tape {
    List<String> tape;

    public Tape(){

    }

    public List<String> getTape() {
        return tape;
    }

    public void setTape(List<String> tape) {
        this.tape = tape;
    }

    public List<String> setValues(int index, String value){
        this.tape.set(index, value);
        return tape;
    }

    public int tapeLength(){
        return  tape.size();
    }

    public String get(int index){
        return tape.get(index);
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i< tape.size(); i++){
            result += tape.get(i);
        }
        return result;
    }
}
