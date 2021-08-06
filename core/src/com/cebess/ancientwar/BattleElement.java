package com.cebess.ancientwar;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;
public class BattleElement {
    public enum ElementType {archer,warrior,knight}

    private final int archerMaxRange = 30;
    private int loc_X;
    private int loc_Y;
    private final ElementType myType;
    private final Boolean leftSide; // it is the left side against the right side
    private int health;
    private Boolean retreatFromField;
    private int maxMovement;
    private int maxDamage;
    private Man myMan;
    private boolean attack;
    // get and set
    public int getHealth() {  return health; }
    public int getLoc_Y() {
        return loc_Y;
    }
    public int getLoc_X() { return loc_X; }
    public Boolean getLeftSide() {
        return leftSide;
    }

    public Boolean getAlive() {
        return (health > 0);
    }

    public String getType() {
        return(myType.toString());
    }

    public BattleElement(ElementType type,int xloc,int yloc,Boolean leftSideValue,TextureAtlas myTextureAtlas)
    {
        loc_X=xloc;
        loc_Y=yloc;
        leftSide = leftSideValue;
        if (myTextureAtlas != null) { // kludge to get around testing issue
            myMan = new Man(myTextureAtlas, Math.round(loc_X / Field.XRatio), Math.round(loc_Y / Field.YRatio), leftSide);
        }
        checkElementLocation();
        myType = type;
        retreatFromField = false;
        attack = false;
        switch (type) {
            case archer:
                maxMovement = 1;
                health = 100;
                maxDamage = 100;
                break;
            case knight:
                maxMovement = 2;
                health = 300;
                maxDamage = 150;
                break;
            case warrior:
                maxMovement = 3;
                health = 200;
                maxDamage = 50;
        }
    }
    // methods
    public void checkElementLocation() throws IllegalArgumentException {
        if (loc_X < 0 || loc_X > Field.getMaxFieldWidth())
            throw new IllegalArgumentException("X location invalid: " + loc_X + " Max: " + Field.getMaxFieldWidth());
        if (loc_Y < 0 || loc_Y > Field.getMaxFieldHeight())
            throw new IllegalArgumentException("Y location invalid: " + loc_Y + " Max: " + Field.getMaxFieldHeight());
    }

    public void damage(int damageValue) {
        if (damageValue>0) {
            health -= damageValue;
        }
        if (health < 5 && health>0) retreatFromField = true;
    }

    @Override
    public String toString()
    {
        return ("Type: " + myType + " X: " + loc_X + " Y: "+ loc_Y + " ls: " + leftSide + " health: " + health);
    }

    public void attack(BattleElement nearestOpponent) {
        int hitValue = MathUtils.random(0,maxDamage);
        attack = true;
        nearestOpponent.damage(hitValue);
    }

    public void archerAttack(BattleElement nearestOpponent) {
        double floatDistance = Field.lineLength(loc_X,loc_Y,nearestOpponent.getLoc_X(),nearestOpponent.getLoc_Y());
        int intDistance = (int)floatDistance;
        int hitValue = MathUtils.random(0,maxDamage);
        int adjustedHitValue = hitValue/intDistance;
        attack = true;

        nearestOpponent.damage(adjustedHitValue);
    }

    public void move(Double xMove,Double yMove) {
        // expecting a value between -1 and 1 for xMove and yMove
        long XDist = Math.round(MathUtils.random(0, maxMovement) * xMove);
        long YDist = Math.round(MathUtils.random(0, maxMovement) * yMove);
        loc_X -= XDist;
        loc_Y -= YDist;
        if (myMan !=null) {   // for testing purposes
            myMan.setLocation(loc_X / Field.XRatio, loc_Y / Field.YRatio);
        }
    }
    //#todo retreating is not making it off the field.
    public boolean exitedTheField() {
        if ((loc_Y<0) || (loc_Y > Field.getMaxFieldHeight())) return true;
        if ((loc_X<0) || (loc_X > Field.getMaxFieldWidth())) return true;
        return false;
    }

    public void action(Field.SideAction mySidesAction, BattleElement nearestOpponent ) throws IllegalArgumentException{
        // elements can do one of four things:
        // go forward
        // go backward
        // fight
        //      if they are an archer they can fire an arrow
        //      if they are a warrior or knight they can fight those on adjacent areas
        // stay still
        if (leftSide == nearestOpponent.getLeftSide()) {
            //something is wrong since both should never be on the same side
            throw new IllegalArgumentException("X location invalid: " + loc_X);
        }
        double angle = getAngleRadians(nearestOpponent);
        double XMovement = Math.cos(angle);
        double YMovement = Math.sin(angle);
        double distance = Field.lineLength(loc_X,loc_Y,nearestOpponent.getLoc_X(),nearestOpponent.getLoc_Y());
        attack = false;
        if (distance < 1.5) {
            if (myType==ElementType.archer) {
                // too close
                move(-XMovement, -YMovement); // go in the opposite direction
                retreatFromField = true; // run away
            } else {
                //              System.err.println("++  attack " + this + " distance: " + distance); // just so I can see something
                // always attack if they are next to each other
                attack(nearestOpponent);
            }
        } else {
            if (retreatFromField) {
                move(-XMovement, -YMovement); // go in the opposite direction
            } else {
                switch (mySidesAction) {
                    case normal:
                        if (myType==ElementType.archer) {

                            // if they are far away move towards them
                            if (distance > archerMaxRange) {

                                move(XMovement, YMovement); // move them closer
                            }
                            else if (distance > archerMaxRange*0.8) {
                                if (Math.random()>0.5) { // half the time move closer to get a better shot
                                    move(XMovement, YMovement);
                                } else {
                                    archerAttack(nearestOpponent);
                                }
                            }
                            else {
                                archerAttack(nearestOpponent);
                            }
                        } else
                             move(XMovement, YMovement);
                        break;
                    case advance:
                        // go towards opponent
                        move(XMovement, YMovement);
                        break;
                    case retreat:
                        // go the other way
                        move(-XMovement, -YMovement); // go in the opposite direction
                        break;
                    case standstill:
                        // no one moves, but archers still try to shoot
                        // if people in range attack
                        if (myType==ElementType.archer) {
                            //archer's shoot
                            archerAttack(nearestOpponent);
                        }
                        break;
                }
            }
        }
    }

    public Double getAngleRadians(BattleElement otherElement) {
        int deltaX = loc_X - otherElement.getLoc_X();
        int deltaY = loc_Y - otherElement.getLoc_Y();
        return Math.atan2(deltaY, deltaX);
    }

    public void debugSetLocation(int x,int y) {
        loc_X = x;
        loc_Y = y;
    }
//#todo it looks like someone can start to retreat and then freeze
    public void draw(SpriteBatch mySpriteBatch,Field.SideAction mySidesAction) {
        if (!exitedTheField()) {
            boolean attackStance = attack;
            if (mySidesAction == Field.SideAction.valueOf("standstill")) attackStance = true;
            myMan.draw(mySpriteBatch, attackStance,retreatFromField,!getAlive());
        }
    }
}

