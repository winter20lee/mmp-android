
package zblibrary.zgl.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.model.RefreshDownEvent;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.EmptyRecyclerView;
import zuo.biao.library.ui.EmptyView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

/**
 * 下载
 */
public class MyDownFilesFragment extends BaseFragment implements View.OnClickListener {

    private static TaskItemAdapter adapter;
    private TextView mydown_edit,mydown_sel_all,mydown_sel_del;
    private EmptyRecyclerView recyclerView;
    private EmptyView empty_view;
    private static ArrayList<Integer> delIds = new ArrayList<>();

    public static Intent createIntent(Context context) {
        return new Intent(context, MyDownFilesFragment.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.mydown_file_activity);
        EventBus.getDefault().register(this);
        mydown_edit = findView(R.id.mydown_edit);
        mydown_sel_all = findView(R.id.mydown_sel_all);
        mydown_sel_del = findView(R.id.mydown_sel_del);
        recyclerView =  findView(R.id.rvBaseRecycler);
        empty_view = findView(R.id.empty_view);
        empty_view.setEmptyText("暂时没有下载记录");
        empty_view.setEmptySecondText("您可以去首页推荐看看");
        recyclerView.setEmptyView(empty_view);
        findView(R.id.mydown_file_back,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(adapter==null){
            adapter = new TaskItemAdapter(getActivity());
        }
        recyclerView.setAdapter(adapter);
        mydown_edit.setOnClickListener(this);
        mydown_sel_all.setOnClickListener(this);
        mydown_sel_del.setOnClickListener(this);
        TasksManager.getImpl().onCreate(new WeakReference<>(this));
        return view;
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    public void postNotifyDataChanged() {
        if (adapter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshDownEvent baseEvent){
        mydown_edit.setText("编辑");
        adapter.isEditState = false;
        findView(R.id.mydown_bottom_divid).setVisibility(View.GONE);
        findView(R.id.mydown_bottom_edit).setVisibility(View.GONE);
        postNotifyDataChanged();
        if(baseEvent.isStart){
            adapter.autoClick(getHolder(TasksManager.getImpl().getTaskCounts()-1));
            showShortToast("开始下载");
        }
    }

    private TaskItemViewHolder getHolder(int index){
        if(null == recyclerView || null==recyclerView.getAdapter()||recyclerView.getAdapter().getItemCount()==0){
            return null;
        }
        int count = recyclerView.getAdapter().getItemCount();
        if(index < 0 || index > count-1){
            return null;
        }

        TaskItemViewHolder myViewHolder = (TaskItemViewHolder) recyclerView.findViewHolderForAdapterPosition(0);//获取到对应Item的View
        if(myViewHolder==null){
            RecyclerView.RecycledViewPool pool = recyclerView.getRecycledViewPool();
            int type =0;
            int recycledViewCount = pool.getRecycledViewCount(type);
            myViewHolder = (TaskItemViewHolder) pool.getRecycledView(type);
            try {
                pool.putRecycledView(myViewHolder);
            }catch (Exception e){

            }
        }

        return myViewHolder;
    }


    @Override
    public void onDestroy() {
        TasksManager.getImpl().onDestroy();
        adapter = null;
        FileDownloader.getImpl().pauseAll();
        adapter.isEditState = false;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mydown_edit:
                if(mydown_edit.getText().toString().equals("编辑")){
                    mydown_edit.setText("完成");
                    adapter.isEditState = true;
                    findView(R.id.mydown_bottom_divid).setVisibility(View.VISIBLE);
                    findView(R.id.mydown_bottom_edit).setVisibility(View.VISIBLE);
                }else{
                    mydown_edit.setText("编辑");
                    adapter.isEditState = false;
                    findView(R.id.mydown_bottom_divid).setVisibility(View.GONE);
                    findView(R.id.mydown_bottom_edit).setVisibility(View.GONE);
                }
                postNotifyDataChanged();
                break;
            case R.id.mydown_sel_all:
                    for(int i=0;i<TasksManager.getImpl().getTaskCounts();i++){
                         TasksManagerModel model = TasksManager.getImpl().get(i);
                        delIds.add(model.id);
                    }
                postNotifyDataChanged();
                break;
            case R.id.mydown_sel_del:
                for (Integer id:delIds) {
                    FileDownloader.getImpl().pause(id);
                    new File(TasksManager.getImpl().getById(id).getPath()).delete();
                    new File(FileDownloadUtils.getTempPath(TasksManager.getImpl().getById(id).getPath())).delete();

                    TasksManager.getImpl().delTask(id+"");
                }
                delIds.clear();
                postNotifyDataChanged();
                break;
            case R.id.mydown_file_back:
                ((MainTabActivity)getActivity()).hideDownFile();
                break;
        }
    }


    // ============================================================================ view adapter ===

    private static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        public TaskItemViewHolder(View itemView) {
            super(itemView);
            assignViews();
        }

        private View findViewById(final int id) {
            return itemView.findViewById(id);
        }

        /**
         * viewHolder position
         */
        private int position;
        /**
         * download id
         */
        private int id;

        public void update(final int id, final int position) {
            this.id = id;
            this.position = position;
        }


        public void updateDownloaded() {
            taskPb.setMax(1);
            taskPb.setProgress(1);

            taskStatusTv.setText(R.string.tasks_manager_demo_status_completed);
            taskActionBtn.setText(R.string.delete);
        }

        public void updateNotDownloaded(final int status, final long sofar, final long total) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar
                        / (float) total;
                taskPb.setMax(100);
                taskPb.setProgress((int) (percent * 100));
                taskTotalTv.setText(StringUtil.bytes2kb(total));
            } else {
                taskPb.setMax(1);
                taskPb.setProgress(0);
            }

            switch (status) {
                case FileDownloadStatus.error:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_error);
                    break;
                case FileDownloadStatus.paused:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
                    break;
                default:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_not_downloaded);
                    break;
            }
            taskActionBtn.setText(R.string.start);
        }

        public void updateDownloading(final int status, final long sofar, final long total,String speed) {
            final float percent = sofar
                    / (float) total;
            taskPb.setMax(100);
            taskPb.setProgress((int) (percent * 100));

            switch (status) {
                case FileDownloadStatus.pending:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
                    break;
                case FileDownloadStatus.started:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
                    break;
                case FileDownloadStatus.connected:
                    taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
                    break;
                case FileDownloadStatus.progress:
                    taskStatusTv.setText(speed);
                    break;
                default:
                    taskStatusTv.setText(MApplication.getInstance().getString(
                            R.string.tasks_manager_demo_status_downloading, status));
                    break;
            }
            taskTotalTv.setText(StringUtil.bytes2kb(total));
            taskActionBtn.setText(R.string.pause);
        }

        private TextView taskNameTv;
        private TextView taskStatusTv,taskTotalTv;
        private ProgressBar taskPb;
        private Button taskActionBtn;
        private ImageView taskSelectIv;
        private ImageView taskPic;
        private void assignViews() {
            taskSelectIv = (ImageView) findViewById(R.id.task_select_iv);
            taskNameTv = (TextView) findViewById(R.id.task_name_tv);
            taskStatusTv = (TextView) findViewById(R.id.task_status_tv);
            taskPb = (ProgressBar) findViewById(R.id.task_pb);
            taskActionBtn = (Button) findViewById(R.id.task_action_btn);
            taskTotalTv = (TextView)findViewById(R.id.task_total_tv);
            taskPic = (ImageView) findViewById(R.id.task_pic);
        }

    }

    private static class TaskItemAdapter extends RecyclerView.Adapter<TaskItemViewHolder> {
        private Context context;
        public TaskItemAdapter(Context context){
            this.context = context;
        }
        private static boolean isEditState;
        private FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

            private TaskItemViewHolder checkCurrentHolder(final BaseDownloadTask task) {
                final TaskItemViewHolder tag = (TaskItemViewHolder) task.getTag();
                if(tag == null){
                    return null;
                }
                if (tag.id != task.getId()) {
                    return null;
                }

                return tag;
            }

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.pending(task, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.updateDownloading(FileDownloadStatus.pending, soFarBytes
                        , totalBytes,task.getSpeed()+" k/s");
                tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_pending);
                tag.taskTotalTv.setText(StringUtil.bytes2kb(totalBytes));
            }

            @Override
            protected void started(BaseDownloadTask task) {
                super.started(task);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_started);
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.updateDownloading(FileDownloadStatus.connected, soFarBytes
                        , totalBytes,task.getSpeed()+" k/s");
                tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_connected);
                tag.taskTotalTv.setText(StringUtil.bytes2kb(totalBytes));
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.progress(task, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.updateDownloading(FileDownloadStatus.progress, soFarBytes
                        , totalBytes,task.getSpeed()+" k/s");
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes()
                        , task.getLargeFileTotalBytes());
                TasksManager.getImpl().removeTaskForViewHolder(task.getId());
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.paused(task, soFarBytes, totalBytes);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }

                tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
                tag.taskStatusTv.setText(R.string.tasks_manager_demo_status_paused);
                TasksManager.getImpl().removeTaskForViewHolder(task.getId());
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                final TaskItemViewHolder tag = checkCurrentHolder(task);
                if (tag == null) {
                    return;
                }
                tag.taskTotalTv.setText(StringUtil.bytes2kb(task.getLargeFileTotalBytes()));
                tag.updateDownloaded();
                TasksManager.getImpl().removeTaskForViewHolder(task.getId());
            }
        };
        private View.OnClickListener taskActionOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    return;
                }

                TaskItemViewHolder holder = (TaskItemViewHolder) v.getTag();

                CharSequence action = ((TextView) v).getText();
                if (action.equals(v.getResources().getString(R.string.pause))) {
                    // to pause
                    FileDownloader.getImpl().pause(holder.id);
                    Toast.makeText(context,"暂停中...",Toast.LENGTH_SHORT).show();
                } else if (action.equals(v.getResources().getString(R.string.start))) {
                    // to start
                    autoClick(holder);
                    Toast.makeText(context,"下载中...",Toast.LENGTH_SHORT).show();
                } else if (action.equals(v.getResources().getString(R.string.delete))) {
                    // to delete
//                    new File(TasksManager.getImpl().get(holder.position).getPath()).delete();
//                    holder.taskActionBtn.setEnabled(true);
//                    holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS, 0, 0);
                    Intent intent = PlayVideoDetailsActivity.createIntent(context,TasksManager.getImpl().get(holder.position).vid);
                    context.startActivity(intent);
                }
            }
        };
        private View.OnClickListener taskSeleOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getTag() == null) {
                    return;
                }
                TaskItemViewHolder holder = (TaskItemViewHolder) v.getTag();
                if(delIds.contains(holder.id)){
                    delIds.remove((Integer)holder.id);
                    holder.taskSelectIv.setImageResource(R.mipmap.shopping_car_unselect);
                }else{
                    delIds.add(holder.id);
                    holder.taskSelectIv.setImageResource(R.mipmap.shopping_car_select);
                }
            }
        };
        public void autoClick(TaskItemViewHolder holder){
            if(holder != null){
                TasksManagerModel model = TasksManager.getImpl().get(holder.position);
                BaseDownloadTask task = FileDownloader.getImpl().create(model.getUrl())
                        .setPath(model.getPath())
                        .setCallbackProgressTimes(100)
                        .setListener(taskDownloadListener);

                TasksManager.getImpl()
                        .addTaskForViewHolder(task);

                TasksManager.getImpl()
                        .updateViewHolder(holder.id, holder);

                task.start();
            }else{
                TasksManagerModel model = TasksManager.getImpl().getEnd();
                BaseDownloadTask task = FileDownloader.getImpl().create(model.getUrl())
                        .setPath(model.getPath())
                        .setCallbackProgressTimes(100)
                        .setListener(taskDownloadListener);

                TasksManager.getImpl()
                        .addTaskForViewHolder(task);

                task.start();
            }
        }

        @Override
        public TaskItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TaskItemViewHolder holder = new TaskItemViewHolder(
                    LayoutInflater.from(
                            parent.getContext())
                            .inflate(R.layout.item_tasks_manager, parent, false));

            holder.taskActionBtn.setOnClickListener(taskActionOnClickListener);
            holder.taskSelectIv.setOnClickListener(taskSeleOnClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(TaskItemViewHolder holder, int position) {
            final TasksManagerModel model = TasksManager.getImpl().get(position);
//
            if(isEditState){
                holder.taskSelectIv.setVisibility(View.VISIBLE);
            }else{
                holder.taskSelectIv.setVisibility(View.GONE);
            }


            if(!delIds.contains(holder.id)){
                holder.taskSelectIv.setImageResource(R.mipmap.shopping_car_unselect);
            }else{
                holder.taskSelectIv.setImageResource(R.mipmap.shopping_car_select);
            }

            holder.update(model.getId(), position);
            holder.taskActionBtn.setTag(holder);
            holder.taskSelectIv.setTag(holder);
            holder.taskNameTv.setText(model.getName());
            GlideUtil.load(context,model.pic,holder.taskPic);

            TasksManager.getImpl()
                    .updateViewHolder(holder.id, holder);

            holder.taskActionBtn.setEnabled(true);

            if (TasksManager.getImpl().isReady()) {
                final int status = TasksManager.getImpl().getStatus(model.getId(), model.getPath());
                if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                        status == FileDownloadStatus.connected) {
                    // start task, but file not created yet
                    holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
                            , TasksManager.getImpl().getTotal(model.getId()),"0 k/s");
                } else if (!new File(model.getPath()).exists() &&
                        !new File(FileDownloadUtils.getTempPath(model.getPath())).exists()) {
                    // not exist file
                    holder.updateNotDownloaded(status, 0, 0);
                } else if (TasksManager.getImpl().isDownloaded(status)) {
                    // already downloaded and exist
                    File file = new File(model.getPath());
                    if(file.exists()){
                        holder.taskTotalTv.setText(StringUtil.bytes2kb(file.length()));
                    }
                    holder.updateDownloaded();
                } else if (status == FileDownloadStatus.progress) {
                    // downloading
                    holder.updateDownloading(status, TasksManager.getImpl().getSoFar(model.getId())
                            , TasksManager.getImpl().getTotal(model.getId()),"0 k/s");
                } else {
                    // not start
                    holder.updateNotDownloaded(status, TasksManager.getImpl().getSoFar(model.getId())
                            , TasksManager.getImpl().getTotal(model.getId()));
                }
            } else {
                holder.taskStatusTv.setText(R.string.tasks_manager_demo_status_loading);
                holder.taskActionBtn.setEnabled(false);
            }
        }

        @Override
        public int getItemCount() {
            return TasksManager.getImpl().getTaskCounts();
        }
    }


    // ============================================================================ controller ====

    public static class TasksManager {
        private final static class HolderClass {
            private final static TasksManager INSTANCE
                    = new TasksManager();
        }

        public static TasksManager getImpl() {
            return HolderClass.INSTANCE;
        }

        private TasksManagerDBController dbController;
        private List<TasksManagerModel> modelList;

        private TasksManager() {
            dbController = new TasksManagerDBController();
            modelList = dbController.getAllTasks();
        }


        private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

        public void addTaskForViewHolder(final BaseDownloadTask task) {
            taskSparseArray.put(task.getId(), task);
        }

        public void removeTaskForViewHolder(final int id) {
            taskSparseArray.remove(id);
        }

        public void updateViewHolder(final int id, final TaskItemViewHolder holder) {
            final BaseDownloadTask task = taskSparseArray.get(id);
            if (task == null) {
                return;
            }

            task.setTag(holder);
        }

        public void releaseTask() {
            taskSparseArray.clear();
        }

        private FileDownloadConnectListener listener;

        private void registerServiceConnectionListener(final WeakReference<MyDownFilesFragment>
                                                               activityWeakReference) {
            if (listener != null) {
                FileDownloader.getImpl().removeServiceConnectListener(listener);
            }

            listener = new FileDownloadConnectListener() {

                @Override
                public void connected() {
                    if (activityWeakReference == null
                            || activityWeakReference.get() == null) {
                        return;
                    }

                    activityWeakReference.get().postNotifyDataChanged();
                }

                @Override
                public void disconnected() {
                    if (activityWeakReference == null
                            || activityWeakReference.get() == null) {
                        return;
                    }

                    activityWeakReference.get().postNotifyDataChanged();
                }
            };

            FileDownloader.getImpl().addServiceConnectListener(listener);
        }

        private void unregisterServiceConnectionListener() {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
            listener = null;
        }

        public void onCreate(final WeakReference<MyDownFilesFragment> activityWeakReference) {
            if (!FileDownloader.getImpl().isServiceConnected()) {
                FileDownloader.getImpl().bindService();
                registerServiceConnectionListener(activityWeakReference);
            }
        }

        public void onDestroy() {
            unregisterServiceConnectionListener();
            releaseTask();
        }

        public boolean isReady() {
            return FileDownloader.getImpl().isServiceConnected();
        }

        public TasksManagerModel get(final int position) {
            return modelList.get(position);
        }

        public TasksManagerModel getEnd() {
            return modelList.get(modelList.size()-1);
        }

        public TasksManagerModel getById(final int id) {
            for (TasksManagerModel model : modelList) {
                if (model.getId() == id) {
                    return model;
                }
            }

            return null;
        }

        /**
         * @param status Download Status
         * @return has already downloaded
         * @see FileDownloadStatus
         */
        public boolean isDownloaded(final int status) {
            return status == FileDownloadStatus.completed;
        }

        public int getStatus(final int id, String path) {
            return FileDownloader.getImpl().getStatus(id, path);
        }

        public long getTotal(final int id) {
            return FileDownloader.getImpl().getTotal(id);
        }

        public long getSoFar(final int id) {
            return FileDownloader.getImpl().getSoFar(id);
        }

        public int getTaskCounts() {
            return modelList.size();
        }


        public int getDowningCounts() {
            int count = 0;
            for (TasksManagerModel tasksManagerModel:modelList) {
                if(getStatus(tasksManagerModel.id,tasksManagerModel.path) == FileDownloadStatus.progress){
                    ++count;
                }
            }
            return count;
        }

        public int getDownedCounts() {
            int count = 0;
            for (TasksManagerModel tasksManagerModel:modelList) {
                if(isDownloaded(getStatus(tasksManagerModel.id,tasksManagerModel.path))){
                    ++count;
                }
            }
            return count;
        }

        public TasksManagerModel addTask(final int vid,final String name,final String pic,final String url) {
            return addTask(vid,name,pic,url, createPath(url));
        }

        public TasksManagerModel addTask(final int vid,final String name,final String pic,final String url, final String path) {
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
                return null;
            }

            final int id = FileDownloadUtils.generateId(url, path);
            TasksManagerModel model = getById(id);
            if (model != null) {
                return model;
            }
            final TasksManagerModel newModel = dbController.addTask(vid,name,pic,url, path);
            if (newModel != null) {
                modelList.add(newModel);
            }

            return newModel;
        }

        public String createPath(final String url) {
            if (TextUtils.isEmpty(url)) {
                return null;
            }

            return FileDownloadUtils.getDefaultSaveFilePath(url);
        }

        public void delTask(String id){
            Iterator<TasksManagerModel> it_b=modelList.iterator();
            while(it_b.hasNext()){
                TasksManagerModel tasksManagerModel=it_b.next();
                if((tasksManagerModel.id+"") .equals(id)){
                    FileDownloadUtils.deleteTaskFiles(tasksManagerModel.url,tasksManagerModel.path);
                    it_b.remove();
                }
            }
//            for(TasksManagerModel tasksManagerModel:modelList){
//                if((tasksManagerModel.id+"") .equals(id)){
//                    FileDownloadUtils.deleteTaskFiles(tasksManagerModel.url,tasksManagerModel.path);
//                    modelList.remove(tasksManagerModel);
//                }
//            }
            dbController.deleTask(id);
        }
    }

    private static class TasksManagerDBController {
        public final static String TABLE_NAME = "tasksmanger";
        private final SQLiteDatabase db;

        private TasksManagerDBController() {
            TasksManagerDBOpenHelper openHelper = new TasksManagerDBOpenHelper(MApplication.getInstance());

            db = openHelper.getWritableDatabase();
        }

        @SuppressLint("Range")
        public List<TasksManagerModel> getAllTasks() {
            final Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            final List<TasksManagerModel> list = new ArrayList<>();
            try {
                if (!c.moveToLast()) {
                    return list;
                }

                do {
                    TasksManagerModel model = new TasksManagerModel();
                    model.setId(c.getInt(c.getColumnIndex(TasksManagerModel.ID)));
                    model.setName(c.getString(c.getColumnIndex(TasksManagerModel.NAME)));
                    model.setUrl(c.getString(c.getColumnIndex(TasksManagerModel.URL)));
                    model.setPath(c.getString(c.getColumnIndex(TasksManagerModel.PATH)));
                    model.pic = c.getString(c.getColumnIndex(TasksManagerModel.PIC));
                    model.vid = c.getInt(c.getColumnIndex(TasksManagerModel.VID));
                    list.add(model);
                } while (c.moveToPrevious());
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            return list;
        }

        public TasksManagerModel addTask(final int vid,final String name,final String pic,final String url, final String path) {
            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
                return null;
            }

            // have to use FileDownloadUtils.generateId to associate TasksManagerModel with FileDownloader
            final int id = FileDownloadUtils.generateId(url, path);

            TasksManagerModel model = new TasksManagerModel();
            model.setId(id);
//            model.setName(MApplication.getInstance().getString(R.string.tasks_manager_name, id));
            model.setName(name);
            model.setUrl(url);
            model.setPath(path);
            model.pic = pic;
            model.vid = vid;

            final boolean succeed = db.insert(TABLE_NAME, null, model.toContentValues()) != -1;
            return succeed ? model : null;
        }

        public void deleTask(String id){
            db.delete(TABLE_NAME, "id = ? ", new String[]{id});
        }
    }

    // ----------------------- model
    private static class TasksManagerDBOpenHelper extends SQLiteOpenHelper {
        public final static String DATABASE_NAME = "tasksmanager.db";
        public final static int DATABASE_VERSION = 2;

        public TasksManagerDBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TasksManagerDBController.TABLE_NAME
                    + String.format(
                    "("
                            + "%s INTEGER PRIMARY KEY, " // id, download id
                            + "%s VARCHAR, " // name
                            + "%s VARCHAR, " // url
                            + "%s VARCHAR, " // path
                            + "%s VARCHAR, " // pic
                            + "%s INTEGER " // vid
                            + ")"
                    , TasksManagerModel.ID
                    , TasksManagerModel.NAME
                    , TasksManagerModel.URL
                    , TasksManagerModel.PATH
                    , TasksManagerModel.PIC
                    , TasksManagerModel.VID

            ));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                db.delete(TasksManagerDBController.TABLE_NAME, null, null);
            }
        }
    }

    private static class TasksManagerModel {
        public final static String ID = "id";
        public final static String NAME = "name";
        public final static String URL = "url";
        public final static String PATH = "path";
        public final static String PIC = "pic";
        public final static String VID = "vid";

        private int id;
        private String name;
        private String url;
        private String path;
        public String pic;
        public int vid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public ContentValues toContentValues() {
            ContentValues cv = new ContentValues();
            cv.put(ID, id);
            cv.put(NAME, name);
            cv.put(URL, url);
            cv.put(PATH, path);
            cv.put(PIC, pic);
            cv.put(VID, vid);
            return cv;
        }
    }
}
