import greenfoot.*;
import java.util.*;

/**
 * MyDodo klasse die verschillende gedragingen en functies van de Dodo implementeert.
 * Deze klasse behandelt diverse opdrachten van week 1 t/m 7 met aandacht voor:
 * - CT skills: decompositie, generiek algoritme, OO-principes
 * - Programming skills: logica, flow control, foutopsporing
 * - Goed gestructureerd en testbaar
 * 
 * @author
 * @version Week 7
 */
public class MyDodo extends Dodo {

    /** Aantal stappen gezet door deze Dodo (voor week 7 race) */
    private int myStepsTaken = 0;
    
    /** Score van deze Dodo, opgebouwd door eieren te verzamelen */
    private int myScore = 0;
    
    /** Lijst met SurpriseEgg-objecten voor week 6 */
    private List<SurpriseEgg> surpriseEggs = new ArrayList<>();

    /**
     * Constructor die de Dodo initialiseert met snelheid 5
     */
    public MyDodo() {
        super(5);
    }

    // === Week 1 – Basisacties ===

    /**
     * Controleert of de Dodo op een nest staat zonder dat er al een ei ligt,
     * en dus een ei kan leggen.
     * @return true als ei gelegd kan worden, anders false
     */
    public boolean canLayEgg() {
        return onNest() && !onEgg();
    }

    /**
     * Laat de Dodo een 180 graden draai maken (omkeren)
     */
    public void turn180() {
        turnLeft(); turnLeft();
    }

    /**
     * Laat de Dodo een sprong maken, twee stappen vooruit
     */
    public void jump() {
        move(1); move(1);
    }

    /**
     * Laat de Dodo lopen tot aan de wereldrand, en print bij elke stap
     * de huidige X- en Y-coördinaten.
     */
    public void walkToWorldEdgePrintingCoordinates() {
        while (canMove()) {
            System.out.println("X: " + getX() + " Y: " + getY());
            move(1);
        }
        System.out.println("X: " + getX() + " Y: " + getY());
    }

    /**
     * Controleert of de Dodo vooruit kan bewegen (geen hek voor zich)
     * @return true als er geen hek voor staat, anders false
     */
    public boolean canMove() {
        return !fenceAhead();
    }

    /**
     * Laat de Dodo over een hek klimmen: een reeks draaien en stappen
     */
    public void climbOverFence() {
        turnLeft(); move(1); turnRight(); move(1); turnRight(); move(1); turnLeft();
    }

    /**
     * Controleert of er graan voor de Dodo ligt, zonder zelf te bewegen
     * @return true als er graan ligt, anders false
     */
    public boolean grainAhead() {
        move(1);
        boolean result = onGrain();
        turn180(); move(1); turn180();
        return result;
    }

    // === Week 2 – Nest, fences, graan ===

    /**
     * Laat de Dodo lopen tot hij op een ei staat
     */
    public void gotoEgg() {
        while (!onEgg()) move(1);
    }

    /**
     * Laat de Dodo lopen tot aan de wereldrand
     */
    public void walkToWorldEdge() {
        while (canMove()) move(1);
    }

    /**
     * Laat de Dodo teruglopen naar het begin van de rij en deze kant op blijven kijken
     */
    public void goBackToStartOfRowAndFaceBack() {
        turn180(); walkToWorldEdge(); turn180();
    }

    /**
     * Laat de Dodo lopen tot aan de wereldrand, en klimmen over hekken waar nodig
     */
    public void walkToWorldEdgeClimbingOverFences() {
        while (true) {
            if (canMove()) move(1);
            else if (fenceAhead()) climbOverFence();
            else break;
        }
    }

    /**
     * Laat de Dodo graan oppakken en print de coördinaten telkens als dat gebeurt
     */
    public void pickUpGrainsAndPrintCoordinates() {
        while (true) {
            if (onGrain()) {
                pickUpGrain();
                System.out.println("X: " + getX() + " Y: " + getY());
            }
            if (canMove()) move(1); else break;
        }
        // Check ook de laatste positie op graan
        if (onGrain()) {
            pickUpGrain();
            System.out.println("X: " + getX() + " Y: " + getY());
        }
    }

    /**
     * Laat de Dodo één cel achteruit stappen
     */
    public void stepOneCellBackwards() {
        turn180(); move(1); turn180();
    }

    /**
     * Laat de Dodo een ei leggen op elk nest waar nog geen ei ligt, tijdens het lopen
     */
    public void noDoubleEggs() {
        while (canMove()) {
            if (onNest() && !onEgg()) layEgg();
            move(1);
        }
    }

