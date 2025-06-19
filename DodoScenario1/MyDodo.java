import greenfoot.*;
import java.util.*;

/**
 * MyDodo class for Mimi's Wereld
 * 
 * @author JouwNaam (of Sjaak Smetsers & Renske Smetsers-Weeda)
 * @version 1.0 - aangepast voor opdrachten week 1-5 en 7
 */
public class MyDodo extends Dodo {

    /* ATTRIBUTES */
    private int myStepsTaken = 0;
    private int myScore = 0;

    /**
     * Constructor: maakt een MyDodo aan met snelheid 5.
     */
    public MyDodo() {
        super(5);
    }

    /**
     * Controleert of Dodo een ei kan leggen, 
     * namelijk als ze op een nest staat en niet op een ei.
     * 
     * @return boolean true als ei gelegd kan worden, anders false
     */
    public boolean canLayEgg() {
        return onNest() && !onEgg();
    }

    /**
     * Draait Dodo 180 graden naar links.
     */
    public void turn180() {
        turnLeft();
        turnLeft();
    }

    /**
     * Springt 2 stappen vooruit.
     */
    public void jump() {
        move(1);
        move(1);
    }

    /**
     * Controleert of Dodo vooruit kan bewegen (geen hek).
     * 
     * @return boolean true als kan bewegen, anders false
     */
    public boolean canMove() {
        return !fenceAhead();
    }

    /**
     * Beklimt een hek volgens het patroon: linksom, stap, rechtsom, stap, rechtsom, stap, linksom.
     */
    public void climbOverFence() {
        turnLeft();
        move(1);
        turnRight();
        move(1);
        turnRight();
        move(1);
        turnLeft();
    }

    /**
     * Loopt door tot de wereldrand of tot er niet meer bewogen kan worden.
     */
    public void walkToWorldEdge() {
        while (canMove()) {
            move(1);
        }
    }

    /**
     * Beweegt naar een specifieke locatie in de wereld.
     * 
     * @param x X-coördinaat doel
     * @param y Y-coördinaat doel
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
     * Telt het aantal eieren in de rij van de huidige locatie.
     * 
     * @return int aantal eieren in de rij
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
        return count;
    }

    /**
     * Telt het aantal eieren in een specifieke kolom.
     * 
     * @param x kolom index
     * @return int aantal eieren in de kolom
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
     * Legt een spoor van eieren van lengte n, zolang er bewogen kan worden.
     * 
     * @param n aantal eieren om te leggen
     */
    public void layTrailOfEggs(int n) {
        for (int i = 0; i < n && canMove(); i++) {
            layEgg();
            move(1);
        }
    }

    /**
     * Bereken en toon het gemiddelde aantal eieren per rij in de wereld.
     */
    public void calculateAverageEggsPerRow() {
        int total = 0;
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            total += countEggsInRow();
        }
        double avg = (double) total / getWorld().getHeight();
        System.out.println("Gemiddeld per rij: " + avg);
    }

    /**
     * Controleert welke rij een onjuist pariteitsbit heeft (oneven aantal eieren).
     * 
     * @return int rij index of -1 als alles klopt
     */
    public int getIncorrectRow() {
        for (int y = 0; y < getWorld().getHeight(); y++) {
            goToLocation(0, y);
            if (countEggsInRow() % 2 != 0) return y;
        }
        return -1;
    }

    /**
     * Controleert welke kolom een onjuist pariteitsbit heeft (oneven aantal eieren).
     * 
     * @return int kolom index of -1 als alles klopt
     */
    public int getIncorrectCol() {
        for (int x = 0; x < getWorld().getWidth(); x++) {
            if (countEggsInCol(x) % 2 != 0) return x;
        }
        return -1;
    }

    /**
     * Herstelt het onjuiste pariteitsbit door een ei te leggen of op te pakken op de juiste locatie.
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

    /**
     * Update de score en het aantal stappen, en werk het scorebord in de wereld bij.
     */
    public void updateScore() {
        if (onEgg()) {
            Egg egg = (Egg) getOneIntersectingObject(Egg.class);
            if (egg instanceof GoldenEgg) myScore += 5;
            else myScore += 1;
            pickUpEgg();
        }
        // Hierbij moet de wereldklasse een updateScoreboard methode hebben:
        ((Mauritius) getWorld()).updateScore(myScore, myStepsTaken);
    }

    /**
     * Beweegt willekeurig tot maximaal het aantal stappen.
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
     * Zoekt slim naar eieren en beweegt ernaartoe, tot het max aantal stappen.
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
     * Vindt het dichtstbijzijnde ei uit een lijst eieren.
     * 
     * @param eggs lijst van eieren
     * @return het dichtstbijzijnde ei of null als geen eieren
     */
    public Egg findClosestEgg(List<Egg> eggs) {
        Egg closest = null;
        int min = Integer.MAX_VALUE;
        for (Egg egg : eggs) {
            int dist = Math.abs(getX() - egg.getX()) + Math.abs(getY() - egg.getY());
            if (dist < min) {
                min = dist;
                closest = egg;
            }
        }
        return closest;
    }
}




