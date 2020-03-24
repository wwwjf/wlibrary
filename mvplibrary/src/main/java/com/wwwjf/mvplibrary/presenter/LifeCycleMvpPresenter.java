package com.wwwjf.mvplibrary.presenter;



import com.wwwjf.mvplibrary.ILifeCycle;
import com.wwwjf.mvplibrary.IMvpView;
import com.wwwjf.mvplibrary.MvpController;

import java.lang.ref.WeakReference;

public abstract class LifeCycleMvpPresenter<T> implements ILifeCycle {

    protected WeakReference<T> weakReference;

    protected LifeCycleMvpPresenter(){
        super();
    }

    public LifeCycleMvpPresenter(IMvpView iMvpView){
        super();
        attachView(iMvpView);
        MvpController mvpController = iMvpView.getMvpController();
        mvpController.savePresenter(this);
    }


    @Override
    public void attachView(IMvpView iMvpView) {
        if (weakReference == null){
            weakReference = new WeakReference(iMvpView);
        } else {
            T view = (T) new WeakReference(iMvpView);
            if (view != iMvpView){
                weakReference = new WeakReference(iMvpView);
            }
        }
    }

    @Override
    public void onDestroy() {
        weakReference = null;
    }

    protected T getView(){
        T view =weakReference != null? weakReference.get():null;
        if (view == null){
            return getEmptyView();
        }
        return view;
    }

    protected abstract T getEmptyView();
}
