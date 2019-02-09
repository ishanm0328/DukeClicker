package com.example.ishan.animationdemo;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    ImageView clicker, upgradeOne, upgradeTwo, upgradeThree, background;
    TextView pointsCounter, cpsCounter, upgradeOneCost, upgradeOneCount, upgradeTwoCost, upgradeTwoCount, upgradeThreeCost, upgradeThreeCount;
    ConstraintLayout layout;
    LinearLayout linearLayout;
    int clicks = 1;
    int upgradeOnePrice = 25;
    int upgradeTwoPrice = 50;
    int upgradeThreePrice = 100;
    int upgradeOneAmount = 0;
    int upgradeTwoAmount = 0;
    int upgradeThreeAmount = 0;
    int tempClicks = 0;
    int tempCPS = 0;
    AtomicInteger points = new AtomicInteger();
    final myThread upThread = new myThread();
    Switch autoSwitch;
    AnimationDrawable zionDunk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clicker = findViewById(R.id.id_imageView);
        layout = findViewById(R.id.id_constraint);
        linearLayout = findViewById(R.id.id_linear);
        pointsCounter = findViewById(R.id.id_score);
        cpsCounter = findViewById(R.id.id_cps);
        upgradeOne = findViewById(R.id.id_upgrade);
        upgradeTwo = findViewById(R.id.id_upgrade2);
        upgradeThree = findViewById(R.id.id_upgrade3);
        upgradeOneCost = findViewById(R.id.id_upgradeOneCost);
        upgradeOneCount = findViewById(R.id.id_upgradeOneCount);
        upgradeTwoCost = findViewById(R.id.id_upgradeTwoCost);
        upgradeTwoCount = findViewById(R.id.id_upgradeTwoCount);
        upgradeThreeCost = findViewById(R.id.id_upgradeThreeCost);
        upgradeThreeCount = findViewById(R.id.id_upgradeThreeCount);
        autoSwitch = findViewById(R.id.id_autoSwitch);
        background = findViewById(R.id.id_background);
        background.setBackgroundResource(R.drawable.zion_dunk);
        zionDunk = (AnimationDrawable) background.getBackground();

        visualChanges(pointsCounter);
        visualChanges(cpsCounter);
        visualChanges(upgradeOneCost);
        visualChanges(upgradeOneCount);
        visualChanges(upgradeTwoCost);
        visualChanges(upgradeTwoCount);
        visualChanges(upgradeThreeCost);
        visualChanges(upgradeThreeCount);


        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    upThread.resumeThread();
                }
                if (!isChecked)
                {
                    upThread.suspendThread();
                }
            }
        });

        clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(300);
                v.startAnimation(animation);
                points.addAndGet(clicks);
                clicksAnimation();
                upgradeAnimation();
                pointsCounter.setText("Points: " + points);
                cpsCounter.setText("Points Per Second: " + upThread.cps);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        zionDunk.start();
        upThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        upThread.stopThread();
    }

    public void visualChanges(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.duke));
        textView.setTypeface(null, Typeface.BOLD);
    }

    public void upgradeAnimation() {
        final AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        final AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeIn.setDuration(300);
        fadeOut.setDuration(300);

        upgradeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCam();
                upgradeOne.startAnimation(fadeOut);
                if (points.get() < upgradeOnePrice)
                    points.addAndGet(-points.get());
                else points.addAndGet(-upgradeOnePrice);
                upgradeOnePrice += (int)(Math.pow(1.5,(double)(upgradeOneAmount)));
                upgradeOneAmount++;
                if (points.get() < upgradeOnePrice) {
                    upgradeOne.startAnimation(fadeOut);
                    upgradeOne.setVisibility(View.INVISIBLE);
                    upgradeOne.setClickable(false);
                }
                upThread.cps += 2;
                clicks++;
                upgradeOneCost.setText("Price: " + upgradeOnePrice);
                upgradeOneCount.setText("Amount: " + upgradeOneAmount);
            }
        });

        upgradeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRJ();
                if (points.get() < upgradeTwoPrice)
                    points.addAndGet(-points.get());
                else points.addAndGet(-upgradeTwoPrice);
                upgradeTwo.startAnimation(fadeOut);
                upgradeTwoPrice += (int)(Math.pow(2,(double)(upgradeTwoAmount)));
                upgradeTwoAmount++;
                if (points.get() < upgradeTwoPrice) {
                    upgradeTwo.startAnimation(fadeOut);
                    upgradeTwo.setVisibility(View.INVISIBLE);
                    upgradeTwo.setClickable(false);
                }
                upgradeTwoCost.setText("Price: " + upgradeTwoPrice);
                upgradeTwoCount.setText("Amount: " + upgradeTwoAmount);
                upThread.cps += 5;
                clicks += 3;
            }
        });

        upgradeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveZion();
                if (points.get() < upgradeThreePrice)
                    points.addAndGet(-points.get());
                else points.addAndGet(-upgradeThreePrice);
                upgradeThree.startAnimation(fadeOut);
                upgradeThreePrice += (int)(Math.pow(3,(double)(upgradeThreeAmount)));
                upgradeThreeAmount++;
                if (points.get() < upgradeThreePrice) {
                    upgradeThree.startAnimation(fadeOut);
                    upgradeThree.setVisibility(View.INVISIBLE);
                    upgradeThree.setClickable(false);
                }
                upThread.cps += 10;
                clicks += 5;
                upgradeThreeCost.setText("Price: " + upgradeThreePrice);
                upgradeThreeCount.setText("Amount: " + upgradeThreeAmount);
            }
        });

        if (points.get() >= upgradeOnePrice) {
            if (upgradeOne.getVisibility() == View.INVISIBLE)
                upgradeOne.startAnimation(fadeIn);
            upgradeOne.setVisibility(View.VISIBLE);
            upgradeOne.setClickable(true);
        }

        if (points.get() >= upgradeTwoPrice) {
            if (upgradeTwo.getVisibility() == View.INVISIBLE)
                upgradeTwo.startAnimation(fadeIn);
            upgradeTwo.setVisibility(View.VISIBLE);
            upgradeTwo.setClickable(true);
        }

        if (points.get() >= upgradeThreePrice) {
            if (upgradeThree.getVisibility() == View.INVISIBLE)
                upgradeThree.startAnimation(fadeIn);
            upgradeThree.setVisibility(View.VISIBLE);
            upgradeThree.setClickable(true);
        }

    }

    public void clicksAnimation() {
        float HOR_BIAS = (float)(Math.random()*0.40f)+0.30f;
        float VER_BIAS = (float)(Math.random()*0.05f)+0.20f;
        final TextView textViewInCode;

        textViewInCode = new TextView(this);
        textViewInCode.setId(View.generateViewId());
        textViewInCode.setText("+"+clicks);
        visualChanges(textViewInCode);

        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        textViewInCode.setLayoutParams(p);

        layout.addView(textViewInCode);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(textViewInCode.getId(),ConstraintSet.BOTTOM,layout.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(textViewInCode.getId(),ConstraintSet.TOP,layout.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(textViewInCode.getId(),ConstraintSet.LEFT,layout.getId(),ConstraintSet.LEFT);
        constraintSet.connect(textViewInCode.getId(),ConstraintSet.RIGHT,layout.getId(),ConstraintSet.RIGHT);

        constraintSet.setHorizontalBias(textViewInCode.getId(),HOR_BIAS);
        constraintSet.setVerticalBias(textViewInCode.getId(),VER_BIAS);

        constraintSet.applyTo(layout);

        textViewInCode.animate().translationY(-150).alpha(0.5f).setDuration(2000);
        Animation scale1 = new TranslateAnimation(0,0,200,0);
        scale1.setDuration(2000);
        textViewInCode.startAnimation(scale1);
        scale1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.removeView(textViewInCode);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void moveCam()
    {
        final AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(300);
        float HOR_BIAS = (float)(Math.random()*0.30f);
        float VERT_BIAS = (float)(Math.random()*0.29f)+0.7f;
        ImageView smallCam;

        smallCam = new ImageView(this);
        smallCam.setId(View.generateViewId());
        smallCam.setImageResource(R.drawable.camreddishsmall);

        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        smallCam.setLayoutParams(p);

        layout.addView(smallCam);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(smallCam.getId(),ConstraintSet.TOP,layout.getId(),ConstraintSet.TOP);
        constraintSet.connect(smallCam.getId(),ConstraintSet.BOTTOM,layout.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(smallCam.getId(),ConstraintSet.LEFT,layout.getId(),ConstraintSet.LEFT);
        constraintSet.connect(smallCam.getId(),ConstraintSet.RIGHT,layout.getId(),ConstraintSet.RIGHT);

        constraintSet.setHorizontalBias(smallCam.getId(),HOR_BIAS);
        constraintSet.setVerticalBias(smallCam.getId(),VERT_BIAS);

        constraintSet.applyTo(layout);

        smallCam.startAnimation(fadeIn);
    }

    public void moveRJ()
    {
        final AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(300);
        float HOR_BIAS = (float)(Math.random()*0.30f)+0.4f;
        float VERT_BIAS = (float)(Math.random()*0.29f)+0.7f;
        ImageView smallRJ;

        smallRJ = new ImageView(this);
        smallRJ.setId(View.generateViewId());
        smallRJ.setImageResource(R.drawable.rjbarrettsmall);

        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        smallRJ.setLayoutParams(p);

        layout.addView(smallRJ);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(smallRJ.getId(),ConstraintSet.TOP,layout.getId(),ConstraintSet.TOP);
        constraintSet.connect(smallRJ.getId(),ConstraintSet.BOTTOM,layout.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(smallRJ.getId(),ConstraintSet.LEFT,layout.getId(),ConstraintSet.LEFT);
        constraintSet.connect(smallRJ.getId(),ConstraintSet.RIGHT,layout.getId(),ConstraintSet.RIGHT);

        constraintSet.setHorizontalBias(smallRJ.getId(),HOR_BIAS);
        constraintSet.setVerticalBias(smallRJ.getId(),VERT_BIAS);

        constraintSet.applyTo(layout);

        smallRJ.startAnimation(fadeIn);
    }

    public void moveZion()
    {
        final AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(300);
        float HOR_BIAS = (float)(Math.random()*0.30f)+0.7f;
        float VERT_BIAS = (float)(Math.random()*0.29f)+0.7f;
        ImageView smallZion;

        smallZion = new ImageView(this);
        smallZion.setId(View.generateViewId());
        smallZion.setImageResource(R.drawable.zionwilliamsonsmall);

        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        smallZion.setLayoutParams(p);

        layout.addView(smallZion);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(smallZion.getId(),ConstraintSet.TOP,layout.getId(),ConstraintSet.TOP);
        constraintSet.connect(smallZion.getId(),ConstraintSet.BOTTOM,layout.getId(),ConstraintSet.BOTTOM);
        constraintSet.connect(smallZion.getId(),ConstraintSet.LEFT,layout.getId(),ConstraintSet.LEFT);
        constraintSet.connect(smallZion.getId(),ConstraintSet.RIGHT,layout.getId(),ConstraintSet.RIGHT);

        constraintSet.setHorizontalBias(smallZion.getId(),HOR_BIAS);
        constraintSet.setVerticalBias(smallZion.getId(),VERT_BIAS);

        constraintSet.applyTo(layout);

        smallZion.startAnimation(fadeIn);
    }

    public class myThread extends Thread {

        final AtomicBoolean running = new AtomicBoolean(false);
        final AtomicBoolean suspended = new AtomicBoolean(false);
        final AtomicBoolean stopped = new AtomicBoolean(false);
        int cps = 0;

        synchronized void suspendThread() {
            suspended.set(true);
        }

        synchronized void resumeThread() {
            suspended.set(false);
            notify();
        }

        synchronized void stopThread() {
            stopped.set(true);
            suspended.set(false);
            notify();
        }


        public void run() {
            running.set(true);
            while (running.get()) {
                try {
                    Thread.sleep(500);
                    synchronized (this) {
                        while (suspended.get())
                            wait();
                        if (stopped.get())
                            break;
                    }
                }
                catch (InterruptedException exc) {
                    System.out.println("Interrupted");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                points.getAndAdd(cps);

                final AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
                fadeIn.setDuration(300);

                pointsCounter.post(new Runnable() {
                    @Override
                    public void run() {
                        pointsCounter.setText("Points: " + points);
                    }
                });

                cpsCounter.post(new Runnable() {
                    @Override
                    public void run() {
                        cpsCounter.setText("Points Per Second: " + cps);
                    }
                });

                upgradeOne.post(new Runnable() {
                    @Override
                    public void run() {
                        if (points.get() >= upgradeOnePrice) {
                            if (upgradeOne.getVisibility() == View.INVISIBLE)
                                upgradeOne.startAnimation(fadeIn);
                            upgradeOne.setVisibility(View.VISIBLE);
                            upgradeOne.setClickable(true);
                        }
                        else {
                            upgradeOne.setVisibility(View.INVISIBLE);
                            upgradeOne.setClickable(false);
                        }
                        if (points.get() >= upgradeTwoPrice) {
                            if (upgradeTwo.getVisibility() == View.INVISIBLE)
                                upgradeTwo.startAnimation(fadeIn);
                            upgradeTwo.setVisibility(View.VISIBLE);
                            upgradeTwo.setClickable(true);
                        }
                        else {
                            upgradeTwo.setVisibility(View.INVISIBLE);
                            upgradeTwo.setClickable(false);
                        }
                        if (points.get() >= upgradeThreePrice) {
                            if (upgradeThree.getVisibility() == View.INVISIBLE)
                                upgradeThree.startAnimation(fadeIn);
                            upgradeThree.setVisibility(View.VISIBLE);
                            upgradeThree.setClickable(true);
                        }
                        else {
                            upgradeThree.setVisibility(View.INVISIBLE);
                            upgradeThree.setClickable(false);
                        }
                    }
                });

                upgradeOneCost.post(new Runnable() {
                    @Override
                    public void run() {
                        upgradeOneCost.setText("Price: "+upgradeOnePrice);
                    }
                });

                upgradeOneCount.post(new Runnable() {
                    @Override
                    public void run() {
                        upgradeOneCount.setText("Amount: "+upgradeOneAmount);
                    }
                });

                upgradeTwo.post(new Runnable() {
                    @Override
                    public void run() {
                        if (points.get() >= upgradeOnePrice) {
                            if (upgradeOne.getVisibility() == View.INVISIBLE)
                                upgradeOne.startAnimation(fadeIn);
                            upgradeOne.setVisibility(View.VISIBLE);
                            upgradeOne.setClickable(true);
                        }
                        else {
                            upgradeOne.setVisibility(View.INVISIBLE);
                            upgradeOne.setClickable(false);
                        }
                        if (points.get() >= upgradeTwoPrice) {
                            if (upgradeTwo.getVisibility() == View.INVISIBLE)
                                upgradeTwo.startAnimation(fadeIn);
                            upgradeTwo.setVisibility(View.VISIBLE);
                            upgradeTwo.setClickable(true);
                        }
                        else {
                            upgradeTwo.setVisibility(View.INVISIBLE);
                            upgradeTwo.setClickable(false);
                        }
                        if (points.get() >= upgradeThreePrice) {
                            if (upgradeThree.getVisibility() == View.INVISIBLE)
                                upgradeThree.startAnimation(fadeIn);
                            upgradeThree.setVisibility(View.VISIBLE);
                            upgradeThree.setClickable(true);
                        }
                        else {
                            upgradeThree.setVisibility(View.INVISIBLE);
                            upgradeThree.setClickable(false);
                        }
                    }
                });

                upgradeTwoCost.post(new Runnable() {
                    @Override
                    public void run() {
                        upgradeTwoCost.setText("Price: "+upgradeTwoPrice);
                    }
                });

                upgradeTwoCount.post(new Runnable() {
                    @Override
                    public void run() {
                        upgradeTwoCount.setText("Amount: "+upgradeTwoAmount);
                    }
                });

                upgradeThree.post(new Runnable() {
                    @Override
                    public void run() {
                        if (points.get() >= upgradeOnePrice) {
                            if (upgradeOne.getVisibility() == View.INVISIBLE)
                                upgradeOne.startAnimation(fadeIn);
                            upgradeOne.setVisibility(View.VISIBLE);
                            upgradeOne.setClickable(true);
                        }
                        else {
                            upgradeOne.setVisibility(View.INVISIBLE);
                            upgradeOne.setClickable(false);
                        }
                        if (points.get() >= upgradeTwoPrice) {
                            if (upgradeTwo.getVisibility() == View.INVISIBLE)
                                upgradeTwo.startAnimation(fadeIn);
                            upgradeTwo.setVisibility(View.VISIBLE);
                            upgradeTwo.setClickable(true);
                        }
                        else {
                            upgradeTwo.setVisibility(View.INVISIBLE);
                            upgradeTwo.setClickable(false);
                        }
                        if (points.get() >= upgradeThreePrice) {
                            if (upgradeThree.getVisibility() == View.INVISIBLE)
                                upgradeThree.startAnimation(fadeIn);
                            upgradeThree.setVisibility(View.VISIBLE);
                            upgradeThree.setClickable(true);
                        }
                        else {
                            upgradeThree.setVisibility(View.INVISIBLE);
                            upgradeThree.setClickable(false);
                        }
                    }
                });

                upgradeThreeCost.post(new Runnable() {
                    @Override
                    public void run() {
                        upgradeThreeCost.setText("Price: "+upgradeThreePrice);
                    }
                });

                upgradeThreeCount.post(new Runnable() {
                    @Override
                    public void run() {
                        upgradeThreeCount.setText("Amount: "+upgradeThreeAmount);
                    }
                });
            }
        }

    }

}