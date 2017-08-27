package zms.song.illustrates.Anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import zms.song.illustrates.base.IllustratesApplication;

/**
 * Created by song on 2017/8/27.
 */

public class AnimUtil {
    /** ObjectAnimator **/
    public static void applyObjectAnimator(@NonNull View view, @NonNull String objectType) {
        applyObjectAnimator(IllustratesApplication.getIllustratesApplication(), view, objectType);
    }

    public static void applyObjectAnimator(@NonNull Context context, @NonNull View view, @NonNull String objectType) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, objectType, 0.0f, 1.0f);
        animator.setDuration(1000);
        animator.setRepeatCount(0);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setStartDelay(200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

    public static void applyObjectAnimatorFromXML(@NonNull View view, @AnimatorRes int animatorRes) {
        applyObjectAnimatorFromXML(IllustratesApplication.getIllustratesApplication(), view, animatorRes);
    }

    public static void applyObjectAnimatorFromXML(@NonNull Context context, @NonNull View view, @AnimatorRes int animatorRes) {
        Animator animator = AnimatorInflater.loadAnimator(context, animatorRes);
        if (animator != null) {
            animator.setTarget(view);
            animator.start();
        }
    }

    public interface OnAnimationCallback {
        void onAnimationComplete();
    }

    public static void applyObjectAnimatorFromXML( @NonNull View view, @AnimatorRes int animatorRes, OnAnimationCallback callback) {
        applyObjectAnimatorFromXML(IllustratesApplication.getIllustratesApplication(), view, animatorRes, callback);
    }

    public static void applyObjectAnimatorFromXML(@NonNull Context context, @NonNull View view,
                                                  @AnimatorRes int animatorRes, final OnAnimationCallback callback) {
        Animator animator = AnimatorInflater.loadAnimator(context, animatorRes);
        if (animator != null) {
            animator.setTarget(view);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (callback != null) {
                        callback.onAnimationComplete();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.start();
        }
    }

    public interface OnAnimationSetCallback {
        void onAnimationDone(View view2);
    }

    public static void applyObjectAnimSetSync(@NonNull View view1, @AnimatorRes int animatorRes1,
                                               @NonNull final View view2, @AnimatorRes int animatorRes2, final OnAnimationSetCallback callback) {
        Context context = IllustratesApplication.getIllustratesApplication();
        Animator animator1 = AnimatorInflater.loadAnimator(context, animatorRes1);
        if (animator1 != null) {
            animator1.setTarget(view1);
        }
        Animator animator2 = AnimatorInflater.loadAnimator(context, animatorRes2);
        if (animator2 != null) {
            animator2.setTarget(view2);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2);
        if (callback != null) {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    callback.onAnimationDone(view2);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        set.start();
    }

    public interface OnAnimationsSetCallback {
        void onAnimationsDone(View... view);
    }
    public static void applyObjectAnimSetSync(@NonNull final View[] views, @AnimatorRes int[] animatorRes, final OnAnimationsSetCallback callback) {
        Context context = IllustratesApplication.getIllustratesApplication();
        int len = views.length > animatorRes.length ? animatorRes.length: views.length;
        AnimatorSet set = new AnimatorSet();
        Animator[] array = new Animator[len];
        for (int i = 0; i < len; i ++) {
            Animator animator = AnimatorInflater.loadAnimator(context, animatorRes[i]);
            if (animator != null) {
                animator.setTarget(views[i]);
            }
            array[i] = animator;
        }
        set.playTogether(array);
        if (callback != null) {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    callback.onAnimationsDone(views);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        set.start();
    }
}
