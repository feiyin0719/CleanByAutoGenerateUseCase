
package com.iffly.clean.usecase;


import com.iffly.clean.usecase.executor.AppExecutors;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 */
public abstract class UseCase<T, Params> {

    private final AppExecutors appExecutors;


    protected UseCase(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }


    protected abstract T execute(Params params);


    public void execute(final CallBack callBack, final Params params) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                final T reuslt=execute(params);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(reuslt!=null)
                            callBack.onSucess(reuslt);
                        else
                            callBack.onError();
                    }
                });
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    public static interface CallBack<T>{
        public void onSucess(T result);
        public void onError();
    }
}
