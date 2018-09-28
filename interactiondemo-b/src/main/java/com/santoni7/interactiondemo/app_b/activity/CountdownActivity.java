package com.santoni7.interactiondemo.app_b.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.santoni7.interactiondemo.app_b.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * An Activity which shows alert dialog, and then counts down and finishes.
 * Custom progress indicator is used: a pulsing ring and text with countdown
 */
public class CountdownActivity extends AppCompatActivity {

    @BindView(R.id.pulsingProgress) ImageView pulsingProgress;
    @BindView(R.id.textCountdown) TextView textCountdown;
    @BindView(R.id.textViewTitle) TextView textTitle;
    @BindView(R.id.root) ConstraintLayout layoutRoot;

    CompositeDisposable disposables = new CompositeDisposable();

    /**
     * Duration of one animation cycle
     */
    private static final int ANIM_CYCLE_DURATION = 1000;
    /**
     * Number of repeats of animation cycle
     */
    private static final int ANIM_CYCLE_REPEATS = 10;
    /**
     * Measures by which part of current scale decrease init scale for next animation cycle
     */
    private static final float SHRINK_FACTOR = 0.1f;


    private int countdownValue = ANIM_CYCLE_REPEATS;


    private float initScale = 0.7f;
    private float targetScale = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        ButterKnife.bind(this);


        pulsingProgress.setScaleX(initScale);
        pulsingProgress.setScaleY(initScale);
        textCountdown.setScaleX(initScale);
        textCountdown.setScaleY(initScale);

        textCountdown.setText(String.valueOf(countdownValue));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.countdown_alert_text)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    initTimer();
                });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // countdown has been already started before, and then suspended, so close app
        if (countdownValue < ANIM_CYCLE_REPEATS) {
            closeApp();
        }
    }

    private void initTimer() {
        disposables.add(
                Observable.interval(ANIM_CYCLE_DURATION + 75, TimeUnit.MILLISECONDS)
                        .take(ANIM_CYCLE_REPEATS + 1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(next -> onTick(),
                                err -> onTimeout(),
                                this::onTimeout));
    }

    private void onTimeout() {
        closeApp();
    }

    private void onTick() {
        if (countdownValue == 0) {
            return;
        } else if(countdownValue == 1){
            textTitle.setText(R.string.goodbye);
        }
        // Set minimum TextView scale to leave it readable
        final float minTextInitScale = 0.75f,
                minTextTargetScale = 0.87f;


        // Update text indicator when pulse animation is at it's largest state
        textCountdown.postDelayed(() -> textCountdown.setText(getString(R.string.countdown_text_format, --countdownValue)), ANIM_CYCLE_DURATION * 3 / 7);

        // Calculate value by which to decrease scale values by, for the next iteration
        float delta = initScale * SHRINK_FACTOR;

        // Start pulse animation cycle for ring
        animateProgress(pulsingProgress, countdownValue, initScale, targetScale, delta);

        // Start pulse animation cycle for text indicator, while limiting its minimal init and target scale
        animateProgress(textCountdown, countdownValue, Math.max(initScale, minTextInitScale),
                Math.max(targetScale, minTextTargetScale), delta);

        // Update init and target scale values for the next animation cycle
        initScale -= delta;
        targetScale -= delta;
    }

    private void animateProgress(View target, int ticksLeft, float initScale, float targetScale, float delta) {
        if (ticksLeft == 0) {
            delta = 0;
        }

        // At first half of animation, target grows from initScale to targetScale
        ObjectAnimator animatorXTo = ObjectAnimator.ofFloat(target, "scaleX", initScale, targetScale);
        ObjectAnimator animatorYTo = ObjectAnimator.ofFloat(target, "scaleY", initScale, targetScale);

        AnimatorSet forward = new AnimatorSet(); // X and Y axes animation combined
        forward.playTogether(animatorXTo, animatorYTo);

        // Second half: shrink from targetScale to (initScale - delta) which will be equal to initScale in next cycle
        ObjectAnimator animatorXBack = ObjectAnimator.ofFloat(target, "scaleX", targetScale, initScale - delta);
        ObjectAnimator animatorYBack = ObjectAnimator.ofFloat(target, "scaleY", targetScale, initScale - delta);

        AnimatorSet backward = new AnimatorSet();
        backward.playTogether(animatorXBack, animatorYBack);

//        TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
        TimeInterpolator interpolator = new AnticipateInterpolator();
        forward.setInterpolator(interpolator);
        backward.setInterpolator(interpolator);

        AnimatorSet set = new AnimatorSet();

        set.playSequentially(forward.setDuration(ANIM_CYCLE_DURATION / 2), backward.setDuration(ANIM_CYCLE_DURATION / 2));
        set.start();
    }


    private void closeApp() {
        disposables.dispose();
        finishAndRemoveTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposables.dispose();
        finishAndRemoveTask();
    }
}
