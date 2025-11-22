package com.comp2042.logic.bricks;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        refillBag();
    }

    private void refillBag(){
        List<Brick> bag = new ArrayList<>(brickList);
        Collections.shuffle(bag);

        nextBricks.clear();
        nextBricks.addAll(bag);
    }

    @Override
    public Brick getBrick() {
        if(nextBricks.isEmpty()) {
            refillBag();
        }

        Brick current = nextBricks.poll();

        if (nextBricks.isEmpty()) {
            refillBag();
        }
        return current;
    }

    @Override
    public Brick getNextBrick() {
        if (nextBricks.isEmpty()){
            refillBag();
        }
        return nextBricks.peek();
    }
}
