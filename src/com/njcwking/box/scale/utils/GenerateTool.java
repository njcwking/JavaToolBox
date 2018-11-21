package com.njcwking.box.scale.utils;

import com.njcwking.box.scale.bus.RxBus;
import com.njcwking.box.scale.model.ConfigItem;
import com.njcwking.box.scale.swing.loading.LoadingDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.File;

/**
 * <pre>
 *     author : 陈伟
 *     e-mail : chenwei@njbandou.com
 *     time   : 2018/11/13
 *     desc   : say something
 *     version: 1.0
 * </pre>
 */

/**
 * 监听的Bus任务
 * 1. 取消任务
 */
public class GenerateTool {

    public static final String MSG_CROP_START = "msg.image.scale.start";

    public static final String MSG_CROP_FAIL = "msg.image.scale.fail";

    public static final String MSG_CROP_SUCCESS = "msg.image.scale.success";

    public static final String MSG_CROP_COMPLETE = "msg.image.scale.complete";

    public static final String MSG_CROP_PROGRESS = "msg.image.scale.progress";

    public static final String MSG_CROP_LOG = "msg.image.scale.log";

    private GenerateBuilder mBuilder = null;
    private boolean isCancel = false;

    public GenerateTool(GenerateBuilder builder) {
        this.mBuilder = builder;
    }

    Disposable cancelDisposable = null;
    Disposable runDisposable = null;

    /**
     * 总文件数
     */
    private int totalImageFileCount = 0;

    /**
     * 当前操作文件位置
     */
    private int progressCount = 0;

    /**
     * 开始生成
     */
    public void startGenerate() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observableEmitter) {
                File srcdir = new File(mBuilder.getRootPath());
                totalImageFileCount = getTotalImageFileCount(srcdir, mBuilder.isRecursion());
                System.out.println("文件数量：" + totalImageFileCount);
                generate(srcdir, File.separator, mBuilder.isRecursion());
                observableEmitter.onNext(true);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.computation()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                runDisposable = disposable;
            }

            @Override
            public void onNext(Boolean success) {
                RxBus.getDefault().post(MSG_CROP_SUCCESS, new Object());
            }

            @Override
            public void onError(Throwable throwable) {
                RxBus.getDefault().post(MSG_CROP_FAIL, throwable);
            }

            @Override
            public void onComplete() {
                RxBus.getDefault().post(MSG_CROP_COMPLETE, new Object());
            }
        });
        cancelDisposable = RxBus.getDefault().toObservable(LoadingDialog.MSG_CROP_CANCEL, Object.class).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                stopGenerate();
                if (runDisposable != null && !runDisposable.isDisposed()) {
                    runDisposable.dispose();
                    runDisposable = null;
                }
                cancelDisposable.dispose();
                cancelDisposable = null;
            }
        });
        RxBus.getDefault().post(MSG_CROP_START, new Object());
    }

    /**
     * 停止生成
     */
    public void stopGenerate() {
        if (!isCancel) {
            isCancel = true;
        }

    }

    private void generate(File srcdir, String dstRelativeDir, boolean recursion) {
        if (isCancel) {
            return;
        }
        File[] files = srcdir.listFiles();
        if (files != null) {
            for (File single : files) {
                if (isCancel) {
                    break;
                }
                if (single.isFile() && (single.getName().toUpperCase().endsWith(".JPG") || single.getName().toUpperCase().endsWith(".PNG"))) {
                    for (ConfigItem model : mBuilder.getGenerateModels()) {
                        if (isCancel) {
                            break;
                        }
                        String filename = single.getName().substring(0, single.getName().length() - 4);
                        String extension = single.getName().substring(single.getName().length() - 4);
                        String fileNameExtension = filename + (!mBuilder.isAutoAddSuffix() || model.getSuffix() == null || "".equals(model.getSuffix()) ? "" : model.getSuffix()) + extension;
                        File dstFile = new File(mBuilder.getFile(), model.getFolderName() + dstRelativeDir + fileNameExtension);
                        if (!mBuilder.isCover() && dstFile.exists()) {
                            for (int i = 1; ; i++) {
                                fileNameExtension = filename + (!mBuilder.isAutoAddSuffix() || model.getSuffix() == null || "".equals(model.getSuffix()) ? "" : model.getSuffix()) + "_"+String.valueOf(i) + extension;
                                dstFile = new File(mBuilder.getFile(), model.getFolderName() + dstRelativeDir + fileNameExtension);
                                if (!dstFile.exists()) {
                                    break;
                                }
                            }
                        }
                        FileUtils.createFolder(dstFile.getParentFile());
                        if (single.getName().equals(mBuilder.getIconName())) {
                            ImageUtils.writeHighQuality(
                                    ImageUtils.zoomImageIcon(single,
                                            model.getIconWidth()), dstFile);
                        } else {
                            ImageUtils.writeHighQuality(
                                    ImageUtils.zoomImage(single,
                                            mBuilder.getCurrentModel(),
                                            model,mBuilder.getTolerance()), dstFile);
                        }
                    }
                    progressCount++;
                    System.out.println(String.valueOf(new Integer(progressCount * 100 / totalImageFileCount)) + " : " + single.getAbsolutePath());
                    RxBus.getDefault().post(MSG_CROP_PROGRESS, new Integer(progressCount * 100 / totalImageFileCount));
                    RxBus.getDefault().post(MSG_CROP_LOG, single.getAbsolutePath());
                } else if (single.isDirectory() && recursion) {
                    String relativeString = single.getAbsolutePath().replace(
                            srcdir.getAbsolutePath(), "")
                            + File.separator;
                    System.out.println("--" + relativeString);
                    generate(single, relativeString, recursion);
                }
            }
        }
    }

    private int getTotalImageFileCount(File startDir, boolean recursion) {
        int count = 0;
        File[] files = startDir.listFiles();
        if (files != null) {
            for (File single : files) {
                if (single.isFile() && (single.getName().toUpperCase().endsWith(".JPG") || single.getName().toUpperCase().endsWith(".PNG"))) {
                    count++;
                } else if (single.isDirectory() && recursion) {
                    count += getTotalImageFileCount(single, recursion);
                }
            }
        }
        return count;
    }
}
