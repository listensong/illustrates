package zms.song.illustrates.async;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zms.song.illustrates.R;
import zms.song.illustrates.base.BaseActivity;
import zms.song.illustrates.databinding.ActivityRxJavaBinding;

public class RxJavaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRxJavaBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_rx_java);
        binding.setHolder(this);
    }

    public void onRxJavaClick(View view) {
        //HelloToast.makeText("onRxJavaClick " + view.toString()).show();
        //testRxJava();
        testRxJavaOkHttp();
    }

    private void testRxJava() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello");
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(LOG_TAG, "onSubscribe " + d);
            }

            @Override
            public void onNext(String s) {
                Log.e(LOG_TAG, "onNext " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(LOG_TAG, "onComplete ");
            }
        };

        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    private static final String TARGET_BASE_URL = "https://api.github.com/users/listensong";
    private void testRxJavaOkHttp() {
        Observable<GitApiModel> observable = Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                Log.e(LOG_TAG, "subscribe ");
                Request.Builder builder = new Request.Builder().url(TARGET_BASE_URL).get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        }).map(new Function<Response, GitApiModel>() {
            @Override
            public GitApiModel apply(Response response) throws Exception {
                String res = response.body().string();
                Log.e(LOG_TAG, "apply " + res);
                Gson gson = new Gson();
                return gson.fromJson(res, new TypeToken<GitApiModel>(){}.getType());
            }
        });

        Observer<GitApiModel> observer = new Observer<GitApiModel>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(LOG_TAG, "onSubscribe " + d);
            }

            @Override
            public void onNext(GitApiModel response) {
                Log.e(LOG_TAG, "onNext " + response.avatar_url);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LOG_TAG, "onError " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(LOG_TAG, "onComplete ");
            }
        };

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
