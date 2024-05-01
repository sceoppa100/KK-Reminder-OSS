package com.hideaki.kk_reminder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static com.hideaki.kk_reminder.UtilClass.copyDatabaseKernel;

public class CopyDatabaseWorker extends Worker {

  private final Context context;

  public CopyDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {

    super(context, workerParams);
    this.context = context;
  }

  @NonNull
  @Override
  public Result doWork() {
    copyDatabaseKernel(context, false);
    return Result.success();
  }
}
