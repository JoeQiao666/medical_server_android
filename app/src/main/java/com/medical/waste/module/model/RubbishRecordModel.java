package com.medical.waste.module.model;

import android.text.TextUtils;

import com.medical.waste.app.App;
import com.medical.waste.base.BaseLifecycleModel;
import com.medical.waste.base.BaseSubscriber;
import com.medical.waste.base.BaseTransformer;
import com.medical.waste.base.LifecycleProvider;
import com.medical.waste.base.ScheduleTransformer;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.common.AppConstant;
import com.medical.waste.greendao.db.DaoSession;
import com.medical.waste.greendao.db.RubbishDao;
import com.medical.waste.module.contract.RubbishRecordContract;
import com.medical.waste.utils.RxBus;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class RubbishRecordModel extends BaseLifecycleModel implements RubbishRecordContract.Model {

    public RubbishRecordModel(LifecycleProvider provider) {
        super(provider);
    }

    @Override
    public void getRubbishById(String id, RequestCallback<Rubbish> callback) {
        App.getApiService().getOneRubbish(id)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .doOnNext(new Consumer<Rubbish>() {
                    @Override
                    public void accept(Rubbish rubbish) throws Exception {
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.add(Calendar.MONTH,-31);
//                        rubbish.setUpdateTime(calendar.getTimeInMillis());
                        rubbish.setUpdateTime(System.currentTimeMillis());
                        RubbishDao dao = App.getContext().getDaoSession().getRubbishDao();
                        dao.insertOrReplace(rubbish);
                        deleteOldData();
                        RxBus.get().post(AppConstant.RxEvent.REFRESH_RECORD, "");
                    }
                })
                .subscribe(new BaseSubscriber<>(callback));
    }
    //删除一个月前的数据
    private void deleteOldData() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -30);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            DaoSession daoSession = App.getContext().getDaoSession();
            RubbishDao dao = daoSession.getRubbishDao();
            List<Rubbish> list = dao.queryBuilder().where(RubbishDao.Properties.UpdateTime.lt(calendar.getTimeInMillis())).list();
            if (list != null && list.size() > 0) {
                dao.deleteInTx(list);
            }
        } catch (Exception e) {
            KLog.e("删除Rubbish失败：" + e.toString());
        }
    }

    @Override
    public void getRubbishRecords(String start, String end, RequestCallback<List<Rubbish>> callback) {
        Observable.create(new ObservableOnSubscribe<List<Rubbish>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Rubbish>> emitter) throws Exception {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -30);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                RubbishDao dao = App.getContext().getDaoSession().getRubbishDao();
                List<Rubbish> all = null;
                if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
                    all = dao.queryBuilder().orderDesc(RubbishDao.Properties.UpdateTime)
                            .where(RubbishDao.Properties.UpdateTime.ge(calendar.getTimeInMillis()))
                            .list();
                } else {
                    long s = format.parse(start).getTime();
                    long e = format.parse(end).getTime();
                    all = dao.queryBuilder()
                            .orderDesc(RubbishDao.Properties.UpdateTime)
                            .where(RubbishDao.Properties.UpdateTime.ge(s), RubbishDao.Properties.UpdateTime.le(e))
                            .list();
                }

                if (all != null) {
                    emitter.onNext(all);
                }
                emitter.onComplete();
            }
        }).compose(provider.bindLifecycle())
                .compose(new ScheduleTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }
}
