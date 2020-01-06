package put.poznan.pl.drsa_sets_assignments;

public class NetFlowScoreResult {
    public int strength;
    public int weakness;
    public int quality;
    public String variant;

    public NetFlowScoreResult(){}

    public NetFlowScoreResult(String variant){
        this.variant = variant;
    }

    public NetFlowScoreResult(int strength, int weakness){
        this.strength = strength;
        this.weakness = weakness;
        calculateQuality();
    }

    public NetFlowScoreResult(String variant, int strength, int weakness){
        this(strength, weakness);
        this.variant = variant;
    }

    public void calculateQuality(){ quality = strength - weakness; }

    public void printScore(){
        System.out.println(variant + ": Strength: " + strength +
                " Weakness: "+ weakness + " Quality: " + quality );
    }
}
