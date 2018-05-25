package com.crazy_putting.game.Bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameObjects.Ball;
import com.crazy_putting.game.GameObjects.Course;
import com.crazy_putting.game.GameObjects.Hole;
import com.crazy_putting.game.Physics.UpdatedPhysics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {

    private ArrayList<Ball> allBalls;
    private ArrayList<Ball> children;

    private Hole hole;
    private Course course;
    private Random rand;

    private Vector3 initial_Position;

    private final int POPULATION_SIZE = 500;
    private final double ELITE_RATE = 0.2;
    private final double MUTATION_RATE = 0.4;
    private static final int MAX_ITER = 50;
    private int  nrOfGenerationsProduced;
    private Ball bestBall;



    public GeneticAlgorithm(Hole hole, Course course){

        this.hole = hole;
        this.course = course;
        this.rand = new Random();
        this.allBalls = new ArrayList<Ball>();
        this.initial_Position = new Vector3();
        nrOfGenerationsProduced = 0;
        createBallObjects();

        run();

        bestBall = allBalls.get(0);
        printBestBall();
    }

    //main method for the algorithm
    private void run(){
        randomizeBallInput();

        nrOfGenerationsProduced  = MAX_ITER;
        for(int i = 0; i < MAX_ITER;i++){
            unFixAllTheBall();

            simulateShots();

            Collections.sort(allBalls);
            //System.out.println("Balls are sorted");

            System.out.println("Generation: " + (i+1) + " The best score is: " + allBalls.get(0).getFitnessValue());

            if(allBalls.get(0).getFitnessValue() == 0){
                System.out.println("Success");
                nrOfGenerationsProduced = i+1;
                break;
            }

            children = null;

            allBalls = crossOver();
        }

    }

    public void printBestBall(){
        System.out.println("The best ball is found");
        System.out.println("Speed: " + bestBall.getVelocityGA().speed);
        System.out.println("Angle: " + bestBall.getVelocityGA().angle);
    }


    private void simulateShots(){

        for(int i = 0; i < POPULATION_SIZE; i++){
           // System.out.println("Simulating shot #" + i);
            simulateShot(allBalls.get(i));

        }
    }

    private ArrayList<Ball> crossOver(){
        int eliteSize = (int) (POPULATION_SIZE*ELITE_RATE);

        children = new ArrayList<Ball>();


        chooseElite(eliteSize);

        for(int i = eliteSize; i<POPULATION_SIZE;i++){

            //first ball from the elites
            int i1 = (int) (Math.random() * eliteSize);

            int i2 = (int) (Math.random() * eliteSize);

            int angle1 = (int) allBalls.get(i1).getVelocityGA().angle;
            float speed1 = allBalls.get(i1).getVelocityGA().speed;

            int angle2 = (int) allBalls.get(i2).getVelocityGA().angle;
            float speed2 = allBalls.get(i2).getVelocityGA().speed;

            Ball iterativeBall = allBalls.get(i);

            // Simple crossover
//            if(rand.nextFloat() < 0.5){
//                iterativeBall.setVelocityGA(speed2,angle1);
//                iterativeBall.setVelocity(speed2,angle1);
//            }
//            else{
//                iterativeBall.setVelocityGA(speed1,angle2);
//                iterativeBall.setVelocity(speed1,angle2);
//            }

            // Uniform crossover - comparable to simple crossover
//            if(rand.nextFloat() < 0.5){
//                if(rand.nextFloat()<0.5){
//                    iterativeBall.setVelocityGA(speed2,angle1);
//                    iterativeBall.setVelocity(speed2,angle1);
//                }
//                else{
//                    iterativeBall.setVelocityGA(speed1,angle1);
//                    iterativeBall.setVelocity(speed1,angle1);
//                }
//
//            }
//            else{
//                if(rand.nextFloat()<0.5){
//                    iterativeBall.setVelocityGA(speed1,angle2);
//                    iterativeBall.setVelocity(speed1,angle2);
//                }
//                else{
//                    iterativeBall.setVelocityGA(speed2,angle2);
//                    iterativeBall.setVelocity(speed2,angle2);                }
//            }

            // Whole arithmetic recombination with random u
            double u = Math.random();
            iterativeBall.setVelocityGA((float)((((1-u)*speed1+u*speed2))/1f),(int)((float)((1-u)*angle1+u*angle2)/1));
            iterativeBall.setVelocity((((float)((1-u)*speed1+u*speed2))/1f),(int)((float)((1-u)*angle1+u*angle2)/1));


            if(rand.nextFloat()<MUTATION_RATE){
                int newAngle = rand.nextInt(361);
                iterativeBall.setVelocityGA(rand.nextFloat()*(speed1+speed2)/2, newAngle);
                iterativeBall.setVelocity(rand.nextFloat()*(speed1+speed2)/2, newAngle);
            }
            iterativeBall.setPosition(course.getStartBall());

            children.add(iterativeBall);
        }

        return children;


    }


    private void chooseElite(int eSize){
        for(int i=0; i<eSize; i++){
            children.add(allBalls.get(i));
        }
    }

    private void simulateShot(Ball b){


        while (b.isMoving() && !b.isFixed()){
            //System.out.println("okey");
            //System.out.println(b.getSpeed());
            UpdatedPhysics.updateBall(b,Gdx.graphics.getDeltaTime());
        }
        if(b.isFixed()){
            b.setFitnessValue(1000);
           // System.out.println("ball fixed,so I quit");
            return;
        }
        int result = calcToHoleDistance(b);
       // System.out.println("result: " + result);

        if(result < hole.getRadius()){
            b.setFitnessValue(0);
        }
        else {
            b.setFitnessValue(result);
        }

    }

    private int calcToHoleDistance(Ball b){
        double xDist = Math.pow(b.getPosition().x - hole.getPosition().x,2);
        double yDist = Math.pow(b.getPosition().y - hole.getPosition().y,2);
        return (int) Math.sqrt(xDist + yDist);
    }

    private void randomizeBallInput(){

        if(!this.allBalls.isEmpty()){
            for(Ball ball : allBalls){
                float speed = rand.nextFloat() * CourseManager.getMaxSpeed();
                int angle = rand.nextInt(361);
                ball.setVelocityGA(speed, angle);
                ball.setVelocity(speed,angle);
            }
        }
    }


    private void createBallObjects(){
        this.initial_Position = course.getStartBall();
        for(int i = 0 ; i < POPULATION_SIZE; i++){
            Ball addBall = new Ball();
            addBall.setPosition(initial_Position);
            addBall.fix(false);
            //UpdatedPhysics.addMovableObject(addBall);
            allBalls.add(addBall);
        }
    }

    private void unFixAllTheBall(){
        for(Ball someBall : allBalls){
            someBall.fix(false);
        }
    }


    public Ball getBestBall() {
        return bestBall;
    }

    public int getNrOfGenerationsProduced() {
        return nrOfGenerationsProduced;
    }
}