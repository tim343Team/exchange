package com.bibi.app;

import android.content.Context;

import com.bibi.data.DataRepository;
import com.bibi.data.LocalDataSource;
import com.bibi.data.RemoteDataSource;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/9
 */
public class Injection {
    public static DataRepository provideTasksRepository(Context context) {
        return DataRepository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
}
