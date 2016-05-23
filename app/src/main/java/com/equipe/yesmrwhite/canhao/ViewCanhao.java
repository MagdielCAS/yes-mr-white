package com.equipe.yesmrwhite.canhao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.equipe.yesmrwhite.main.MainActivity;
import com.equipe.yesmrwhite.quimica.ElementoMolecula;
import com.equipe.yesmrwhite.quimica.ListaMoleculas;
import com.equipe.yesmrwhite.quimica.Molecula;
import com.example.syllas.yesmrwhite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by magdi on 18/05/2016.
 */
public class ViewCanhao extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "ViewCanhao";
    private CannonThread cannonThread;
    private Activity activity;
    private boolean dialogIsDisplayed = false;
    public int TARGET_PIECES = 7;
    public static final int MISS_PENALTY = 2;
    public static final int HIT_REWARD = 3;
    private boolean gameOver;
    private double timeLeft;
    private int shotsFired;
    private double totalElapsedTime;
    private Line blocker;
    private int blockerDistance;
    private int blockerBeginning;
    private int blockerEnd;
    private int initialBlockerVelocity;
    private float blockerVelocity;
    private Line target;
    private int targetDistance;
    private int targetBeginning;
    private double pieceLength;
    private int targetEnd;
    private int initialTargetVelocity;
    private float targetVelocity;
    private int lineWidth;
    private boolean[] hitStates;
    private int targetPiecesHit;
    private Point cannonball;
    private int cannonballVelocityX;
    private int cannonballVelocityY;
    private boolean cannonballOnScreen;
    private int cannonballRadius;
    private int cannonballSpeed;
    private int cannonBaseRadius;
    private int cannonLength;
    private Point barrelEnd;
    private int screenWidth;
    private int screenHeight;
    private static final int TARGET_SOUND_ID = 0;
    private static final int CANNON_SOUND_ID = 1;
    private static final int BLOCKER_SOUND_ID = 2;
    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private Paint elementoTexto;
    private Paint timeText;
    private Paint textPaint;
    private Paint cannonballPaint;
    private Paint cannonPaint;
    private Paint blockerPaint;
    private Paint targetPaint;
    private Paint backgroundPaint;
    private Molecula molecula;
    private ListaMoleculas moleculas;
    private Random random;
    private List<ElementoMolecula> elementos;
    private int numElementos;
    private int numRemainElements;

    private Molecula gerarMolecula() {
        random = new Random();
        elementos = new ArrayList<ElementoMolecula>();
        Molecula molecula = moleculas.getMoleculeByIndex(random.nextInt(moleculas.size()));
        elementos = molecula.getElementosMolecula();
        numElementos = molecula.numElements();
        numRemainElements = numElementos;
        System.out.println(molecula.getNome());
        this.TARGET_PIECES = molecula.numElements() * 2 + 1;
        elementos = new ArrayList<ElementoMolecula>();
        for (ElementoMolecula a :
                elementos) {
            System.out.println(a.getElemento());
        }
        return molecula;
    }

    public ViewCanhao(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;
        moleculas = new ListaMoleculas(activity);
        molecula = gerarMolecula();
        getHolder().addCallback(this);
        blocker = new Line();
        target = new Line();
        cannonball = new Point();
        hitStates = new boolean[TARGET_PIECES];
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundMap = new SparseIntArray(3);
        soundMap.put(TARGET_SOUND_ID, soundPool.load(context, R.raw.target_hit, 1));
        soundMap.put(CANNON_SOUND_ID, soundPool.load(context, R.raw.cannon_fire, 1));
        soundMap.put(BLOCKER_SOUND_ID, soundPool.load(context, R.raw.blocker_hit, 1));
        elementoTexto = new Paint();
        textPaint = new Paint();
        timeText = new Paint();
        cannonPaint = new Paint();
        cannonballPaint = new Paint();
        blockerPaint = new Paint();
        targetPaint = new Paint();
        backgroundPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        cannonBaseRadius = h / 18;
        cannonLength = w / 8;
        cannonballRadius = w / 36;
        cannonballSpeed = w * 3 / 2;
        lineWidth = w / 24;
        blockerDistance = w * 5 / 8;
        blockerBeginning = h / 8;
        blockerEnd = h * 3 / 8;
        initialBlockerVelocity = h / 2;
        blocker.start = new Point(blockerDistance, blockerBeginning);
        blocker.end = new Point(blockerDistance, blockerEnd);
        targetDistance = w * 7 / 8;
        targetBeginning = h / 8;
        targetEnd = h * TARGET_PIECES / 8;
        pieceLength = (targetEnd - targetBeginning) / TARGET_PIECES;
        initialTargetVelocity = -h / 4;
        target.start = new Point(targetDistance, targetBeginning);
        target.end = new Point(targetDistance, targetEnd);
        barrelEnd = new Point(cannonLength, h / 2);
        elementoTexto.setTextSize(w / 20);
        elementoTexto.setAntiAlias(true);
        elementoTexto.setColor(Color.WHITE);
        timeText.setTextSize(w / 20);
        timeText.setAntiAlias(true);
        timeText.setColor(Color.WHITE);
        textPaint.setTextSize(w / 20);
        textPaint.setAntiAlias(true);
        cannonPaint.setStrokeWidth(lineWidth * 1.5f);
        blockerPaint.setStrokeWidth(lineWidth);
        targetPaint.setStrokeWidth(lineWidth);
        backgroundPaint.setColor(Color.WHITE);
        newGame();
    }

    public void newGame() {
        molecula = gerarMolecula();
        targetEnd = (int)(screenHeight * TARGET_PIECES / 8 - pieceLength/2);
        hitStates = new boolean[TARGET_PIECES];
        for (int i = 0; i < TARGET_PIECES; i++)
            hitStates[i] = false;
        targetPiecesHit = 0;
        blockerVelocity = initialBlockerVelocity;
        targetVelocity = initialTargetVelocity;
        timeLeft = 10;
        cannonballOnScreen = false;
        shotsFired = 0;
        totalElapsedTime = 0.0;
        blocker.start.set(blockerDistance, blockerBeginning);
        blocker.end.set(blockerDistance, blockerEnd);
        target.start.set(targetDistance, targetBeginning);
        target.end.set(targetDistance, targetEnd);
        if (gameOver) {
            gameOver = false;
            cannonThread = new CannonThread(getHolder());
            cannonThread.start();
        }
    }

    private void updatePositions(double elapsedTimeMS) {
        double interval = elapsedTimeMS / 1000.0;
        if (cannonballOnScreen) {
            cannonball.x += interval * cannonballVelocityX;
            cannonball.y += interval * cannonballVelocityY;
            if (cannonball.x + cannonballRadius > blockerDistance && cannonball.x - cannonballRadius <
                    blockerDistance &&
                    cannonball.y + cannonballRadius > blocker.start.y && cannonball.y - cannonballRadius <
                    blocker.end.y) {
                cannonballVelocityX *= -1;
                timeLeft -= MISS_PENALTY;
                soundPool.play(soundMap.get(BLOCKER_SOUND_ID), 1, 1, 1, 0, 1f);
            } else if (cannonball.x + cannonballRadius > screenWidth || cannonball.x - cannonballRadius < 0) {
                cannonballOnScreen = false;
            } else if (cannonball.y + cannonballRadius > screenHeight || cannonball.y - cannonballRadius < 0) {
                cannonballOnScreen = false;
            } else if (cannonball.x + cannonballRadius > targetDistance && cannonball.x - cannonballRadius <
                    targetDistance &&
                    cannonball.y + cannonballRadius > target.start.y && cannonball.y - cannonballRadius < target.end.y) {
                int section = (int) ((cannonball.y - target.start.y) / pieceLength);
                if ((section >= 0 && section < TARGET_PIECES) && !hitStates[section]) {
                    if(section%2!=0){
                        if(molecula.getElementosMolecula().get((int)section/2).getPrioridade()==0){
                            hitStates[section]=true;
                            for(ElementoMolecula e:
                                    molecula.getElementosMolecula()){
                                if(e.getPrioridade()>0) e.setPrioridade(e.getPrioridade()-1);
                            }
                            numRemainElements--;
                            cannonballOnScreen = false;
                            timeLeft += HIT_REWARD;
                            soundPool.play(soundMap.get(TARGET_SOUND_ID), 1, 1, 1, 0, 1f);
                            if (++targetPiecesHit == TARGET_PIECES || numRemainElements==0) {
                                cannonThread.setRunning(false);
                                showGameOverDialog(R.string.win);
                                gameOver = true;
                            }
                        }
                    }else {
                        hitStates[section] = true;
                        cannonballOnScreen = false;
                        timeLeft += HIT_REWARD;
                        soundPool.play(soundMap.get(TARGET_SOUND_ID), 1, 1, 1, 0, 1f);
                        if (++targetPiecesHit == TARGET_PIECES) {
                            cannonThread.setRunning(false);
                            showGameOverDialog(R.string.win);
                            gameOver = true;
                        }
                    }
                }
            }
        }
        double blockerUpdate = interval * blockerVelocity;
        blocker.start.y += blockerUpdate;
        blocker.end.y += blockerUpdate;
        double targetUpdate = interval * targetVelocity;
        target.start.y += targetUpdate;
        target.end.y += targetUpdate;
        if (blocker.start.y < 0 || blocker.end.y > screenHeight) blockerVelocity *= -1;
        if (target.start.y < 0 || target.end.y > screenHeight) targetVelocity *= -1;
        timeLeft -= interval;
        if (timeLeft <= 0.0) {
            timeLeft = 0.0;
            gameOver = true;
            cannonThread.setRunning(false);
            showGameOverDialog(R.string.lose);
        }
    }

    public void fireCannonball(MotionEvent event) {
        if (cannonballOnScreen) return;
        double angle = alignCannon(event);
        cannonball.x = cannonballRadius;
        cannonball.y = screenHeight / 2;
        cannonballVelocityX = (int) (cannonballSpeed * Math.sin(angle));
        cannonballVelocityY = (int) (-cannonballSpeed * Math.cos(angle));
        cannonballOnScreen = true;
        ++shotsFired;
        soundPool.play(soundMap.get(CANNON_SOUND_ID), 1, 1, 1, 0, 1f);
    }

    public double alignCannon(MotionEvent event) {
        Point touchPoint = new Point((int) event.getX(), (int) event.getY());
        double centerMinusY = (screenHeight / 2 - touchPoint.y);
        double angle = 0;
        if (centerMinusY != 0) angle = Math.atan((double) touchPoint.x / centerMinusY);
        if (touchPoint.y > screenHeight / 2) angle += Math.PI;
        barrelEnd.x = (int) (cannonLength * Math.sin(angle));
        barrelEnd.y = (int) (-cannonLength * Math.cos(angle) + screenHeight / 2);
        return angle;
    }

    public void drawGameElements(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
        canvas.drawText(getResources().getString(R.string.time_remaining_format, timeLeft), 30, 50, textPaint);
        canvas.drawText(molecula.getNome()+": "+molecula.getMolecula(), 30, screenHeight - 50, textPaint);
        if (cannonballOnScreen)
            canvas.drawCircle(cannonball.x, cannonball.y, cannonballRadius, cannonballPaint);
        canvas.drawLine(0, screenHeight / 2, barrelEnd.x, barrelEnd.y, cannonPaint);
        canvas.drawCircle(0, (int) screenHeight / 2, (int) cannonBaseRadius, cannonPaint);
        canvas.drawLine(blocker.start.x, blocker.start.y, blocker.end.x, blocker.end.y, blockerPaint);
        Point currentPoint = new Point();
        currentPoint.x = target.start.x;
        currentPoint.y = target.start.y;
        for (int i = 0; i < TARGET_PIECES; i++) {
            if (!hitStates[i]) {
                if (i % 2 != 0) {
                    targetPaint.setColor(Color.GREEN);
                    canvas.drawCircle(currentPoint.x, (float) (currentPoint.y + pieceLength / 2), (float) (pieceLength * 2 / 5), targetPaint);
                    if(i/2<numElementos) canvas.drawText(molecula.getElementosMolecula().get((int) (i / 2)).getElemento(), (currentPoint.x - screenWidth / 40), (int) (currentPoint.y + pieceLength / 2 + screenWidth / 60), elementoTexto);
                } else {
                    targetPaint.setColor(Color.BLUE);
                    canvas.drawCircle(currentPoint.x, (float) (currentPoint.y + pieceLength / 2), (float) (pieceLength * 2 / 5), targetPaint);
                    canvas.drawText("+3s", (currentPoint.x - screenWidth / 40), (int) (currentPoint.y + pieceLength / 2 + screenWidth / 60), timeText);
                }
                // canvas.drawLine(currentPoint.x, currentPoint.y, target.end.x, (int) (currentPoint.y + pieceLength), targetPaint);

            }
            currentPoint.y += pieceLength;
        }
    }


    private void showGameOverDialog(final int messageId) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getResources().getString(messageId));
        dialogBuilder.setCancelable(false);
        dialogBuilder.setMessage(getResources().getString(R.string.results_format, shotsFired, totalElapsedTime));
        dialogBuilder.setPositiveButton(R.string.reset_game, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogIsDisplayed = false;
                newGame();
            }
        });
        dialogBuilder.setNeutralButton("Voltar",new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        activity.startActivity(new Intent(activity, MainActivity.class));;
                    } // end method onClick
                }
        );
        activity.runOnUiThread(
                new Runnable() {
                    public void run() {
                        dialogIsDisplayed = true;
                        dialogBuilder.show();
                    }
                }
        );
    }

    public void stopGame() {
        if (cannonThread != null) cannonThread.setRunning(false);
    }

    public void releaseResources() {
        soundPool.release();
        soundPool = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!dialogIsDisplayed) {
            cannonThread = new CannonThread(holder);
            cannonThread.setRunning(true);
            cannonThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        cannonThread.setRunning(false);
        while (retry) {
            try {
                cannonThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread interrupted", e);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            fireCannonball(e);
        }
        return true;
    }

    private class CannonThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public CannonThread(SurfaceHolder holder) {
            surfaceHolder = holder;
            setName("CannonThread");
        }

        public void setRunning(boolean running) {
            threadIsRunning = running;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            long previousFrameTime = System.currentTimeMillis();
            while (threadIsRunning) {
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    synchronized (surfaceHolder) {
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime -
                                previousFrameTime;
                        totalElapsedTime += elapsedTimeMS / 1000.0;
                        updatePositions(elapsedTimeMS);
                        drawGameElements(canvas);
                        previousFrameTime = currentTime;
                    }
                } finally {
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