    /**
     * Laat de Dodo naar het nest lopen, waarbij hij hekken ontwijkt door erover te klimmen,
     * en vervolgens een ei leggen
     */
    public void goToNestAvoidFences() {
        while (!onNest()) {
            if (fenceAhead()) climbOverFence();
            else move(1);
        }
        layEgg();
    }

    /**
     * Laat de Dodo rondlopen binnen een omheind gebied, telkens rechtsaf bij een hek,
     * tot hij op een ei staat
     */
    public void walkAroundFencedArea() {
        while (!onEgg()) {
            if (canMove()) move(1);
            else turnRight();
        }
    }

    // === Week 3 – Navigatie en locatie ===

    /**
     * Laat de Dodo altijd naar het oosten kijken
     */
    public void faceEast() {
        while (getDirection() != EAST) turnLeft();
    }

    /**
     * Laat de Dodo lopen naar de gegeven (x, y) locatie.
     * De Dodo beweegt eerst horizontaal, dan verticaal.
     * @param x doel-x-coördinaat
     * @param y doel-y-coördinaat
     */
    public void goToLocation(int x, int y) {
        while (getX() != x || getY() != y) {
            if (getX() < x) setDirection(EAST);
            else if (getX() > x) setDirection(WEST);
            else if (getY() < y) setDirection(SOUTH);
            else setDirection(NORTH);
            move(1);
        }
    }

    /**
     * Controleert of de Dodo zich op de gegeven (x, y) locatie bevindt
     * @param x coördinaat X
     * @param y coördinaat Y
     * @return true als Dodo op (x, y) staat, anders false
     */
    public boolean locationReached(int x, int y) {
        return getX() == x && getY() == y;
    }

    /**
     * Controleert of de gegeven coördinaten geldig zijn binnen de wereld
     * @param x coördinaat X
     * @param y coördinaat Y
     * @return true als geldig, anders false en toont foutmelding
     */
    public boolean validCoordinates(int x, int y) {
        if (x >= 0 && x < getWorld().getWidth() && y >= 0 && y < getWorld().getHeight()) {
            return true;
        } else {
            showError("Invalid coordinates");
            return false;
        }
    }

    /**
     * Telt het aantal eieren in de huidige rij en keert terug naar de oorspronkelijke positie
     * @return aantal eieren in de rij
     */
    public int countEggsInRow() {
        int count = 0;
        int startX = getX();
        while (canMove()) {
            if (onEgg()) count++;
            move(1);
        }
        if (onEgg()) count++;
        goToLocation(startX, getY());
        setDirection(EAST);
        return count;
    }

    // === Week 4 – Patronen, tellen, gemiddelde ===

    /**
     * Laat de Dodo een spoor van n eieren leggen
     * @param n aantal eieren om te leggen
     */
    public void layTrailOfEggs(int n) {
        if (n <= 0) {
            showError("Aantal eieren moet positief zijn.");
            return;
        }
        for (int i = 0; i < n; i++) {
            layEgg();
            if (canMove()) move(1);
            else break;
        }
    }

    /**
     * Telt het totaal aantal eieren in de wereld
     * @return totaal aantal eieren
     */
    public int countAllEggs() {
        return getWorld().getObjects(Egg.class).size();
    }

