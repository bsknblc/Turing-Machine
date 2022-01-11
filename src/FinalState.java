class FinalState implements States {
    String name;
    int number;

    public FinalState (String name, int numberOfStates){
        this.name=name;
        if(name.substring(1).equals("A")){
            this.number=numberOfStates-2;
        }else if(name.substring(1).equals("R")){
            this.number=numberOfStates-1;
        }else{
            this.number=Integer.parseInt(name.substring(1));
        }
    }

    String getName(){
        return this.name;
    }

    void setName(String name){
        this.name=name;
        if(name.substring(1).equals("A")){
            this.number=-1;
        }else if(name.substring(1).equals("R")){
            this.number=-2;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
