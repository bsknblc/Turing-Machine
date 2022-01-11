class State implements States {
    String name;
    int number;

    public State (String name, int numberOfStates){
        this.name=name;
        if(name.substring(1).equals("A")){
            this.number=numberOfStates-1;
        }else if(name.substring(1).equals("R")){
            this.number=numberOfStates;
        }else{
            this.number=Integer.parseInt(name.substring(1));
        }
    }

    String getName(){
        return this.name;
    }

    void setName(String name){
        this.name=name;
        this.number=Integer.parseInt(name.substring(1));
    }

    int getNumber(){return this.number;}

    @Override
    public String toString() {
        return name+ " ";
    }
}