    /**
     * Vindt de rij met de meeste eieren en print deze
     * @return rij-index met meeste eieren
     */
    public int findRowWithMostEggs() {
        int max = 0;
        int maxRow = 0;
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            int eggs = countEggsInRow();
            if (eggs > max) {
                max = eggs;
                maxRow = y;
            }
        }
        System.out.println("Meeste eieren in rij: " + maxRow);
        return maxRow;
    }

    /**
     * Berekent en print het gemiddelde aantal eieren per rij
     */
    public void calculateAverageEggsPerRow() {
        int totalEggs = 0;
        int rows = getWorld().getHeight();
        for (int y = 0; y < rows; y++) {
            goToLocation(0, y);
            totalEggs += countEggsInRow();
        }
        double avg = (double) totalEggs / rows;
        System.out.println("Gemiddeld per rij: " + avg);
    }

    // === Week 5 – Pariteitsbit-algoritme ===

    /**
     * Telt het aantal eieren in een specifieke kolom
     * @param x kolomindex
     * @return aantal eieren in kolom x
     */
    public int countEggsInCol(int x) {
        int count = 0;
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(x, y);
            if (onEgg()) count++;
        }
        return count;
    }

    /**
     * Zoekt naar de rij met een oneven aantal eieren (incorrecte pariteit)
     * @return rijindex van incorrecte rij, of -1 als geen fout
     */
    public int getIncorrectRow() {
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            if (countEggsInRow() % 2 != 0) return y;
        }
        return -1;
    }

    /**
     * Zoekt naar de kolom met een oneven aantal eieren (incorrecte pariteit)
     * @return kolomindex van incorrecte kolom, of -1 als geen fout
     */
    public int getIncorrectCol() {
        for (int x = 0; x < getWorld().getWidth(); x++) {
            if (countEggsInCol(x) % 2 != 0) return x;
        }
        return -1;
    }

    /**
     * Corrigeert de incorrecte pariteit door op de kruising van foutieve rij en kolom
     * een ei te leggen of te verwijderen
     */
    public void fixIncorrectBit() {
        int row = getIncorrectRow();
        int col = getIncorrectCol();
        if (row != -1 && col != -1) {
            goToLocation(col, row);
            if (onEgg()) pickUpEgg();
            else layEgg();
        }
    }

    // === Week 6 – SurpriseEggs en lijsten ===
    /**
     * Print de waarde en locatie van alle SurpriseEggs in de lijst
     */
    public void printEggData() {
        for (SurpriseEgg egg : surpriseEggs) {
            System.out.println("Waarde: " + egg.getValue() + ", X: " + egg.getX() + ", Y: " + egg.getY());
        }
    }

    /**
     * Print de gemiddelde waarde van alle SurpriseEggs in de lijst
     */
    public void printAverageValue() {
        int total = 0;
        for (SurpriseEgg egg : surpriseEggs) total += egg.getValue();
        System.out.println("Gemiddelde waarde: " + (double) total / surpriseEggs.size());
    }

    /**
     * Zoekt en retourneert het SurpriseEgg met de hoogste waarde
     * @return het SurpriseEgg met de hoogste waarde, of null als lijst leeg is
     */
    public Egg findMostValuableEgg() {
        Egg best = null;
        int max = 0;
        for (SurpriseEgg e : surpriseEggs) {
            if (e.getValue() > max) {
                max = e.getValue(); best = e;
            }
        }
        return best;
    }

    // === Week 7 – Dodo Race / Slim zoeken ===

    /**
     * Laat de Dodo willekeurig bewegen tot het maximum aantal stappen bereikt is
     * @implNote Beweegt in willekeurige richting indien mogelijk
     */
    public void moveRandomly() {
        while (myStepsTaken < Mauritius.MAXSTEPS) {
            int dir = randomDirection();
            setDirection(dir);
            if (canMove()) {
                move(1);
                myStepsTaken++;
                updateScore();
            }
        }
    }

    /**
     * Update de score van de Dodo bij het verzamelen van eieren,
     * en update de score op het scorebord in de wereld
     */
    public void updateScore() {
        if (onEgg()) {
            Egg egg = (Egg)getOneIntersectingObject(Egg.class);
            if (egg instanceof SurpriseEgg) myScore += ((SurpriseEgg)egg).getValue();
            else if (egg instanceof GoldenEgg) myScore += 5;
            else myScore += 1;
            pickUpEgg();
        }
    }

    /**
     * Slimme zoekmethode: zoekt het dichtstbijzijnde ei en loopt er naartoe,
     * herhaalt totdat maximaal aantal stappen is bereikt of geen eieren meer zijn
     */
    public void smartEggHunter() {
        while (myStepsTaken < Mauritius.MAXSTEPS) {
            if (onEgg()) updateScore();
            List<Egg> eggs = getWorld().getObjects(Egg.class);
            Egg closest = findClosestEgg(eggs);
            if (closest == null) break;
            goToLocation(closest.getX(), closest.getY());
        }
    }

    /**
     * Bepaalt het dichtsbijzijnde ei uit een lijst van eieren,
     * gemeten via Manhattan distance (x en y afstand bij elkaar opgeteld)
     * @param eggs lijst van eieren
     * @return het dichtstbijzijnde ei, of null als lijst leeg is
     */
    public Egg findClosestEgg(List<Egg> eggs) {
        Egg closest = null;
        int minDist = Integer.MAX_VALUE;
        for (Egg egg : eggs) {
            int dist = Math.abs(getX() - egg.getX()) + Math.abs(getY() - egg.getY());
            if (dist < minDist) {
                minDist = dist;
                closest = egg;
            }
        }
        return closest;
    }
}

