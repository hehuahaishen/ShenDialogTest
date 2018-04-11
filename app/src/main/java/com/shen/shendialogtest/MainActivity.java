package com.shen.shendialogtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shen.shendialog.dialog.ShenDialog;
import com.shen.shendialog.dialog.ShenProgressDialog;
import com.shen.shendialog.dialog.config.DialogConfig;
import com.shen.shendialog.dialog.config.ProgressDialogConfig;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    ShenProgressDialog dialog = null;
    ShenDialog shenDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        findViewById(R.id.btn_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShenDialog shenDialog = new ShenDialog(mContext);
                DialogConfig config = new DialogConfig.Builder()
                        .setDialogType(DialogConfig.DialogType.Error)
                        .setTitle("错误标题")
                        .setContent("错误内容")
                        .setCancelText("否")
                        .setConfirmText("是")
                        .setCancelOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).setConfirmOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).create();

                shenDialog.init(config);
                shenDialog.show();
            }
        });

        findViewById(R.id.btn_warn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShenDialog shenDialog = new ShenDialog(mContext);
                DialogConfig config = new DialogConfig.Builder()
                        .setDialogType(DialogConfig.DialogType.Warn)
                        .setTitle("警告标题")
                        .setContent("警告内容")
                        .setCancelText("否")
                        .setConfirmText("是")
                        .setCancelOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).setConfirmOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).create();

                shenDialog.init(config);
                shenDialog.show();
            }
        });

        findViewById(R.id.btn_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShenDialog shenDialog = new ShenDialog(mContext);
                DialogConfig config = new DialogConfig.Builder()
                        .setDialogType(DialogConfig.DialogType.Success)
                        .setTitle("成功标题")
                        .setContent("成功内容")
                        .setCancelText("否")
                        .setConfirmText("是")
                        .setCancelOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).setConfirmOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).create();

                shenDialog.init(config);
                shenDialog.show();
            }
        });

        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shenDialog = new ShenDialog(mContext);
                DialogConfig config = new DialogConfig.Builder()
                        .setDialogType(DialogConfig.DialogType.Load)
                        .setTitle("加载标题")
                        .setContent("加载内容")
                        .setCancelText("否")
                        .setConfirmText("是")
                        .setCancelOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).setConfirmOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).create();

                shenDialog.init(config);
                shenDialog.show();

                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception { // Observable  (被观察者)

                        emitter.onNext("");
                        emitter.onComplete();
                    }
                }).observeOn(Schedulers.io())
                        .map(new Function<String, Integer>() {
                            @Override
                            public Integer apply(@NonNull String fileName) throws Exception {
                                boolean b = false;

//                                Observable.just("正在读取 -- \n" + FilesUtils.getFileName(fileName))
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(progressObserver);

                                int i = 0;
                                while (i++ < 100) {
                                    Log.i("shen", "i:" + i);
                                    //dialog.setProgress(i+"", i + "/100");
                                    //dialog.setProgress(i);
                                    Observable.just(i)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(progressObserver1);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                return 0;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {

                            int count = 0;
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                count = integer;
                            }


                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete(){

                            }
                        });

            }
        });

        findViewById(R.id.btn_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShenDialog shenDialog = new ShenDialog(mContext);
                DialogConfig config = new DialogConfig.Builder()
                        //.setDialogType(DialogConfig.DialogType.Normal)
                        .setTitle("正常标题")
                        .setContent("正常内容")
//                        .setCancelText("否")
//                        .setConfirmText("是")
                        .setCancelOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).setConfirmOnClickListener(new DialogConfig.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shenDialog.dismiss();
                            }
                        }).create();

                shenDialog.init(config);
                shenDialog.show();
            }
        });


        dialog = new ShenProgressDialog(mContext);
        final ProgressDialogConfig config = new ProgressDialogConfig.Builder()
                //.setDialogType(DialogConfig.DialogType.Normal)
                .setTitle("进度条标题")
                .setContent("进度条内容")
                .setCancelText("取消")
                .setConfirmText("确定")
                .setMax(100)
                .setCancelOnClickListener(new ProgressDialogConfig.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).setConfirmOnClickListener(new ProgressDialogConfig.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).create();

        Button btnPb = findViewById(R.id.btn_progress);
        btnPb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.init(config);
                dialog.show();


                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception { // Observable  (被观察者)

                        emitter.onNext("");
                        emitter.onComplete();
                    }
                }).observeOn(Schedulers.io())
                        .map(new Function<String, Integer>() {
                            @Override
                            public Integer apply(@NonNull String fileName) throws Exception {
                                boolean b = false;

//                                Observable.just("正在读取 -- \n" + FilesUtils.getFileName(fileName))
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(progressObserver);

                                int i = 0;
                                while (i++ < 100) {
                                    Log.i("shen", "i:" + i);
                                    //dialog.setProgress(i+"", i + "/100");
                                    //dialog.setProgress(i);
                                    Observable.just(i)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(progressObserver);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                return 0;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {

                            int count = 0;
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                count = integer;
                            }


                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete(){

                            }
                        });


//                Log.i("shen", "dialog.show();");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int i = 0;
//                        while (i++ < 100) {
//                            Log.i("shen", "i:" + i);
//                            //dialog.setProgress(i+"", i + "/100");
//                            //dialog.setProgress(i);
//                            dialog.setContent("内容：" + i);
//                            try {
//                                Thread.sleep(500);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
//                Log.i("shen", "start();");
            }
        });

    }

    /**
     * 将数据从excel中读取到db
     * rxjava -- 主线程
     */
    Observer progressObserver = new Observer<Integer>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            //dialog.setProgress(integer);
            dialog.setProgress(integer+"", integer + "/100");
            dialog.setContent("内容：" + integer);
        }


//        @Override
//        public void onNext(@NonNull String str) {
//            String[] strings = str.split(":");
//            if(strings.length == 2) {
//                //mDialog.setMax();
//                //updataLoadingDialogTemp(strings[0], strings[1]);
//                //LogUtils.i(str);
//                //mDialog.setMax(100);
//                //mDialog.setProgress(strings[0], strings[1]);
//            }else {
//                //updataLoadingDialog(str, "");
//            }
//        }

        @Override
        public void onError(@NonNull Throwable e) {
        }

        @Override
        public void onComplete() {

        }
    };


    /**
     * 将数据从excel中读取到db
     * rxjava -- 主线程
     */
    Observer progressObserver1 = new Observer<Integer>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            //dialog.setProgress(integer);
//            dialog.setProgress(integer+"", integer + "/100");
//            dialog.setContent("内容：" + integer);

            shenDialog.setTitleText("标题").setContentText("内容：" + integer);
        }


//        @Override
//        public void onNext(@NonNull String str) {
//            String[] strings = str.split(":");
//            if(strings.length == 2) {
//                //mDialog.setMax();
//                //updataLoadingDialogTemp(strings[0], strings[1]);
//                //LogUtils.i(str);
//                //mDialog.setMax(100);
//                //mDialog.setProgress(strings[0], strings[1]);
//            }else {
//                //updataLoadingDialog(str, "");
//            }
//        }

        @Override
        public void onError(@NonNull Throwable e) {
        }

        @Override
        public void onComplete() {

        }
    };
}
